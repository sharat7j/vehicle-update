package com.tesla.code.service;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.beans.RollOut;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.exceptions.UniquenessException;
import com.tesla.code.repository.JobRepository;
import com.tesla.code.repository.JobStatusRepository;
import com.tesla.code.repository.RollOutRepository;
import com.tesla.code.utils.JobState;
import org.hibernate.mapping.Collection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tesla.code.util.Helpers.churnJob;
import static com.tesla.code.util.Helpers.churnJobStatus;
import static com.tesla.code.util.Helpers.churnRollOut;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class JobServiceTest {

    @Mock
    JobRepository jobRepository;
    @Mock
    JobStatusRepository jobStatusRepository;
    @Mock
    RollOutRepository rollOutRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    JobService jobService = new JobService(jobRepository, jobStatusRepository, rollOutRepository);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        doNothing().when(rollOutRepository).deleteRollOut(any(String.class));
        when(jobRepository.save(any(Job.class))).thenReturn(churnJob());
        when(jobStatusRepository.save(any(JobStatus.class))).thenReturn(churnJobStatus());
        doNothing().when(jobRepository).delete(any(Job.class));
    }

    @Test
    public void testCreateJobMissingExc() throws Exception, MissingDataException {
        // test missing roll out information
        when(rollOutRepository.findOne(any(String.class))).thenReturn(null);
        exception.expect(MissingDataException.class);
        Job inputJob = churnJob();
        inputJob.setRollOutId("1");
        jobService.createJob(inputJob);
    }

    @Test
    public void testCreateJobUniqueness() throws Exception, MissingDataException {
        // test uniqueness exception with jobs
        RollOut rollOut1 = churnRollOut();
        when(rollOutRepository.findOne(any(String.class))).thenReturn(rollOut1);
        when(jobRepository.nameExists(any(String.class))).thenReturn(churnJob());
        exception.expect(UniquenessException.class);
        jobService.createJob(churnJob());
    }

    @Test
    public void testCreateJobValidPath() throws Exception, MissingDataException {
        // valid path
        RollOut rollOut2 = churnRollOut();
        when(rollOutRepository.findOne(any(String.class))).thenReturn(rollOut2);
        when(jobRepository.nameExists(any(String.class))).thenReturn(null);
        Job inputJob2 = churnJob();
        inputJob2.setRollOutId("1");
        Job job = jobService.createJob(inputJob2);
        assertNotNull(job);
        assertNotNull(job.getJobStatusList());
        assertEquals(job.getJobStatusList().size(), 1);
        assertEquals(job.getJobStatusList().get(0).getState(), JobState.CREATED);
    }

    @Test
    public void testCancelJobMissingExc() throws Exception, MissingDataException {
        when(jobRepository.findOne(any(String.class))).thenReturn(null);
        exception.expect(MissingDataException.class);
        jobService.cancelJob("1");
    }

    @Test
    public void testCancelJobValidPathEmptyStatus() throws Exception, MissingDataException {
        when(jobRepository.findOne(any(String.class))).thenReturn(churnJob());
        when(jobRepository.getJobStatusList(any(String.class))).thenReturn(Collections.emptyList());
        jobService.cancelJob("1");

    }

    @Test
    public void testCancelJobValidPathUpdateStatus() throws Exception, MissingDataException {
        when(jobRepository.findOne(any(String.class))).thenReturn(churnJob());
        List<JobStatus> jobStatusList = new ArrayList<>();
        JobStatus status = churnJobStatus();
        status.setState(JobState.INSTALLING);
        jobStatusList.add(status);
        when(jobRepository.getJobStatusList(any(String.class))).thenReturn(jobStatusList);
        jobService.cancelJob("1");
        // default created state
        jobStatusList.remove(0);
        JobStatus status1 = churnJobStatus();
        jobStatusList.add(status1);
        when(jobRepository.getJobStatusList(any(String.class))).thenReturn(jobStatusList);
        jobService.cancelJob("1");


    }

    @Test
    public void testListJobs() throws Exception {
        List<Job> jobs = new ArrayList<>();
        jobs.add(churnJob());
        jobs.add(churnJob());
        jobs.add(churnJob());
        PageImpl<Job> jobsPage = new PageImpl<Job>(jobs);
        List<JobStatus> jobStatuses = new ArrayList<>();
        JobStatus s = churnJobStatus();
        s.setJob(churnJob());
        jobStatuses.add(s);

        when(jobRepository.getJobsForRollOut(any(Pageable.class), any(String.class))).thenReturn(jobsPage);
        when(jobRepository.getJobStatusList(any(String.class))).thenReturn(jobStatuses);
        Page<Job> result = jobService.listJobs(null, "1");
        assertNotNull(result);
        assertEquals(result.getTotalElements(), 3);
        // check if the transient fields were loaded
        assertEquals(result.getContent().get(0).getJobStatusList().size(), 1);
        assertNotNull(result.getContent().get(0).getRollOutId());
        assertEquals(result.getContent().get(0).getJobStatusList().get(0).getState(), JobState.CREATED);

        when(jobRepository.findAllSorted(any(Pageable.class))).thenReturn(jobsPage);
        Page<Job> result2 = jobService.listJobs(new PageRequest(1, 20), null);
        assertNotNull(result2);
        assertEquals(result2.getTotalElements(), 3);
        // check if the transient fields were loaded
        assertEquals(result2.getContent().get(0).getJobStatusList().size(), 1);
        assertNotNull(result2.getContent().get(0).getRollOutId());
        assertEquals(result2.getContent().get(0).getJobStatusList().get(0).getState(), JobState.CREATED);


    }

    @Test
    public void testJobDetailsException() throws Exception, MissingDataException {
        when(jobRepository.findOne(any(String.class))).thenReturn(null);
        exception.expect(MissingDataException.class);
        jobService.jobDetails("1");
    }

    @Test
    public void testJobDetails() throws Exception, MissingDataException {
        Job s = churnJob();
        when(jobRepository.findOne(any(String.class))).thenReturn(s);
        when(jobRepository.getJobStatusList(any(String.class))).thenReturn(Collections.emptyList());
        assertEquals(jobService.jobDetails("1").getId(), s.getId());
    }

    @Test
    public void testJobStatusForJob() throws Exception {
        when(jobRepository.getJobStatusList(any(String.class))).thenReturn(Collections.emptyList());
        assertEquals(jobService.jobStatusForJob("1").size(), 0);
    }

    @Test
    public void testDeleteJobException() throws Exception, MissingDataException {
        when(jobRepository.findOne(any(String.class))).thenReturn(null);
        exception.expect(MissingDataException.class);
        jobService.deleteJob("1");
    }

    @Test
    public void testDelete() throws Exception, MissingDataException {
        when(jobRepository.findOne(any(String.class))).thenReturn(churnJob());
        jobService.deleteJob("1");

    }
}
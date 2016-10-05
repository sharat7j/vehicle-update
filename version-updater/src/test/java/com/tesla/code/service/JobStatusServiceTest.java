package com.tesla.code.service;

import com.tesla.code.beans.JobStatus;
import com.tesla.code.exceptions.InvalidDataException;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.repository.JobRepository;
import com.tesla.code.repository.JobStatusRepository;
import com.tesla.code.utils.JobState;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static com.tesla.code.util.Helpers.churnJob;
import static com.tesla.code.util.Helpers.churnJobStatus;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class JobStatusServiceTest {

    @Mock
    JobStatusRepository jobStatusRepository;
    @Mock
    JobRepository jobRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    JobStatusService jobStatusService = new JobStatusService(jobRepository, jobStatusRepository);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(jobStatusRepository.save(any(JobStatus.class))).thenReturn(churnJobStatus());
    }

    @Test(expected = MissingDataException.class)
    public void testCreateJobStatusMissingExc1() throws Exception, MissingDataException {
        jobStatusService.createJobStatus(null);
    }

    @Test(expected = MissingDataException.class)
    public void testCreateJobStatusMissingExc2() throws Exception, MissingDataException {
        JobStatus status = new JobStatus();
        jobStatusService.createJobStatus(status);
    }

    @Test(expected = MissingDataException.class)
    public void testCreateJobStatusMissingExc3() throws Exception, MissingDataException {
        JobStatus status = new JobStatus();
        status.setJobId("1");
        when(jobRepository.findOne(any(String.class))).thenReturn(null);
        jobStatusService.createJobStatus(status);

    }

    @Test(expected = InvalidDataException.class)
    public void testJobIsCancelledState() throws Exception {
        JobStatus status = new JobStatus();
        status.setJobId("1");
        status.setState(JobState.CANCELLED);
        when(jobRepository.findOne(any(String.class))).thenReturn(churnJob());
        jobStatusService.createJobStatus(status);

    }

    @Test(expected = InvalidDataException.class)
    public void testJobIsInUnknownState() throws Exception {
        JobStatus status1 = new JobStatus();
        status1.setJobId("1");
        status1.setState(JobState.UNKNOWN);
        when(jobRepository.findOne(any(String.class))).thenReturn(churnJob());
        jobStatusService.createJobStatus(status1);
    }

    @Test(expected = InvalidDataException.class)
    public void testNewJobStatusNotLogical() throws Exception {
        JobStatus status = new JobStatus();
        status.setJobId("1");
        // bypassing the DOWNLOADING step
        status.setState(JobState.INSTALLING);
        when(jobRepository.findOne(any(String.class))).thenReturn(churnJob());
        // set up existing current job status for the job
        List<JobStatus> jobStatuses = new ArrayList<>();
        jobStatuses.add(churnJobStatus());
        when(jobRepository.getJobStatusList(any(String.class))).thenReturn(jobStatuses);
        jobStatusService.createJobStatus(status);
    }

    @Test
    public void testNewJobValid() throws Exception {
        JobStatus status = new JobStatus();
        status.setJobId("1");
        // bypassing the DOWNLOADING step
        status.setState(JobState.DOWNLOADING);
        when(jobRepository.findOne(any(String.class))).thenReturn(churnJob());
        // set up existing current job status for the job
        List<JobStatus> jobStatuses = new ArrayList<>();
        jobStatuses.add(churnJobStatus());
        when(jobRepository.getJobStatusList(any(String.class))).thenReturn(jobStatuses);
        // override the save call here to ensure it returns the new status as this is what hibernate would do.
        when(jobStatusRepository.save(any(JobStatus.class))).thenReturn(status);
        JobStatus result = jobStatusService.createJobStatus(status);
        assertNotNull(result);
        assertEquals(result.getId(), status.getId());
        assertEquals(result.getState(), status.getState());
    }

    @Test(expected = InvalidDataException.class)
    public void testNewJobStatusUpdatingFinishedState() throws MissingDataException, InvalidDataException {
        JobStatus status = new JobStatus();
        status.setJobId("1");
        // bypassing the DOWNLOADING step
        status.setState(JobState.FAILED);
        when(jobRepository.findOne(any(String.class))).thenReturn(churnJob());
        // set up existing current job status for the job
        List<JobStatus> jobStatuses = new ArrayList<>();
        jobStatuses.add(status);
        when(jobRepository.getJobStatusList(any(String.class))).thenReturn(jobStatuses);
        jobStatusService.createJobStatus(status);
    }

    @Test
    public void testListStatus() throws Exception {
        List<JobStatus> jobStatusList = new ArrayList<>();
        jobStatusList.add(churnJobStatus());
        jobStatusList.add(churnJobStatus());
        jobStatusList.add(churnJobStatus());
        jobStatusList.add(churnJobStatus());
        PageImpl<JobStatus> jobStatuses = new PageImpl<>(jobStatusList);
        when(jobStatusRepository.getStatusList(any(Pageable.class))).thenReturn(jobStatuses);
        when(jobStatusRepository.getStatusListForJob(any(Pageable.class), any(String.class))).thenReturn(jobStatuses);
        when(jobStatusRepository.getStatusListForRollOut(any(Pageable.class), any(String.class))).thenReturn(jobStatuses);
        when(jobStatusRepository.getStatusListForRolloutAndJob(any(Pageable.class), any(String.class), any(String.class))).thenReturn(jobStatuses);
        assertEquals(jobStatusService.listStatus(null, null, null), jobStatuses);
        assertEquals(jobStatusService.listStatus(null, null, "1"), jobStatuses);
        assertEquals(jobStatusService.listStatus(null, "1", null), jobStatuses);
        assertEquals(jobStatusService.listStatus(null, "1", "1"), jobStatuses);
    }
}
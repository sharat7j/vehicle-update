package com.tesla.code.controller;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.beans.request.JobRequest;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.exceptions.UniquenessException;
import com.tesla.code.service.JobService;
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


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.tesla.code.util.Helpers.churnJob;
import static com.tesla.code.util.Helpers.churnJobStatus;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


public class JobControllerTest {

    @Mock
    JobService jobService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    JobController jobController = new JobController(jobService);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = MissingDataException.class)
    public void testNewJobCreationEmptyBody() throws MissingDataException, UniquenessException {
        jobController.createJob(null);
    }

    @Test
    public void testNewJobCreationWithNoRollOutId() throws MissingDataException, UniquenessException {
        exception.expect(MissingDataException.class);
        jobController.createJob(new JobRequest());
    }

    @Test
    public void testCreateJobValidInput() throws Exception, MissingDataException {
        JobRequest jobR = new JobRequest();
        jobR.setRollOutId("1");
        when(jobService.createJob(any(Job.class))).thenReturn(Job.getJobFromRequest(jobR));
        assertNotNull(jobController.createJob(jobR));
        Job result = jobController.createJob(jobR);
        assertEquals(result.getRollOutId(), "1");

    }

    @Test(expected = MissingDataException.class)
    public void testDeleteJobException() throws MissingDataException {
        String id = "1";
        doThrow(new MissingDataException("could not find job")).when(jobService).deleteJob(any(String.class));
        jobController.deleteJob(id);
    }

    @Test
    public void testDeleteJobValid() throws Exception, MissingDataException {
        String id = "1";
        doNothing().when(jobService).deleteJob(any(String.class));
        jobController.deleteJob(id);
    }

    @Test(expected = MissingDataException.class)
    public void testCancelJobWithException() throws Exception, MissingDataException {
        doThrow(new MissingDataException("could not find job")).when(jobService).cancelJob(any(String.class));
        jobController.cancelJob("1");
    }

    @Test
    public void testCancelJobValid() throws Exception, MissingDataException {
        doNothing().when(jobService).cancelJob(any(String.class));
        jobController.cancelJob("1");
    }


    @Test
    public void testListJobs() throws Exception {
        List<Job> jobList = new ArrayList<>();
        jobList.add(churnJob());
        jobList.add(churnJob());
        jobList.add(churnJob());
        PageImpl<Job> jobPage = new PageImpl<>(jobList);

        when(jobService.listJobs(any(Pageable.class), any(String.class))).thenReturn(jobPage);
        assertNotNull(jobController.listJobs(null, null));
        assertEquals(jobController.listJobs(null, null).getTotalElements(), jobPage.getTotalElements());
        assertEquals(jobController.listJobs(null, null).getTotalPages(), jobPage.getTotalPages());
    }

    @Test(expected = MissingDataException.class)
    public void testJobDetailsException() throws Exception, MissingDataException {
        when(jobService.jobDetails(any(String.class))).thenThrow(new MissingDataException("Could not find job with Id"));
        jobController.jobDetails("1");
    }

    @Test
    public void testJobDetails() throws Exception, MissingDataException {
        Job job = churnJob();
        when(jobService.jobDetails(any(String.class))).thenReturn(job);
        assertNotNull(jobController.jobDetails("1"));
        assertEquals(jobController.jobDetails("1"), job);
    }

    @Test
    public void testJobStatusesForJob() throws Exception {
        List<JobStatus> jobList = new ArrayList<>();
        jobList.add(churnJobStatus());
        jobList.add(churnJobStatus());
        jobList.add(churnJobStatus());
        when(jobService.jobStatusForJob(any(String.class))).thenReturn(jobList);
        assertNotNull(jobController.jobStatusesForJob("1"));
        assertEquals(jobController.jobStatusesForJob("1"), jobList);
    }
}
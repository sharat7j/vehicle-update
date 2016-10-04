package com.tesla.code.controller;

import com.tesla.code.beans.JobStatus;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.service.JobStatusService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static com.tesla.code.util.Helpers.churnJobStatus;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class JobStatusControllerTest {

    @Mock
    JobStatusService jobStatusService;

    @InjectMocks
    JobStatusController jobStatusController = new JobStatusController(jobStatusService);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = MissingDataException.class)
    public void testCreateJobStatusException() throws Exception, MissingDataException {
        when(jobStatusService.createJobStatus(any(JobStatus.class))).thenThrow(new MissingDataException("missing job status payload"));
        jobStatusController.createJobStatus(null);
    }

    @Test
    public void testCreateJobValidInput() throws Exception, MissingDataException {
        when(jobStatusService.createJobStatus(any(JobStatus.class))).thenReturn(new JobStatus());
        assertNotNull(jobStatusController.createJobStatus(new JobStatus()));
    }

    @Test
    public void testJobStatusList() throws Exception {

        List<JobStatus> jobStatusList = new ArrayList<>();
        jobStatusList.add(churnJobStatus());
        jobStatusList.add(churnJobStatus());
        jobStatusList.add(churnJobStatus());
        PageImpl<JobStatus> jobPage = new PageImpl<>(jobStatusList);
        when(jobStatusService.listStatus(any(Pageable.class), any(String.class), any(String.class))).thenReturn(jobPage);

        assertNotNull(jobStatusController.listJobStatuses(null, null, null));
        assertEquals(jobStatusController.listJobStatuses(null, null, null), jobPage);
    }


}
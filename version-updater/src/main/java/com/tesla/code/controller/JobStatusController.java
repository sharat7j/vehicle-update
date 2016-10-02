package com.tesla.code.controller;

import com.tesla.code.beans.JobStatus;
import com.tesla.code.service.JobStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/v1")
public class JobStatusController {

    JobStatusService jobStatusService;

    @Autowired
    public JobStatusController(JobStatusService jobStatusService) {
        this.jobStatusService = jobStatusService;
    }

    private final Logger logger = Logger.getLogger(String.valueOf(JobStatusController.class));

    @RequestMapping(value = "/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JobStatus createJobStatus(@RequestBody JobStatus jobStatus) {
        return jobStatusService.createJobStatus(jobStatus);
    }

    @RequestMapping(value = "/statusList", method = RequestMethod.GET)
    public Page<JobStatus> listJobStatuses(Pageable pageable) {
        return jobStatusService.listStatus(pageable);
    }

}

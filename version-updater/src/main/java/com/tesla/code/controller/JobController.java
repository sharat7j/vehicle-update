package com.tesla.code.controller;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/v1")
public class JobController {
    private final Logger logger = Logger.getLogger(String.valueOf(JobController.class));

    JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @RequestMapping(value = "/job", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Job createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @RequestMapping(value = "/job", method = RequestMethod.DELETE)
    public void deleteJob(@RequestParam(value = "id") String identifier) {
        jobService.deleteJob(identifier);
    }

    @RequestMapping(value = "/cancelJob", method = RequestMethod.GET)
    public Job cancelJob(@RequestParam(value = "id") String identifier) {
        return jobService.cancelJob(identifier);
    }

    @RequestMapping(value = "/listJobs", method = RequestMethod.GET)
    public Page<Job> listJobs(Pageable pageable) {
        return jobService.listJobs(pageable);
    }

    @RequestMapping(value = "/job", method = RequestMethod.GET)
    public Job jobDetails(@RequestParam(value = "id") String identifier) {
        return jobService.jobDetails(identifier);
    }

    @RequestMapping(value = "/statuses", method = RequestMethod.GET)
    public List<JobStatus> jobStatusesForJob(@RequestParam(value = "id") String identifier, Pageable pageable) {
        return jobService.jobStatusForJob(identifier);
    }


}

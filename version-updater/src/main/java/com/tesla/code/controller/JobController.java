package com.tesla.code.controller;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/v1")
public class JobController {
    private final Logger logger = Logger.getLogger(String.valueOf(JobController.class));

    private JobService jobService;
    private HttpServletResponse response;

    @Autowired
    public JobController(JobService jobService, HttpServletResponse response) {
        this.jobService = jobService;
        this.response = response;
    }

    @RequestMapping(value = "/job", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Job createJob(@RequestBody Job job) {
        if(job.getRollOutId() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
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
    public Page<Job> listJobs(Pageable pageable, @RequestParam(value = "rollOutId", required = false) String rollOutId) {
        return jobService.listJobs(pageable, rollOutId);
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

package com.tesla.code.controller;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.exceptions.UniquenessException;
import com.tesla.code.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API interfaces that operate on Job
 */
@RestController
@RequestMapping(value = "/v1")
public class JobController {
    private JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    /**
     * Interface to create a new Job associated to a roll out.
     *
     * @param job The serialized version of the Job entity containing the information for the job.
     * @return The serialized version of the Job object as received from the DB.
     * @throws MissingDataException Thrown if the required data is not available to do the association to a roll out
     * @throws UniquenessException Thrown if the job is a duplicate of an existing job with the same name.
     */
    @RequestMapping(value = "/job", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Job createJob(@RequestBody Job job) throws MissingDataException, UniquenessException {
        if(job == null || job.getRollOutId() == null) {
            throw new MissingDataException("JSON must contain the Roll Out ID to which this job is to be associated");
        }
        return jobService.createJob(job);
    }

    /**
     * Delete an existing Job
     *
     * @param identifier The Identifier that uniquely maps to a Job
     * @throws MissingDataException thrown if no job exists for deletion
     */
    @RequestMapping(value = "/job", method = RequestMethod.DELETE)
    public void deleteJob(@RequestParam(value = "id") String identifier) throws MissingDataException {
        jobService.deleteJob(identifier);
    }

    /**
     * Cancel a Job which is in the CREATED, DOWNLOADING state. The operation has no effect if the job is not in either
     * of those two states.
     *
     * @param identifier The identifier that uniquely maps to a Job
     * @throws MissingDataException thrown if no job exists with that identifier
     */
    @RequestMapping(value = "/cancelJob", method = RequestMethod.GET)
    public void cancelJob(@RequestParam(value = "id") String identifier) throws MissingDataException {
        jobService.cancelJob(identifier);
    }

    /**
     * Lists all jobs with supported filtering for pagination and specific to a given Roll out
     * @param pageable The page object automatically interpreted by Spring. This interprets parameters
     *                 like page=?&size=? to enable pagination for lists
     * @param rollOutId The identifier that uniquely maps to a given roll out
     * @return The list of Jobs filtered by rollOutId optionally or by the pagination attributes
     */
    @RequestMapping(value = "/listJobs", method = RequestMethod.GET)
    public Page<Job> listJobs(Pageable pageable, @RequestParam(value = "rollOutId", required = false) String rollOutId) {
        return jobService.listJobs(pageable, rollOutId);
    }

    /**
     * Retrieve the details of a specific job
     *
     * @param identifier The unique identifier that maps to a specific job
     * @return The Job object containing the details of the job
     * @throws MissingDataException thrown if no job exists that matches the unique identifier
     */
    @RequestMapping(value = "/job", method = RequestMethod.GET)
    public Job jobDetails(@RequestParam(value = "id") String identifier) throws MissingDataException {
        return jobService.jobDetails(identifier);
    }

    /**
     * Lists all job status received for a specific job ordered by descending order of timestamp i.e. most recent status
     * first.
     * @param identifier The unique identifier that maps to a specific Job
     * @return The list of job status that belong to a specific job.
     */
    @RequestMapping(value = "/statuses", method = RequestMethod.GET)
    public List<JobStatus> jobStatusesForJob(@RequestParam(value = "id") String identifier) {
        return jobService.jobStatusForJob(identifier);
    }


}

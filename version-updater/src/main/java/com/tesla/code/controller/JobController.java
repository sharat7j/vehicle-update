package com.tesla.code.controller;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.exceptions.UniquenessException;
import com.tesla.code.service.JobService;
import io.swagger.annotations.*;
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
@Api(value = "Job Controller", tags = "Job Controller", description = "Interfaces that would support operations on a Job")
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
     * @throws UniquenessException  Thrown if the job is a duplicate of an existing job with the same name.
     */
    @ApiOperation(value = "Create a new Job", notes = "Creates a new Job associated to a roll out. The Jobs need to " +
            "have a unique name", response = Job.class)
    @RequestMapping(value = "/job", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Job.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class)
    })
    public Job createJob(@ApiParam(value = "The serialized Job JSON") @RequestBody Job job) throws MissingDataException, UniquenessException {
        if (job == null || job.getRollOutId() == null) {
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
    @ApiOperation(value = "Delete a Job", notes = "Deletes a specific job identified by the unique identifier")
    @RequestMapping(value = "/job", method = RequestMethod.DELETE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public void deleteJob(@ApiParam(value = "The unique identifier of a Job") @RequestParam(value = "id") String identifier) throws MissingDataException {
        jobService.deleteJob(identifier);
    }

    /**
     * Cancel a Job which is in the CREATED, DOWNLOADING state. The operation has no effect if the job is not in either
     * of those two states.
     *
     * @param identifier The identifier that uniquely maps to a Job
     * @throws MissingDataException thrown if no job exists with that identifier
     */
    @ApiOperation(value = "Cancel an existing Job", notes = "Cancel an existing Job only if it is in the CREATED or DOWNLOADING state")
    @RequestMapping(value = "/cancelJob", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public void cancelJob(@ApiParam(value = "The unique identifier to a Job")
                          @RequestParam(value = "id") String identifier) throws MissingDataException {
        jobService.cancelJob(identifier);
    }

    /**
     * Lists all jobs with supported filtering for pagination and specific to a given Roll out
     *
     * @param pageable  The page object automatically interpreted by Spring. This interprets parameters
     *                  like page=?&size=? to enable pagination for lists
     * @param rollOutId The identifier that uniquely maps to a given roll out
     * @return The list of Jobs filtered by rollOutId optionally or by the pagination attributes
     */
    @ApiOperation(value = "List all Jobs", notes = "List all Jobs and optionally allow filtering by a specific RollOut",
            response = Job.class, responseContainer = "List")
    @RequestMapping(value = "/listJobs", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns a Page<Job> object containing the pagination attributes"),
    })
    public Page<Job> listJobs(@ApiParam(hidden = true) Pageable pageable,
                              @ApiParam(value = "The identifier for a specific roll out")
                              @RequestParam(value = "rollOutId", required = false) String rollOutId) {
        return jobService.listJobs(pageable, rollOutId);
    }

    /**
     * Retrieve the details of a specific job
     *
     * @param identifier The unique identifier that maps to a specific job
     * @return The Job object containing the details of the job
     * @throws MissingDataException thrown if no job exists that matches the unique identifier
     */
    @ApiOperation(value = "Get Job details", notes = "Get details of a specific Job")
    @RequestMapping(value = "/job", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Job details for a specific Job", response = Job.class),
            @ApiResponse(code = 400, message = "Could not locate the Job with the specified identifier",
                    response = String.class)
    })
    public Job jobDetails(@ApiParam(value = "The unique identifier for a specific Job")
                          @RequestParam(value = "id") String identifier) throws MissingDataException {
        return jobService.jobDetails(identifier);
    }

    /**
     * Lists all job status received for a specific job ordered by descending order of timestamp i.e. most recent status
     * first.
     *
     * @param identifier The unique identifier that maps to a specific Job
     * @return The list of job status that belong to a specific job.
     */
    @ApiOperation(value = "Get list of status messages for a job", notes = "Retrieve all status messages obtained " +
            "for the job in descending order of UTC timestamp", response = JobStatus.class, responseContainer = "List")
    @RequestMapping(value = "/statuses", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = JobStatus.class),
    })
    public List<JobStatus> jobStatusesForJob(@ApiParam(value = "The unique identifier for a Job")
                                             @RequestParam(value = "id") String identifier) {
        return jobService.jobStatusForJob(identifier);
    }


}

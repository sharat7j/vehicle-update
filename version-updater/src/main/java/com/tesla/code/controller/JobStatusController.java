package com.tesla.code.controller;

import com.tesla.code.beans.JobStatus;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.service.JobStatusService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Job Status Controller", tags = "Job Status Controller", description = "The interfaces that support all " +
        "operations specific to JobStatus messages for a job")
@RequestMapping(value = "/v1")
public class JobStatusController {

    private JobStatusService jobStatusService;

    @Autowired
    public JobStatusController(JobStatusService jobStatusService) {
        this.jobStatusService = jobStatusService;
    }

    /**
     * Create a new Job Status that is associate to a specific job. The job Id is added in the serialized JSON object
     * of JobStatus
     *
     * @param jobStatus The JobStatus serialized JSON that containing the Job Id to which this status is associated
     * @return The JobStatus object as stored with the unique identifier to easily retrieve this later.
     * @throws MissingDataException thrown if the job status JSON is null or if the Job Id is not present.
     */
    @ApiOperation(value = "Create a new Job Status", notes = "Creates a new Job status for a job if its not already " +
            "in the same state as what is in the update")
    @RequestMapping(value = "/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = JobStatus.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class)
    })
    public JobStatus createJobStatus(@ApiParam(value = "The JobStatus JSON message")
                                     @RequestBody JobStatus jobStatus) throws MissingDataException {
        return jobStatusService.createJobStatus(jobStatus);
    }

    /**
     * The list of Job status with additional filtering options. Includes pagination support.
     *
     * @param pageable  The pageination attributes composed in a single object by spring filters.
     * @param rollOutId Supports filtering the list of job status specific to a single roll out identified by the
     *                  roll out identifier
     * @param jobId     Supports filtering the list of job status specific to a single job identified by the job identifier.
     * @return The paginated list of JobStatus objects upon applying the corresponding filters
     */
    @ApiOperation(value = "Get the list of Job Status messages", notes = "Retrieve job status messages and filter " +
            "optionally by RollOut and/or Job", response = JobStatus.class, responseContainer = "List")
    @RequestMapping(value = "/statusList", method = RequestMethod.GET)
    @ApiResponse(code = 200, message = "Success")
    public Page<JobStatus> listJobStatuses(@ApiParam(hidden = true) Pageable pageable,
                                           @ApiParam(value = "The identifier to a specific RollOut that this list " +
                                                   "needs to be filtered for")
                                           @RequestParam(value = "rollOutId", required = false) String rollOutId,
                                           @ApiParam(value = "The identifier for a specific Job that this list " +
                                                   "needs to be filtered for")
                                           @RequestParam(value = "jobId", required = false) String jobId) {
        return jobStatusService.listStatus(pageable, rollOutId, jobId);
    }

}

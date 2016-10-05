package com.tesla.code.service;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.exceptions.InvalidDataException;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.repository.JobRepository;
import com.tesla.code.repository.JobStatusRepository;
import com.tesla.code.utils.JobState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JobStatusService {

    private JobStatusRepository jobStatusRepository;
    private JobRepository jobRepository;

    @Autowired
    public JobStatusService(JobRepository jobRepository, JobStatusRepository jobStatusRepository) {
        this.jobStatusRepository = jobStatusRepository;
        this.jobRepository = jobRepository;
    }

    public JobStatus createJobStatus(JobStatus status) throws MissingDataException, InvalidDataException {
        if(status == null) {
            throw new MissingDataException("Status object received was null. Cannot create new Job Status");
        }
        if(status.getJobId() == null) {
            throw new MissingDataException("Missing jobId in the JSON to create JobStatus. This is a required field");
        }
        Job job = jobRepository.findOne(status.getJobId());
        if(job == null) {
            throw new MissingDataException("Job with identifier " + status.getJobId() + " not found");
        }
        if(status.getState().equals(JobState.CANCELLED) || status.getState().equals(JobState.UNKNOWN)) {
            throw new InvalidDataException("Invalid Job State found in request. Must be one of CREATED, DOWNLOADING, INSTALLING, SUCCEEDED or FAILED");
        }
        // there will always be atleast one Job status for a  given job.
        JobStatus currentStatus = jobRepository.getJobStatusList(job.getId()).get(0);
        // Accept only if the incoming state is a logical next step from the existing state the Job is in.
        if(!currentStatus.getState().getNextState().equals(status.getState())) {
            throw new InvalidDataException("New Job State does not logically follow the existing state of the Job. Existing state: " + currentStatus.getState());
        } else {
            status.setJob(job);
            status.setTimestamp(Instant.now().getEpochSecond());
            return jobStatusRepository.save(status);
        }
    }

    private class JobStatusConverter implements Converter<JobStatus, JobStatus> {
        public JobStatus convert(JobStatus jobStatus) {
            jobStatus.setJobId(jobStatus.getJob().getId());
            return jobStatus;
        }
    }
    public Page<JobStatus> listStatus(Pageable pageable, String rollOutId, String jobId) {
        if(rollOutId!=null && jobId != null) {
            return jobStatusRepository.getStatusListForRolloutAndJob(pageable, rollOutId, jobId).map(new JobStatusConverter());
        } else if(rollOutId != null) {
            return jobStatusRepository.getStatusListForRollOut(pageable, rollOutId).map(new JobStatusConverter());

        } else if(jobId != null) {
            return jobStatusRepository.getStatusListForJob(pageable, jobId).map(new JobStatusConverter());

        } else {
            return jobStatusRepository.getStatusList(pageable).map(new JobStatusConverter());
        }

    }
}

package com.tesla.code.service;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.repository.JobRepository;
import com.tesla.code.repository.JobStatusRepository;
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

    public JobStatus createJobStatus(JobStatus status) {
        if(status == null) {
            return null;
        }
        status.setJob(jobRepository.findOne(status.getJobId()));
        status.setTimestamp(Instant.now().getEpochSecond());
        return jobStatusRepository.save(status);
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

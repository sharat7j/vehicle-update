package com.tesla.code.service;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.repository.JobRepository;
import com.tesla.code.repository.JobStatusRepository;
import com.tesla.code.utils.JobState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class JobService {

    JobRepository jobRepository;
    JobStatusRepository jobStatusRepository;

    @Autowired
    public JobService(JobRepository jobRepository, JobStatusRepository jobStatusRepository) {
        this.jobRepository = jobRepository;
        this.jobStatusRepository = jobStatusRepository;
    }

    public Job createJob(Job job) {
        jobRepository.save(job);
        JobStatus status = new JobStatus(JobState.CREATED, job);
        jobStatusRepository.save(status);
        return job;
    }

    public Job cancelJob(String identifier) {
        Job job = jobRepository.findOne(identifier);
        List<JobStatus> status = jobRepository.getJobStatusList(identifier);
        if (!status.isEmpty()) {
            JobStatus st = status.get(0);
            if (st.getState() == JobState.CREATED || st.getState() == JobState.DOWNLOADING) {
                st.setTimestamp(Instant.now());
                st.setState(JobState.CANCELLED);
                jobStatusRepository.save(st);
            }
        }
        return job;
    }

    public Page<Job> listJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    public Job jobDetails(String identifier) {
        Job job = jobRepository.findOne(identifier);
        List<JobStatus> statuses = jobRepository.getJobStatusList(identifier);
        job.setJobStatusList(statuses);
        return job;
    }

    public List<JobStatus> jobStatusForJob(String identifier) {
        return jobRepository.getJobStatusList(identifier);
    }

    public void deleteJob(String identifier) {
        jobRepository.delete(identifier);
    }
}

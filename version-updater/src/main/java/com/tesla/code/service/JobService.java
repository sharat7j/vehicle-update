package com.tesla.code.service;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.repository.JobRepository;
import com.tesla.code.repository.JobStatusRepository;
import com.tesla.code.repository.RollOutRepository;
import com.tesla.code.utils.JobState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {

    private JobRepository jobRepository;
    private JobStatusRepository jobStatusRepository;
    private RollOutRepository rollOutRepository;

    @Autowired
    public JobService(JobRepository jobRepository, JobStatusRepository jobStatusRepository, RollOutRepository rollOutRepository) {
        this.jobRepository = jobRepository;
        this.jobStatusRepository = jobStatusRepository;
        this.rollOutRepository = rollOutRepository;
    }

    public Job createJob(Job job) {
        job.setRollOut(rollOutRepository.findOne(job.getRollOutId()));
        jobRepository.save(job);
        JobStatus status = new JobStatus(JobState.CREATED, job);
        status.setJobId(job.getId());
        jobStatusRepository.save(status);
        List<JobStatus> statuses = job.getJobStatusList();
        statuses.add(status);
        job.setJobStatusList(statuses);
        return job;
    }

    public Job cancelJob(String identifier) {
        Job job = jobRepository.findOne(identifier);
        if(job == null) {
            return null;
        }
        List<JobStatus> status = jobRepository.getJobStatusList(identifier);
        if (!status.isEmpty()) {
            JobStatus st = status.get(0);
            if (st.getState() == JobState.CREATED || st.getState() == JobState.DOWNLOADING) {
                JobStatus cancelStatus = new JobStatus(JobState.CANCELLED, job);
                cancelStatus.setTimestamp(Instant.now().getEpochSecond());
                jobStatusRepository.save(cancelStatus);
            }
        }
        return job;
    }


    private class JobConverter implements Converter<Job, Job> {
        public Job convert(Job job) {
            job.setJobStatusList(jobRepository.getJobStatusList(job.getId()));
            job.setRollOutId(job.getRollOut().getId());
            List<JobStatus> statuses = new ArrayList<>();
            for(JobStatus status: job.getJobStatusList()) {
                status.setJobId(status.getJob().getId());
                statuses.add(status);
            }
            job.setJobStatusList(statuses);
            return job;
        }
    }

    public Page<Job> listJobs(Pageable pageable, String rollOutId) {
        if (rollOutId != null) {
            return jobRepository.getJobsForRollOut(pageable, rollOutId).map(new JobConverter());
        }
        else {
            return jobRepository.findAllSorted(pageable).map(new JobConverter());
        }

    }

    public Job jobDetails(String identifier) {
        Job job = jobRepository.findOne(identifier);
        if (job == null) {
            return null;
        }
        List<JobStatus> statuses = jobRepository.getJobStatusList(identifier);
        job.setJobStatusList(statuses);
        job.setRollOutId(job.getRollOut().getId());
        return job;
    }

    public List<JobStatus> jobStatusForJob(String identifier) {
        return jobRepository.getJobStatusList(identifier);
    }

    public void deleteJob(String identifier) {
        Job job = jobRepository.findOne(identifier);
        if(job == null) {
            return;
        }
        job.setRollOut(null);
        jobRepository.delete(job);
    }
}

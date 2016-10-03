package com.tesla.code.service;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.beans.RollOut;
import com.tesla.code.controller.JobStatusController;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.exceptions.UniquenessException;
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
import java.util.logging.Logger;

@Service
public class JobService {

    private JobRepository jobRepository;
    private JobStatusRepository jobStatusRepository;
    private RollOutRepository rollOutRepository;
    private final Logger logger = Logger.getLogger(String.valueOf(JobStatusController.class));

    @Autowired
    public JobService(JobRepository jobRepository, JobStatusRepository jobStatusRepository, RollOutRepository rollOutRepository) {
        this.jobRepository = jobRepository;
        this.jobStatusRepository = jobStatusRepository;
        this.rollOutRepository = rollOutRepository;
    }

    public Job createJob(Job job) throws MissingDataException, UniquenessException {
        RollOut rollOut = rollOutRepository.findOne(job.getRollOutId());
        if (rollOut == null) {
            throw new MissingDataException("No roll out found with identifier: " + job.getRollOutId());
        }
        job.setRollOut(rollOut);
        // check if job exists
        if (jobRepository.nameExists(job.getName()) != null) {
            throw new UniquenessException("Job uniqueness constraint has been violated. Please use a different job name");
        }
        jobRepository.save(job);
        JobStatus status = new JobStatus(JobState.CREATED, job);
        status.setJobId(job.getId());
        jobStatusRepository.save(status);
        List<JobStatus> statuses = job.getJobStatusList();
        statuses.add(status);
        job.setJobStatusList(statuses);
        return job;
    }

    public void cancelJob(String identifier) throws MissingDataException {
        Job job = jobRepository.findOne(identifier);
        if (job == null) {
            throw new MissingDataException("No job found with identifier: " + identifier);
        }
        List<JobStatus> status = jobRepository.getJobStatusList(identifier);
        if (!status.isEmpty()) {
            JobStatus st = status.get(0);
            if (st.getState() == JobState.CREATED || st.getState() == JobState.DOWNLOADING) {
                JobStatus cancelStatus = new JobStatus(JobState.CANCELLED, job);
                cancelStatus.setTimestamp(Instant.now().getEpochSecond());
                jobStatusRepository.save(cancelStatus);
            } else {
                logger.info("Cancel was called on a job whose current state is " + st.getState());
            }
        }
    }


    private class JobConverter implements Converter<Job, Job> {
        public Job convert(Job job) {
            job.setJobStatusList(jobRepository.getJobStatusList(job.getId()));
            job.setRollOutId(job.getRollOut().getId());
            List<JobStatus> statuses = new ArrayList<>();
            for (JobStatus status : job.getJobStatusList()) {
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
        } else {
            return jobRepository.findAllSorted(pageable).map(new JobConverter());
        }

    }

    public Job jobDetails(String identifier) throws MissingDataException {
        Job job = jobRepository.findOne(identifier);
        if (job == null) {
            throw new MissingDataException("Job with identifier " + identifier + " not found");
        }
        List<JobStatus> statuses = jobRepository.getJobStatusList(identifier);
        job.setJobStatusList(statuses);
        job.setRollOutId(job.getRollOut().getId());
        return job;
    }

    public List<JobStatus> jobStatusForJob(String identifier) {
        return jobRepository.getJobStatusList(identifier);
    }

    public void deleteJob(String identifier) throws MissingDataException {
        Job job = jobRepository.findOne(identifier);
        if (job == null) {
            throw new MissingDataException("Could not find job with identifier: " + identifier);
        }
        // ensure we dont violate referential integrity
        job.setRollOut(null);
        jobRepository.delete(job);
    }
}

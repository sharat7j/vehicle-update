package com.tesla.code.service;

import com.tesla.code.beans.JobStatus;
import com.tesla.code.repository.JobStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class JobStatusService {

    JobStatusRepository jobStatusRepository;

    @Autowired
    public JobStatusService(JobStatusRepository jobStatusRepository) {
        this.jobStatusRepository = jobStatusRepository;
    }

    public JobStatus createJobStatus(JobStatus status) {
        return jobStatusRepository.save(status);
    }
    public Page<JobStatus> listStatus(Pageable pageable) {
        return jobStatusRepository.getStatusList(pageable);
    }
}

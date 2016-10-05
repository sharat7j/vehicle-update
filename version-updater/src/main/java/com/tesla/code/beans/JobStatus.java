package com.tesla.code.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tesla.code.beans.request.JobStatusRequest;
import com.tesla.code.utils.JobState;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;

/**
 * Bean containing the definition of a JobStatus.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(nullable = false)
    private JobState state;
    @Column(nullable = false, unique = true)
    private Long timestamp;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "job_id")
    @JsonIgnore
    private Job job;
    @Transient
    private String jobId;

    public JobStatus() {
        this.timestamp = Instant.now().getEpochSecond();

    }
    public JobStatus(JobState state, Job job) {
        this.state = state;
        this.job = job;
        this.timestamp = Instant.now().getEpochSecond();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public static JobStatus getJobStatusFromRequest(JobStatusRequest jobStatusRequest) {
        if(jobStatusRequest == null) {
            return null;
        }
        JobStatus status = new JobStatus();
        status.setState(jobStatusRequest.getState());
        status.setJobId(jobStatusRequest.getJobId());
        return status;
    }
}

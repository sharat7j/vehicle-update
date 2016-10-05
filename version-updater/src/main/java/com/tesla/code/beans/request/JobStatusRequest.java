package com.tesla.code.beans.request;

import com.tesla.code.utils.JobState;

public class JobStatusRequest {

    JobState state;
    String jobId;

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}


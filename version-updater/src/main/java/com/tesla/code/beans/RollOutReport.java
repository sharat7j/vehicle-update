package com.tesla.code.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tesla.code.utils.JobState;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RollOutReport {

    private Map<JobState, Integer> currentState;
    private Long totalJobCount;

    public Map<JobState, Integer> getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Map<JobState, Integer> currentState) {
        this.currentState = currentState;
    }

    public Long getTotalJobCount() {
        return totalJobCount;
    }

    public void setTotalJobCount(Long totalJobCount) {
        this.totalJobCount = totalJobCount;
    }
}

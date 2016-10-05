package com.tesla.code.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The enum that maintains the mapping of the Job States. The order of the states are important as this is taken into
 * account when determining what the next valid state should be with the exception of the CANCELLED and UNKNOWN states.
 * <p>
 * This in essence is used as a state machine to validate in what sequence the JobStatus requests are to be honored or
 * accepted. E.g. We would not accept a status confirming a Job when to SUCCEEDED when the current STATE is CREATED. It
 * has to go through DOWNLOADING and INSTALLING before SUCCEEDED OR FAILED states.
 */
public enum JobState {
    CREATED("created"),
    DOWNLOADING("downloading"),
    INSTALLING("installing"),
    SUCCEEDED("succeeded"),
    FAILED("failed"),
    CANCELLED("cancelled"),
    UNKNOWN("unknown");

    private String value;

    JobState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static JobState getEnumFromString(final String str) {
        for (JobState s : JobState.values()) {
            if (s.getValue().equalsIgnoreCase(str)) {
                return s;
            }
        }
        return JobState.UNKNOWN;
    }

    public JobState getNextState() {
        int index = this.ordinal();
        JobState[] states = JobState.values();
        return states[(index + 1) % JobState.values().length];
    }

    public static List<JobState> decisionStates() {
        List<JobState> states = new ArrayList<>();
        states.add(JobState.SUCCEEDED);
        states.add(JobState.FAILED);
        return states;
    }
}

package com.tesla.code.utils;

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
}

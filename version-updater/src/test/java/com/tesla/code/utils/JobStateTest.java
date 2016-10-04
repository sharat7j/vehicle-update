package com.tesla.code.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class JobStateTest {

    @Test
    public void testJobStateGetter() throws Exception {
        for(JobState state: JobState.values()) {
            assertEquals(state, JobState.valueOf(state.getValue().toUpperCase()));
        }
    }

    @Test
    public void testGetUnknownEnumFromString() throws Exception {
        String tester = "new-state";
        assertEquals(JobState.UNKNOWN, JobState.getEnumFromString(tester));
    }

    @Test
    public void testGetValidEnumFromString() throws Exception {
        String tester = "creAted";
        assertEquals(JobState.CREATED, JobState.getEnumFromString(tester));

    }
}
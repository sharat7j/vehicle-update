package com.tesla.code.util;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import com.tesla.code.beans.RollOut;
import com.tesla.code.utils.JobState;

import java.time.Instant;

public class Helpers {

    public static Job churnJob() {
        Long curTime = Instant.now().getEpochSecond();
        Job job = new Job();
        job.setName("Job Number :" + curTime);
        job.setRollOutId("1");
        job.setRollOut(churnRollOut());
        job.setSoftwareVersion(curTime.toString());
        job.setVehicleId("Vehicle: " + curTime % 1000);
        job.setId("a" + curTime);
        return job;
    }

    public static JobStatus churnJobStatus() {
        JobStatus status = new JobStatus();
        status.setJobId("1");
        status.setState(JobState.CREATED);
        return status;
    }

    public static RollOut churnRollOut() {
        RollOut rollOut = new RollOut();
        rollOut.setId("id" + Instant.now().getEpochSecond());
        Long curTime = Instant.now().getEpochSecond();
        rollOut.setName(curTime.toString());
        rollOut.setDescription("sample roll out");
        return rollOut;
    }

}

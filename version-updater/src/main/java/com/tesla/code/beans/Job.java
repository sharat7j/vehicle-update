package com.tesla.code.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(unique = true)
    private String name;
    private String softwareVersion;
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "rollout_id", referencedColumnName = "id", nullable = false)
    private RollOut rollOut;
    private String vehicleId;
    private Long date_created;
    @Transient
    private List<JobStatus> jobStatusList = new ArrayList<JobStatus>();
    @Transient
    private String rollOutId;


    public Job() {
        this.date_created = Instant.now().getEpochSecond();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public RollOut getRollOut() {
        return rollOut;
    }

    public void setRollOut(RollOut rollOut) {
        this.rollOut = rollOut;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getDate_created() {
        return date_created;
    }

    public void setDate_created(Long date_created) {
        this.date_created = date_created;
    }

    public List<JobStatus> getJobStatusList() {
        return jobStatusList;
    }

    public void setJobStatusList(List<JobStatus> jobStatusList) {
        this.jobStatusList = jobStatusList;
    }

    public String getRollOutId() {
        return rollOutId;
    }

    public void setRollOutId(String rollOutId) {
        this.rollOutId = rollOutId;
    }
}

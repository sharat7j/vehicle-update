package com.tesla.code.beans.request;

public class JobRequest {

    String name;
    String softwareVersion;
    String vehicleId;
    String rollOutId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRollOutId() {
        return rollOutId;
    }

    public void setRollOutId(String rollOutId) {
        this.rollOutId = rollOutId;
    }
}

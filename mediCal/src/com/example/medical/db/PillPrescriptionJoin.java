package com.example.medical.db;

/**
 * Created by meanheffry on 11/11/14.
 */
public class PillPrescriptionJoin {

    private long id;
    private long pillId;
    private long prescriptionId;
    private String time;
    private String lastModified;

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    private String pillName;

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPillId() {
        return pillId;
    }

    public void setPillId(long pillId) {
        this.pillId = pillId;
    }

    public long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return this.pillName + " at "+ this.time;
    }
}

package com.example.medical.db;

/**
 * Created by meanheffry on 12/11/14.
 */
public class MarReportEntry {
    private long id,marsId;
    private String lastModified,person,med,time,dosage;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMarsId() {
        return marsId;
    }

    public void setMarsId(long marsId) {
        this.marsId = marsId;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String toString() {
        return this.person + " was given "+ this.dosage+" of " +this.med;
    }
}

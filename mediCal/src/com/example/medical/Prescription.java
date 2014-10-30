package com.example.medical;

import java.util.Dictionary;

/**
 * Created by meanheffry on 10/30/14.
 */
public class Prescription {
    private long id;
    private String patient;
    private String pills;
    private String times;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getPatient() {
        return patient;
    }

    public void setPills(String pills) {
        this.pills = pills;
    }

    public String getPills() {
        return pills;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getTimes() {
        return times;
    }

    @Override
    public String toString() {
        return this.patient + " : "+ this.id;
    }
}

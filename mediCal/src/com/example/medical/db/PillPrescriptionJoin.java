package com.example.medical.db;

/**
 * Created by meanheffry on 11/11/14.
 */
public class PillPrescriptionJoin {

    private int id;
    private int pillId;
    private int prescriptionId;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPillId() {
        return pillId;
    }

    public void setPillId(int pillId) {
        this.pillId = pillId;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
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
        return "Pill Id "+ this.pillId +" Prescription Id "+ this.prescriptionId+ " at "+ this.time;
    }
}

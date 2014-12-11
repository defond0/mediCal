package com.example.medical.db;

import java.util.Date;
import java.util.Dictionary;

/**
 * Created by meanheffry on 10/30/14.
 */
public class Prescription {
    private long id;
    private String patient;



    private String lastModified;
    private byte[] rfid;

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

    public byte[] getRfid() {
        return rfid;
    }

    public void setRfid(byte[] rfid) {
        this.rfid = rfid;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return this.patient;
    }


}

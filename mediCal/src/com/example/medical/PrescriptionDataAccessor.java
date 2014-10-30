package com.example.medical;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meanheffry on 10/30/14.
 */
public class PrescriptionDataAccessor {
    private SQLiteDatabase db;
    private DbHelper helper;
    private String[] columns = {DbHelper.COLUMN_ID,DbHelper.COLUMN_PATIENT,
            DbHelper.COLUMN_PILLS,
            DbHelper.COLUMN_TIMES};

    public PrescriptionDataAccessor(Context context){
        helper = new DbHelper(context);
    }


    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    public Prescription createPrescription(String patient, String pills, String times ){
        ContentValues prescriptionsValues = new ContentValues();
        prescriptionsValues.put(DbHelper.COLUMN_PATIENT, patient);
        prescriptionsValues.put(DbHelper.COLUMN_PILLS, pills);
        prescriptionsValues.put(DbHelper.COLUMN_TIMES, times);
        long Idl = db.insert(DbHelper.TABLE_PILL, null, prescriptionsValues);
        String Ids = String.valueOf(Idl);
        String[] whereArgs = {Ids};
        Cursor c = db.query(DbHelper.TABLE_PRESCRIPTION,
                columns,DbHelper.COLUMN_ID + " = ?",
                whereArgs, null, null, null);
        c.moveToFirst();
        Prescription createdPrescription = cursorToPrescription(c);
        System.out.println("Creating prescription "+createdPrescription);
        c.close();
        return createdPrescription;
    }

    public Prescription cursorToPrescription(Cursor c){
        Prescription prescription = new Prescription();
        prescription.setId(c.getLong(0));
        prescription.setPatient(c.getString(1));
        prescription.setPills(c.getString(2));
        prescription.setTimes(c.getString(3));
        return prescription;
    }


    public List<Prescription> getAllPrescriptions(){
        List<Prescription> prescriptions = new ArrayList<Prescription>();
        Cursor c = db.query(DbHelper.TABLE_PILL,null,null,null,null,null,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            Prescription prescription = cursorToPrescription(c);
            prescriptions.add(prescription);
            c.moveToNext();
        }
        c.close();
        return prescriptions;
    }
}

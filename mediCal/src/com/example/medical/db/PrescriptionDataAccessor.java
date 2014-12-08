package com.example.medical.db;

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
         DbHelper.COLUMN_RFID};
    private Context context;

    public PrescriptionDataAccessor(Context context){
        helper = new DbHelper(context);
        this.context = context;
    }


    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    public Prescription createPrescription(String patient, byte[] rfid){
        ContentValues prescriptionsValues = new ContentValues();
        prescriptionsValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        prescriptionsValues.put(DbHelper.COLUMN_PATIENT, patient);
        prescriptionsValues.put(DbHelper.COLUMN_RFID, rfid);

        long Idl = db.insert(DbHelper.TABLE_PRESCRIPTION, null, prescriptionsValues);
        String Ids = String.valueOf(Idl);
        String[] whereArgs = {Ids};
        Cursor c = db.query(DbHelper.TABLE_PRESCRIPTION,
                null,DbHelper.COLUMN_ID + " = ?",
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
        prescription.setLastModified(c.getString(1));
        prescription.setPatient(c.getString(2));
        prescription.setRfid(c.getBlob(3));
        return prescription;
    }




    public List<Prescription> getAllPrescriptions(){
        List<Prescription> prescriptions = new ArrayList<Prescription>();
        Cursor c = db.query(DbHelper.TABLE_PRESCRIPTION,null,null,null,null,null,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            Prescription prescription = cursorToPrescription(c);
            prescriptions.add(prescription);
            c.moveToNext();
        }
        c.close();
        return prescriptions;
    }

    public void updatePrescription(long id, String patient, byte[] rfid){
        System.out.println("patient, "+ patient+" rfid, "+ rfid);
        ContentValues prescriptionsValues = new ContentValues();
        prescriptionsValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        prescriptionsValues.put(DbHelper.COLUMN_PATIENT, patient);
        prescriptionsValues.put(DbHelper.COLUMN_RFID, rfid);
        db.update(DbHelper.TABLE_PRESCRIPTION, prescriptionsValues, DbHelper.COLUMN_ID + " = " + id , null);

    }

    public Prescription getPrescriptionbyId(long id){
        String[] whereArgs = new String[]{String.valueOf(id)};
        Cursor c = db.query(DbHelper.TABLE_PRESCRIPTION,null,DbHelper.COLUMN_ID+ " = ?", whereArgs, null,null,null);
        c.moveToFirst();
        return cursorToPrescription(c);
    }

    public void deletePrescriptionSafe(Long id){
        String[] whereArgs = new String[]{String.valueOf(id)};
        Cursor c=db.query(DbHelper.TABLE_PRESCRIPTION, null,
                DbHelper.COLUMN_ID + " = ?", whereArgs, null, null, null);
        c.moveToFirst();
        if (!c.isAfterLast()) {
            Prescription p = cursorToPrescription(c);
            PrescriptionPillJoinDataAccessor JDA = new PrescriptionPillJoinDataAccessor(context);
            JDA.open();
            JDA.getAllJoins();
            ArrayList<PillPrescriptionJoin> joins = (ArrayList) JDA.getAllJoins();
            for (int j = 0; j < joins.size(); j++) {
                if (joins.get(j).getPrescriptionId() == id) {
                    System.out.println("Deleting join " + joins.get(j) + " associated with " + p.getPatient());
                    whereArgs = new String[]{String.valueOf(joins.get(j).getId())};
                    db.delete(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS, DbHelper.COLUMN_ID + " =?", whereArgs);
                }
            }
            System.out.println("Deleted Prescription " + p);
            whereArgs = new String[]{String.valueOf(p.getId())};
            db.delete(DbHelper.TABLE_PRESCRIPTION, DbHelper.COLUMN_ID + " =?", whereArgs);
        }
        else{
            System.out.println("da fuck?");
        }

    }
}

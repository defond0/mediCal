package com.example.medical;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meanheffry on 11/11/14.
 */
public class PrescriptionPillJoinDataAccessor{
    private SQLiteDatabase db;
    private DbHelper helper;
    private String[] columns = {DbHelper.COLUMN_ID,DbHelper.COLUMN_PILL_ID,
            DbHelper.COLUMN_PRESCRIPTION_ID,
            DbHelper.COLUMN_TIMES};
    public PrescriptionPillJoinDataAccessor(Context context){
        helper = new DbHelper(context);
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    public PillPrescriptionJoin createPPJoin(int pill, int prescription, String times){
        ContentValues joinValues = new ContentValues();
        joinValues.put(DbHelper.COLUMN_PILL_ID, pill);
        joinValues.put(DbHelper.COLUMN_PRESCRIPTION_ID, prescription);
        joinValues.put(DbHelper.COLUMN_TIMES, times);
        long Idl = db.insert(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS, null, joinValues);
        String Ids = String.valueOf(Idl);
        String[] whereArgs = {Ids};
        Cursor c = db.query(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS,
                columns,DbHelper.COLUMN_ID + " = ?",
                whereArgs, null, null, null);
        c.moveToFirst();
        PillPrescriptionJoin createdJoin = cursorToJoin(c);
        c.close();
        return  createdJoin;
    }

    public PillPrescriptionJoin cursorToJoin(Cursor c){
        PillPrescriptionJoin ppJoin = new PillPrescriptionJoin();
        ppJoin.setPillId(c.getInt(0));
        ppJoin.setPrescriptionId(c.getInt(1));
        ppJoin.setTime(c.getString(2));
        return ppJoin;
    }

    public List<PillPrescriptionJoin> getJoinsByPrescriptionId(int pId){
        List<PillPrescriptionJoin> joins = new ArrayList<PillPrescriptionJoin>();
        String Id = String.valueOf(pId);
        String[] whereArgs = {Id};
        Cursor c = db.query(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS,
                columns,DbHelper.COLUMN_PRESCRIPTION_ID + " =?",
                whereArgs,null,null,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            PillPrescriptionJoin join = cursorToJoin(c);
            joins.add(join);
            c.moveToNext();
        }
        c.close();
        return joins;
    }

    public List<PillPrescriptionJoin> getAllJoins(){
        List<PillPrescriptionJoin> joins = new ArrayList<PillPrescriptionJoin>();
        Cursor c = db.query(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS,null,null,null,null,null,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            PillPrescriptionJoin join = cursorToJoin(c);
            joins.add(join);
            c.moveToNext();
        }
        c.close();
        return joins;
    }

}

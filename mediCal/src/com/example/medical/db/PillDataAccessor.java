package com.example.medical.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.medical.db.DbHelper;
import com.example.medical.db.Pill;

/**
 * Created by meanheffry on 10/28/14.
 */
public class PillDataAccessor {
    private SQLiteDatabase db;
    private DbHelper helper;
    private String[] columns =
            {DbHelper.COLUMN_ID,
            DbHelper.COLUMN_PILLS,
            DbHelper.COLUMN_TUBE,
            DbHelper.COLUMN_LOAD

    };
    private Context pContext;

    public PillDataAccessor(Context context){
        helper = new DbHelper(context);
        pContext=context;
    }

    public void open() throws SQLException{
        db = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    public Pill createPill(String name, String tube, String dose, String load){
        ContentValues pillsValues = new ContentValues();
        pillsValues.put(DbHelper.COLUMN_TUBE, tube);
        pillsValues.put(DbHelper.COLUMN_NAME, name);
        pillsValues.put(DbHelper.COLUMN_DOSE, dose);
        pillsValues.put(DbHelper.COLUMN_LOAD, load);
        pillsValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        long Idl = db.insert(DbHelper.TABLE_PILL, null, pillsValues);
        String Ids = String.valueOf(Idl);
        String selectQuery = "SELECT * FROM "+DbHelper.TABLE_PILL +
                " WHERE " + DbHelper.COLUMN_ID + " = " + Idl;
        Cursor c = db.rawQuery(selectQuery,null);
        c.moveToFirst();
        Pill createdPill = cursorToPill(c);
        System.out.println("Creating pill "+createdPill);
        c.close();
        return createdPill;
    }

    public void deletePill(Pill pill){
        if ((Object)pill!=null){
            long id = pill.getId();
            System.out.println("Deleting pill " + pill);
            String[] whereArgs = new String[]{String.valueOf(id)};
            db.delete(DbHelper.TABLE_PILL, DbHelper.COLUMN_ID + " =?", whereArgs);
        }
    }

    private Pill cursorToPill(Cursor c){
        Pill pill = new Pill();
        pill.setId(c.getLong(0));
        pill.setLastModified(c.getString(1));
        pill.setTube(c.getString(2));
        pill.setName(c.getString(3));
        pill.setDose(c.getString(4));
        pill.setLoad(c.getString(5));
        return pill;

    }
    public Pill getPillByName(String name){
        Pill pill = null ;
        String selectQuery = "SELECT * FROM pill  WHERE name  = '"+ name +"'";
        Cursor c = db.rawQuery(selectQuery,null);
        if (c.moveToFirst()) {
            pill = cursorToPill(c);
        }
        c.close();
        return pill;
    }
    public Pill getPillById(long id){
        Pill pill = null ;
        String selectQuery = "SELECT * FROM pill  WHERE _id  = '"+ id +"'";
        Cursor c = db.rawQuery(selectQuery,null);
        if (c.moveToFirst()) {
            pill = cursorToPill(c);
        }
        c.close();
        return pill;
    }

    public void decrementPill(long id){
        Pill p = getPillById(id);
        ContentValues pillsValues = new ContentValues();
        pillsValues.put(DbHelper.COLUMN_TUBE, p.getTube());
        pillsValues.put(DbHelper.COLUMN_NAME, p.getName());
        pillsValues.put(DbHelper.COLUMN_DOSE, p.getDose());
        pillsValues.put(DbHelper.COLUMN_LOAD, Integer.getInteger(p.getLoad())-1);
        pillsValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        db.update(DbHelper.TABLE_PILL, pillsValues, DbHelper.COLUMN_ID + " = " + id , null);
    }

    public void updatePill(long id, String name, String tube, String dose, String load){
        ContentValues pillsValues = new ContentValues();
        pillsValues.put(DbHelper.COLUMN_TUBE, tube);
        pillsValues.put(DbHelper.COLUMN_NAME, name);
        pillsValues.put(DbHelper.COLUMN_DOSE, dose);
        pillsValues.put(DbHelper.COLUMN_LOAD, load);
        pillsValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        db.update(DbHelper.TABLE_PILL, pillsValues, DbHelper.COLUMN_ID + " = " + id , null);
    }

    public List<Pill> getAllPills(){
        List<Pill> pills = new ArrayList<Pill>();
        Cursor c = db.query(DbHelper.TABLE_PILL,null,null,null,null,null,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            Pill pill = cursorToPill(c);
            pills.add(pill);
            c.moveToNext();
        }
        c.close();
        return pills;
    }

    public void deletePillSafe(Long id) {
        String[] whereArgs = new String[]{String.valueOf(id)};
        Cursor c=db.query(DbHelper.TABLE_PILL, null,
                DbHelper.COLUMN_ID + " = ?", whereArgs, null, null, null);
        c.moveToFirst();
        if (!c.isAfterLast()) {
            Pill p = cursorToPill(c);
            PrescriptionPillJoinDataAccessor JDA = new PrescriptionPillJoinDataAccessor(pContext);
            JDA.open();
            ArrayList<PillPrescriptionJoin> joins = (ArrayList) JDA.getAllJoins();
            for (int j = 0; j < joins.size(); j++) {
                if (joins.get(j).getPillId() == id) {
                    System.out.println("Deleting join " + joins.get(j) + " associated with " + p.getName());
                    whereArgs = new String[]{String.valueOf(joins.get(j).getId())};
                    db.delete(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS, DbHelper.COLUMN_ID + " =?", whereArgs);
                }
            }
            System.out.println("Deleted Pill " + p);
            whereArgs = new String[]{String.valueOf(p.getId())};
            db.delete(DbHelper.TABLE_PILL, DbHelper.COLUMN_ID + " =?", whereArgs);
        }
        else{
            System.out.println("da fuck?");
        }

    }

}

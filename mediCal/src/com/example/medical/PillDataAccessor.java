package com.example.medical;

import java.util.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    public PillDataAccessor(Context context){
        helper = new DbHelper(context);
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
        pill.setTube(c.getString(1));
        pill.setName(c.getString(2));
        pill.setDose(c.getString(3));
        pill.setLoad(c.getString(4));
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
}

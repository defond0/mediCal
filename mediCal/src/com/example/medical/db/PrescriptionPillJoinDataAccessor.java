package com.example.medical.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.medical.db.DbHelper;
import com.example.medical.db.PillPrescriptionJoin;

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
    private Context joinContext;
    public PrescriptionPillJoinDataAccessor(Context context){
        helper = new DbHelper(context);
        joinContext=context;
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
        joinValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        long Idl = db.insert(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS, null, joinValues);
        String Ids = String.valueOf(Idl);
        String[] whereArgs = {Ids};
        Cursor c = db.query(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS,
                null,DbHelper.COLUMN_ID + " = ?",
                whereArgs, null, null, null);
        c.moveToFirst();
        PillPrescriptionJoin createdJoin = cursorToJoin(c);
        c.close();
        return  createdJoin;
    }

    public void updateJoin(long id,long pill, long prescription, String time){
        ContentValues joinValues = new ContentValues();
        joinValues.put(DbHelper.COLUMN_PILL_ID, pill);
        joinValues.put(DbHelper.COLUMN_PRESCRIPTION_ID, prescription);
        joinValues.put(DbHelper.COLUMN_TIMES, time);
        joinValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        db.update(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS, joinValues, DbHelper.COLUMN_ID + " = " + id , null);
    }

    public PillPrescriptionJoin cursorToJoin(Cursor c){
        PillDataAccessor pda = new PillDataAccessor(joinContext);
        pda.open();
        PillPrescriptionJoin ppJoin = new PillPrescriptionJoin();
        ppJoin.setId(c.getLong(0));
        ppJoin.setLastModified(c.getString(1));
        ppJoin.setPillId(c.getInt(2));
        ppJoin.setPrescriptionId(c.getInt(3));
        ppJoin.setTime(c.getString(4));
        Pill p = pda.getPillById(ppJoin.getPillId());
        System.out.println(ppJoin.getPillId());
        ppJoin.setPillName(p.getName());
        pda.close();
        return ppJoin;
    }

    public void delete(long joinId){
        System.out.println("Deleting join " + joinId);
        String[] whereArgs = new String[]{String.valueOf(joinId)};
        db.delete(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS, DbHelper.COLUMN_ID + " =?", whereArgs);
    }

    public PillPrescriptionJoin getJoinbyId(long joinId){
        String[] whereArgs = new String[]{String.valueOf(joinId)};
        System.out.println(whereArgs[0]);
        String selectQuery = "SELECT * FROM "+ DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS +" WHERE _id  = '"+ joinId +"'";
        Cursor c = db.rawQuery(selectQuery,null);
        System.out.println(c);
        c.moveToFirst();
        return cursorToJoin(c);
    }

    public List<PillPrescriptionJoin> getJoinsByPrescriptionId(long pId){
        List<PillPrescriptionJoin> joins = new ArrayList<PillPrescriptionJoin>();
        String Id = String.valueOf(pId);
        String[] whereArgs = {Id};
        Cursor c = db.query(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS,null,DbHelper.COLUMN_PRESCRIPTION_ID + " = ?",whereArgs,null,null,null);
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

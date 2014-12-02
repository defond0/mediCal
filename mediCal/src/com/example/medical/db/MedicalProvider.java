package com.example.medical.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meanheffry on 11/15/14.
 */
public class MedicalProvider extends ContentProvider {
    //db
    private DbHelper dbhelper;

    //uri matcher ids
    private static final int PILL = 1;
    private static final int PILL_ID = 2;
    private static final int PRESCRIPTION = 3;
    private static final int PRESCRIPTION_ID = 4;
    private static final int JOIN = 7;
    private static final int JOIN_ID = 8;

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String authority ="com.example.app.provider";
    private static final String PILL_BASE_PATH = "pill";
    private static final String PRESCRIPTION_BASE_PATH="prescription";
    private static final String JOIN_BASE_PATH="ppjoin";
   // private static final Uri CONTENT_URI = Uri.parse("content://"+authority+"/"+BASE_PATH);
    static {
        matcher.addURI(authority, PILL_BASE_PATH, PILL);
        matcher.addURI(authority, PILL_BASE_PATH+"/#", PILL_ID);
        matcher.addURI(authority, PRESCRIPTION_BASE_PATH, PRESCRIPTION);
        matcher.addURI(authority, PRESCRIPTION_BASE_PATH+"/#", PRESCRIPTION_ID);
        matcher.addURI(authority, JOIN_BASE_PATH, JOIN);
        matcher.addURI(authority, JOIN_BASE_PATH+"/#", JOIN_ID);

    }


    @Override
    public synchronized boolean onCreate() {
        dbhelper = new DbHelper(getContext());
        return false;
    }

    @Override
    public synchronized Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
        //checkColumns(projection);
        int uriType= matcher.match(uri);
        switch(uriType) {
            case PILL:
                qBuilder.setTables(dbhelper.TABLE_PILL);
                break;
            case PILL_ID:
                qBuilder.setTables(dbhelper.TABLE_PILL);
                qBuilder.appendWhere(dbhelper.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case PRESCRIPTION:
                qBuilder.setTables(dbhelper.TABLE_PRESCRIPTION);
                break;
            case PRESCRIPTION_ID:
                qBuilder.setTables(dbhelper.TABLE_PRESCRIPTION);
                qBuilder.appendWhere(dbhelper.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case JOIN:
                qBuilder.setTables(dbhelper.TABLE_JOIN_PRESCRIPTION_PILLS);
                break;
            case JOIN_ID:
                qBuilder.setTables(dbhelper.TABLE_JOIN_PRESCRIPTION_PILLS);
                qBuilder.appendWhere(dbhelper.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor c = qBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        c.setNotificationUri(getContext().getContentResolver(),uri);
        db.close();
        return c;
    }

    @Override
    public synchronized String getType(Uri uri) {
        return null;
    }

    @Override
    public synchronized Uri insert(Uri uri, ContentValues values) {
        int uriType = matcher.match(uri);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        values.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        long id = 0;
        switch(uriType) {
            case PILL:
                id = db.insert(dbhelper.TABLE_PILL,null,values);
                getContext().getContentResolver().notifyChange(uri,null);
                return Uri.parse(PILL_BASE_PATH+"/"+id);
            case PRESCRIPTION:
                id = db.insert(dbhelper.TABLE_PRESCRIPTION,null,values);
                getContext().getContentResolver().notifyChange(uri,null);
                return Uri.parse(PRESCRIPTION_BASE_PATH+"/"+id);
            case JOIN:
                id = db.insert(dbhelper.TABLE_JOIN_PRESCRIPTION_PILLS,null,values);
                getContext().getContentResolver().notifyChange(uri,null);
                return Uri.parse(JOIN_BASE_PATH+"/"+id);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public synchronized int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = matcher.match(uri);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int rowsDeleted = 0;
        String id = null;
        switch(uriType){
            case PILL:
                rowsDeleted = db.delete(dbhelper.TABLE_PILL,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            case PILL_ID:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    rowsDeleted = db.delete(dbhelper.TABLE_PILL,dbhelper.COLUMN_ID+"="+id,null);
                }
                else{
                    rowsDeleted = db.delete(dbhelper.TABLE_PILL,dbhelper.COLUMN_ID+"="+id+" and "+ selection,selectionArgs);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            case PRESCRIPTION:
                rowsDeleted = db.delete(dbhelper.TABLE_PRESCRIPTION,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            case PRESCRIPTION_ID:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    rowsDeleted = db.delete(dbhelper.TABLE_PRESCRIPTION,dbhelper.COLUMN_ID+"="+id,null);
                }
                else{
                    rowsDeleted = db.delete(dbhelper.TABLE_PRESCRIPTION, dbhelper.COLUMN_ID+"="+id+" and "+ selection,selectionArgs);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            case JOIN:
                rowsDeleted = db.delete(dbhelper.TABLE_JOIN_PRESCRIPTION_PILLS,selection,selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            case JOIN_ID:
                id = uri.getLastPathSegment();
                if(TextUtils.isEmpty(selection)){
                    rowsDeleted = db.delete(dbhelper.TABLE_JOIN_PRESCRIPTION_PILLS,dbhelper.COLUMN_ID+"="+id,null);
                }
                else{
                    rowsDeleted = db.delete(dbhelper.TABLE_JOIN_PRESCRIPTION_PILLS, dbhelper.COLUMN_ID+"="+id+" and "+ selection,selectionArgs);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
    }

    @Override
    public synchronized int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = matcher.match(uri);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        values.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        int rowsUpdated = 0;
        String id = null;
        switch(uriType) {
            case PILL:
                rowsUpdated = db.update(dbhelper.TABLE_PILL,values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsUpdated;
            case PILL_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(dbhelper.TABLE_PILL,values, dbhelper.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = db.update(dbhelper.TABLE_PILL,values, dbhelper.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsUpdated;
            case PRESCRIPTION:
                rowsUpdated = db.update(dbhelper.TABLE_PRESCRIPTION,values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsUpdated;
            case PRESCRIPTION_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(dbhelper.TABLE_PRESCRIPTION,values, dbhelper.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = db.update(dbhelper.TABLE_PRESCRIPTION,values, dbhelper.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsUpdated;
            case JOIN:
                rowsUpdated = db.update(dbhelper.TABLE_JOIN_PRESCRIPTION_PILLS,values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsUpdated;
            case JOIN_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(dbhelper.TABLE_JOIN_PRESCRIPTION_PILLS,values, dbhelper.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = db.update(dbhelper.TABLE_JOIN_PRESCRIPTION_PILLS,values, dbhelper.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return rowsUpdated;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

    }

    public List<PillPrescriptionJoin> getJoinsByPrescriptionId(int pId){
        List<PillPrescriptionJoin> joins = new ArrayList<PillPrescriptionJoin>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String Id = String.valueOf(pId);
        String[] whereArgs = {Id};
        Cursor c = db.query(DbHelper.TABLE_JOIN_PRESCRIPTION_PILLS,null,null,null,null,null,null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            PillPrescriptionJoin join = cursorToJoin(c);
            joins.add(join);
            c.moveToNext();
        }
        c.close();
        db.close();
        return joins;
    }

    public PillPrescriptionJoin cursorToJoin(Cursor c){
        PillPrescriptionJoin ppJoin = new PillPrescriptionJoin();
        ppJoin.setId(c.getInt(0));
        ppJoin.setLastModified(c.getString(1));
        ppJoin.setPillId(c.getInt(2));
        ppJoin.setPrescriptionId(c.getInt(3));
        ppJoin.setTime(c.getString(4));
        return ppJoin;
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
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor c = db.query(DbHelper.TABLE_PRESCRIPTION,null,null,null,null,null,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            Prescription prescription = cursorToPrescription(c);
            prescriptions.add(prescription);
            c.moveToNext();
        }
        c.close();
        db.close();
        return prescriptions;
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

    public List<Pill> getAllPills(){
        List<Pill> pills = new ArrayList<Pill>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor c = db.query(DbHelper.TABLE_PILL,null,null,null,null,null,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            Pill pill = cursorToPill(c);
            pills.add(pill);
            c.moveToNext();
        }
        c.close();
        db.close();
        return pills;
    }

}

package com.example.medical.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meanheffry on 12/11/14.
 */
public class MarsDataAccessor {

    private SQLiteDatabase db;
    private DbHelper helper;

    public static final String OPEN_MAR = "open";

    private Context marsContext;
    public MarsDataAccessor(Context context){
        helper = new DbHelper(context);
        marsContext=context;
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    public long getCurrentMARId(){
        String[] whereArgs = {OPEN_MAR};
        Cursor c = db.query(DbHelper.TABLE_MARS,
                null, DbHelper.COLUMN_ENDDATE + " = ?",
                whereArgs, null, null, null);

        if(c.moveToFirst()) {
            MarReport report = cursorToMarReport(c);
            c.close();

            return report.getId();
        }
        else {
            return -1;
        }
    }

    public MarReport createMARsReport(String facilityname, String physician) {
        ContentValues reportValues = new ContentValues();
        reportValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        reportValues.put(DbHelper.COLUMN_FACILITY_NAME, facilityname);
        reportValues.put(DbHelper.COLUMN_STARTDATE,DbHelper.getDateTimeString());
        reportValues.put(DbHelper.COLUMN_ENDDATE,OPEN_MAR);
        reportValues.put(DbHelper.COLUMN_PHYSICIAN,physician);
        reportValues.put(DbHelper.COLUMN_COMMENTS,"");
        long Idl = db.insert(DbHelper.TABLE_MARS, null, reportValues);
        String Ids = String.valueOf(Idl);
        String[] whereArgs = {Ids};
        Cursor c = db.query(DbHelper.TABLE_MARS,
                null, DbHelper.COLUMN_ID + " = ?",
                whereArgs, null, null, null);
        c.moveToFirst();
        MarReport createdReport = cursorToMarReport(c);
        System.out.println("Creating MAR report " + createdReport);
        c.close();
        long oldMars = getCurrentMARId();
        if (oldMars!=-1) {
            whereArgs = new String[]{String.valueOf(oldMars)};
            c = db.query(DbHelper.TABLE_MARS,
                    null, DbHelper.COLUMN_ID + " = ?",
                    whereArgs, null, null, null);
            c.moveToFirst();
            MarReport oldReport = cursorToMarReport(c);
            c.close();
            ContentValues newOldValues = new ContentValues();
            newOldValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
            newOldValues.put(DbHelper.COLUMN_FACILITY_NAME, oldReport.getFacilityName());
            newOldValues.put(DbHelper.COLUMN_STARTDATE, oldReport.getStartDate());
            newOldValues.put(DbHelper.COLUMN_ENDDATE, DbHelper.getDateTimeString());
            newOldValues.put(DbHelper.COLUMN_PHYSICIAN, oldReport.getPhysician());
            newOldValues.put(DbHelper.COLUMN_COMMENTS, oldReport.getComments());
            db.update(DbHelper.TABLE_MARS,null,DbHelper.COLUMN_ID + " = ?",whereArgs);
            return createdReport;
        }
        else{
            return createdReport;
        }
    }

    public void updateMARsReport(Long marId,String facilityname, String physicianName){
        String[] whereArgs = new String[]{String.valueOf(marId)};
        Cursor c = db.query(DbHelper.TABLE_MARS,
                null, DbHelper.COLUMN_ID + " = ?",
                whereArgs, null, null, null);
        c.moveToFirst();
        MarReport oldReport = cursorToMarReport(c);
        c.close();
        ContentValues reportValues = new ContentValues();
        reportValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        reportValues.put(DbHelper.COLUMN_FACILITY_NAME, facilityname);
        reportValues.put(DbHelper.COLUMN_STARTDATE, oldReport.getStartDate());
        reportValues.put(DbHelper.COLUMN_ENDDATE, oldReport.getEndDate());
        reportValues.put(DbHelper.COLUMN_PHYSICIAN, physicianName);
        reportValues.put(DbHelper.COLUMN_COMMENTS, oldReport.getComments());
        db.update(DbHelper.TABLE_MARS,null,DbHelper.COLUMN_ID + " = ?",whereArgs);
    }

    public MarReportEntry createMARsEntry(Long marId, String person, String med, String dosage) {
        ContentValues entryValues = new ContentValues();
        entryValues.put(DbHelper.COLUMN_LAST_MODIFIED, DbHelper.getDateTimeString());
        entryValues.put(DbHelper.COLUMN_MARS, marId);
        entryValues.put(DbHelper.COLUMN_TIME, DbHelper.getDateTimeString());
        entryValues.put(DbHelper.COLUMN_PERSON, person);
        entryValues.put(DbHelper.COLUMN_MED, med);
        entryValues.put(DbHelper.COLUMN_DOSAGE, dosage);
        long Idl = db.insert(DbHelper.TABLE_MARS_ENTRY, null, entryValues);
        String Ids = String.valueOf(Idl);
        String[] whereArgs = {Ids};
        Cursor c = db.query(DbHelper.TABLE_MARS_ENTRY,
                null, DbHelper.COLUMN_ID + " = ?",
                whereArgs, null, null, null);
        c.moveToFirst();
        MarReportEntry createdEntry = cursorToMarEntry(c);
        System.out.println("Creating MAR entry " + createdEntry);
        c.close();

        return createdEntry;
    }

    public List<MarReport> getAllMARsReports(){
        List<MarReport> reports = new ArrayList<MarReport>();
        Cursor c = db.query(DbHelper.TABLE_MARS,null,null,null,null,null,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            MarReport report = cursorToMarReport(c);
            reports.add(report);
            c.moveToNext();
        }
        c.close();
        return reports;
    }
    public List<MarReportEntry> getAllMARsEntries(long marid){
        List<MarReportEntry> entries = new ArrayList<MarReportEntry>();
        String[] whereArgs = new String[]{String.valueOf(marid)};
        Cursor c = db.query(DbHelper.TABLE_MARS_ENTRY,null,DbHelper.COLUMN_MARS+ " = ?", whereArgs, null,null,null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            MarReportEntry entry = cursorToMarEntry(c);
            entries.add(entry);
            c.moveToNext();
        }
        c.close();
        return entries;
    }

    public MarReport cursorToMarReport(Cursor c){
        MarReport marReport = new MarReport();
        marReport.setId(c.getLong(0));
        marReport.setLastModified(c.getString(1));
        marReport.setFacilityName(c.getString(2));
        marReport.setStartDate(c.getString(3));
        marReport.setEndDate(c.getString(4));
        marReport.setPhysician(c.getString(5));
        marReport.setComments(c.getString(6));
        return marReport;
    }

    public MarReportEntry cursorToMarEntry(Cursor c){
        MarReportEntry entry = new MarReportEntry();
        entry.setId(c.getLong(0));
        entry.setLastModified(c.getString(1));
        entry.setMarsId(c.getLong(2));
        entry.setTime(c.getString(3));
        entry.setPerson(c.getString(4));
        entry.setMed(c.getString(5));
        entry.setDosage(c.getString(6));
        return entry;
    }

}

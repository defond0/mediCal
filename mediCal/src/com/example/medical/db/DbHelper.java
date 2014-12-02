package com.example.medical.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by meanheffry on 10/27/14.
 */
public class DbHelper extends SQLiteOpenHelper {


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAST_MODIFIED = "lastmodified";

    public static final String TABLE_PILL = "pill";
    public static final String COLUMN_TUBE = "tube";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DOSE = "dose";
    public static final String COLUMN_LOAD = "load";

    public static final String TABLE_PRESCRIPTION = "prescription";
    public static final String COLUMN_PATIENT= "patient";
    public static final String COLUMN_PILLS="pills";
    public static final String COLUMN_RFID="rfid";

    public static final String TABLE_JOIN_PRESCRIPTION_PILLS = "ppjoin";
    public static final String COLUMN_PILL_ID="pillId";
    public static final String COLUMN_PRESCRIPTION_ID="prescriptionId";
    public static final String COLUMN_TIMES="times";

    private static final String DATABASE_NAME = "pill.db";
    private static final int DATABASE_VERSION = 1;

    private static final String PILL_DATABASE_CREATE =
            "CREATE TABLE " + TABLE_PILL + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LAST_MODIFIED+ " TEXT NOT NULL, "
            + COLUMN_TUBE +" TEXT NOT NULL, "
            + COLUMN_NAME +" TEXT NOT NULL, "
            + COLUMN_DOSE +" TEXT NOT NULL, "
            + COLUMN_LOAD +" TEXT NOT NULL" +     "); ";

    private static final String PRESCRIPTION_DATABASE_CREATE =
            "CREATE TABLE "+ TABLE_PRESCRIPTION + " ( "
            + COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LAST_MODIFIED+ " TEXT NOT NULL, "
            + COLUMN_PATIENT + " TEXT NOT NULL, "
            + COLUMN_RFID + " BLOB"+"); ";

    private static final String PRESCRIPTION_PILL_JOIN_DATABASE_CREATE =
            "CREATE TABLE "+ TABLE_JOIN_PRESCRIPTION_PILLS + " ( "
                    + COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_LAST_MODIFIED+ " TEXT NOT NULL, "
                    + COLUMN_PILL_ID + " INTEGER REFERENCES "+TABLE_PILL+", "
                    + COLUMN_PRESCRIPTION_ID + " INTEGER REFERENCES "+TABLE_PRESCRIPTION+", "
                    + COLUMN_TIMES + " TEXT NOT NULL "+");";


    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PILL_DATABASE_CREATE);
        db.execSQL(PRESCRIPTION_DATABASE_CREATE);
        db.execSQL(PRESCRIPTION_PILL_JOIN_DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PILL);
        onCreate(db);
    }

    public static String getDateTimeString(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        String localTime = date.format(currentLocalTime);
        System.out.println(localTime);
        return localTime;
    }
}

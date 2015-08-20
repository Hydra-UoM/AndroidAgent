package com.uom.cse.androidagent;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Relational database
 * astraction in regard to
 * alternation and quering.
 */

public class Database extends SQLiteOpenHelper
{
    // Database refrence
    private static SQLiteDatabase instance = null;
    // Database version
    private static final int DATABASE_VERSION = 1;
    // Database name
    public static final String DATABASE_NAME = "evaluation";
    // Physical SQLite instance location
    public static final String DATABASE_PATH = "/data/data/asper.evaluation/databases/" + DATABASE_NAME;
    // Table name
    public static final String TABLE_NAME = "test";
    // Contacts Table Columns names
    public static final String KEY_ID = "ID";
    public static final String KEY_T1 = "T1";
    public static final String KEY_T2 = "T2";
    public static final String KEY_T3 = "T3";
    public static final String KEY_T4 = "T4";
    public static final String KEY_T5 = "T5";

    /**
     * Constructor
     *
     */
    public Database()
    {
        super(MainActivity.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        instance = this.getWritableDatabase();
    }

    /**
     * Database initialization procedure
     * Ensuring that the instance exists and
     * retains a table with the proper schema.
     *
     * @param database
     */
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME

                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_T1 + " DOUBLE,"
                + KEY_T2 + " DOUBLE,"
                + KEY_T3 + " DOUBLE,"
                + KEY_T4 + " DOUBLE,"
                + KEY_T5 + " DOUBLE"
                + ")";

        database.execSQL(CREATE_CONTACTS_TABLE);
    }

    /**
     * Database upgrade procedure.
     * Tears down and initiates a new instance instance.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public static SQLiteDatabase getInstance()
    {
        return instance;
    }

}
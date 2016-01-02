package com.uom.cse.androidagent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uom.cse.androidagent.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sirojan on 11/19/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static DatabaseHandler dbDatabaseHandler;

    private static final String DATABASE_NAME = "EventStore",
    TABLE_APPINFO = "appInfo",
    TABLE_CALLLOGS = "callLogs",
    KEY_ID = "id",
    KEY_APPLICATIONNAME = "applicationName",
    KEY_PACKAGENAME = "packageName",
    KEY_CPUUSAGE = "cpuUsage",
    KEY_PRIVATEMEMORYUSAGE = "privateMemoryUsage",
    KEY_SHAREDMEMORYUSAGE = "sharedMemoryUsage",
    KEY_SENTDATA = "sentData",
    KEY_RECEIVEDDATA = "receiveddata",
    KEY_TIMESTAMP = "timestamp",
    KEY_PID = "pid";

    private DatabaseHandler (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(){
        if(dbDatabaseHandler == null){
            return new DatabaseHandler(MainActivity.getContext());
        }
        return dbDatabaseHandler;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_APPINFO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_APPLICATIONNAME + " TEXT,"
                + KEY_PACKAGENAME + " TEXT," + KEY_CPUUSAGE + " TEXT," + KEY_PRIVATEMEMORYUSAGE + " TEXT," + KEY_SHAREDMEMORYUSAGE +
                " TEXT," + KEY_RECEIVEDDATA + " TEXT" + KEY_SENTDATA + " TEXT," + KEY_TIMESTAMP + " TEXT," + KEY_PID + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPINFO);
        onCreate(db);
    }

    public void createAppInfo(ApplicationData appData){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(KEY_APPLICATIONNAME,appData.get_applicationName());
        values.put(KEY_PACKAGENAME,appData.get_packageName());
        values.put(KEY_CPUUSAGE,appData.get_averageCPU());
        values.put(KEY_PRIVATEMEMORYUSAGE,appData.get_averagePrivateMemoryUsage());
        values.put(KEY_SHAREDMEMORYUSAGE,appData.get_averageSharedMemoryUsage());
        values.put(KEY_RECEIVEDDATA,appData.get_averagereceivedData());
        values.put(KEY_SENTDATA,appData.get_averageSentData());
        values.put(KEY_TIMESTAMP,appData.get_timestamp());
        values.put(KEY_PID,appData.get_pid());

        db.insert(TABLE_APPINFO, null, values);
        db.close();
    }

    public ApplicationData getAppInfo(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_APPINFO,new String[]{KEY_ID,KEY_APPLICATIONNAME,KEY_PACKAGENAME,KEY_CPUUSAGE,KEY_SHAREDMEMORYUSAGE,KEY_PRIVATEMEMORYUSAGE,KEY_SENTDATA,KEY_RECEIVEDDATA,KEY_TIMESTAMP,KEY_PID},KEY_ID + "=?",new String[]{String.valueOf("id")},null,null,null,null );

        if(cursor != null){
            cursor.moveToFirst();
        }

        ApplicationData applicationData = new ApplicationData(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(5),cursor.getString(4),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
        db.close();
        cursor.close();
        return applicationData;
    }

    public void deleteAppInfo(ApplicationData applicationData){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_APPINFO, KEY_ID + "=?", new String[]{String.valueOf(applicationData.getId())});
        db.close();
    }

    public void deleteAllAppInfo(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete * from "+ TABLE_APPINFO);
        db.close();
    }

    public int getAppInfoCount(){
        SQLiteDatabase db =getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_APPINFO, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int updateAppInfo(ApplicationData appData){
        SQLiteDatabase db =getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_APPLICATIONNAME,appData.get_applicationName());
        values.put(KEY_PACKAGENAME,appData.get_packageName());
        values.put(KEY_CPUUSAGE,appData.get_averageCPU());
        values.put(KEY_PRIVATEMEMORYUSAGE,appData.get_averagePrivateMemoryUsage());
        values.put(KEY_SHAREDMEMORYUSAGE,appData.get_averageSharedMemoryUsage());
        values.put(KEY_RECEIVEDDATA,appData.get_averagereceivedData());
        values.put(KEY_SENTDATA, appData.get_averageSentData());
        values.put(KEY_TIMESTAMP,appData.get_timestamp());
        values.put(KEY_PID,appData.get_pid());

        return db.update(TABLE_APPINFO, values, KEY_ID + "=?", new String[]{String.valueOf(appData.getId())});
    }

    public List<ApplicationData> getAllAppInfo(){
        List<ApplicationData> appdataList = new ArrayList<ApplicationData>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_APPINFO,null);

        if(cursor.moveToFirst()){
            do{
                ApplicationData applicationData = new ApplicationData(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(7),cursor.getString(6),cursor.getString(8),cursor.getString(9));
                appdataList.add(applicationData);
            }
            while (cursor.moveToNext());
        }
        return appdataList;
    }


}

package com.luclx.nab.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.luclx.nab.data.entities.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LucLX on 3/18/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "urlManager.sqlite";

    // Contacts table name
    private static final String TABLE_URL = "url";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_URL = "url";
    private static final String KEY_TYPE = "type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_URL_TABLE = "CREATE TABLE " + TABLE_URL + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_URL + " TEXT,"
                + KEY_TYPE + " INTEGER" + ")";
        db.execSQL(CREATE_URL_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_URL);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new urls
    public void addURLs(ArrayList<String> urlList, int type) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (String url : urlList) {
            ContentValues values = new ContentValues();
            values.put(KEY_URL, url);
            values.put(KEY_TYPE, type);

            // Inserting Row
            db.insert(TABLE_URL, null, values);
        }
        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public List<URL> getAllURLWithType(int type) {

        List<URL> urlList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_URL, new String[]{
                        KEY_URL, KEY_TYPE}, KEY_TYPE + "=?",
                new String[]{String.valueOf(type)}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                URL url = new URL(cursor.getString(0), Integer.parseInt(cursor.getString(1)));

                // Adding url to list
                urlList.add(url);
            } while (cursor.moveToNext());
        }

        // return contact list
        return urlList;
    }

    public boolean noDataExist() {
        return getAllURLWithType(1).size() == 0;
    }
}

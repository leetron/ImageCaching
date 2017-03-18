package com.luclx.nab;

import android.app.Application;

import com.luclx.nab.data.sqlite.DatabaseHelper;

/**
 * Created by LucLX on 3/18/17.
 */

public class NABApplication extends Application {

    private static NABApplication mInstance;
    private DatabaseHelper db;

    public DatabaseHelper getDatabase() {
        if (db == null) {
            db = new DatabaseHelper(this);
        }
        return db;
    }

    public static synchronized NABApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}

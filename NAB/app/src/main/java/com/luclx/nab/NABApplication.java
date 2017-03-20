package com.luclx.nab;

import android.app.Application;

import com.luclx.nab.data.sqlite.DatabaseHelper;
import com.luclx.nab.loader.ImageCache;
import com.luclx.nab.loader.ImageLoader;

import okhttp3.OkHttpClient;

/**
 * Created by LucLX on 3/18/17.
 */

public class NABApplication extends Application {
    private static NABApplication mInstance;
    private DatabaseHelper db;
    private OkHttpClient mOkHttpClient;

    public DatabaseHelper getDatabase() {
        if (db == null) {
            db = new DatabaseHelper(this);
        }
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initOkHttp();
    }

    public static synchronized NABApplication getInstance() {
        return mInstance;
    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private void initOkHttp() {
        mOkHttpClient = new OkHttpClient();
    }
}

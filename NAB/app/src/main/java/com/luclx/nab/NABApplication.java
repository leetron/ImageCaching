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
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private static NABApplication mInstance;
    private DatabaseHelper db;
    private ImageLoader mImageLoader;
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
        initImageLoader();
        initOkHttp();
    }

    public static synchronized NABApplication getInstance() {
        return mInstance;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private void initImageLoader() {
        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageLoader = new ImageLoader(this, 400);
        mImageLoader.setLoadingImage(R.drawable.empty_photo);
        mImageLoader.addImageCache(cacheParams);
    }

    private void initOkHttp() {
        mOkHttpClient = new OkHttpClient();
    }
}

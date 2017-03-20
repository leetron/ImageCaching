package com.luclx.nab.activity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.luclx.nab.R;
import com.luclx.nab.customview.RecyclingImageView;
import com.luclx.nab.fragment.PagerFragment;
import com.luclx.nab.loader.ImageCache;
import com.luclx.nab.loader.ImageLoader;

/**
 * Created by LucLX on 3/18/17.
 */

public class ImageActivity extends AbstractActivity implements ImageLoader.OnImageLoadedListener {
    private static final String IMAGE_CACHE_DIR = "images";
    private String mImageUrl;
    private RecyclingImageView mImageView;
    private ProgressBar mProgressBar;

    @Override
    protected int getContentView() {
        return R.layout.activity_fullimage;
    }

    @Override
    protected void initImageLoader() {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);

        // Set memory cache to 25% of app memory
        cacheParams.setMemCacheSizePercent(0.25f);

        mImageLoader = new ImageLoader(this, width, height);
        mImageLoader.addImageCache(cacheParams);
        mImageLoader.setImageFadeIn(false);

        loadImage();
    }

    @Override
    protected void onViewReady() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mImageView = (RecyclingImageView) findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mImageUrl = getIntent() != null ? getIntent().getExtras().getString(PagerFragment.URL) : "";
        Log.e("Luc", "URL " + mImageUrl);
    }

    private void loadImage() {
        mImageLoader.loadImage(mImageUrl, mImageView, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageLoader.setExitTasksEarly(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mImageLoader.setExitTasksEarly(true);
        mImageLoader.flushCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageLoader.closeCache();
    }

    @Override
    public void onImageLoaded(boolean success) {
        mProgressBar.setVisibility(View.GONE);
    }
}

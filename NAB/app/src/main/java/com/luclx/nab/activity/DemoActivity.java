package com.luclx.nab.activity;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.luclx.nab.R;
import com.luclx.nab.adapter.ViewPagerAdapter;
import com.luclx.nab.fragment.PagerFragment;
import com.luclx.nab.loader.ImageCache;
import com.luclx.nab.loader.ImageLoader;

/**
 * Created by LucLX on 3/18/17.
 */

public class DemoActivity
        extends AbstractActivity {
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private ViewPager viewPager;

    @Override
    protected int getContentView() {
        return R.layout.activity_demo;
    }

    @Override
    protected void onViewReady() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTabLayout.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        mTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initImageLoader() {
        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);

        // Set memory cache to 25% of app memory
        cacheParams.setMemCacheSizePercent(0.25f);

        final int longest = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);

        mImageLoader = new ImageLoader(this, longest);
        mImageLoader.setLoadingImage(R.drawable.empty_photo);
        mImageLoader.addImageCache(cacheParams);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PagerFragment.newInstance(0), "IMAGES0");
        adapter.addFragment(PagerFragment.newInstance(1), "IMAGES1");
        adapter.addFragment(PagerFragment.newInstance(2), "IMAGES2");
        viewPager.setAdapter(adapter);
    }
}

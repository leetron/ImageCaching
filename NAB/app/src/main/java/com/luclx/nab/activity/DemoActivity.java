package com.luclx.nab.activity;

import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.luclx.nab.R;
import com.luclx.nab.adapter.ViewPagerAdapter;
import com.luclx.nab.fragment.PagerFragment;

/**
 * Created by LucLX on 3/18/17.
 */

public class DemoActivity
        extends AbstractActivity {

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
        viewPager.setVisibility(View.VISIBLE);
        setupViewPager(viewPager);
        mTabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(PagerFragment.newInstance(0), "IMAGES0");
        adapter.addFragment(PagerFragment.newInstance(1), "IMAGES1");
        adapter.addFragment(PagerFragment.newInstance(2), "IMAGES2");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

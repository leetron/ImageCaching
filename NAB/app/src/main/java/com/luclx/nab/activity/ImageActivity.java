package com.luclx.nab.activity;

import com.luclx.nab.R;
import com.luclx.nab.customview.TouchViewTrim;
import com.luclx.nab.fragment.PagerFragment;

/**
 * Created by LucLX on 3/18/17.
 */

public class ImageActivity extends AbstractActivity {
    TouchViewTrim mImage;

    @Override
    protected int getContentView() {
        return R.layout.activity_fullimage;
    }

    @Override
    protected void onViewReady() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mImage = (TouchViewTrim) findViewById(R.id.image);
        String url = getIntent() != null ? getIntent().getExtras().getString(PagerFragment.URL) : "";
//        Glide.with(this).load(url).into(mImage);
    }
}

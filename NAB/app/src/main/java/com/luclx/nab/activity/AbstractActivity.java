package com.luclx.nab.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.luclx.nab.R;

/**
 * Created by LucLX on 3/18/17.
 */

public abstract class AbstractActivity extends AppCompatActivity {
    protected Toolbar mToolbar;
    protected TabLayout mTabLayout;
    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setVisibility(View.GONE);
        setSupportActionBar(mToolbar);

        ViewGroup replaceLayout = (ViewGroup) findViewById(R.id.content);
        getLayoutInflater().inflate(getContentView(), replaceLayout, true);

        // init another views
        onViewReady();
    }

    protected abstract int getContentView();

    protected abstract void onViewReady();

    protected void showDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMax(100);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

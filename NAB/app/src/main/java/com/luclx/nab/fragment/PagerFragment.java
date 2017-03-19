package com.luclx.nab.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.luclx.nab.NABApplication;
import com.luclx.nab.R;
import com.luclx.nab.activity.ImageActivity;
import com.luclx.nab.adapter.GalleryAdapter;
import com.luclx.nab.data.entities.URL;
import com.luclx.nab.loader.ImageCache;
import com.luclx.nab.loader.ImageLoader;

import java.util.ArrayList;


public class PagerFragment extends AbstractFragment {
    public static final String URL = "url";
    private static final String INDEX = "index";
    private static final String TAG = "PagerFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";

    private int mIndex;
    private RecyclerView mRecycleView;
    private GalleryAdapter mAdapter;
    private ArrayList<URL> urlList;
    private ImageLoader mImageLoader;

    public static PagerFragment newInstance(int index) {
        PagerFragment fragment = new PagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIndex = getArguments() != null ? getArguments().getInt(INDEX, 0) : 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pager;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        settingLoader();
        mAdapter = new GalleryAdapter(getActivity(), mImageLoader);
        mAdapter.onItemClickListener()
                .filter(dataPair -> dataPair != null && dataPair.second != null)
                .map(dataPair -> dataPair.second.getUrl())
                .subscribe(url -> startActivity(url));
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycleView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    mImageLoader.setPauseWork(true);
                } else {
                    mImageLoader.setPauseWork(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageLoader.setExitTasksEarly(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageLoader.setPauseWork(true);
        mImageLoader.setExitTasksEarly(true);
        mImageLoader.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageLoader.closeCache();
    }

    private void settingLoader() {
        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageLoader = new ImageLoader(getActivity(), 600);
        mImageLoader.setLoadingImage(R.drawable.empty_photo);
        mImageLoader.addImageCache(cacheParams);
    }

    @Override
    public void setData() {
        urlList = new ArrayList<>();
        urlList.addAll(NABApplication.getInstance().getDatabase().getAllURLWithType(mIndex));
        mAdapter.setData(urlList);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("Luc", mIndex + " isVisibleToUser " + isVisibleToUser);
    }

    private void startActivity(String url) {
        Intent intent = new Intent(getActivity(), ImageActivity.class);
        intent.putExtra(URL, url);
        startActivity(intent);
    }
}

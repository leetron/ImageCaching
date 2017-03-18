package com.luclx.nab.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.luclx.nab.NABApplication;
import com.luclx.nab.R;
import com.luclx.nab.activity.ImageActivity;
import com.luclx.nab.adapter.GalleryAdapter;
import com.luclx.nab.data.entities.URL;
import com.luclx.nab.utils.PixelUtils;

import java.util.ArrayList;


public class PagerFragment extends AbstractFragment {
    public static final String URL = "url";
    private static final String INDEX = "index";

    private int mIndex;
    private RecyclerView mRecycleView;
    private GalleryAdapter mAdapter;
    private ArrayList<URL> urlList;

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
        mAdapter = new GalleryAdapter(getActivity());
        mAdapter.onItemClickListener()
                .filter(dataPair -> dataPair != null && dataPair.second != null)
                .map(dataPair -> dataPair.second.getUrl())
                .subscribe(url -> startActivity(url));
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycleView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.addItemDecoration(new GridSpacingItemDecoration(2, PixelUtils.dpToPx(getActivity(), 0), true));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setAdapter(mAdapter);
    }

    @Override
    public void setData() {
        urlList = new ArrayList<>();
        urlList.addAll(NABApplication.getInstance().getDatabase().getAllURLWithType(mIndex));
        mAdapter.setData(urlList);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private void startActivity(String url) {
        Intent intent = new Intent(getActivity(), ImageActivity.class);
        intent.putExtra(URL, url);
        startActivity(intent);
    }
}

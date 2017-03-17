package com.luclx.nab.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.luclx.nab.R;


public class PagerFragment extends AbstractFragment {
    private static final String INDEX = "index";
    private int mIndex;
    private RecyclerView mRecycleView;


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
        mRecycleView = (RecyclerView) view.findViewById(R.id.recycleView);
    }
}

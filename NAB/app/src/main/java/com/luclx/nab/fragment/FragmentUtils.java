package com.luclx.nab.fragment;

import android.os.Bundle;
import android.view.View;

/**
 * Created by LucLX on 3/18/17.
 */

public interface FragmentUtils {
    int getLayoutId();
    void initView(View view, Bundle bundle);
    void setData();
}

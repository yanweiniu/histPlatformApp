package com.marchsoft.organization;

import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * activity基类
 */
public class BaseFragmentActivity extends FragmentActivity {
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 返回上一页
     *
     * @param v
     */
    public void onBackClick(View v) {
        finish();
    }
}

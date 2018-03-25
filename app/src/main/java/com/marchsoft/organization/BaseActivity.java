package com.marchsoft.organization;

import android.app.Activity;
import android.view.View;
/**
 * activity基类
 */
public class BaseActivity extends Activity {
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

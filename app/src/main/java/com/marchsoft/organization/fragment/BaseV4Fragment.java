package com.marchsoft.organization.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
public class BaseV4Fragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

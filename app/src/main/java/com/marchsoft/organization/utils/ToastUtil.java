package com.marchsoft.organization.utils;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.marchsoft.organization.LoginActivity;
import com.marchsoft.organization.R;
import com.marchsoft.organization.widget.AlertDialog;


public class ToastUtil {
    private static ToastUtil sToastUtil = null;
    private Toast mToast;
    private TextView mTextTv;

    private ToastUtil() {

    }

    public static synchronized ToastUtil make(Context context) {
        if (sToastUtil == null) {
            sToastUtil = newInstance(context);
        }
        return sToastUtil;
    }

    public Toast getToast() {
        return mToast;
    }

    public void show(int resId) {
        mTextTv.setText(resId);
        mToast.show();
    }

    public void show(CharSequence text) {
        mTextTv.setText(text);
        mToast.show();
    }

    public void cancel() {
        mToast.cancel();
    }

    public void setDuration(int duration) {
        mToast.setDuration(duration);
    }

    /**
     * 产生新实例
     *
     * @param context
     * @return
     */
    public static ToastUtil newInstance(Context context) {
        ToastUtil toastUtil = new ToastUtil();
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.layout_toast, null);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toastUtil.mToast = toast;
        toastUtil.mTextTv = (TextView) layout
                .findViewById(R.id.tv_toast_text);
        return toastUtil;
    }

    }

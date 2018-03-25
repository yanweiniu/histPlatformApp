package com.marchsoft.organization.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.marchsoft.organization.R;

public class AlertDialog extends Dialog implements View.OnClickListener {
    private OnAlertDialogListener mButtonselcetor;
    private Context mContext;
    private TextView messageView;
    private Button sureButton;
    private Button cancelButton;
    private String mMessage;
    private String mOkButtonMessage;
    private String mCancelButtonMessage;

    public AlertDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public AlertDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public void setOnButtonListener(OnAlertDialogListener buttonListener) {
        mButtonselcetor = buttonListener;
    }

    public static interface OnAlertDialogListener {
        void onOk(AlertDialog alertDialogView);

        void onCancel(AlertDialog alertDialogView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);
        messageView = (TextView) findViewById(R.id.tv_alert_dialog_msg);
        sureButton = (Button) findViewById(R.id.btn_alert_dialog_ok);
        cancelButton = (Button) findViewById(R.id.btn_alert_dialog_cancel);
        messageView.setText(mMessage);
        sureButton.setText(mOkButtonMessage);
        cancelButton.setText(mCancelButtonMessage);
        sureButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        if (TextUtils.isEmpty(mOkButtonMessage)) {
            sureButton.setVisibility(View.GONE);
        } else {
            sureButton.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(mCancelButtonMessage)) {
            cancelButton.setVisibility(View.GONE);
        } else {
            cancelButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_alert_dialog_ok) {
            mButtonselcetor.onOk(this);
        } else {
            mButtonselcetor.onCancel(this);
        }
    }

    public static AlertDialog build(Context context, String message,
                                    String sureButtonMessage, String cancelButtonMessage,
                                    OnAlertDialogListener onButtonSelectorListener) {
        AlertDialog alertDialogView = new AlertDialog(context,
                R.style.AlertDialog);
        alertDialogView.mMessage = message;
        alertDialogView.mOkButtonMessage = sureButtonMessage;
        alertDialogView.mCancelButtonMessage = cancelButtonMessage;
        alertDialogView.setOnButtonListener(onButtonSelectorListener);
        return alertDialogView;
    }

    public static AlertDialog build(Context context, int resMsgId, int resOkId,
                                    int resCancelId, OnAlertDialogListener onAlertDialogListener) {
        return build(context, context.getString(resMsgId),
                resOkId != 0 ? context.getString(resOkId) : null,
                resCancelId != 0 ? context.getString(resCancelId) : null,
                onAlertDialogListener);
    }




}

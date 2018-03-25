package com.marchsoft.organization.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.marchsoft.organization.R;

public class InputDialog extends Dialog implements View.OnClickListener {
    private EditText mResultEt;
    private Button mOkBtn;
    private Context mContext;
    private OnInputDialogListener mButtonListener;
    private String mOkButtonMessage;
    private String mHintMessage;

    public InputDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public InputDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public interface OnInputBackListener {
        void onCallBack();
    }

    public void setOnButtonListener(OnInputDialogListener buttonListener) {
        mButtonListener = buttonListener;
    }

    public static interface OnInputDialogListener {
        void onOk(InputDialog alertDialogView, String content);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_view_inputdialog);
        mResultEt = (EditText) findViewById(R.id.edittxt_result);
        mOkBtn = (Button) findViewById(R.id.btn_ok);
        mResultEt.setHint(mHintMessage);
        mOkBtn.setText(mOkButtonMessage);
        mOkBtn.setOnClickListener(this);
    }

    public String getResult() {
        return mResultEt.getText().toString();
    }

    @Override
    public void onClick(View view) {
        String content = mResultEt.getText().toString();
        mButtonListener.onOk(this, content);
    }


    public static InputDialog build(Context context, String message,
                                    String sureButtonMessage,
                                    OnInputDialogListener onButtonSelectorListener) {
        InputDialog alertDialogView = new InputDialog(context,
                R.style.AlertDialog);
        alertDialogView.mHintMessage = message;
        alertDialogView.mOkButtonMessage = sureButtonMessage;
        alertDialogView.setOnButtonListener(onButtonSelectorListener);
        return alertDialogView;
    }

    public static InputDialog build(Context context, int resMsgId, int resOkId,
                                    OnInputDialogListener onAlertDialogListener) {
        return build(context, context.getString(resMsgId),
                resOkId != 0 ? context.getString(resOkId) : null,
                onAlertDialogListener);
    }
}

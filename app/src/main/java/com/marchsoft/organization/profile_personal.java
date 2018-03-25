package com.marchsoft.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.model.User;
import com.marchsoft.organization.utils.ToastUtil;

/**
 * Created by w on 2016/3/5.
 */
public class profile_personal extends BaseActivity implements View.OnClickListener{
    private TextView cancle;
    private TextView ok;
    private EditText personal_sign;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_sign);
        ok =(TextView) findViewById(R.id.personal_save);
        cancle = (TextView) findViewById(R.id.btn_cancle);
        personal_sign =(EditText) findViewById(R.id.personal_sign_edit);
        mUser= Preferences.getLoginUser();
        personal_sign.setText(mUser.getmSign());
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == ok){
            Intent intent=new Intent();
            intent.putExtra("personal_sign",personal_sign.getText().toString());
            setResult(4, intent);
            finish();
        }
        }
}

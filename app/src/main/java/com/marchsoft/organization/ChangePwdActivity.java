package com.marchsoft.organization;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.jar.JarException;

/**
 * Created by FXB on 2016/2/28.
 */
public class ChangePwdActivity extends BaseActivity implements OnClickListener{
    private TextView mTVchangePwdOK;
    private EditText mETchangePwdOld;
    private EditText mETchangePwdNew;
    private EditText mETchangePwdRenew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        mTVchangePwdOK=(TextView)findViewById(R.id.tv_changePwd_ok);
        mETchangePwdOld=(EditText)findViewById(R.id.et_changePwd_old);
        mETchangePwdNew=(EditText)findViewById(R.id.et_changePwd_new);
        mETchangePwdRenew=(EditText)findViewById(R.id.et_changePwd_reNew);
        mTVchangePwdOK.setOnClickListener(this);

        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordStates();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mETchangePwdOld.addTextChangedListener(textWatcher);
        mETchangePwdNew.addTextChangedListener(textWatcher);
        mETchangePwdRenew.addTextChangedListener(textWatcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_changePwd_ok:
                changePassword();
                break;
        }
    }

    private void passwordStates(){
        String oldText=mETchangePwdOld.getText().toString();
        String newText=mETchangePwdNew.getText().toString();
        String reNewText=mETchangePwdRenew.getText().toString();
        if (oldText.equals("")||newText.equals("")||reNewText.equals("")){
            mTVchangePwdOK.setTextColor(getResources().getColor(R.color.common_no_changepwd_textcolor));
            mTVchangePwdOK.setEnabled(false);
        }else {
            mTVchangePwdOK.setTextColor(getResources().getColor(R.color.common_actionbar_textcolor));
            mTVchangePwdOK.setEnabled(true);
        }

    }
    private void changePassword(){
        String oldText=mETchangePwdOld.getText().toString().trim();
        if (oldText.equals("")){
            mETchangePwdOld.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        String newText=mETchangePwdNew.getText().toString().trim();
        if (newText.equals("")){
            mETchangePwdNew.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        String reNewText=mETchangePwdRenew.getText().toString().trim();
        if (reNewText.equals("")){
            mETchangePwdRenew.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        if (!newText.equals(reNewText)){
            ToastUtil.make(this).show(R.string.change_pass_no_equls);
            return;
        }
        if (newText.length()<6){
            ToastUtil.make(this).show(R.string.change_password_length);
            return;
        }
        RequestParams params=new RequestParams();
        params.put("id", Preferences.getUserId());
        params.put("old_password",oldText);
        params.put("new_password",newText);
        RestClient.post(Constant.API_GET_MINE_CHANGEPWD,params,new AsyncHttpResponseHandler(getBaseContext(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject rlt) {
                super.onSuccess(statusCode, headers, rlt);
                try {
                    if(rlt.getInt("msg_code")==Constant.CODE_SUCCESS){
                        ToastUtil.make(getBaseContext()).show(R.string.change_pass_equls);
                         finish();
                    }else{
                        ToastUtil.make(getBaseContext()).show(rlt.getString("msg"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        }));
    }


}

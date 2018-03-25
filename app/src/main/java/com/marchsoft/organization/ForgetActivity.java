package com.marchsoft.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonIOException;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by FXB on 2016/3/3.
 */
public class ForgetActivity extends BaseActivity implements View.OnClickListener{
    private Intent intent;
    private Button mFindPassword;
    private EditText mEmailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        mFindPassword=(Button)findViewById(R.id.btn_find_password_ok);
        mEmailText=(EditText)findViewById(R.id.et_find_password_email);
        mFindPassword.setOnClickListener(this);
        intent=getIntent();
    }
    private void findPassword(String email){
        RequestParams params=new RequestParams();
        params.put("student_no",intent.getStringExtra("student_no"));
        params.put("email",email);
        RestClient.post(Constant.API_POST_FORGETPASSWORD,params,new AsyncHttpResponseHandler(getBaseContext(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject rlt) {
                super.onSuccess(statusCode, headers, rlt);
                try {
                    if(rlt.getInt("msg_code")==Constant.CODE_SUCCESS){
                      ToastUtil.make(ForgetActivity.this).show("邮件发送成功");
                    }else{
                        ToastUtil.make(ForgetActivity.this).show(rlt.getString("msg"));
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

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_find_password_ok){
            String email=mEmailText.getText().toString().trim();
            if(email.equals("")){
                mEmailText.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                return;
            }
            findPassword(email);
        }
    }
}

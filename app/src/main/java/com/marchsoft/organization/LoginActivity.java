package com.marchsoft.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.convert.UserJSONConvert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.fragment.MineFragment;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.User;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.jar.JarException;

/**
 * Created by FXB on 2016/3/1.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText mUserNameET;
    private EditText mPassWordET;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    private TextView mForgetPasswrodTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserNameET=(EditText)findViewById(R.id.et_userName);
        mPassWordET=(EditText)findViewById(R.id.et_password);
        mLoginBtn=(Button)findViewById(R.id.btn_login);
        mRegisterBtn=(Button)findViewById(R.id.btn_login_register);
        mForgetPasswrodTV=(TextView)findViewById(R.id.tv_login_forgetPwd);
        mLoginBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mForgetPasswrodTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.btn_login:
               goLogIn();
               break;
           case R.id.btn_login_register:
               Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
               startActivity(intent);
               break;
           case R.id.tv_login_forgetPwd:
               Intent intent1=new Intent(LoginActivity.this,ForgetActivity.class);
               String userName= mUserNameET.getText().toString().trim();
               if(userName.equals("")){
                   mUserNameET.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                   ToastUtil.make(this).show("请填写学号");
                   return;
               }
               intent1.putExtra("student_no",userName);
               startActivity(intent1);
               break;
       }

    }

    private void login(String userName,String passWord){
        RequestParams params=new RequestParams();
        params.put("student_no",userName);
        params.put("password",passWord);
        params.put("device_id", Preferences.getDeviceId());
        params.put("version", Preferences.getVersionName());
        RestClient.post(Constant.API_POST_LOGIN, params, new AsyncHttpResponseHandler(getBaseContext(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                          setLoginStatus(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject rlt) {
                //super.onSuccess(statusCode, headers, rlt);
                try {
                    if (rlt.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        JSONObject data = rlt.getJSONObject("data");
                        User user = UserJSONConvert.convertJsonToItem(data);
                        Preferences.setLoginUser(user);
                        Preferences.setAccessToken(user.getmToken());
                        Preferences.setUserNumber(user.getmStudent_id());
                        Preferences.setUserId(user.getmUserId());
                        Preferences.setLoginUser(user);
                        setResult(MineFragment.LOGIN_RESULT_CODE);
                        finish();
                    } else {
                        ToastUtil.make(getBaseContext()).show(rlt.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                setLoginStatus(false);
            }
        }));
    }
    private void setLoginStatus(boolean isLoading){
        if(isLoading){
            mLoginBtn.setText(R.string.login_status_loading);
            mLoginBtn.setEnabled(false);
        }else {
            mLoginBtn.setText(R.string.login_status_common_lable);
            mLoginBtn.setEnabled(true);
        }
    }
    private void goLogIn(){
         String userName=mUserNameET.getText().toString().trim();
        if (userName.equals("")){
            mUserNameET.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        String passWord=mPassWordET.getText().toString().trim();
        if (passWord.equals("")){
            mPassWordET.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        login(userName,passWord);
    }
}

package com.marchsoft.organization;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private TextView mRegisterUserName;
    private EditText mRegisterPassword;
    private EditText mRegisterRepassword;
    private Button mRegisterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRegisterUserName=(TextView)findViewById(R.id.tv_register_username);
        mRegisterPassword=(EditText)findViewById(R.id.et_register_password);
        mRegisterRepassword=(EditText)findViewById(R.id.et_register_Repassword);
        mRegisterButton=(Button)findViewById(R.id.btn_register);
        mRegisterButton.setOnClickListener(this);
    }
    private void goRegister(){
        String username=mRegisterUserName.getText().toString().trim();
        if (username.equals("")){
            mRegisterUserName.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
           return;
        }
        String password=mRegisterPassword.getText().toString().trim();
        if (password.equals("")){
            mRegisterPassword.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        String rePassword=mRegisterRepassword.getText().toString().trim();
        if (rePassword.equals("")){
            mRegisterRepassword.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        if (password.length()<6){
            ToastUtil.make(this).show(R.string.change_password_length);
            return;
        }else{
            if (!rePassword.equals(password)){
                ToastUtil.make(this).show(R.string.change_pass_no_equls);
                return;
            }
        }
        RequestParams params=new RequestParams();
        params.put("student_no",username);
        params.put("password",password);
        RestClient.post(Constant.API_POST_REGISTER,params,new AsyncHttpResponseHandler(getBaseContext(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject rlt) {
                super.onSuccess(statusCode, headers, rlt);
                try {
                    if(rlt.getInt("msg_code")==Constant.CODE_SUCCESS){
                          ToastUtil.make(RegisterActivity.this).show(R.string.common_label_register_msg);
                          finish();
                    }else{
                        ToastUtil.make(RegisterActivity.this).show(rlt.getString("msg"));
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
        goRegister();
    }
}

package com.marchsoft.organization;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

/**
 * Created by FXB on 2016/2/28.
 */
public class SuggestionActivity extends BaseActivity implements View.OnClickListener{
    private TextView mSubmitTv;
    private EditText mSubmitTextEt;
    private EditText mSubmitEmailEt;
    private TextView mCountTextSizeTv;
    private static final int textSize=127;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        mSubmitTv=(TextView)findViewById(R.id.tv_suggestion_submit);
        mSubmitTextEt=(EditText)findViewById(R.id.et_suggestion_back);
        mSubmitEmailEt=(EditText)findViewById(R.id.et_suggestion_email);
        mCountTextSizeTv=(TextView)findViewById(R.id.tv_suggestion_count);
        mSubmitTv.setOnClickListener(this);
        TextWatcher watcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int st=(mSubmitTextEt.getText()).length();
                mCountTextSizeTv.setText(String.valueOf(textSize-st));

            }
        };
        mSubmitTextEt.addTextChangedListener(watcher);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_suggestion_submit){
            String text=mSubmitTextEt.getText().toString().trim();
            if (text.equals("")){
                mSubmitTextEt.startAnimation(AnimationUtils.loadAnimation(SuggestionActivity.this,R.anim.shake));
                return;
            }else if((mSubmitTextEt.getText()).length()>textSize){
                ToastUtil.make(SuggestionActivity.this).show("字数超出不能提交！！！");
                return;
            }
            backSuggestion(text);
        }

    }
    private void backSuggestion(String text){

        RequestParams params=new RequestParams();
        params.put("user_id", Preferences.getUserId());
        params.put("content",text);
        params.put("email",mSubmitEmailEt.getText().toString());
        RestClient.post(Constant.API_GET_FEEDBACK,params,new AsyncHttpResponseHandler(getBaseContext(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject rlt) {
                super.onSuccess(statusCode, headers, rlt);
                try {

                    if(rlt.getInt("msg_code")==Constant.CODE_SUCCESS){
                        ToastUtil.make(SuggestionActivity.this).show("提交成功");
                        finish();
                    }else{
                        ToastUtil.make(SuggestionActivity.this).show(rlt.getString("msg"));
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
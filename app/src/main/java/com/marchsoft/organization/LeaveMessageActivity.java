package com.marchsoft.organization;

import android.content.Intent;
import android.os.Bundle;
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
 * Created by Administrator on 2016/3/25 0025.
 */
public class LeaveMessageActivity extends BaseActivity implements View.OnClickListener{
    private EditText mContent;
    private TextView leaveMessage;
    private int associationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_message);
        mContent = (EditText) findViewById(R.id.et_leave_message);
        leaveMessage = (TextView) findViewById(R.id.tv_leave_message);
        leaveMessage.setOnClickListener(this);
        associationId = getIntent().getIntExtra("associationId",0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_leave_message:
                leaveMessage();
                break;
        }

    }

    private void leaveMessage() {
        String content = mContent.getText().toString().trim();
        if (content.length() == 0){
            mContent.setFocusable(true);
            mContent.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("association_id",associationId);
        params.put("user_id", Preferences.getUserId());
        params.put("content",content);
        RestClient.post(Constant.API_GET_ORGANIZATION_LEAVE_MESSAGE,params,new AsyncHttpResponseHandler(this,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS){
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        String msg = response.getString("msg");
                        ToastUtil.make(LeaveMessageActivity.this).show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }));
    }
}

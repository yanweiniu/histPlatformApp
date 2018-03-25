package com.marchsoft.organization;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.utils.Constant;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/3/8 0008.
 */
public class SplashActivity extends BaseActivity{
    private static final long DELAY_MILLIS = 1 * 1000;
    private static final int MSG_WHAT_STARTMAIN = 1;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new MyHandler(this);
        mHandler.sendEmptyMessageDelayed(MSG_WHAT_STARTMAIN, DELAY_MILLIS);
        if (Preferences.getUserId()!=0){
            judgeToken(Preferences.getUserId());
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            mHandler.removeMessages(MSG_WHAT_STARTMAIN);
        }
        return super.onKeyDown(keyCode, event);
    }

    private static class MyHandler extends Handler{
        WeakReference<SplashActivity> mActivity;
        MyHandler(SplashActivity activity){
            mActivity = new WeakReference<SplashActivity>(activity);
        };

        @Override
        public void handleMessage(Message msg) {
            SplashActivity splashActivity = mActivity.get();
            if (splashActivity != null){
                switch (msg.what){
                    case MSG_WHAT_STARTMAIN:
                        if (!Preferences.isShowWelcome()){
                            splashActivity.startActivity(new Intent(splashActivity, MainActivity.class));
                        }else {
                            splashActivity.startActivity(new Intent(splashActivity,WelcomeActivity.class));
                            Preferences.updateVersionCode();
                        }
                        splashActivity.finish();
                        break;
                }
            }
        }

    }
    private void judgeToken(int id){
        RequestParams params=new RequestParams();
        params.put("id",id);
        RestClient.post(Constant.API_POST_CHECKTOKEN,params,new AsyncHttpResponseHandler(getBaseContext(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response.getInt("msg_code")==Constant.CODE_SUCCESS){
                        if(response.getInt("data")==0){
                            Preferences.clear();
                        }

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }));
    }
}

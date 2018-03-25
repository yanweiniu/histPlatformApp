package com.marchsoft.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.fragment.MineFragment;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.AlertDialog;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by FXB on 2016/2/28.
 */
public class SetMineActivity extends BaseActivity implements OnClickListener{
    private TextView mTVChangePwd;
    private TextView mTVAbout;
    private TextView mTVSuggestion;
    private Button   mBtnLogout;
    private LinearLayout mVersionUpdateTv;
    private TextView mVersionTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mine);
        mTVChangePwd=(TextView)findViewById(R.id.tv_set_changePwd);
        mTVAbout=(TextView)findViewById(R.id.tv_set_about);
        mTVSuggestion=(TextView)findViewById(R.id.tv_set_suggestion);
        mBtnLogout=(Button)findViewById(R.id.btn_set_logout);
        mVersionUpdateTv=(LinearLayout)findViewById(R.id.tv_set_Version_update);
        mVersionTv=(TextView)findViewById(R.id.tv_get_version);
        mTVChangePwd.setOnClickListener(this);
        mTVAbout.setOnClickListener(this);
        mTVSuggestion.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
        mVersionUpdateTv.setOnClickListener(this);
        mVersionTv.setText(Preferences.getVersionName());
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.tv_set_changePwd:
                intent=new Intent(SetMineActivity.this,ChangePwdActivity.class);
                break;
            case R.id.tv_set_about:
                intent=new Intent(SetMineActivity.this,AboutActivity.class);
                break;
            case R.id.tv_set_suggestion:
                intent=new Intent(SetMineActivity.this,SuggestionActivity.class);
                break;
            case R.id.btn_set_logout:
                showDialogLogout();
                break;
            case R.id.tv_set_Version_update:
                checkVersion();
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }

    }
    private void checkVersion(){
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(SetMineActivity.this, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        ToastUtil.make(SetMineActivity.this).show(R.string.update_version_new);
                        break;
                    case UpdateStatus.Timeout: // time out
                        ToastUtil.make(SetMineActivity.this).show("超时");
                        break;
                }
            }
        });
        UmengUpdateAgent.update(this);
    }
    private void showDialogLogout(){
        AlertDialog.build(this, R.string.login_label_unlogout_msg,
                R.string.common_label_ok, R.string.common_label_cancel,
                new AlertDialog.OnAlertDialogListener() {

                    @Override
                    public void onOk(AlertDialog alertDialogView) {
                        alertDialogView.dismiss();
                        Preferences.clear();
                        setResult(MineFragment.LOGIN_RESULT_CODE);
                        finish();
                        //logout();
                    }

                    @Override
                    public void onCancel(AlertDialog alertDialogView) {
                        alertDialogView.dismiss();
                    }
                }).show();
    }
    private void logout(){
        RequestParams params =new RequestParams();
        params.put("id", Preferences.getUserId());
        RestClient.post(Constant.API_POST_LOGOUT,params,new AsyncHttpResponseHandler(getBaseContext(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject rlt) {
                super.onSuccess(statusCode, headers, rlt);
                try {
                    if (rlt.getInt("msg_code")==Constant.CODE_SUCCESS){
                        Preferences.clear();
                        setResult(MineFragment.LOGIN_RESULT_CODE);
                        finish();
                    }else{
                        ToastUtil.make(SetMineActivity.this).show(rlt.getString("msg"));
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

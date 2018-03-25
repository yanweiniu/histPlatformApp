package com.marchsoft.organization;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.convert.DynamicJSONConvert;
import com.marchsoft.organization.convert.NewsJSONConvert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.Dynamic;
import com.marchsoft.organization.model.News;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.AlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/2/28 0028.
 */
public class DynamicDetailsActivity extends BaseActivity implements View.OnClickListener{
    private int id;
    private CircleImageView organizationPic;
    private TextView organizationName;
    private TextView activityName;
    private TextView activityTheme;
    private TextView activitySupervisor;
    private TextView activityDate;
    private TextView endTime;
    private TextView activityPlace;
    private TextView activityFormat;
    private TextView activityNumber;
    private TextView activityEnroll;
    private TextView activityDeclaration;
    private CircleImageView activitySupervisorPic;
    private LinearLayout lnSupervisor;
    private ImageLoader mImageLoader;
    private ImageLoadingListener mImageLoadingListener;
    private int activityId;
    private int associationId;
    private int status = -100;
    private int supervisorId;
    private int numbeir;
    private int clickNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dyamic_details);
        id = getIntent().getIntExtra("id",0);
        mImageLoader = ImageLoader.getInstance();
        mImageLoadingListener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        };
        initHeader();

        getDynamicDetails();


    }

    private void getDynamicDetails() {
        RequestParams params = new RequestParams();
        params.put("user_id",Preferences.getUserId());
        params.put("activity_id", id);
        RestClient.get(Constant.API_GET_MAIN_DYNAMIC_DETAILS, params, new AsyncHttpResponseHandler(this, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("activity_info");
                        List<Dynamic> list = DynamicJSONConvert.convertJsonArrayToItemList(jsonArray);
                        Dynamic dynamic = list.get(0);
                        mImageLoader.displayImage(dynamic.getIcon(), organizationPic);
                        mImageLoader.displayImage(dynamic.getImage(), activitySupervisorPic);
                        organizationName.setText(dynamic.getAsNmae());
                        activityName.setText(dynamic.getName());
                        activityTheme.setText(dynamic.getTheme());
                        activitySupervisor.setText(dynamic.getStudentName());
                        activityDate.setText(dynamic.getStartTime());
                        endTime.setText(dynamic.getEndTime());
                        activityPlace.setText(dynamic.getPlace());
                        String format = "";
                        switch (dynamic.getFormat()) {
                            case 0:
                                format = "比赛";
                                break;
                            case 1:
                                format = "演出";
                                break;
                            case 2:
                                format = " 报告";
                                break;
                            case 3:
                                format = "会议";
                                break;
                            case 4:
                                format = "参观";
                                break;
                            case 5:
                                format = "公益活动";
                                break;
                            case 6:
                                format = "其他";
                                break;
                        }
                        activityFormat.setText(format);
                        numbeir = dynamic.getRegistering();
                        activityNumber.setText(dynamic.getRegistering() + "");
                        String isJoin = "";
                        Drawable drawable;
                        if (dynamic.getIsJoin() == 0) {
                            isJoin = "已报名";
                            drawable = getResources().getDrawable(R.mipmap.main_ic_complete);
                        } else {
                            drawable = getResources().getDrawable(R.mipmap.main_ic_no_complete);
                            isJoin = "报名";
                        }
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        activityEnroll.setCompoundDrawables(drawable, null, null, null);
                        activityEnroll.setText(isJoin);
                        activityDeclaration.setText(dynamic.getDeclaration());
                        activityId = dynamic.getId();
                        associationId = dynamic.getAssociationId();
                        supervisorId = dynamic.getSupervisorId();
                    } else {
                        String msg = response.getString("msg");
                        ToastUtil.make(DynamicDetailsActivity.this).show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void initHeader() {
        organizationPic = (CircleImageView) findViewById(R.id.organization_logo);
        organizationName = (TextView) findViewById(R.id.organization_name);
        activityName = (TextView) findViewById(R.id.activity_name);
        activityTheme = (TextView) findViewById(R.id.activity_theme);
        activitySupervisor = (TextView) findViewById(R.id.activity_supervisor);
        activityDate = (TextView) findViewById(R.id.activity_date);
        activityPlace = (TextView) findViewById(R.id.activity_place);
        activityFormat = (TextView) findViewById(R.id.activity_format);
        activityNumber = (TextView) findViewById(R.id.activity_number);
        activityEnroll = (TextView) findViewById(R.id.activity_enroll);
        activityDeclaration = (TextView) findViewById(R.id.activity_declaration);
        activitySupervisorPic = (CircleImageView) findViewById(R.id.activity_supervisor_pic);
        lnSupervisor = (LinearLayout) findViewById(R.id.ln_supervisor);
        endTime = (TextView) findViewById(R.id.activity_end_date);
        activityEnroll.setOnClickListener(this);
        lnSupervisor.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ln_supervisor:
                Intent intent = new Intent(this,PrincipalDetailsActivity.class);
                intent.putExtra("id",supervisorId);
                startActivity(intent);
                break;
            case R.id.activity_enroll:
                joinActivity();
                break;
        }


    }

    private void joinActivity() {
        if(!Preferences.isLogin()){
            AlertDialog.build(this, R.string.login_label_unlogin_msg,
                    R.string.login_label_unlogin_ok, R.string.common_label_cancel,
                    new AlertDialog.OnAlertDialogListener() {
                        @Override
                        public void onOk(AlertDialog alertDialogView) {
                            Intent intent = new Intent(DynamicDetailsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Preferences.isRefreshing(true);
                            alertDialogView.dismiss();
                        }

                        @Override
                        public void onCancel(AlertDialog alertDialogView) {
                            alertDialogView.dismiss();
                        }
                    }).show();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("activity_id", activityId);
        params.put("user_id", Preferences.getUserId());
        params.put("association_id", associationId);
        RestClient.get(Constant.API_GET_MAIN_DYNAMIC_JOINUS,params,new AsyncHttpResponseHandler(this,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS){
                        JSONObject jsonObject = response.getJSONObject("data");
                        status = jsonObject.optInt("status");
                        Drawable drawable;
                        if (status == 0) {
                            activityEnroll.setText("已报名");
                            activityNumber.setText(String.valueOf(++numbeir));
                            drawable = getResources().getDrawable(R.mipmap.main_ic_complete);
                        } else {
                            activityNumber.setText(String.valueOf(--numbeir));
                            activityEnroll.setText("报名");
                            drawable = getResources().getDrawable(R.mipmap.main_ic_no_complete);
                        }
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        activityEnroll.setCompoundDrawables(drawable, null, null, null);
                        ++ clickNum;
                        if (clickNum % 2 == 0){
                            status = -100;
                        }
                    }else{
                        String msg = response.getString("msg");
                        ToastUtil.make(DynamicDetailsActivity.this).show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    @Override
    public void onBackClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("status",status);
        this.setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            intent.putExtra("status",status);
            this.setResult(RESULT_OK,intent);
            finish();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }
}

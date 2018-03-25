package com.marchsoft.organization;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.convert.OrganizationDetailJSONConvert;
import com.marchsoft.organization.convert.UserJSONConvert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.User;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrganizationPresidentActivity extends Activity{
    private CircleImageView circleImageView;
    private TextView nameTextView;
    private TextView nicknameTextView;
    private TextView sexTextView;
    private TextView departmentTextView;
    private TextView majorTextView;
    private TextView classTextView;
    private TextView phoneTextView;
    private TextView mailTextView;
    private TextView personalTextView;
    private TextView title;
    private User president;
    private int presidnetId;
    private Context mContext;
    private ImageLoadingListener mImageLoadingListener;
    private ImageLoader mImageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_president_data);
        initView();
        mContext = this;
        presidnetId = getIntent().getIntExtra("presidnetId", 0);
        getPresidentData();
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
    }

    private void initView() {
        circleImageView = (CircleImageView)findViewById(R.id.org_president_data_image);
        title = (TextView)findViewById(R.id.organization_data_title);
        nameTextView = (TextView)findViewById(R.id.org_presidient_data_name);
        nicknameTextView = (TextView)findViewById(R.id.org_presidient_data_nickName);
        sexTextView = (TextView)findViewById(R.id.org_presidient_data_sex);
        departmentTextView = (TextView)findViewById(R.id.org_presidient_data_department);
        majorTextView = (TextView)findViewById(R.id.org_presidient_data_major);
        personalTextView = (TextView)findViewById(R.id.org_presidient_data_personalinfo);
        classTextView = (TextView)findViewById(R.id.org_presidient_data_class);
        phoneTextView = (TextView)findViewById(R.id.org_presidient_data_phone);
        mailTextView = (TextView)findViewById(R.id.org_presidient_data_mail);

    }

    private void setValues() {
        mImageLoader.displayImage(president.getmHeadImage(), circleImageView);
        nameTextView.setText(president.getmStudent_name());
        nicknameTextView.setText(getValue(president.getmNickname()));
        sexTextView.setText(convertIntToString(president.getmSex()));
        departmentTextView.setText(president.getmDepartment());
        majorTextView.setText(president.getmMajor());
        classTextView.setText(president.getmClass());
        phoneTextView.setText(getValue(president.getmCell_phone()));
        mailTextView.setText(getValue(president.getmEmail()));
        personalTextView.setText(getValue(president.getmSign()));
    }


    private String convertIntToString(int sex){
        if (isNull(String.valueOf(sex))){
            return "未填写";
        }else {
            if (sex == 0){
                return "男";
            }else {
                return "女";
            }
        }

    }
    private boolean isNull(String value){
        if (value.equals("null") || value == ""){
            return true;
        }else {
            return false;
        }
    }
    private String getValue(String value){
        if (isNull(value)){
            return "未填写";
        }else {
            return value;
        }
    }

    public void getPresidentData() {
        int userId = Preferences.getUserId();
        RequestParams params = new RequestParams();
        params.put("id", presidnetId);
        RestClient.get(Constant.API_GET_ORGANIZATION_SHOW_PRESIDENT_DATA, params, new AsyncHttpResponseHandler(this, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        president = UserJSONConvert.convertJsonToItem(jsonObject);
                        setValues();
                    } else {
                        String msg = response.getString("msg");
                        ToastUtil.make(mContext).show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }



    public void onBackClick(View v) {
        finish();
    }
}

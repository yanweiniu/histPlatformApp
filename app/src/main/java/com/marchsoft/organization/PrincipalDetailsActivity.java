package com.marchsoft.organization;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.convert.UserJSONConvert;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/3/3 0003.
 */
public class PrincipalDetailsActivity extends  BaseActivity{
    private int id;
    private CircleImageView circleImageView;
    private TextView nickName;
    private TextView studentName;
    private TextView age;
    private TextView address;
    private TextView department;
    private TextView mClass;
    private TextView major;
    private TextView email;
    private TextView phone;
    private TextView sign;
    private ImageLoader mImageLoader;
    private ImageLoadingListener mImageLoadingListener;
    private TextView sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_details);
        id = getIntent().getIntExtra("id", -1);
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
        getPrincipalDetails();
        
    }

    private void getPrincipalDetails() {
        RequestParams params = new RequestParams();
        params.put("id", id);
        RestClient.get(Constant.API_GET_MINE_DETAILS, params, new AsyncHttpResponseHandler(PrincipalDetailsActivity.this, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        //List<User> list = UserJSONConvert.convertJsonArrayToItemList(response.getJSONArray("data"));
                        User user = UserJSONConvert.convertJsonToItem(response.getJSONObject("data"));
                        //User user = list.get(0);
                        mImageLoader.displayImage(user.getmHeadImage(), circleImageView);
                        nickName.setText(user.getmNickname());
                        studentName.setText(user.getmStudent_name());
                        if (!user.getmBirthday().equals("未知")){
                            age.setText(calcAge(Long.parseLong(user.getmBirthday())*1000)+"");
                        }else{
                            age.setText(user.getmBirthday());
                        }
                        address.setText(user.getmLocation());
                        department.setText(user.getmDepartment());
                        mClass.setText(user.getmClass() + "");
                        major.setText(user.getmMajor());
                        email.setText(user.getmEmail());
                        phone.setText(user.getmCell_phone());
                        sign.setText(user.getmSign());
                        if (user.getmSex() == 2){
                            sex.setText("未知");
                        }else {
                            sex.setText(user.getmSex() > 0 ? "女" : "男");
                        }
                    } else {
                        String msg = response.getString("msg");
                        ToastUtil.make(PrincipalDetailsActivity.this).show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private void initHeader() {
        circleImageView = (CircleImageView) findViewById(R.id.img_avatar);
        nickName = (TextView) findViewById(R.id.tv_nickname);
        studentName = (TextView) findViewById(R.id.tv_student_name);
        age = (TextView) findViewById(R.id.tv_age);
        address = (TextView) findViewById(R.id.tv_address);
        department = (TextView) findViewById(R.id.tv_department);
        mClass = (TextView) findViewById(R.id.tv_class);
        major = (TextView) findViewById(R.id.tv_major);
        email = (TextView) findViewById(R.id.tv_email);
        phone = (TextView) findViewById(R.id.tv_cell_phone);
        sign = (TextView) findViewById(R.id.tv_sign);
        sex = (TextView) findViewById(R.id.tv_sex);

    }

    public static int calcAge(long birth){
        Date nowDate= new Date();
        Date birthDate= new Date(birth);
        Calendar flightCal= Calendar.getInstance();
        flightCal.setTime(nowDate);
        Calendar birthCal= Calendar.getInstance();
        birthCal.setTime(birthDate);
        int y= flightCal.get(Calendar.YEAR)-birthCal.get(Calendar.YEAR);
        int m= flightCal.get(Calendar.MONTH)-birthCal.get(Calendar.MONTH);
        int d= flightCal.get(Calendar.DATE)-birthCal.get(Calendar.DATE);
        if(y<=0){
            return 0;
        }

        if(m<0){
            y--;
        }
        if(m>=0&&d<0){
            y--;
        }

        return y;
    }
}

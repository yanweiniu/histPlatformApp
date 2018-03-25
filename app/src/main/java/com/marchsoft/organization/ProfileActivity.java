

package com.marchsoft.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.model.User;
import com.marchsoft.organization.utils.DateUtils;
import com.marchsoft.organization.utils.ToastUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by w on 2016/2/23.
 */
public class ProfileActivity extends BaseActivity implements  View.OnClickListener{
    private TextView profile_cancle;
    private TextView profile_info_name;
    private TextView profile_info_nickname;
    private TextView profile_info_sex;
    private TextView profile_info_birthday;
    private TextView profile_info_department;
    private TextView profile_info_major;
    private TextView profile_info_class;
    private TextView profile_info_location;
    private TextView profile_info_hometown;
    private TextView profile_info_email;
    private TextView profile_info_personal;
    private TextView profile_info_phone;
    public  User mUser;
    private TextView profile_editor;
    private String dstring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);
        profile_editor = (TextView)findViewById(R.id.profile_editor);
        profile_info_name = (TextView) findViewById(R.id.profile_info_name);
        profile_info_nickname = (TextView) findViewById(R.id.profile_info_nickname);
        profile_info_sex = (TextView) findViewById(R.id.profile_info_sex);
        profile_info_birthday = (TextView) findViewById(R.id.profile_info_birthday);
        profile_info_department = (TextView) findViewById(R.id.profile_info_department);
        profile_info_major = (TextView) findViewById(R.id.profile_info_major);
        profile_info_class = (TextView) findViewById(R.id.profile_info_class);
        profile_info_location = (TextView) findViewById(R.id.profile_info_location);
        profile_info_hometown = (TextView) findViewById(R.id.profile_info_hometown);
        profile_info_email = (TextView) findViewById(R.id.profile_info_email);
        profile_info_personal = (TextView) findViewById(R.id.profile_info_personal);
        profile_info_phone = (TextView)findViewById(R.id.profile_info_phone);
        mUser=Preferences.getLoginUser();
        profile_info_name.setText(mUser.getmStudent_name());
        profile_info_nickname.setText(mUser.getmNickname());
        if(mUser.getmSex() == 0){
            profile_info_sex.setText("男");
        }else if(mUser.getmSex() == 1){
            profile_info_sex.setText("女");
        }else{
            profile_info_sex.setText("未知");
        }

        Date date = null;
                if(mUser.getmBirthday().equals("未知")){
                    profile_info_birthday.setText("未知");
                }else{
                    Calendar calendar = Calendar.getInstance();
                    try {
                        date = DateUtils.getDateToString(Long.parseLong(mUser.getmBirthday()));
                        calendar.setTime(date);
                    } catch (ParseException e1) {
                        // TODO 自动生成的 catch 块
                        e1.printStackTrace();
                    }
                    int year =  calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH)+1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    if(year == 0 || month == 0 ||  day == 0){
                        String dstring = year+"年"+month+"月"+day+"日";
                    }else{
                        String dstring = year+"年"+month+"月"+day+"日";
                    }
                    profile_info_birthday.setText(year+"年"+month+"月"+day+"日");
                }


        if(mUser.getmBirthday().equals("未知")){
            profile_info_birthday.setText("未知");
        }else{
            Calendar calendar = Calendar.getInstance();
            try {
                date = DateUtils.getDateToString(Long.parseLong(mUser.getmBirthday()));
                calendar.setTime(date);
            } catch (ParseException e1) {
                // TODO 自动生成的 catch 块
                e1.printStackTrace();
            }
            int year =  calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            profile_info_birthday.setText(year+"年"+month+"月"+day+"日");
        }
        

        profile_info_department.setText(mUser.getmDepartment());
        profile_info_major.setText(mUser.getmMajor());
        profile_info_class.setText(mUser.getmClass());
        profile_info_location.setText(mUser.getmLocation());
        profile_info_hometown.setText(mUser.getmHome());
        profile_info_email .setText(mUser.getmEmail());
        profile_info_personal.setText(mUser.getmSign());
        profile_info_phone.setText(mUser.getmCell_phone());
        profile_editor.setOnClickListener(this);
    }

    //回调方法传递值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3){
            if (data!=null){
                profile_info_nickname.setText(data.getStringExtra("nickname"));
                if(data.getStringExtra("email").equals("")){
                    profile_info_email.setText("未知");
                }
                if(data.getStringExtra("cell_phone").equals("")){
                    profile_info_phone.setText("未知");
                }
                profile_info_personal.setText(data.getStringExtra("sign"));
                profile_info_location.setText(data.getStringExtra("location"));
                profile_info_hometown.setText(data.getStringExtra("home"));
                profile_info_birthday.setText(data.getStringExtra("birthday"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if(v == profile_editor){
            intent = new Intent(this,ProfileEditor.class);
            startActivityForResult(intent, 3);
        }
    }
}


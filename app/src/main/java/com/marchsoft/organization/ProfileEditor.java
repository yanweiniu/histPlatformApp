package com.marchsoft.organization;

import android.content.Intent;
import android.os.Bundle;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.PCD;
import com.marchsoft.organization.model.User;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.DateUtils;
import com.marchsoft.organization.utils.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marchsoft.organization.wheel.widget.WheelView;
import com.marchsoft.organization.wheel.widget.adapters.ArrayWheelAdapter;
import com.marchsoft.organization.widget.AlertDialog;
import com.marchsoft.organization.widget.ScreenInfo;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.MyAlertDialog;
import com.marchsoft.organization.widget.WheelAlertDialog;
import com.marchsoft.organization.widget.WheelMain;
import org.apache.http.Header;

import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by w on 2016/2/28.
 */
public class ProfileEditor extends BaseActivity implements View.OnClickListener{
    private LinearLayout nickname;
    private LinearLayout birthday;
    private LinearLayout location;
    private LinearLayout hometown;
    private LinearLayout phone;
    private LinearLayout email;
    private LinearLayout personal;
    private TextView profile_editor_save;
    private TextView profile_info_name;
    private TextView profile_info_sex;
    private EditText profile_info_nickname1;
    private TextView profile_info_birthday;
    private TextView profile_info_department;
    private TextView  profile_info_major;
    private TextView  profile_info_class;
    private TextView profile_info_location;
    private TextView profile_info_hometown;
    private TextView profile_info_phone;
    private TextView profile_info_email;
    private TextView profile_info_personal;

    //院系里面的选项
    private TextView henan_college;
    private TextView message_college;
    private TextView Electrical_college;
    private TextView art_college;
    private TextView life_college;
    private TextView education_college;
    private TextView grammar_college;
    protected int mYear;
    protected int mMonth;
    protected int mDay;
    private String birthdayss;
    private User mUser;
    private String prsonal_sign;
    private String province_content;
    private String home_content;
    private long birthday1;
    private ImageView profile_editor_break;
    private int ff;
    private int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_editor);
        flag = 0;
        profile_editor_break = (ImageView) findViewById(R.id.profile_break);
        profile_editor_break.setOnClickListener(this);
        //个人信息的修改
        profile_info_name = (TextView)findViewById(R.id.profile_info_name);
        profile_info_nickname1 = (EditText)findViewById(R.id.profile_info_nickname1);
        profile_info_sex = (TextView)findViewById(R.id.profile_info_sex);
        profile_info_birthday =(TextView) findViewById(R.id.profile_info_birthday);
        profile_info_department = (TextView)findViewById(R.id.profile_info_department);
        profile_info_major =(TextView) findViewById(R.id.profile_info_major);
        profile_info_class = (TextView)findViewById(R.id.profile_info_class);
        profile_info_location =(TextView) findViewById(R.id.profile_info_location);
        profile_info_hometown = (TextView) findViewById(R.id.profile_info_hometown);
        profile_info_phone =(TextView) findViewById(R.id.profile_info_phone);
        profile_info_email = (TextView) findViewById(R.id.profile_info_email);
        profile_info_personal = (TextView)findViewById(R.id.profile_info_personal);
        mUser= Preferences.getLoginUser();
        profile_info_name.setText(mUser.getmStudent_name());
        profile_info_nickname1.setText(mUser.getmNickname());
        if(mUser.getmSex() == 0){
            profile_info_sex.setText("男");
        }else if(mUser.getmSex() == 1){
            profile_info_sex.setText("女");
        }else{
            profile_info_sex.setText("未知");
        }

//年月日的读取
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


            profile_info_birthday.setText(year+"年"+month+"月"+day+"日");
        }

        profile_info_department.setText(mUser.getmDepartment());
        profile_info_major.setText(mUser.getmMajor());
        profile_info_class.setText(mUser.getmClass());
        profile_info_location.setText(mUser.getmLocation());
        profile_info_hometown.setText(mUser.getmHome());
        profile_info_email.setText(mUser.getmEmail());
        profile_info_personal.setText(mUser.getmSign());
        profile_info_nickname1.setText(mUser.getmNickname());
        profile_info_phone.setText(mUser.getmCell_phone());
        profile_info_location.setText(mUser.getmLocation());
        profile_info_hometown.setText(mUser.getmHome());
        profile_info_email.setText(mUser.getmEmail());
        profile_info_personal.setText(mUser.getmSign());
        //控件的监听事件
        nickname =  (LinearLayout)findViewById(R.id.nickname);
        birthday = (LinearLayout) findViewById(R.id.birthday);
        location =(LinearLayout) findViewById(R.id.location);
        hometown = (LinearLayout) findViewById(R.id.hometown);
        phone =(LinearLayout) findViewById(R.id.phone);
        email = (LinearLayout)findViewById(R.id.email);
        personal = (LinearLayout)findViewById(R.id.personal);
        profile_editor_save = (TextView)findViewById(R.id.profile_editor_save);
        province_content = mUser.getmLocation();
        home_content = mUser.getmHome();
        //事件的监听
        profile_info_birthday.setOnClickListener(this);
        profile_editor_save.setOnClickListener(this);
        profile_info_nickname1.setOnClickListener(this);
        profile_info_hometown.setOnClickListener(this);
        profile_info_location.setOnClickListener(this);
        profile_info_personal.setOnClickListener(this);

        //文本框的数据改变事件
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeStates();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        //给文本框加监听
        profile_info_nickname1.addTextChangedListener(textWatcher);
        profile_info_birthday.addTextChangedListener(textWatcher);
        profile_info_location.addTextChangedListener(textWatcher);
        profile_info_hometown.addTextChangedListener(textWatcher);
        profile_info_phone.addTextChangedListener(textWatcher);
        profile_info_email.addTextChangedListener(textWatcher);
        profile_info_personal.addTextChangedListener(textWatcher);
    }

    @Override
    public void onClick(View v) {
        if (v == profile_editor_save) {
            Intent intent = new Intent();
            //将数剧传送给主页
            intent.putExtra("birthday", profile_info_birthday.getText());
            intent.putExtra("nickname", profile_info_nickname1.getText().toString());
            intent.putExtra("email", profile_info_email.getText().toString());
            intent.putExtra("cell_phone", profile_info_phone.getText().toString());
            intent.putExtra("sign", profile_info_personal.getText().toString());
            intent.putExtra("location", province_content);
            intent.putExtra("home", home_content);
            if (!Utils.isMobileNO(profile_info_phone.getText().toString())) {
                ToastUtil.make(this).show("你输入的手机号码不合法！");
            } else if (!Utils.isEmail(profile_info_email.getText().toString())) {
                ToastUtil.make(this).show("你输入的邮箱格式不合法！");
            } else {
                profile_info_phone.setFocusable(true);
                editorMine();
                setResult(3, intent);
                Preferences.isRefreshing(true);
                finish();
            }
        }

        if (v == profile_info_nickname1) {
            profile_info_nickname1.setFocusable(true);
        }
        if (v == profile_info_birthday) {
            showDateDialog(profile_info_birthday);
        }
        if (v == profile_info_location) {
            showPCd();
        }
        if (v == profile_info_hometown) {
            showHome();
    }
    if (v == profile_info_personal) {
        Intent intent = new Intent(this, profile_personal.class);
        startActivityForResult(intent, 4);
    }

        if (v == profile_editor_break) {
            if (!(profile_info_nickname1.getText().toString().equals(mUser.getmNickname())) || !(profile_info_email.getText().toString().equals(mUser.getmEmail()))
                    || !(profile_info_phone.getText().toString().equals(mUser.getmCell_phone())) || !(profile_info_personal.getText().toString().equals(mUser.getmSign()))
                    || !(profile_info_location.getText().toString().equals(mUser.getmLocation())) || !(profile_info_hometown.getText().toString().equals(mUser.getmHome()))
                    || !(String.valueOf(DateUtils.getStringToDate(profile_info_birthday.getText().toString())).equals(mUser.getmBirthday()))) {
                showDialogEditor();
            }else {
                finish();
            }
        }
    }

    //文本框的值改变时，保存按钮的状态改变
    private void changeStates() {
        if (!(profile_info_nickname1.getText().toString().equals(mUser.getmNickname())) || !(profile_info_email.getText().toString().equals(mUser.getmEmail()))
                || !(profile_info_phone.getText().toString().equals(mUser.getmCell_phone())) || !(profile_info_personal.getText().toString().equals(mUser.getmSign()))
                || !(profile_info_location.getText().toString().equals(mUser.getmLocation())) || !(profile_info_hometown.getText().toString().equals(mUser.getmHome()))
                || !(String.valueOf(DateUtils.getStringToDate(profile_info_birthday.getText().toString())).equals(mUser.getmBirthday()))) {
            profile_editor_save.setTextColor(getResources().getColor(R.color.common_actionbar_textcolor));
            profile_editor_save.setEnabled(true);
        } else {
            profile_editor_save.setTextColor(getResources().getColor(R.color.common_no_changepwd_textcolor));
            profile_editor_save.setEnabled(false);
        }
    }


    //当编辑完代码，未保存，直接点击退出按钮时执行的方法
    private void showDialogEditor(){
        AlertDialog.build(this, R.string.profile_editor_give_up,
                R.string.common_label_ok, R.string.common_label_cancel,
                new AlertDialog.OnAlertDialogListener() {
                    @Override
                    public void onOk(AlertDialog alertDialogView) {
                        alertDialogView.dismiss();
                        finish();
                    }
                    @Override
                    public void onCancel(AlertDialog alertDialogView) {
                        alertDialogView.dismiss();
                    }
                }).show();
    }

    //所在地弹出对话框
    public void showPCd() {
        WheelAlertDialog.build(this, "所在地", "保存", "取消", new WheelAlertDialog.OnAlertDialogListener() {
            @Override
            public void onOk(WheelAlertDialog alertDialogView, String content,PCD pcd) {
                //地址信息存在PCD pcd = Preferences.getPCD(),设置pcd;
                Preferences.setPCD(pcd);
                province_content = content;
                    profile_info_location.setText(content);
            alertDialogView.dismiss();
        }
        @Override
        public void onCancel(WheelAlertDialog alertDialogView, String content) {
                if (Preferences.getFirst()){
                    Preferences.setFirst(true);
                    Preferences.setisFirst(true);
                }
                alertDialogView.dismiss();
            }
        }).show();;
    }

    //故乡弹出对话框
    public void showHome() {
        WheelAlertDialog.build(this, "故乡", "保存", "取消", new WheelAlertDialog.OnAlertDialogListener() {
            @Override
            public void onOk(WheelAlertDialog alertDialogView, String content,PCD pcd) {
                Preferences.setPCD(pcd);
                home_content = content;
                profile_info_hometown.setText(content);
                //记得把数据提交给服务器
                //编辑资料页关闭时个人资料页所在页的地址也得更新，我把这些地址信息存在PCD pcd = Preferences.getPCD()里,你自己设置
                alertDialogView.dismiss();
            }
            @Override
            public void onCancel(WheelAlertDialog alertDialogView, String content) {
                if (Preferences.getFirst()){
                    Preferences.setFirst(true);
                    Preferences.setisFirst(true);
                }
                alertDialogView.dismiss();
            }
        }).show();
    }


    //生日弹出对话框
    public void showDateDialog(View v) {
        LayoutInflater inflater = LayoutInflater.from(this
                .getApplicationContext());
        ScreenInfo screenInfo = new ScreenInfo(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        final WheelMain wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        String bir = mUser.getmBirthday();

        Date date = null;
        Calendar calendar = Calendar.getInstance();
        if(mUser.getmBirthday().equals("未知")){

        }else{
            try {
                date = DateUtils.getDateToString(Long.parseLong(mUser.getmBirthday()));
                calendar.setTime(date);
            } catch (ParseException e1) {
                // TODO 自动生成的 catch 块
                e1.printStackTrace();
            }
        }

        int year =  calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, day);


        final MyAlertDialog dialog = new MyAlertDialog(this).builder()
                .setTitle("选择生日").setView(timepickerview)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataString = wheelMain.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse(dataString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH) + 1;
                mDay = c.get(Calendar.DAY_OF_MONTH);
                birthdayss = String.valueOf(mYear)+"年"+String.valueOf(mMonth)+"月"+String.valueOf(mDay)+"日";
                profile_info_birthday.setText(birthdayss);
            }
        });
        dialog.show();
    }

    //连接接口修改信息
    private void editorMine(){
        RequestParams params=new RequestParams();
//        params.put("student_id",mUser.getmUserId() );
        params.put("student_id", Preferences.getUserId());

        if(!(profile_info_nickname1.getText().toString().equals(mUser.getmNickname()))){
            params.put("nickname", profile_info_nickname1.getText().toString());
        }
        if(!(profile_info_email.getText().toString().equals(mUser.getmEmail()))) {
            params.put("email", profile_info_email.getText().toString());
        }
        if(!(profile_info_phone.getText().toString().equals(mUser.getmCell_phone()))){
            params.put("cell_phone", profile_info_phone.getText().toString());
        }
        if(!(profile_info_personal.getText().toString().equals(mUser.getmSign()))){
            params.put("sign", profile_info_personal.getText().toString());
        }
        if(!(profile_info_location.getText().toString().equals(mUser.getmLocation()))){
            params.put("location", profile_info_location.getText().toString());
        }
        if(!(profile_info_hometown.getText().toString().equals(mUser.getmHome()))){
            params.put("home", profile_info_hometown.getText().toString());
        }
        if(!((mUser.getmBirthday()).equals("未知"))){
            if(!(String.valueOf(DateUtils.getStringToDate(profile_info_birthday.getText().toString())).equals(mUser.getmBirthday()))){
                params.put("birthday", DateUtils.getStringToDate(birthdayss));
            }
        }
        RestClient.post(Constant.API_EDITOR_MINE, params, new AsyncHttpResponseHandler(getBaseContext(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //把数据放回到缓存中
                mUser.setmNickname(profile_info_nickname1.getText().toString());
                mUser.setmEmail(profile_info_email.getText().toString());
                mUser.setmCell_phone(profile_info_phone.getText().toString());
                mUser.setmSign(profile_info_personal.getText().toString());
                mUser.setmLocation(profile_info_location.getText().toString());
                mUser.setmHome(profile_info_hometown.getText().toString());
                mUser.setmClass(profile_info_class.getText().toString());
                mUser.setmMajor(profile_info_major.getText().toString());
                mUser.setmDepartment(profile_info_department.getText().toString());
                if(!((mUser.getmBirthday()).equals("未知"))){
                    mUser.setmBirthday(String.valueOf(DateUtils.getStringToDate(profile_info_birthday.getText().toString())));
                }
                Preferences.setLoginUser(mUser);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        }));
    }


    //回调方法传递值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4){
            if (data!=null){
                prsonal_sign = data.getStringExtra("personal_sign");
                profile_info_personal.setText(prsonal_sign);
            }
        }
    }

    //判断信息是否修改
    @Override
    public boolean equals(Object obj) {
        User other = (User) obj;
        //判断个人信息是否已经修改
        if (profile_info_nickname1.getText().toString() != other.getmNickname()){
            return false;
        }
        if (profile_info_phone.getText().toString() != other.getmCell_phone()){
            return false;
        }
        if (profile_info_email.getText().toString() != other.getmEmail()){
            return false;
        }
        if (profile_info_personal.getText().toString() != other.getmSign()){
            return false;
        }
        if (profile_info_location.getText().toString() != other.getmLocation()){
            return false;
        }
        if (profile_info_hometown.getText().toString() != other.getmHome()){
            return false;
        }
        if ((DateUtils.getStringToDate(profile_info_birthday.getText().toString()) != Long.parseLong(other.getmBirthday()))){
            return false;
        }
        return true;
    }

}

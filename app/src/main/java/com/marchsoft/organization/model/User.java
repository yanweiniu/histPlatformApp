package com.marchsoft.organization.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.marchsoft.organization.utils.ToastUtil;

public class User implements Parcelable {

    private int mUserId;// id
    private String mStudent_id;//学号
    private String mStudent_name;//姓名
    private String mNickname; //昵称
    private String mHeadImage; //头像
    private String mPhoto;    //图片
    private String mCell_phone;//手机号
    private int   mQQ;         //QQ号
    private String mEmail;    //email邮件
    private int mSex;         //性别 （0:男，1：女，2：保密）
    private String mLocation; //所属地
    private String mHome;      //故乡
    private String mSign;     //个性签名
    private String mMajor;     //所学专业
    private String mClass;     //所属班级
    private String mDepartment;    //所属院系
    private String mBirthday;//出生日期

    private int mStatus;//登录状态
    private String mToken;//TOEN
    public User(){}
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
    protected User(Parcel in) {
    }

    public String getmStudent_id() {
        return mStudent_id;
    }

    public void setmStudent_id(String mStudent_id) {
        this.mStudent_id = mStudent_id;
    }
    public String getmStudent_name() {
        return mStudent_name;
    }

    public void setmStudent_name(String mStudent_name) {
        this.mStudent_name = mStudent_name;
    }
    public String getmNickname() {
        return mNickname;
    }

    public void setmNickname(String mNickname) {
        this.mNickname = mNickname;
    }

    public String getmCell_phone() {
        return mCell_phone;
    }

    public void setmCell_phone(String mCell_phone) {
        this.mCell_phone = mCell_phone;
    }

    public int getmQQ() {
        return mQQ;
    }

    public void setmQQ(int mQQ) {
        this.mQQ = mQQ;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getmSex() {
        return mSex;
    }

    public void setmSex(int mSex) {
        this.mSex = mSex;
    }

    public String getmLocation() {
        return mLocation;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmHome() {
        return mHome;
    }

    public void setmHome(String mHome) {
        this.mHome = mHome;
    }
    public String getmClass() {
        return mClass;
    }

    public void setmClass(String mClass) {
        this.mClass = mClass;
    }

    public String getmDepartment() {
        return mDepartment;
    }

    public void setmDepartment(String mDepartment) {
        this.mDepartment = mDepartment;
    }
    public String getmMajor() {
        return mMajor;
    }

    public void setmMajor(String mMajor) {
        this.mMajor = mMajor;
    }


    public String getmHeadImage() {
        return mHeadImage;
    }

    public void setmHeadImage(String mHeadImage) {
        this.mHeadImage = mHeadImage;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }
    public String getmSign() {
        return mSign;
    }

    public void setmSign(String mSign) {
        this.mSign = mSign;
    }

    public String getmBirthday() {
        return mBirthday;
    }

    public void setmBirthday(String mBirthday) {
        this.mBirthday = mBirthday;
    }
    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }
    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * 从json串中解析user对象
     *
     * @param json
     * @return
     */
    public static User fromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        return user;
    }

    /**
     * 把user对象转化成json串
     *
     * @param user
     * @return
     */
    public static String toJson(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        return userJson;
    }



}

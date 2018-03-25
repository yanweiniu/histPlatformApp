package com.marchsoft.organization.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.marchsoft.organization.model.PCD;
import com.marchsoft.organization.model.User;

public final class Preferences {
    private static SharedPreferences sSHARED_REFERENCES = null;
    private static Context sAPPLICATION_CONTEXT;
    private static String sDEVICE_ID;
    private static String sVERSION_NAME;

    public Preferences() {
    }

    public static void init(Context context) {
        if (sSHARED_REFERENCES == null) {
            sAPPLICATION_CONTEXT = context.getApplicationContext();
            sSHARED_REFERENCES = PreferenceManager
                    .getDefaultSharedPreferences(sAPPLICATION_CONTEXT);

        }
    }

    public static Context getApplicationContext() {
        return sAPPLICATION_CONTEXT;
    }

    public static SharedPreferences getSharedPreferences() {
        return sSHARED_REFERENCES;
    }

    public static boolean isShowWelcome() {
        try {
            PackageInfo info = sAPPLICATION_CONTEXT.getPackageManager()
                    .getPackageInfo(sAPPLICATION_CONTEXT.getPackageName(), 0);
            // 当前版本的版本号
            int versionCode = info.versionCode;
            int saveVersionCode = sSHARED_REFERENCES.getInt("version_code", 0);
            return versionCode != saveVersionCode;
        } catch (NameNotFoundException e) {
            return true;
        }
    }

    public static boolean updateVersionCode() {
        try {
            PackageInfo info = sAPPLICATION_CONTEXT.getPackageManager()
                    .getPackageInfo(sAPPLICATION_CONTEXT.getPackageName(), 0);
            // 当前版本的版本号
            int versionCode = info.versionCode;
            Editor editor = sSHARED_REFERENCES.edit();
            editor.putInt("version_code", versionCode);
            return editor.commit();
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static String getDeviceId() {
        if (sDEVICE_ID == null) {
            sDEVICE_ID = ((TelephonyManager) sAPPLICATION_CONTEXT
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        return sDEVICE_ID;
    }

    public static String getVersionName() {
        if (sVERSION_NAME == null) {
            try {
                PackageInfo info = sAPPLICATION_CONTEXT.getPackageManager()
                        .getPackageInfo(sAPPLICATION_CONTEXT.getPackageName(),
                                0);
                // 当前应用的版本名称
                sVERSION_NAME = info.versionName;
                // 当前版本的版本号
                // int versionCode = info.versionCode;

                // 当前版本的包名
                // String packageNames = info.packageName;
            } catch (NameNotFoundException e) {
            }
        }
        return sVERSION_NAME;
    }


    public static String getAccessToken() {
        return sSHARED_REFERENCES.getString("access_token", null);
    }


    public static boolean setAccessToken(String accessToken) {
        Editor editor = sSHARED_REFERENCES.edit();
        editor.putString("access_token", accessToken);
        if (editor.commit()) {
            return true;
        }
        return false;
    }


    public static boolean isLogin() {
        return sSHARED_REFERENCES.getString("access_token", null) == null ? false : true;
    }

    public static boolean clear() {
        // 清除数据库表内容
        DatabaseManager.clear();
        // clear token & user info & order settting
        Editor editor = sSHARED_REFERENCES.edit();
        editor.remove("access_token");
        editor.remove("login_user");
        editor.remove("user_id");
        editor.remove("user_number");
        return editor.commit();
    }

    public static boolean setLoginUser(User user) {
        Editor editor = sSHARED_REFERENCES.edit();
        editor.putString("login_user", User.toJson(user));
        if (editor.commit()) {
            return true;
        }
        return false;
    }

    public static User getLoginUser() {
        //TODO 测试，如果字段不一致会不会抛异常
        return User.fromJson(sSHARED_REFERENCES.getString("login_user", null));
    }
    public static int getUserId() {
        return sSHARED_REFERENCES.getInt("user_id", 0);
    }

    public static boolean setUserId(int useId) {
        Editor editor = sSHARED_REFERENCES.edit();
        editor.putInt("user_id", useId);
        if (editor.commit()) {
            return true;
        }
        return false;
    }
    public static String getUserNumber() {
        return sSHARED_REFERENCES.getString("user_number", null);
    }


//    public static int getUserID(){
//        User user = getLoginUser();
//        User user1 = new User();
//        return
//    }
    public static boolean setUserNumber(String userName) {
        Editor editor = sSHARED_REFERENCES.edit();
        editor.putString("user_number", userName);
        if (editor.commit()) {
            return true;
        }
        return false;
    }


    public static boolean setPCD(PCD pcd){
        Editor editor = sSHARED_REFERENCES.edit();
        editor.putString("PCD",PCD.toJson(pcd));
        if (editor.commit()){
            return  true;
        }
        return  false;
    }
    public static PCD getPCD(){
        Boolean isFirst = sSHARED_REFERENCES.getBoolean("FIRST", true);
       if(isFirst){//第一次
            sSHARED_REFERENCES.edit().putBoolean("FIRST", false).commit();
            return  new PCD();
        }
        return PCD.fromJson(sSHARED_REFERENCES.getString("PCD",null));
    }


    public static void setFirst(boolean flag){
         sSHARED_REFERENCES.edit().putBoolean("FIRST",flag).commit();
    }
    public static void setisFirst(boolean flag){
        sSHARED_REFERENCES.edit().putBoolean("isFIRST",flag).commit();
    }
    public static boolean getFirst(){
        Boolean isFirst = sSHARED_REFERENCES.getBoolean("isFIRST", true);
        if (isFirst){
            sSHARED_REFERENCES.edit().putBoolean("isFIRST", false).commit();
            return true;
        }
        return false;
    }

    public static boolean isRefreshing(boolean flag){
        Editor editor = sSHARED_REFERENCES.edit();
        editor.putBoolean("isRefreshing",flag);
        if (editor.commit()){
            return  true;
        }
        return  false;
    }
    public static boolean getRefreshing(){
        return sSHARED_REFERENCES.getBoolean("isRefreshing",false);
    }



}

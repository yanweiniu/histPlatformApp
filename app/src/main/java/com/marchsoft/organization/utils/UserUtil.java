package com.marchsoft.organization.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.LoginActivity;
import com.marchsoft.organization.MainActivity;
import com.marchsoft.organization.R;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.fragment.MainFragment;
import com.marchsoft.organization.widget.AlertDialog;
import com.marchsoft.organization.widget.InputDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public final class UserUtil {

    private UserUtil() {
    }

    public static void unloginConfirmActivity(final Activity context) {
        AlertDialog.build(context, R.string.login_label_unlogin_msg,
                R.string.login_label_unlogin_ok, R.string.common_label_cancel,
                new AlertDialog.OnAlertDialogListener() {

                    @Override
                    public void onOk(AlertDialog alertDialogView) {
                        alertDialogView.dismiss();
                        login(context);
                    }

                    @Override
                    public void onCancel(AlertDialog alertDialogView) {
                        alertDialogView.dismiss();
                    }
                }).show();
    }
    public static void unlogout(final Context context) {

    }

    /*
    修改用户昵称
     */
   /* public static void noNickNameInputActivity(final Context context, final InputDialog.OnInputBackListener back) {
        InputDialog.build(context, R.string.nick_input_text, R.string.nick_input_ok_text,
                new InputDialog.OnInputDialogListener() {
                    @Override
                    public void onOk(final InputDialog alertDialogView, final String content) {
                        User user = Preferences.getLoginUser();
                        user.setNickname(content);
                        Preferences.setLoginUser(user);

                        RequestParams params = new RequestParams();
                        params.put("nickname", content);
                        RestClient.post(Constant.API_USER_SETTING, params,
                                new AsyncHttpResponseHandler(context,
                                        new JsonHttpResponseHandler() {

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                super.onFailure(statusCode, headers, responseString, throwable);
                                                ToastUtil.make(context)
                                                        .show(R.string.nick_input_fail);
                                            }

                                            @Override
                                            public void onSuccess(int statusCode,
                                                                  Header[] headers, JSONObject rlt) {
                                                try {
                                                    if (rlt.getInt("code") == Constant.CODE_SUCCESS) {
                                                        User user = Preferences.getLoginUser();
                                                        user.setNickname(content);
                                                        Preferences.setLoginUser(user);
                                                        alertDialogView.dismiss();
                                                        back.onCallBack();
                                                    } else {
                                                        String msg = rlt.getString("msg");
                                                        ToastUtil.make(context)
                                                                .show(msg);
                                                    }
                                                } catch (
                                                        JSONException e
                                                        )

                                                {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                ));
                    }
                }).show();
    }*/

   private static void login(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult(intent, Activity.RESULT_OK);
        //context.startActivity(intent);
    }
    /*
    重新登录
     */

    /*public static synchronized void relogin(final Context context) {
        Log.i("jinsou-relogin", "need relogin");
        if (Preferences.isLogin()) {
            try {
                AlertDialog alertDialog = AlertDialog.build(context,
                        R.string.relogin_msg, R.string.relogin_btn_relogin,
                        R.string.relogin_btn_close,
                        new AlertDialog.OnAlertDialogListener() {

                            @Override
                            public void onOk(AlertDialog alertDialogView) {
                                Log.i("relogin",
                                        "login: " + Preferences.isLogin());
                                alertDialogView.dismiss();
                                login(context);
                            }

                            @Override
                            public void onCancel(AlertDialog alertDialogView) {
                                alertDialogView.dismiss();
                                Intent intent = new Intent(context,
                                        MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                        });
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();
                Preferences.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}

package com.marchsoft.organization.http;

import android.content.Context;
import android.content.Intent;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.LoginActivity;
import com.marchsoft.organization.R;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.Log;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.utils.UserUtil;
import com.marchsoft.organization.utils.Utils;
import com.marchsoft.organization.widget.AlertDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public final class AsyncHttpResponseHandler extends
        com.loopj.android.http.JsonHttpResponseHandler {
    private JsonHttpResponseHandler mAsyncHttpResponseHandler;
    private Context mContext;
    private boolean mIsSilent;// 静默获取数据，失败不做任何提示

    public AsyncHttpResponseHandler(Context context,
                                    JsonHttpResponseHandler asyncHttpResponseHandler) {
        this(context, asyncHttpResponseHandler, false);
    }

    public AsyncHttpResponseHandler(Context context,
                                    JsonHttpResponseHandler asyncHttpResponseHandler, boolean silent) {
        mContext = context;
        mAsyncHttpResponseHandler = asyncHttpResponseHandler;
        mIsSilent = silent;
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,
                          String responseString, Throwable throwable) {
        if (!mIsSilent) {
            if (mContext != null) {
                if (Utils.isNetworkConnected(mContext)) {
                    ToastUtil.make(mContext).show(
                            R.string.common_toast_connectionnodata);
                } else {
                    ToastUtil.make(mContext).show(
                            R.string.common_toast_connectionfailed);
                }
                Log.i("http response", responseString);
                JSONObject errorJsonObject = new JSONObject();
                try {
                    errorJsonObject.put("code", "-123789");
                    errorJsonObject.put("msg", responseString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mAsyncHttpResponseHandler != null) {
                    mAsyncHttpResponseHandler.onFailure(statusCode, headers,
                            throwable, errorJsonObject);
                } else {
                    onFailure(statusCode, headers, throwable, errorJsonObject);
                }
            }
        } else {
            super.onFailure(statusCode, headers, responseString, throwable);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,
                          Throwable throwable, JSONObject errorResponse) {

        Log.i("http result", throwable.toString());

        if (!mIsSilent) {
            if (mContext != null) {
                if (Utils.isNetworkConnected(mContext)) {
                    ToastUtil.make(mContext).show(
                            R.string.common_toast_connectionnodata);
                } else {
                    ToastUtil.make(mContext).show(
                            R.string.common_toast_connectionfailed);
                }
                if (mAsyncHttpResponseHandler != null) {
                    mAsyncHttpResponseHandler.onFailure(statusCode, headers,
                            throwable, errorResponse);
                } else {
                    super.onFailure(statusCode, headers, throwable,
                            errorResponse);
                }
            }
        } else {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.i("http result", response.toString());
        try {
            // 补充完整系统级参数
            switch (response.getInt("msg_code")) {
                case Constant.CODE_TIMEOUT:
                case Constant.CODE_DATA_ERROR:
                case Constant.CODE_DB_ERROR:
                    if (!mIsSilent) {
                        if (mContext != null) {
                            ToastUtil.make(mContext).show(
                                    R.string.common_toast_error);
                        }
                    }
                    break;
                case Constant.CODE_RELOGIN:
                /*if(Preferences.isLogin()){
					AlertDialog.build(mContext, R.string.login_label_relogin_msg,
							R.string.login_label_relogin, R.string.common_label_cancel,
							new AlertDialog.OnAlertDialogListener() {

								@Override
								public void onOk(AlertDialog alertDialogView) {
									Intent intent = new Intent(mContext, LoginActivity.class);
									mContext.startActivity(intent);
									Preferences.isRefreshing(true);
									alertDialogView.dismiss();
								}

								@Override
								public void onCancel(AlertDialog alertDialogView) {
									alertDialogView.dismiss();
								}
							}).show();
				}*/
                    break;
                case Constant.CODE_SUCCESS:
                case Constant.CODE_FAILURE:
                default:
                    if (mContext != null) {
                        if (mAsyncHttpResponseHandler == null) {
                            super.onSuccess(statusCode, headers, response);
                        } else {
                            mAsyncHttpResponseHandler.onSuccess(statusCode,
                                    headers, response);
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            super.onSuccess(statusCode, headers, response);
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish() {
        if (mContext != null) {
            if (mAsyncHttpResponseHandler == null) {
                super.onFinish();
            } else {
                mAsyncHttpResponseHandler.onFinish();
            }
        }
    }

    @Override
    public void onStart() {
        if (mContext != null) {
            if (mAsyncHttpResponseHandler == null) {
                super.onStart();
            } else {
                mAsyncHttpResponseHandler.onStart();
            }
        }
    }
}

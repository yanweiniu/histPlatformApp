package com.marchsoft.organization.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gammainfo.avatarpick.CropImageActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.LoginActivity;
import com.marchsoft.organization.MineActivity;
import com.marchsoft.organization.MineOrganizationActivity;
import com.marchsoft.organization.ProfileActivity;
import com.marchsoft.organization.R;
import com.marchsoft.organization.SetMineActivity;
import com.marchsoft.organization.convert.UserJSONConvert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.User;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.Log;
import com.marchsoft.organization.widget.AlertDialog;
import com.marchsoft.organization.widget.PhotoChoose;
import com.marchsoft.organization.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
/**
 * Created by Administrator on 2016/2/18 0018.
 */
public class MineFragment extends BaseFragment implements OnClickListener,PullToRefreshScrollView.OnRefreshListener2{
    private static final String TAG = MineFragment.class.getSimpleName();
    private static final int SEX_MANTAG = 0;  //性别：0:男，1女
    private static final int SEX_WOMMANTAG = 1;
    private static final int LOGIN_REQUEST_CODE=1001;
    public  static final int LOGIN_RESULT_CODE=2001;
    private ImageLoader mImageLoader;
    private ImageLoadingListener mImageLoadingListener;
    private LayoutInflater mLayoutInflater;
    private CircleImageView mHeadCircleImageView;
    private PullToRefreshScrollView mScrollView;
    private ImageView mIVMineSet;
    private TextView mTVNickname;
    private ImageView mIVsex;
    private TextView mTVsign;
    private TextView mTVOrganization;
    private TextView mTVActivity;
    private TextView mTVPerson;
    private Button mLoginBtn;
    private User mUser;
    private static final int FLAG_CHOOSE_IMG = 5;
    private static final int FLAG_CHOOSE_PHONE = 6;
    private static final int FLAG_MODIFY_FINISH = 7;
    private static final int RATIOX = 4;// 方框横坐标比例
    private static final int RATIOY = 4;// 方框纵坐标比例
    public static final String IMAGE_PATH = "shetuan";
    public static final File FILE_SDCARD = Environment
            .getExternalStorageDirectory();
    public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
    public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
            "images/screenshots");
    private PhotoChoose photoChoose;
    private Bitmap mBitmap;
    private boolean isLogin=false;
    private static String localTempImageFileName = "";
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        View view = mLayoutInflater.inflate(R.layout.fragment_mine,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mScrollView=(PullToRefreshScrollView)mActivity.findViewById(R.id.sv_mine);
        mTVOrganization = (TextView) mActivity.findViewById(R.id.tv_mine_organization);
        mTVActivity = (TextView) mActivity.findViewById(R.id.tv_mine_activity);
        mTVPerson = (TextView) mActivity.findViewById(R.id.tv_mine_datil);
        mHeadCircleImageView = (CircleImageView) mActivity.findViewById(R.id.iv_mine_head);
        mTVNickname = (TextView) mActivity.findViewById(R.id.tv_mine_name);
        mTVsign = (TextView) mActivity.findViewById(R.id.tv_mine_sign);
        mIVsex = (ImageView) mActivity.findViewById(R.id.iv_mine_ageimg);
        mIVMineSet=(ImageView)mActivity.findViewById(R.id.iv_mine_set);
        mLoginBtn=(Button)mActivity.findViewById(R.id.btn_mine_login);
        mLoginBtn.setOnClickListener(this);
        mScrollView.setOnRefreshListener(this);
        mTVOrganization.setOnClickListener(this);
        mTVActivity.setOnClickListener(this);
        mTVPerson.setOnClickListener(this);
        mHeadCircleImageView.setOnClickListener(this);
        mIVMineSet.setOnClickListener(this);
        mImageLoader = ImageLoader.getInstance();
        mImageLoadingListener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
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
        mScrollView.doRefreshing(true);
        setStatus();
        super.onActivityCreated(savedInstanceState);
    }
    private void setStatus(){
       if (Preferences.isLogin()){
          setLoginStatus();
          loadUserData();
      }else{
           setInLogInStatus();
       }
    }
    private void setLoginStatus() {
        mLoginBtn.setVisibility(View.GONE);
        mTVNickname.setVisibility(View.VISIBLE);
        mHeadCircleImageView.setVisibility(View.VISIBLE);
        mIVsex.setVisibility(View.VISIBLE);
        mTVsign.setVisibility(View.VISIBLE);
        mUser = Preferences.getLoginUser();
        if (mUser.getmSex() == SEX_WOMMANTAG) {

            if(mUser.getmHeadImage().equals("null"))  mHeadCircleImageView.setImageResource(R.mipmap.ic_mine_sex_womman_headimg);
            else {
                mImageLoader.displayImage(mUser.getmHeadImage(), mHeadCircleImageView, mImageLoadingListener);
            }
            mIVsex.setImageResource(R.mipmap.ic_mine_sex_womman);
        } else {
            mIVsex.setImageResource(R.mipmap.ic_mine_sex_man);
            if(mUser.getmHeadImage().equals("null"))  mHeadCircleImageView.setImageResource(R.mipmap.ic_mine_sex_womman_headimg);
            else {
                mImageLoader.displayImage(mUser.getmHeadImage(), mHeadCircleImageView, mImageLoadingListener);
            }
        }
        if((!mUser.getmNickname().equals("未知"))&&(!mUser.getmNickname().equals("null"))){
            mTVNickname.setText(mUser.getmNickname());
        }
        if ((!mUser.getmSign().equals("未知"))&&(!mUser.getmSign().equals("null"))){
            mTVsign.setText(mUser.getmSign());
        }

    }

    private void setInLogInStatus(){
        mLoginBtn.setVisibility(View.VISIBLE);
        mHeadCircleImageView.setVisibility(View.GONE);
        mTVNickname.setVisibility(View.GONE);
        mIVsex.setVisibility(View.GONE);
        mTVsign.setVisibility(View.GONE);
        mScrollView.onRefreshComplete();
    }
    private void loadUserData() {
        RequestParams params = new RequestParams();
        params.put("id", Preferences.getUserId());
        RestClient.post(Constant.API_GET_MINE_DETAILS,
                params, new AsyncHttpResponseHandler(mActivity, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                                JSONObject data = response.getJSONObject("data");
                                User user = UserJSONConvert.convertJsonToItem(data);
                                Preferences.setLoginUser(user);
                            } else {
                                String msg = response.getString("msg");
                                ToastUtil.make(mActivity).show(msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mScrollView.onRefreshComplete();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                }));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(isLogin!=Preferences.isLogin()){
            mScrollView.doRefreshing(true);
            isLogin=Preferences.isLogin();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_mine_organization:
                if(!Preferences.isLogin()){
                    unloginConfirmActivity(mActivity);
                   return;
                }
                intent = new Intent(mActivity, MineOrganizationActivity.class);
                break;
            case R.id.tv_mine_activity:
                if(!Preferences.isLogin()){
                    unloginConfirmActivity(mActivity);
                    return;
                }
                intent = new Intent(mActivity, MineActivity.class);
                break;
            case R.id.iv_mine_set:
                if(!Preferences.isLogin()){
                    unloginConfirmActivity(mActivity);
                    return;
                }
                Intent intentSet =new Intent(mActivity, SetMineActivity.class);
                startActivityForResult(intentSet,LOGIN_REQUEST_CODE);
                break;
            case R.id.iv_mine_head:
                    showDialog();
                break;
            case R.id.btn_mine_login:
                Intent intentLog = new Intent(mActivity, LoginActivity.class);
                startActivityForResult(intentLog, LOGIN_REQUEST_CODE);
                break;
            case R.id.tv_mine_datil:
                if(!Preferences.isLogin()){
                    unloginConfirmActivity(mActivity);
                    return;
                }
                Intent intentProfile=new Intent(mActivity, ProfileActivity.class);
                startActivityForResult(intentProfile,LOGIN_REQUEST_CODE);
                break;
        }
            if (intent != null) {
                startActivity(intent);
            }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mActivity,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        mScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        setStatus();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

    private  void unloginConfirmActivity(final Context context) {
        AlertDialog.build(context, R.string.login_label_unlogin_msg,
                R.string.login_label_unlogin_ok, R.string.common_label_cancel,
                new AlertDialog.OnAlertDialogListener() {

                    @Override
                    public void onOk(AlertDialog alertDialogView) {
                        login(context);
                        alertDialogView.dismiss();
                    }

                    @Override
                    public void onCancel(AlertDialog alertDialogView) {
                        alertDialogView.dismiss();
                    }
                }).show();
    }
    private void login(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }
    /**
     * 显示选择对话框
     */
    private void showDialog() {
        if (photoChoose == null) {
            photoChoose = PhotoChoose.build(mActivity,
                    new PhotoChoose.OnPhotoChooseListener() {

                        @Override
                        public void cancel(PhotoChoose target) {
                            target.dismiss();
                        }

                        @Override
                        public void camera(PhotoChoose target) {
                            target.dismiss();
                            String status = Environment
                                    .getExternalStorageState();
                            if (status.equals(Environment.MEDIA_MOUNTED)) {
                                try {
                                    localTempImageFileName = "";
                                    localTempImageFileName = String
                                            .valueOf((new Date()).getTime())
                                            + ".png";
                                    File filePath = FILE_PIC_SCREENSHOT;
                                    if (!filePath.exists()) {
                                        filePath.mkdirs();
                                    }
                                    Intent intent = new Intent(
                                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    File f = new File(filePath,
                                            localTempImageFileName);
                                    // localTempImgDir和localTempImageFileName是自己定义的名字
                                    Uri u = Uri.fromFile(f);
                                    intent.putExtra(
                                            MediaStore.Images.Media.ORIENTATION,
                                            0);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                                    startActivityForResult(intent,
                                            FLAG_CHOOSE_PHONE);
                                } catch (ActivityNotFoundException e) {
                                    //
                                }
                            }
                        }

                        @Override
                        public void album(PhotoChoose target) {
                            target.dismiss();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, FLAG_CHOOSE_IMG);
                        }
                    });
        }
        photoChoose.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==LOGIN_REQUEST_CODE||Preferences.getRefreshing()){
            setStatus();
            Preferences.isRefreshing(false);
        }

        if (requestCode == FLAG_CHOOSE_IMG && resultCode == mActivity.RESULT_OK) {

            if (data != null) {
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    Cursor cursor = mActivity.getContentResolver().query(uri,
                            new String[]{MediaStore.Images.Media.DATA},
                            null, null, null);
                    if (null == cursor) {
                        ToastUtil.make(mActivity).show("没找到图片");
                        return;
                    }
                    cursor.moveToFirst();
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();
                    Intent intent = new Intent(mActivity, CropImageActivity.class);
                    intent.putExtra("path", path);
                    intent.putExtra("rectRatioX", RATIOX);
                    intent.putExtra("rectRatioY", RATIOY);
                    intent.putExtra("name", "name");
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                } else {
                    Intent intent = new Intent(mActivity, CropImageActivity.class);
                    intent.putExtra("path", uri.getPath());
                    intent.putExtra("rectRatioX", RATIOX);
                    intent.putExtra("rectRatioY", RATIOY);
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                }
            }
        } else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == mActivity.RESULT_OK) {
            File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
            Intent intent = new Intent(mActivity, CropImageActivity.class);
            intent.putExtra("path", f.getAbsolutePath());
            intent.putExtra("rectRatioX", RATIOX);
            intent.putExtra("rectRatioY", RATIOY);
            startActivityForResult(intent, FLAG_MODIFY_FINISH);
        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == mActivity.RESULT_OK) {
            if (data != null) {
                final String path = data.getStringExtra("path");

                mBitmap = BitmapFactory.decodeFile(path);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                byte[] buffer = out.toByteArray();
                byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
                String photo = new String(encode);
                UploadPhotoToInternet(photo);
            }
        }

    }
    /* 将得到的图片上传到后台 */
    public void UploadPhotoToInternet(String photo) {
        RequestParams params = new RequestParams();
        params.put("id",Preferences.getUserId());
        params.put("image", photo);
        RestClient.post(Constant.API_SET_PHOTO, params,
                new AsyncHttpResponseHandler(mActivity,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode,
                                                  Header[] headers, JSONObject rlt) {
                                super.onSuccess(statusCode, headers, rlt);
                                try {

                                    if (rlt.getInt("msg_code") == Constant.CODE_SUCCESS) {
                                        String photoUrl = rlt
                                                .getString("data");
                                        mHeadCircleImageView.setImageBitmap(mBitmap);
                                        mUser.setmHeadImage(photoUrl); // 设置本地的用户的图片路径
                                        Preferences.setLoginUser(mUser);
                                        ToastUtil
                                                .make(mActivity)
                                                .show(R.string.change_user_info_change_img_success);

                                    } else {
                                        ToastUtil.make(
                                                mActivity)
                                                .show(rlt.getString("msg"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();}}}));
    }

}

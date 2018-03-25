package com.marchsoft.organization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.convert.OrganizationDetailJSONConvert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.OrganizationDetail;
import com.marchsoft.organization.model.OrganizationMemberBrefInfo;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.AlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrganizationDetailActivity extends Activity implements View.OnClickListener {
    RelativeLayout presidentRel;
    RelativeLayout vicePresidentRel;
    RelativeLayout memberRel;
    LinearLayout activityLayout;
    LinearLayout newsLayout;
    LinearLayout leaveMessagelinearLayout;
    int organizationId;
    private Context mContext;
    private OrganizationDetail organizationDetail;
    private CircleImageView organizationIcon;
    private CircleImageView presidentIcon;
    private TextView organizationName;
    private TextView createTime;
    private TextView declear;
    private TextView memberCount;
    private ImageLoader mImageLoader;
    private ImageLoadingListener mImageLoadingListener;
    private CircleImageView memberOneIcon;
    private CircleImageView memberTwoIcon;
    private boolean isMember = false;
    private LinearLayout viceIconLayout;
    private TextView ellipsisTextView;
    private boolean vicePresidentIsNull;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_detail);
        organizationId = getIntent().getIntExtra("organizationId", 0);
        mContext = this;
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
        initView();
        getOrganizationDetail();
        isMember = judgeIsMember();
    }


    private void initView() {
        organizationIcon = (CircleImageView) findViewById(R.id.organization_detail_logo);
        presidentIcon = (CircleImageView) findViewById(R.id.orgainzation_president_icon);
        organizationName = (TextView) findViewById(R.id.organization_detail_name);
        createTime = (TextView) findViewById(R.id.organization_detail_create_time);
        declear = (TextView) findViewById(R.id.organization_detail_declaration);
        memberOneIcon = (CircleImageView) findViewById(R.id.organization_detail_member_one);
        memberTwoIcon = (CircleImageView) findViewById(R.id.organization_detail_member_two);
        ellipsisTextView = (TextView)findViewById(R.id.organization_detail_ellipsis);
        memberCount = (TextView) findViewById(R.id.organization_detail_member_count);
        viceIconLayout = (LinearLayout) findViewById(R.id.organization_detail_vice_icon_Lin);
        presidentRel = (RelativeLayout) findViewById(R.id.organization_detail_president_rel);
        vicePresidentRel = (RelativeLayout) findViewById(R.id.organization_vice_president_rel);
        memberRel = (RelativeLayout) findViewById(R.id.organization_member_rel);
        activityLayout = (LinearLayout) findViewById(R.id.organization_detail_activity_rel);
        newsLayout = (LinearLayout) findViewById(R.id.organization_detail_news_rel);
        leaveMessagelinearLayout = (LinearLayout) findViewById(R.id.organization_detail_message_rel);
        presidentRel.setOnClickListener(this);
        vicePresidentRel.setOnClickListener(this);
        memberRel.setOnClickListener(this);
        activityLayout.setOnClickListener(this);
        newsLayout.setOnClickListener(this);
        leaveMessagelinearLayout.setOnClickListener(this);
    }


    public void getOrganizationDetail() {
        RequestParams params = new RequestParams();
        params.put("user_id", Preferences.getUserId());
        params.put("association_id", organizationId);
        RestClient.get(Constant.API_GET_ORGANIZATION_DETAIL_MESSAGE, params, new AsyncHttpResponseHandler(this, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        JSONObject jsonObject = response.getJSONObject("data").getJSONObject("details");
                        organizationDetail = OrganizationDetailJSONConvert.convertToItem(jsonObject);
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

    public boolean judgeIsMember() {
        RequestParams params = new RequestParams();
        params.put("association_id", organizationId);
        params.put("user_id", Preferences.getUserId());
        RestClient.get(Constant.API_GET_ORGANIZATION_JUDGE_ISMEMBER, params, new AsyncHttpResponseHandler(mContext, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        isMember = true;
                    } else {
                        isMember = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                isMember = false;
            }
        }));
        return isMember;
    }

    public void setValues() {
        mImageLoader.displayImage(organizationDetail.getOrganizationIcon(), organizationIcon);
        organizationName.setText(organizationDetail.getOrganizationName());
        createTime.setText("创建时间:" + organizationDetail.getCreateTime());
        mImageLoader.displayImage(organizationDetail.getPresidentIcon(), presidentIcon);
        setVicePresidentIcon(organizationDetail.getVicePresidentList());
        declear.setText(organizationDetail.getDeclaration());
        setMemberIcon(organizationDetail.getMemberList());
        memberCount.setText(organizationDetail.getMemberCount() + "人");
    }

    private void setVicePresidentIcon(ArrayList list) {
        if (list == null || list.equals("null")) {
            vicePresidentIsNull = true;
            return;
        } else {
            int listSize = list.size();
            for (int i = 0; i < listSize; i++) {
                if (i < 5) {
                    CircleImageView imageView = new CircleImageView(mContext);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.common_btn_height), (int) getResources().getDimension(R.dimen.common_btn_height));
                    layoutParams.setMargins(10, 0, 0, 0);
                    imageView.setLayoutParams(layoutParams);
                    mImageLoader.displayImage(((OrganizationMemberBrefInfo) list.get(i)).getUserIcon(), imageView);
                    viceIconLayout.addView(imageView);
                } else {
                    return;
                }
            }
        }
    }

    private void setMemberIcon(ArrayList list) {
        if (list == null || list.equals("null")) {
            ellipsisTextView.setVisibility(View.GONE);
            return;
        } else {
            int listSize = list.size();
            if (0 < listSize && listSize <= 2) {
                if (listSize == 1){
                    mImageLoader.displayImage(((OrganizationMemberBrefInfo) list.get(0)).getUserIcon(), memberOneIcon);
                }else {
                    mImageLoader.displayImage(((OrganizationMemberBrefInfo) list.get(0)).getUserIcon(), memberOneIcon);
                    mImageLoader.displayImage(((OrganizationMemberBrefInfo) list.get(1)).getUserIcon(), memberTwoIcon);
                }

            } else if (listSize > 2){
                mImageLoader.displayImage(((OrganizationMemberBrefInfo) list.get(0)).getUserIcon(), memberOneIcon);
                mImageLoader.displayImage(((OrganizationMemberBrefInfo) list.get(1)).getUserIcon(), memberTwoIcon);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.organization_detail_president_rel:
                viewPresident();
                break;
            case R.id.organization_vice_president_rel:
                viewVicePresident();
                break;
            case R.id.organization_member_rel:
                viewMembers();
                break;
            case R.id.organization_detail_activity_rel:
                viewMyActivity();
                break;
            case R.id.organization_detail_news_rel:
                viewNews();
                break;
            case R.id.organization_detail_message_rel:
                leaveMessage();
                break;
            default:
                break;
        }
    }

    private void leaveMessage() {
        if (Preferences.isLogin()) {
            if (isMember) {
                Intent leaveMessage = new Intent(this, OrganizationLeaveMessageActivity.class);
                leaveMessage.putExtra("associationId", organizationDetail.getOrganizationId());
                startActivity(leaveMessage);
            } else {
                ToastUtil.make(mContext).show("您不是本社团成员！");
            }
        } else {
            AlertDialog.build(mContext, R.string.login_label_unlogin_msg,
                    R.string.login_label_unlogin_ok, R.string.common_label_cancel,
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
            return;
        }


    }

    private void viewNews() {
        Intent viewNews = new Intent(this, OrganizationNewsActivity.class);
        viewNews.putExtra("associationId", organizationDetail.getOrganizationId());
        startActivity(viewNews);
    }

    private void viewMyActivity() {
        Intent viewMyActivity = new Intent(this, OrganizationDynamicActivity.class);
        viewMyActivity.putExtra("associationId", organizationDetail.getOrganizationId());
        startActivity(viewMyActivity);
    }

    private void viewMembers() {
        if (Preferences.isLogin()) {
            Intent viewMembers = new Intent(this, OrganizationMemberActivity.class);
            viewMembers.putExtra("associationId", organizationDetail.getOrganizationId());
            viewMembers.putExtra("isMember", organizationDetail.getIsMember());
            startActivity(viewMembers);
        } else {
            AlertDialog.build(mContext, R.string.login_label_unlogin_msg,
                    R.string.login_label_unlogin_ok, R.string.common_label_cancel,
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
            return;
        }

    }

    private void viewVicePresident() {
//        if (!vicePresidentIsNull){
            Intent viewVicePresident = new Intent(this, OrganizationVicePresidentActivity.class);
            viewVicePresident.putExtra("associationId", organizationDetail.getOrganizationId());
            startActivity(viewVicePresident);
//        }else {
//            ToastUtil.make(this).show("暂无成员");
//        }

    }

    private void viewPresident() {
        Intent viewPresident = new Intent(this, OrganizationPresidentActivity.class);
        viewPresident.putExtra("presidnetId", organizationDetail.getPresentId());
        startActivity(viewPresident);
    }


    public void onBackClick(View v) {
        this.setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            this.setResult(RESULT_OK);
            finish();
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
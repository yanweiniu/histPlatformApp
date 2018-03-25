package com.marchsoft.organization.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.LoginActivity;
import com.marchsoft.organization.R;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.Organization;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.AlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wm on 16-2-27.
 */
public class OrganizationAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Organization> mOrganizationsList;
    private ImageLoader mImageLoader;
    private ImageLoadingListener mImageLoadingListener;
    private int mPageIndex = 1;
    private int mPageSize = Constant.PAGE_SIZE;
    private int associationId;
    private PullToRefreshScrollView mPullToRefreshScotllView;

    public OrganizationAdapter(Context context, List<Organization> organizationsList,
                               PullToRefreshScrollView mPullToRefreshScotllView) {
        this.mContext = context;
        this.mOrganizationsList = organizationsList;
        this.mPullToRefreshScotllView = mPullToRefreshScotllView;
        mLayoutInflater = LayoutInflater.from(context);
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

    @Override
    public int getCount() {
        return mOrganizationsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrganizationsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (getCount() != 0){
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.listitem_organization, null);
                viewHolder.icon = (CircleImageView) convertView.findViewById(R.id.orgainzation_logo);
                viewHolder.name = (TextView) convertView.findViewById(R.id.orgainzation_name);
                viewHolder.viewCount = (TextView) convertView.findViewById(R.id.organization_view_count);
                viewHolder.memberCount = (TextView) convertView.findViewById(R.id.organization_member_count);
                viewHolder.signUp = (Button) convertView.findViewById(R.id.organzation_sign_up);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Organization organization = mOrganizationsList.get(position);
            mImageLoader.displayImage(organization.getIconAddress(), viewHolder.icon);
            viewHolder.name.setText(organization.getName());
            viewHolder.viewCount.setText("浏览量: " + organization.getViews() + "次");
            viewHolder.memberCount.setText("已加入: " + organization.getMemberCount() + "人");
            viewHolder.signUp.setText(getStatus(organization.getStatus(), viewHolder));
            viewHolder.signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int status = organization.getStatus();
                    int clubId = organization.getId();
                    if (Preferences.isLogin()) {
                        if (status == 1 || status == 3) {
                            RequestParams params = new RequestParams();
                            params.put("user_id", Preferences.getUserId());
                            params.put("association_id", clubId);
                            RestClient.get(Constant.API_ORGANIZATION_JOIN_CLUB, params, new AsyncHttpResponseHandler(mContext, new JsonHttpResponseHandler() {
                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                                            organization.setStatus(2);
                                            viewHolder.signUp.setText("取消加入");
                                            viewHolder.signUp.setBackgroundResource(R.drawable.common_round_green_button);
                                            viewHolder.signUp.setClickable(true);
                                        } else {
                                            String msg = response.getString("msg");
                                            ToastUtil.make(mContext).show(msg);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                }
                            }));
                        }
                        if (status == 2) {
                            RequestParams params = new RequestParams();
                            params.put("user_id", Preferences.getUserId());
                            params.put("association_id", clubId);
                            RestClient.get(Constant.API_ORGANIZATION_SIGN_OUT, params, new AsyncHttpResponseHandler(mContext, new JsonHttpResponseHandler() {
                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                                            organization.setStatus(3);
                                            viewHolder.signUp.setText("点击加入");
                                            viewHolder.signUp.setBackgroundResource(R.drawable.common_round_blue_button);
                                            viewHolder.signUp.setClickable(true);
                                        } else {
                                            String msg = response.getString("msg");
                                            ToastUtil.make(mContext).show(msg);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                }
                            }));

    //                        if (organizationManager.signOut(clubId)) {

    //                        }
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
                                        mPullToRefreshScotllView.doRefreshing(true);
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


            });
        }else {
            convertView = mLayoutInflater.inflate(R.layout.no_data_view, null);
        }
        return convertView;
    }

    class ViewHolder {
        private CircleImageView icon;
        private TextView name;
        private TextView viewCount;
        private TextView memberCount;
        private Button signUp;
    }


    public void setDataSource(List<Organization> mList) {
        this.mOrganizationsList = mList;
        notifyDataSetChanged();
    }

    public List<Organization> getDataSource() {
        return mOrganizationsList;
    }

    public void appendDataSource(List<Organization> datasource) {
        mOrganizationsList.addAll(datasource);
        notifyDataSetChanged();
    }

    public int getmPageIndex() {
        return mPageIndex;
    }

    public void setmPageIndex(int mPageIndex) {
        this.mPageIndex = mPageIndex;
    }

    public int getmPageSize() {
        return mPageSize;
    }

    public void setmPageSize(int mPageSize) {
        this.mPageSize = mPageSize;
    }

    public synchronized int increasePageIndex() {
        return ++mPageIndex;
    }

    public Organization getItemDataSource(int position) {
        return mOrganizationsList.get(position);
    }


    private String getStatus(int status, ViewHolder viewHolder) {
        String statusStr = "";
        if (Preferences.isLogin()) {
            switch (status) {
                case 0:
                    statusStr = "审核通过";
                    viewHolder.signUp.setBackgroundResource(R.drawable.common_round__button);
                    viewHolder.signUp.setClickable(false);
                    break;
                case 1:
                case 3:
                    statusStr = "点击加入";
                    viewHolder.signUp.setBackgroundResource(R.drawable.common_round_blue_button);
                    viewHolder.signUp.setClickable(true);
                    break;
                case 2:
                    statusStr = "取消加入";
                    viewHolder.signUp.setBackgroundResource(R.drawable.common_round_green_button);
                    viewHolder.signUp.setClickable(true);
                    break;
                default:
                    break;
            }
        } else {
            statusStr = "点击加入";
            viewHolder.signUp.setClickable(true);
        }
        return statusStr;
    }

}

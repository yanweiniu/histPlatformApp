package com.marchsoft.organization.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.OrganizationDetailActivity;
import com.marchsoft.organization.R;
import com.marchsoft.organization.adapter.OrganizationAdapter;
import com.marchsoft.organization.convert.OrganizationJSONConvert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.Organization;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.AutoHeightListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/18 0018.
 */
public class OrganizationFragment extends BaseFragment implements PullToRefreshScrollView.OnRefreshListener2,
        AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener {
    private LayoutInflater mLayoutInflater;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private AutoHeightListView mHadJoinedListView;
    private AutoHeightListView mFailedJoinedListView;
    private RadioGroup mRadioGroup;
    private RadioButton mJoinedRadio;
    private RadioButton mFailedJoinedRadio;

    private List<Organization> mOrganizationList;
    private List<Organization> mFailedJoinedOrganizationList;
    private static OrganizationAdapter organizationAdapter;
    private OrganizationAdapter notJoinedAdapter;
    private int status;
    private boolean isLogin;
    private int position;
    private TextView noDataTextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        View view = mLayoutInflater.inflate(R.layout.fragment_organization, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLogin = Preferences.isLogin();
        status = 1;
        initView();
        mOrganizationList = new ArrayList<Organization>();
        mFailedJoinedOrganizationList = new ArrayList<Organization>();
        organizationAdapter = new OrganizationAdapter(mActivity, mOrganizationList, mPullToRefreshScrollView);
        mHadJoinedListView.setAdapter(organizationAdapter);
        notJoinedAdapter = new OrganizationAdapter(mActivity, mFailedJoinedOrganizationList, mPullToRefreshScrollView);
        mFailedJoinedListView.setAdapter(notJoinedAdapter);
        getOrganization();
        getNotJoinedOrganization();
        mPullToRefreshScrollView.doRefreshing(true);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isLogin != Preferences.isLogin()) {
            mPullToRefreshScrollView.doRefreshing(true);
            isLogin = Preferences.isLogin();
        }
        if (Preferences.getRefreshing()){
            mPullToRefreshScrollView.doRefreshing(true);
            Preferences.isRefreshing(false);
        }

    }


    private void initView() {
        mPullToRefreshScrollView = (PullToRefreshScrollView) mActivity.findViewById(R.id.organization_pull_to_refresh_scrollview);
        mHadJoinedListView = (AutoHeightListView) mActivity.findViewById(R.id.had_join_organization_list);
        mFailedJoinedListView = (AutoHeightListView) mActivity.findViewById(R.id.failed_join_organization_list);
        mRadioGroup = (RadioGroup) mActivity.findViewById(R.id.rg_organizaton_show_joined);
        mJoinedRadio = (RadioButton) mActivity.findViewById(R.id.rbtn_organization_joined);
        mFailedJoinedRadio = (RadioButton) mActivity.findViewById(R.id.rbtn_organization_failed_joined);
        noDataTextView = (TextView)mActivity.findViewById(R.id.no_data_text);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mHadJoinedListView.setDivider(null);
        mFailedJoinedListView.setDivider(null);
        mHadJoinedListView.setFocusable(false);
        mHadJoinedListView.setOnItemClickListener(this);
        mFailedJoinedListView.setFocusable(false);
        mFailedJoinedListView.setOnItemClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
    }


    public void getOrganization() {
        RequestParams params = new RequestParams();
        params.put("st_id", Preferences.getUserId());
         params.put("join_status", 1);
        params.put("page_index", organizationAdapter.getmPageIndex());
        params.put("page_size", Constant.PAGE_SIZE);
        RestClient.get(Constant.API_GET_ORGANIZATION_INTRODUCTION, params, new AsyncHttpResponseHandler(mActivity, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
                mPullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        if (organizationAdapter.getmPageIndex() == 1) {
                            mOrganizationList.clear();
                        }
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("display");
                        List<Organization> list = OrganizationJSONConvert.convertJsonArrayToItemList(jsonArray);
                        mOrganizationList.addAll(list);
                        if (list.size() < organizationAdapter.getmPageSize()) {
                            ToastUtil.make(mActivity).show("亲，没有了");
                        }
                        organizationAdapter.increasePageIndex();
                        organizationAdapter.notifyDataSetChanged();
                    } else {
                        String msg = response.getString("msg");
                        ToastUtil.make(mActivity).show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtil.make(mActivity).show("失败");

            }
        }));
    }

    public void getNotJoinedOrganization() {
        RequestParams params = new RequestParams();
        params.put("st_id", Preferences.getUserId());
        params.put("join_status", 0);
        params.put("page_index", notJoinedAdapter.getmPageIndex());
        params.put("page_size", Constant.PAGE_SIZE);
        RestClient.get(Constant.API_GET_ORGANIZATION_INTRODUCTION, params, new AsyncHttpResponseHandler(mActivity, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
                mPullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        if (notJoinedAdapter.getmPageIndex() == 1) {
                            mFailedJoinedOrganizationList.clear();
                        }
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("display");
                        List<Organization> list = OrganizationJSONConvert.convertJsonArrayToItemList(jsonArray);
                        mFailedJoinedOrganizationList.addAll(list);
                        if (list.size() < notJoinedAdapter.getmPageSize()) {
                            ToastUtil.make(mActivity).show("亲，没有了");
                        }
                        notJoinedAdapter.increasePageIndex();
                        notJoinedAdapter.notifyDataSetChanged();
                    } else {
                        String msg = response.getString("msg");
                        ToastUtil.make(mActivity).show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()) {
            case R.id.rbtn_organization_joined:
                status = 0;
                mHadJoinedListView.setVisibility(View.VISIBLE);
                mFailedJoinedListView.setVisibility(View.GONE);
                mPullToRefreshScrollView.doRefreshing(true);
                if (mOrganizationList.size() == 0){
                    noDataTextView.setVisibility(View.VISIBLE);
                }else {
                    noDataTextView.setVisibility(View.GONE);
                }
                break;
            case R.id.rbtn_organization_failed_joined:
                status = 1;
                mFailedJoinedListView.setVisibility(View.VISIBLE);
                mHadJoinedListView.setVisibility(View.GONE);
                mPullToRefreshScrollView.doRefreshing(true);
                if (mFailedJoinedOrganizationList.size() == 0){
                    noDataTextView.setVisibility(View.VISIBLE);
                }else {
                    noDataTextView.setVisibility(View.GONE);
                }
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        switch (parent.getId()) {
            case R.id.had_join_organization_list:
                organizationAdapter.notifyDataSetChanged();
                Intent organizationDetail = new Intent(mActivity, OrganizationDetailActivity.class);
                organizationDetail.putExtra("organizationId", mOrganizationList.get(position).getId());
                startActivityForResult(organizationDetail, 100);
                break;
            case R.id.failed_join_organization_list:
                Intent failedOrganizationDetail = new Intent(mActivity, OrganizationDetailActivity.class);
                failedOrganizationDetail.putExtra("organizationId", mFailedJoinedOrganizationList.get(position).getId());
                startActivityForResult(failedOrganizationDetail, 101);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK){
            int views = mOrganizationList.get(position).getViews();
            mOrganizationList.get(position).setViews(++views);
            organizationAdapter.notifyDataSetChanged();
        }
        if (requestCode == 101 && resultCode == Activity.RESULT_OK){
            int views = mFailedJoinedOrganizationList.get(position).getViews();
            mFailedJoinedOrganizationList.get(position).setViews(++views);
            notJoinedAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mActivity,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        if (status == 0) {
            organizationAdapter.setmPageIndex(1);
            getOrganization();
            mPullToRefreshScrollView.onRefreshComplete();
        } else {
            notJoinedAdapter.setmPageIndex(1);
            getNotJoinedOrganization();
            mPullToRefreshScrollView.onRefreshComplete();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mActivity,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        if (status == 0) {
            getOrganization();
            mPullToRefreshScrollView.onRefreshComplete();
        } else {
            getNotJoinedOrganization();
            mPullToRefreshScrollView.onRefreshComplete();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Preferences.getRefreshing()) {
            mPullToRefreshScrollView.doRefreshing(true);
            Preferences.isRefreshing(false);
        }
    }

}

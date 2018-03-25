package com.marchsoft.organization;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.adapter.OrganizationAdapter;
import com.marchsoft.organization.convert.DynamicJSONConvert;
import com.marchsoft.organization.convert.OrganizationJSONConvert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.Dynamic;
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
import java.util.jar.JarException;

/**
 * Created by FXB on 2016/2/26.
 */
public class MineOrganizationActivity extends BaseActivity implements PullToRefreshListView.OnRefreshListener2,AdapterView.OnItemClickListener{
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private AutoHeightListView mMineAutoHeightListView;
    private LayoutInflater inflater;
    private OrganizationAdapter organizationAdapter;
    private List<Organization> organizationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_organization);
        inflater=LayoutInflater.from(this);
        mPullToRefreshScrollView=(PullToRefreshScrollView)findViewById(R.id.mine_organization_pull_to_refresh_scrollview);
        mMineAutoHeightListView=(AutoHeightListView)findViewById(R.id.lv_mine_organization);
        mMineAutoHeightListView.setOnItemClickListener(this);
        organizationList=new ArrayList<Organization>();
        organizationAdapter=new OrganizationAdapter(MineOrganizationActivity.this,organizationList, mPullToRefreshScrollView);
        mMineAutoHeightListView.setAdapter(organizationAdapter);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.doRefreshing(true);

    }
    private void getMineOrganizationData(){
        RequestParams params=new RequestParams();
        params.put("st_id", Preferences.getUserId());
        params.put("join_status", 1);
        params.put("page_index", organizationAdapter.getmPageIndex());
        params.put("page_size", Constant.PAGE_SIZE);
        RestClient.post(Constant.API_GET_MINE_ORGANIZATION, params, new AsyncHttpResponseHandler(getBaseContext(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject rlt) {
                super.onSuccess(statusCode, headers, rlt);
                try {
                    if (rlt.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        if (organizationAdapter.getmPageIndex()==1){
                            organizationList.clear();
                        }
                       JSONArray data=rlt.getJSONObject("data").optJSONArray("display");
                        if(data==null){
                            ToastUtil.make(MineOrganizationActivity.this).show("亲，没有参加社团");
                        }else{
                            List<Organization> list=OrganizationJSONConvert.convertJsonArrayToItemList(data);
                            organizationList.addAll(list);
                            if (list.size()< organizationAdapter.getmPageSize()){
                                ToastUtil.make(MineOrganizationActivity.this).show("亲，没有了");
                            }
                            organizationAdapter.increasePageIndex();
                            organizationAdapter.notifyDataSetChanged();
                        }


                    } else {
                        String msg = rlt.getString("msg");
                        ToastUtil.make(MineOrganizationActivity.this).show("获取数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPullToRefreshScrollView.onRefreshComplete();
            }
        }));
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(this,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        organizationAdapter.setmPageIndex(1);
        getMineOrganizationData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(this,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        getMineOrganizationData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent organizationDetail = new Intent(this, OrganizationDetailActivity.class);
        organizationDetail.putExtra("organizationId", organizationList.get(position).getId());
        startActivity(organizationDetail);
    }
}

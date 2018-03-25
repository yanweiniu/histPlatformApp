package com.marchsoft.organization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.adapter.OrganizationMemberAdapter;
import com.marchsoft.organization.convert.OrganizationMemberJSONConvert;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.OrganizationMember;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.AutoHeightListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrganizationVicePresidentActivity extends Activity implements PullToRefreshScrollView.OnRefreshListener2,AdapterView.OnItemClickListener {
    private PullToRefreshScrollView pullToRefreshScrollView;
    private AutoHeightListView autoHeightListView;
    private OrganizationMemberAdapter organizationMemberAdapter;
    private ArrayList<OrganizationMember> mOrganizationMemberList;
    private Context mContext;
    private int associationId;
    private TextView noDataTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_vice_president);
        mContext = this;
        associationId = getIntent().getIntExtra("associationId", 0);
        initView();
        mOrganizationMemberList = new ArrayList<>();
        organizationMemberAdapter = new OrganizationMemberAdapter(this, mOrganizationMemberList);
        autoHeightListView.setAdapter(organizationMemberAdapter);
        getViceMember();
    }

    private void initView() {
        pullToRefreshScrollView = (PullToRefreshScrollView)findViewById(R.id.org_vice_president_pull_to_refresh_scrollview);
        autoHeightListView = (AutoHeightListView)findViewById(R.id.organization_vice_president_listView);
        noDataTextView = (TextView)findViewById(R.id.org_vice_president_no_data_text);
        pullToRefreshScrollView.setOnRefreshListener(this);
        autoHeightListView.setOnItemClickListener(this);
        autoHeightListView.setDivider(null);
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent vicePresidnet = new Intent(mContext, OrganizationPresidentActivity.class);
        vicePresidnet.putExtra("presidnetId", mOrganizationMemberList.get(position).getUserId());
        startActivity(vicePresidnet);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(this,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        getViceMember();
        pullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(this,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        getViceMember();
        pullToRefreshScrollView.onRefreshComplete();
    }
    public void getViceMember(){
        System.out.println("******getViceMember******");
        RequestParams params = new RequestParams();
        params.put("association_id", associationId);
        params.put("type", 1);
        RestClient.get(Constant.API_GET_ORGANIZATION_SHOW_MEMBER, params, new AsyncHttpResponseHandler(this, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("user");
                        List<OrganizationMember> list = OrganizationMemberJSONConvert.convertJsonArrayToItemList(jsonArray);
                        mOrganizationMemberList.addAll(list);
                        organizationMemberAdapter.notifyDataSetChanged();
                        if (mOrganizationMemberList.size() == 0){
                            noDataTextView.setVisibility(View.VISIBLE);

                        }else {
                            noDataTextView.setVisibility(View.GONE);
                        }
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
                ToastUtil.make(mContext).show("失败！");
            }

        }));
    }

    public void onBackClick(View v) {
        finish();
    }
}

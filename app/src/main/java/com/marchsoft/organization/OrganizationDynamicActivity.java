package com.marchsoft.organization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.adapter.DynamicAdapter;
import com.marchsoft.organization.convert.DynamicJSONConvert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.Dynamic;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.AutoHeightListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrganizationDynamicActivity extends Activity implements PullToRefreshScrollView.OnRefreshListener2, AdapterView.OnItemClickListener {
    private PullToRefreshScrollView pullToRefreshScrollView;
    private AutoHeightListView autoHeightListView;
    private DynamicAdapter dynamicAdapter;
    private List<Dynamic> dynamicsList;
    private Context mContext;
    private int associationId;
    private TextView noDataTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_dynamic);
        initView();
        mContext = this;
        associationId = getIntent().getIntExtra("associationId", 1);
        dynamicsList = new ArrayList<Dynamic>();
        dynamicAdapter = new DynamicAdapter((Activity) mContext, dynamicsList);
        autoHeightListView.setAdapter(dynamicAdapter);
        getDynamic();
    }


    public void initView() {
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.org_activity_pull_to_refresh_scrollview);
        autoHeightListView = (AutoHeightListView) findViewById(R.id.org_activity_autoListView);
        noDataTextView = (TextView)findViewById(R.id.org_dynamic_no_data_textView);
        pullToRefreshScrollView.setOnRefreshListener(this);
        autoHeightListView.setOnItemClickListener(this);
    }


    public void getDynamic() {
        RequestParams params = new RequestParams();
        params.put("id", associationId);
        params.put("user_id", Preferences.getUserId());
        params.put("page_index", dynamicAdapter.getmPageIndex());
        params.put("page_size", Constant.PAGE_SIZE);
        RestClient.get(Constant.API_GET_ORGANIZATION_ACTIVITY, params, new AsyncHttpResponseHandler(mContext, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {

                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("as_activity");
                        List<Dynamic> list = DynamicJSONConvert.convertJsonArrayToItemList(jsonArray);
                        if (dynamicAdapter.getmPageIndex() == 1) {
                            dynamicsList.clear();
                        } else {
                            if (list.size() < dynamicAdapter.getmPageSize()) {
                                ToastUtil.make(mContext).show("亲，没有了");
                            }
                        }
                        dynamicsList.addAll(list);
                        if (dynamicsList.size() == 0){
                            noDataTextView.setVisibility(View.VISIBLE);
                        }else {
                            noDataTextView.setVisibility(View.GONE);
                        }
                        dynamicAdapter.increasePageIndex();
                        dynamicAdapter.notifyDataSetChanged();
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


    public void onBackClick(View v) {
        finish();
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mContext,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        getDynamic();
        dynamicAdapter.setmPageIndex(1);
        pullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mContext,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        getDynamic();
        dynamicAdapter.setmPageIndex(1);
        pullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent dynamicIntent = new Intent(mContext, DynamicDetailsActivity.class);
        dynamicIntent.putExtra("id", dynamicsList.get(position).getId());
        startActivityForResult(dynamicIntent, 100);
    }
}

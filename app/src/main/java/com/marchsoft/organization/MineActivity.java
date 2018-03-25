package com.marchsoft.organization;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.adapter.BannerPagerAdapter;
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

/**
 * Created by FXB on 2016/2/26.
 */
public class MineActivity extends BaseActivity implements PullToRefreshListView.OnRefreshListener2,AdapterView.OnItemClickListener{
    private PullToRefreshScrollView mPullRefreshScrollView;
    private AutoHeightListView mLVmineActivity;
    private List<Dynamic> dynamicsList;
    private DynamicAdapter dynamicAdapter;
    private int position=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_activity);
        mPullRefreshScrollView=(PullToRefreshScrollView)findViewById(R.id.activity_pull_to_refresh_scrollview);
        mLVmineActivity=(AutoHeightListView)findViewById(R.id.lv_mine_activity);
        dynamicsList=new ArrayList<Dynamic>();
        dynamicAdapter=new DynamicAdapter(this, dynamicsList);
        mLVmineActivity.setAdapter(dynamicAdapter);
        mLVmineActivity.setOnItemClickListener(this);
        mPullRefreshScrollView.setOnRefreshListener(this);
        mPullRefreshScrollView.doRefreshing(true);

    }
    private void getMineOrganizationData(){
        RequestParams params=new RequestParams();
        params.put("user_id", Preferences.getUserId());
        params.put("page",dynamicAdapter.getmPageIndex());
        params.put("num",7);
        RestClient.post(Constant.API_GET_MINE_ACTIVITY, params, new AsyncHttpResponseHandler(getBaseContext(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        if (dynamicAdapter.getmPageIndex()==1) {
                            dynamicsList.clear();
                        }
                        JSONArray data=response.optJSONArray("data");
                        if(data==null){
                            ToastUtil.make(MineActivity.this).show(response.getString("msg"));
                        }else {
                            List<Dynamic>  list=DynamicJSONConvert.convertJsonArrayToItemList(data);
                            dynamicsList.addAll(list);
                            if(list.size()<7){
                                ToastUtil.make(MineActivity.this).show("亲，没有了");
                            }
                            dynamicAdapter.increasePageIndex();
                            dynamicAdapter.notifyDataSetChanged();
                        }

                    } else {
                        String msg = response.getString("msg");
                        ToastUtil.make(MineActivity.this).show(msg);
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
                mPullRefreshScrollView.onRefreshComplete();
            }
        }));
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(this,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        dynamicAdapter.setmPageIndex(1);
        getMineOrganizationData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(this,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        getMineOrganizationData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent dynamicIntent = new Intent(this, DynamicDetailsActivity.class);
        dynamicIntent.putExtra("id",dynamicsList.get(position).getId());
        startActivityForResult(dynamicIntent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            int status = data.getIntExtra("status",-100);
            if (status != -100){
                dynamicsList.remove(position);
                dynamicAdapter.notifyDataSetChanged();
                Preferences.isRefreshing(true);
            }
        }
    }
}

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
import com.marchsoft.organization.adapter.NewAdapter;
import com.marchsoft.organization.convert.NewsJSONConvert;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.News;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.AutoHeightListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrganizationNewsActivity extends Activity implements PullToRefreshScrollView.OnRefreshListener2, AdapterView.OnItemClickListener{
    private PullToRefreshScrollView pullToRefreshScrollView;
    private AutoHeightListView autoHeightListView;
    private NewAdapter newAdapter;
    private List<News> newsList;
    private Context mContext;
    private int associationId;
    private TextView noDataTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_news);
        mContext = this;
        associationId = getIntent().getIntExtra("associationId", 1);
        newsList = new ArrayList<News>();
        initView();
        newAdapter = new NewAdapter(mContext,newsList);
        autoHeightListView.setAdapter(newAdapter);
        getNews();
    }
    public void initView(){
        pullToRefreshScrollView = (PullToRefreshScrollView)findViewById(R.id.org_news_pull_to_refresh_scrollview);
        autoHeightListView = (AutoHeightListView)findViewById(R.id.org_news_autoListView);
        noDataTextView = (TextView)findViewById(R.id.org_news_no_data_text);
        pullToRefreshScrollView.setOnRefreshListener(this);
        autoHeightListView.setOnItemClickListener(this);
    }


    public void getNews(){
        RequestParams params = new RequestParams();
        params.put("id", associationId);
        params.put("page", newAdapter.getmPageIndex());
        params.put("num", Constant.PAGE_SIZE);
        RestClient.get(Constant.API_GET_ORGANIZATION_NEWS, params, new AsyncHttpResponseHandler(mContext, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("news");
                        List<News> list = NewsJSONConvert.convertJsonArrayToItemList(jsonArray);
                        if (newAdapter.getmPageIndex() == 1) {
                            newsList.clear();
                        } else {
                            if (list.size() < newAdapter.getmPageSize()) {
                                ToastUtil.make(mContext).show("亲，没有了");
                            }
                        }
                        newsList.addAll(list);
                        if (newsList.size() == 0){
                            noDataTextView.setVisibility(View.VISIBLE);
                        }else {
                            noDataTextView.setVisibility(View.GONE);
                        }
                        newAdapter.notifyDataSetChanged();
                        newAdapter.increasePageIndex();
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
        getNews();
        newAdapter.setmPageIndex(1);
        pullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mContext,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        getNews();
        newAdapter.setmPageIndex(1);
        pullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent newsIntent = new Intent(mContext, NewsDetailsActivity.class);
        newsIntent.putExtra("id", newsList.get(position).getId());
        startActivity(newsIntent);
    }
}

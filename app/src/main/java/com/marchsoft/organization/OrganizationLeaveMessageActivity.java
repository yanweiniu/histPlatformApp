package com.marchsoft.organization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.adapter.LeaveMessageAdapter;
import com.marchsoft.organization.convert.LeaveMessageJSONConvert;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.LeaveMessage;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.AutoHeightListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrganizationLeaveMessageActivity extends BaseActivity implements View.OnClickListener,PullToRefreshScrollView.OnRefreshListener2,AdapterView.OnItemClickListener{
    private Button leaveMessage;
    private Context mContext;
    private PullToRefreshScrollView pullToRefreshScrollView;
    private AutoHeightListView listView;
    private LeaveMessageAdapter adapter;
    private List<LeaveMessage> leaveMessageList;
    private int associationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_leave_message);
        mContext = this;
        initView();
        associationId = getIntent().getIntExtra("associationId",0);
        adapter = new LeaveMessageAdapter(this,leaveMessageList);
        listView.setAdapter(adapter);
        pullToRefreshScrollView.doRefreshing(true);
    }

    public void showMessage(){
        RequestParams params = new RequestParams();
        params.put("association_id",associationId);
        params.put("page_index",adapter.getmPageIndex());
        params.put("page_size",Constant.PAGE_SIZE);
        RestClient.post(Constant.API_GET_ORGANIZATION_SHOW_MESSAGE,params,new AsyncHttpResponseHandler(this,new JsonHttpResponseHandler(){
            @Override
            public void onFinish() {
                super.onFinish();
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        List<LeaveMessage> mList =  LeaveMessageJSONConvert.convertJsonArrayToItemList(jsonArray);
                        if (adapter.getmPageIndex() == 1){
                            leaveMessageList.clear();
                        }else {
                            if (mList.size() < adapter.getmPageSize()){
                                ToastUtil.make(OrganizationLeaveMessageActivity.this).show("亲，没有了");
                            }
                        }
                        leaveMessageList.addAll(mList);
                        adapter.increasePageIndex();
                        adapter.notifyDataSetChanged();
                    } else {
                        String msg = response.getString("msg");
                        ToastUtil.make(OrganizationLeaveMessageActivity.this).show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

   private void initView() {
       leaveMessage = (Button)findViewById(R.id.org_leave_message_editText);
       pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_scrollview);
       listView = (AutoHeightListView) findViewById(R.id.org_leave_message_listView);
       leaveMessage.setOnClickListener(this);
       pullToRefreshScrollView.setOnRefreshListener(this);
       listView.setFocusable(false);
       listView.setOnItemClickListener(this);
       leaveMessageList = new ArrayList<LeaveMessage>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.org_leave_message_editText:
                Intent intent = new Intent(OrganizationLeaveMessageActivity.this,LeaveMessageActivity.class);
                intent.putExtra("associationId",associationId);
                startActivityForResult(intent,1010);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010 && resultCode == Activity.RESULT_OK){
            pullToRefreshScrollView.doRefreshing(true);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(this,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        adapter.setmPageIndex(1);
        showMessage();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(this,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        showMessage();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

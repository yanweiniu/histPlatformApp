package com.marchsoft.organization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class OrganizationMemberActivity extends Activity implements PullToRefreshScrollView.OnRefreshListener2,
        AdapterView.OnItemClickListener, View.OnClickListener{
    private PullToRefreshScrollView pullToRefreshScrollView;
    private AutoHeightListView presidentAutoListView;
    private AutoHeightListView freshmanAutoListView;
    private AutoHeightListView sophomoreAutoListView;
    private AutoHeightListView juniorAutoListView;
    private AutoHeightListView seniorAutoListView;
    private OrganizationMemberAdapter presidentAdapter;
    private OrganizationMemberAdapter freshmanAdapter;
    private OrganizationMemberAdapter sophsmoreAdapter;
    private OrganizationMemberAdapter juniorAdapter;
    private OrganizationMemberAdapter seniorAdapter;
    private List<OrganizationMember> presidentList;
    private List<OrganizationMember> freshmanList;
    private List<OrganizationMember> sophomoreList;
    private List<OrganizationMember> juniorList;
    private List<OrganizationMember> seniorList;
    private Context mContext;
    private int associationId;
    private TextView freshmanTextView;
    private TextView sophomoreTextView;
    private TextView juniorTextView;
    private TextView seniorTextView;
    private TextView freshmanNodataText;
    private TextView sophmoreNodataText;
    private TextView juniorNodataText;
    private TextView seniorNodataText;
    private boolean showFresh = false;
    private boolean showSophsmore = false;
    private boolean showJunior = false;
    private boolean showSenior = false;
    private boolean[] clickStatus;
    private int status = -1;
    private LinearLayout topLinearLayout;
    private int isMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_member);
        mContext = this;
        associationId = getIntent().getIntExtra("associationId", 0);
        isMember = getIntent().getIntExtra("isMember", 1);
        presidentList = new ArrayList<OrganizationMember>();
        freshmanList = new ArrayList<OrganizationMember>();
        sophomoreList = new ArrayList<OrganizationMember>();
        juniorList = new ArrayList<OrganizationMember>();
        seniorList = new ArrayList<OrganizationMember>();
        initView();
        presidentAdapter = new OrganizationMemberAdapter(mContext, presidentList);
        presidentAutoListView.setAdapter(presidentAdapter);
        freshmanAdapter = new OrganizationMemberAdapter(mContext, freshmanList);
        freshmanAutoListView.setAdapter(freshmanAdapter);
        sophsmoreAdapter = new OrganizationMemberAdapter(mContext, sophomoreList);
        sophomoreAutoListView.setAdapter(sophsmoreAdapter);
        juniorAdapter = new OrganizationMemberAdapter(mContext, juniorList);
        juniorAutoListView.setAdapter(juniorAdapter);
        seniorAdapter = new OrganizationMemberAdapter(mContext, seniorList);
        seniorAutoListView.setAdapter(seniorAdapter);
        clickStatus = new boolean[4];
        for (int i = 0; i < 4; i++) {
            clickStatus[i] = false;
        }
        getMembers();
    }

    private void initView() {
        topLinearLayout = (LinearLayout) findViewById(R.id.organization_member_top_LinearLayout);
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.org_member_pull_to_refresh_scrollview);
        presidentAutoListView = (AutoHeightListView) findViewById(R.id.org_member_president_listVIew);
        freshmanAutoListView = (AutoHeightListView) findViewById(R.id.org_member_freshman);
        sophomoreAutoListView = (AutoHeightListView) findViewById(R.id.org_member_sophomore);
        juniorAutoListView = (AutoHeightListView) findViewById(R.id.org_member_junior);
        seniorAutoListView = (AutoHeightListView) findViewById(R.id.org_member_senior);
        freshmanTextView = (TextView) findViewById(R.id.organization_member_freshman_textview);
        sophomoreTextView = (TextView) findViewById(R.id.organization_member_sophsmore_textview);
        seniorTextView = (TextView) findViewById(R.id.organization_member_senior_textview);
        juniorTextView = (TextView) findViewById(R.id.organization_member_junior_textview);
        freshmanNodataText = (TextView)findViewById(R.id.organization_member_freshman_nodata_text);
        sophmoreNodataText = (TextView)findViewById(R.id.organization_member_sophmore_nodata_text_nodata_text);
        juniorNodataText = (TextView)findViewById(R.id.organization_member_junior_nodata_text);
        seniorNodataText = (TextView)findViewById(R.id.organization_member_senior_nodata_text);
        pullToRefreshScrollView.setOnRefreshListener(this);
        presidentAutoListView.setDivider(null);
        freshmanAutoListView.setDivider(null);
        sophomoreAutoListView.setDivider(null);
        juniorAutoListView.setDivider(null);
        seniorAutoListView.setDivider(null);
        presidentAutoListView.setFocusable(false);
        freshmanAutoListView.setFocusable(false);
        sophomoreAutoListView.setFocusable(false);
        juniorAutoListView.setFocusable(false);
        seniorAutoListView.setFocusable(false);
        freshmanTextView.setOnClickListener(this);
        sophomoreTextView.setOnClickListener(this);
        juniorTextView.setOnClickListener(this);
        seniorTextView.setOnClickListener(this);
        presidentAutoListView.setOnItemClickListener(this);
        freshmanAutoListView.setOnItemClickListener(this);
        sophomoreAutoListView.setOnItemClickListener(this);
        juniorAutoListView.setOnItemClickListener(this);
        seniorAutoListView.setOnItemClickListener(this);

    }

    public void getMembers() {
        RequestParams params = new RequestParams();
        params.put("association_id", associationId);
        params.put("type", 0);
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
                        if (presidentList.size() != 0){
                            presidentList.clear();
                        }
                        if (freshmanList.size() != 0){
                            freshmanList.clear();
                        }
                        if (sophomoreList.size() != 0){
                            sophomoreList.clear();
                        }
                        if (juniorList.size() != 0){
                            juniorList.clear();
                        }
                        if (seniorList.size() != 0){
                            seniorList.clear();
                        }
                        JSONObject jsonObject = response.getJSONObject("data").getJSONObject("user");
                        presidentList.addAll(OrganizationMemberJSONConvert.convertJsonArrayToItemList(jsonObject.getJSONArray("office")));
                        presidentAdapter.notifyDataSetChanged();
                        if (presidentList.size() != 0 ){

                        }
                        freshmanList.addAll(OrganizationMemberJSONConvert.convertJsonArrayToItemList(jsonObject.getJSONArray("frosh")));
                        freshmanAdapter.notifyDataSetChanged();
                        freshmanTextView.setText("大一（" + freshmanList.size() + ")");
                        sophomoreList.addAll(OrganizationMemberJSONConvert.convertJsonArrayToItemList(jsonObject.getJSONArray("sophomore")));
                        sophsmoreAdapter.notifyDataSetChanged();
                        sophomoreTextView.setText("大一（" + sophomoreList.size() + ")");
                        juniorList.addAll(OrganizationMemberJSONConvert.convertJsonArrayToItemList(jsonObject.getJSONArray("junior")));
                        juniorAdapter.notifyDataSetChanged();
                        juniorTextView.setText("大一（" + juniorList.size() + ")");
                        seniorList.addAll(OrganizationMemberJSONConvert.convertJsonArrayToItemList(jsonObject.getJSONArray("senior")));
                        seniorAdapter.notifyDataSetChanged();
                        seniorTextView.setText("大一（"+seniorList.size()+")");
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



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int compontentId = parent.getId();
        if (compontentId == R.id.org_member_president_listVIew){
            Intent viewPresidnet = new Intent(this,  OrganizationPresidentActivity.class);
            viewPresidnet.putExtra("presidnetId", presidentList.get(position).getUserId());
            startActivity(viewPresidnet);
        }else {
            if (isMember != 0){
                ToastUtil.make(mContext).show("您不是本社团成员！");
            }else {
                Intent viewPresidnet = new Intent(this,  OrganizationPresidentActivity.class);
                switch (compontentId){
                    case R.id.org_member_freshman:
                        viewPresidnet.putExtra("presidnetId", freshmanList.get(position).getUserId());
                        break;
                    case R.id.org_member_sophomore:
                        viewPresidnet.putExtra("presidnetId", sophomoreList.get(position).getUserId());
                        break;
                    case R.id.org_member_junior:
                        viewPresidnet.putExtra("presidnetId", juniorList.get(position).getUserId());
                        break;
                    case R.id.org_member_senior:
                        viewPresidnet.putExtra("presidnetId", seniorList.get(position).getUserId());
                        break;
                    default:
                        break;
                }
                startActivity(viewPresidnet);
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mContext,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        getMembers();
        pullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mContext,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        getMembers();
        pullToRefreshScrollView.onRefreshComplete();

    }

    public void onBackClick(View v) {
        finish();
    }

//    if (freshmanList.size() == 0){freshmanNodataText.setVisibility(View.VISIBLE);}
//    if (sophomoreList.size() == 0){sophmoreNodataText.setVisibility(View.VISIBLE);}
//    if (juniorList.size() == 0){juniorNodataText.setVisibility(View.VISIBLE);}
//    if (seniorList.size() == 0){seniorNodataText.setVisibility(View.VISIBLE);}
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.organization_member_freshman_textview:
                status = 0;
                clickStatus[0] = !clickStatus[0];
                if (showFresh) {
                    freshmanAutoListView.setVisibility(View.GONE);
                    freshmanNodataText.setVisibility(View.GONE);
                    showFresh = false;
                } else {
                    freshmanAutoListView.setVisibility(View.VISIBLE);
                    if (freshmanList.size() == 0){
                        freshmanNodataText.setVisibility(View.VISIBLE);
                    }
                    showFresh = true;
                }
                break;
            case R.id.organization_member_sophsmore_textview:
                status = 1;
                clickStatus[1] = !clickStatus[1];
                if (showSophsmore) {
                    sophomoreAutoListView.setVisibility(View.GONE);
                    sophmoreNodataText.setVisibility(View.GONE);
                    showSophsmore = false;
                } else {
                    sophomoreAutoListView.setVisibility(View.VISIBLE);
                    if (sophomoreList.size() == 0){
                        sophmoreNodataText.setVisibility(View.VISIBLE);
                    }
                    showSophsmore = true;
                }
                break;
            case R.id.organization_member_senior_textview:
                status = 2;
                clickStatus[2] = !clickStatus[2];

                if (showSenior) {
                    seniorAutoListView.setVisibility(View.GONE);
                    seniorNodataText.setVisibility(View.GONE);
                    showSenior = false;
                } else {
                    seniorAutoListView.setVisibility(View.VISIBLE);
                    if (seniorList.size() == 0){
                        seniorNodataText.setVisibility(View.VISIBLE);
                    }
                    showSenior = true;
                }
                break;
            case R.id.organization_member_junior_textview:
                status = 3;
                clickStatus[3] = !clickStatus[3];
                if (showJunior) {
                    juniorAutoListView.setVisibility(View.GONE);
                    juniorNodataText.setVisibility(View.GONE);
                    showJunior = false;
                } else {
                    juniorAutoListView.setVisibility(View.VISIBLE);
                    if (juniorList.size() == 0){
                        juniorNodataText.setVisibility(View.VISIBLE);
                    }
                    showJunior = true;
                }
                break;
        }

        if (clickStatus[status]) {
            v.setSelected(true);
        } else {
            v.setSelected(false);
        }
    }
}



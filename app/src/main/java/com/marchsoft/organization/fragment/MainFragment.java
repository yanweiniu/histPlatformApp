package com.marchsoft.organization.fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.Log;
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
import com.marchsoft.organization.DynamicDetailsActivity;
import com.marchsoft.organization.NewsDetailsActivity;
import com.marchsoft.organization.R;
import com.marchsoft.organization.adapter.BannerPagerAdapter;
import com.marchsoft.organization.adapter.DynamicAdapter;
import com.marchsoft.organization.adapter.NewAdapter;
import com.marchsoft.organization.convert.BannerJSONConvert;
import com.marchsoft.organization.convert.DynamicJSONConvert;
import com.marchsoft.organization.convert.NewsJSONConvert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.Banner;
import com.marchsoft.organization.model.Dynamic;
import com.marchsoft.organization.model.News;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.viewpager.AutoScrollViewPager;
import com.marchsoft.organization.widget.AutoHeightListView;
import com.viewpagerindicator.CirclePageIndicator;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/18 0018.
 */
public class MainFragment extends BaseFragment implements  PullToRefreshScrollView.OnRefreshListener2,AdapterView.OnItemClickListener,RadioGroup.OnCheckedChangeListener{
    private LayoutInflater mLayoutInflater;
    private AutoScrollViewPager pager;
    private CirclePageIndicator circle;
    private List<Banner> list;
    private TextView title;
    private PullToRefreshScrollView pullToRefreshScrollView;
    private AutoHeightListView dynamicListview;
    private AutoHeightListView newsListview;
    private BannerPagerAdapter adapter;
    private DynamicAdapter dynamicAdapter;
    private List<Dynamic> dynamicList;
    private NewAdapter newAdapter;
    private List<News> newsList;
    private RadioGroup radioGroup;
    private RadioButton dynamicRB;
    private RadioButton newsRB;
    private int status;
    private int position;
    private boolean isLogin;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        View view = mLayoutInflater.inflate(R.layout.fragment_main,container,false);
        list = new ArrayList<Banner>();
        dynamicList = new ArrayList<Dynamic>();
        newsList = new ArrayList<News>();
        return view;
    }

    /**
     * isLogin存储之前登录状态
     * Preferences.isLogin()当前用户登录状态
     * 当fragment状态改变时通过比较是否相同来确定用户状态是否发生变化，做到自动刷新
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (isLogin != Preferences.isLogin()){
            pullToRefreshScrollView.doRefreshing(true);
            isLogin = Preferences.isLogin();
        }
        if (Preferences.getRefreshing()){
            pullToRefreshScrollView.doRefreshing(true);
            Preferences.isRefreshing(false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initHeaderView();
        radioGroup.setOnCheckedChangeListener(this);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int count = list.size();
                if (count > 0) {
                    position = position % count;
                    final Banner banner = list.get(position);
                    //title.setText(banner.getmTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter = new BannerPagerAdapter(mActivity,list);
        pager.setAdapter(adapter);
        circle.setViewPager(pager);
        dynamicAdapter = new DynamicAdapter(mActivity, dynamicList);
        newAdapter = new NewAdapter(mActivity,newsList);
        dynamicListview.setAdapter(dynamicAdapter);
        newsListview.setAdapter(newAdapter);
        pullToRefreshScrollView.doRefreshing(true);
        //getNewsBanner();
        //getDynamic();
        getNews();
    }

    private void initHeaderView() {
        pager = (AutoScrollViewPager)mActivity.findViewById(R.id.viewpager) ;
        circle = (CirclePageIndicator) mActivity.findViewById(R.id.circle);
        title = (TextView) mActivity.findViewById(R.id.tv);
        dynamicListview = (AutoHeightListView) mActivity.findViewById(R.id.ls_dynamic);
        newsListview = (AutoHeightListView) mActivity.findViewById(R.id.ls_news);
        pullToRefreshScrollView = (PullToRefreshScrollView) mActivity.findViewById(R.id.pull_to_refresh_scrollview);
        radioGroup = (RadioGroup) mActivity.findViewById(R.id.rg_show_mode);
        dynamicRB = (RadioButton) mActivity.findViewById(R.id.rbtn_dynamic);
        newsRB = (RadioButton) mActivity.findViewById(R.id.rbtn_ews);
        pullToRefreshScrollView.setOnRefreshListener(this);
        dynamicListview.setFocusable(false);
        newsListview.setFocusable(false);
        dynamicListview.setOnItemClickListener(this);
        newsListview.setOnItemClickListener(this);
        isLogin = Preferences.isLogin();
    }

    public void getNews(){
        RequestParams params = new RequestParams();
        params.put("page", newAdapter.getmPageIndex());
        params.put("num", Constant.PAGE_SIZE);
        RestClient.get(Constant.API_GET_MAIN_NEWS, params, new AsyncHttpResponseHandler(mActivity, new JsonHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("journalism");
                        List<News> list = NewsJSONConvert.convertJsonArrayToItemList(jsonArray);
                        if (newAdapter.getmPageIndex() == 1) {
                            newsList.clear();
                            //newsListview.setAdapter(newAdapter);
                        }else{
                            if (list.size() < newAdapter.getmPageSize()) {
                                ToastUtil.make(mActivity).show("亲，没有了");
                            }
                        }
                        newsList.addAll(list);
                        newAdapter.notifyDataSetChanged();
                        newAdapter.increasePageIndex();
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

    public void getDynamic(){
        RequestParams params = new RequestParams();
        params.put("user_id", Preferences.getUserId());
        params.put("page",dynamicAdapter.getmPageIndex());
        params.put("num",Constant.PAGE_SIZE);
        RestClient.get(Constant.API_GET_MAIN_DYNAMIC,params,new AsyncHttpResponseHandler(mActivity,new JsonHttpResponseHandler(){
            @Override
            public void onFinish() {
                super.onFinish();
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS){
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("activity");
                        List<Dynamic> list = DynamicJSONConvert.convertJsonArrayToItemList(jsonArray);
                        if(dynamicAdapter.getmPageIndex() == 1){
                            dynamicList.clear();
                            //dynamicListview.setAdapter(dynamicAdapter);
                        }else {
                            if (list.size() < dynamicAdapter.getmPageSize()){
                                ToastUtil.make(mActivity).show("亲，没有了");
                            }
                        }
                        dynamicList.addAll(list);
                        dynamicAdapter.increasePageIndex();
                        dynamicAdapter.notifyDataSetChanged();
                    }else{
                        String msg = response.getString("msg");
                        ToastUtil.make(mActivity).show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public void getNewsBanner(){
        RequestParams params = new RequestParams();
        RestClient.get(Constant.API_GET_BANNER, params, new AsyncHttpResponseHandler(mActivity, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                        list.clear();
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("top_journalism");
                        List<Banner> bannerList = BannerJSONConvert.convertJsonArrayToItemList(jsonArray);
                        list.addAll(bannerList);
                        circle.setRecyclePageCount(list.size());
                        pager.getAdapter().notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
        pager.startAutoScroll();
    }

    @Override
    public void onPause() {
        pager.stopAutoScroll();
        super.onPause();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mActivity,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        getNewsBanner();
        if (status == 0){
            dynamicAdapter.setmPageIndex(1);
            getDynamic();
        }else{
            newAdapter.setmPageIndex(1);
            getNews();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mActivity,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        if (status == 0){
            getDynamic();
        }else {
            getNews();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.ls_dynamic:
                this.position = position;
                Intent dynamicIntent = new Intent(mActivity, DynamicDetailsActivity.class);
                dynamicIntent.putExtra("id",dynamicList.get(position).getId());
                startActivityForResult(dynamicIntent,100);
                break;
            case R.id.ls_news:
                Intent newsIntent = new Intent(mActivity, NewsDetailsActivity.class);
                newsIntent.putExtra("id", newsList.get(position).getId());
                startActivity(newsIntent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK){
            int status = data.getIntExtra("status",-100);
            if (status != -100){
                int count = dynamicList.get(position).getRegistering();
                if (status == 0){
                    ++count;
                }else {
                    --count;
                }
                dynamicList.get(position).setRegistering(count);
                dynamicList.get(position).setIsJoin(status);
                dynamicAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()){
            case R.id.rbtn_dynamic:
                status = 0;
                newsListview.setVisibility(View.GONE);
                dynamicListview.setVisibility(View.VISIBLE);
                break;
            case R.id.rbtn_ews:
                status = 1;
                dynamicListview.setVisibility(View.GONE);
                newsListview.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * DynamicAdapter无法回调onActivityResult,所以当调转也finish掉之后，会重新调用onstart,然后刷新数据
     */
    @Override
    public void onStart() {
        super.onStart();
        if (Preferences.getRefreshing()){
            pullToRefreshScrollView.doRefreshing(true);
            Preferences.isRefreshing(false);
        }
    }


}

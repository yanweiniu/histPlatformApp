package com.marchsoft.organization.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.DynamicDetailsActivity;
import com.marchsoft.organization.LoginActivity;
import com.marchsoft.organization.NewsDetailsActivity;
import com.marchsoft.organization.R;
import com.marchsoft.organization.adapter.DynamicAdapter;
import com.marchsoft.organization.adapter.NewAdapter;
import com.marchsoft.organization.convert.DynamicJSONConvert;
import com.marchsoft.organization.convert.NewsJSONConvert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.Dynamic;
import com.marchsoft.organization.model.News;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.Log;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.widget.AlertDialog;
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
public class DynamicFragment extends BaseFragment implements  PullToRefreshScrollView.OnRefreshListener2,View.OnClickListener,AdapterView.OnItemClickListener{
    private AutoHeightListView Dynamic_listview;            //活动动态列表listview
    private AutoHeightListView News_listview;               //新闻动态列表listview
    private PullToRefreshScrollView pullToRefreshScrollView;//滚动视图容器
    private PopupWindow popupWindow;                        //弹出窗口
    private DynamicAdapter dynamicAdapter;                  //活动适配器
    private NewAdapter newAdapter;                          //新闻适配器
    private List<Dynamic> dynamicList;                      //活动集合
    private List<News> newsList;                            //新闻集合
    private int refreshStatus;                              //活动新闻状态（0表示活动，1表示新闻）
    private int position;                                   //活动与新闻listitem位置
    private int typeLocation;                               //活动类型值；
    private int newsType;                                   //新闻类型值；
    private int attendLocation;                             //参加菜单item位置
    private View  view;
    private View  popview;
    private LayoutInflater mLayoutInflater;
    private View DynamicTabItem_public;
    private View DynamicTabItem_activity;                   //检索导航1
    private View DynamicTabItem_all;                        //检索导航2
    private View DynamicTabItem_attended;                   //检索导航3
    private ArrayList<String> list = new ArrayList<String>();      //活动新闻下拉列表
    private ArrayList<String> activityTypeList = new ArrayList<String>();//活动类型信息集合
    private ArrayList<String> newsTypelist = new ArrayList<String>();//新闻类型信息集合
    private ArrayList<String> attendList = new ArrayList<String>();     //是否加入下拉列表
    private ArrayList<String> newsAttendList = new ArrayList<String>();     //是否加入下拉列表
    private  TextView Dynamic_tabbar_activity_text;           //检索导航text1
    private  TextView Dynamic_tabbar_type_text;               //检索导航text2
    private  TextView Dynamic_tabbar_attended_text;           //检索导航text3
    private  ListView listviews;
    private Activity mContext;
    private boolean isLogin;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        view = mLayoutInflater.inflate(R.layout.fragment_dynamic,container,false);
        popview=inflater.inflate(R.layout.popwindow_dynamic,container,false);
        dynamicList = new ArrayList<Dynamic>();
        newsList=new ArrayList<News>();
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
        initView();
        mContext=mActivity;
        dynamicAdapter = new DynamicAdapter(mActivity, dynamicList);
        Dynamic_listview.setAdapter(dynamicAdapter);
        newAdapter = new NewAdapter(mActivity, newsList);
        News_listview.setAdapter(newAdapter);
        getTypeListInfo(100,2);

    }
    private void initView(){
        listviews = (ListView) popview.findViewById(R.id.popwindowListView);
       DynamicTabItem_activity=mActivity.findViewById(R.id.dynamic_tab_activity);
       DynamicTabItem_all=mActivity.findViewById(R.id.dynamic_tab_type);
       DynamicTabItem_attended=mActivity.findViewById(R.id.dynamic_tab_attended);
       DynamicTabItem_activity.setOnClickListener(this);
       DynamicTabItem_all.setOnClickListener(this);
       DynamicTabItem_attended.setOnClickListener(this);
       Dynamic_tabbar_activity_text=(TextView) DynamicTabItem_activity.findViewById(R.id.dynamic_tab_activity_text);
       Dynamic_tabbar_type_text=(TextView) DynamicTabItem_all.findViewById(R.id.dynamic_tab_type_text);
       Dynamic_tabbar_attended_text=(TextView) DynamicTabItem_attended.findViewById(R.id.dynamic_tab_attended_text);
       pullToRefreshScrollView = (PullToRefreshScrollView) mActivity.findViewById(R.id.Dynamic_pull_to_refresh_scrollview);
       Dynamic_listview =(AutoHeightListView) mActivity.findViewById(R.id.dynamic_listview);
       News_listview=(AutoHeightListView) mActivity.findViewById(R.id.news_listview);
        isLogin = Preferences.isLogin();
        pullToRefreshScrollView.setOnRefreshListener(this);
       Dynamic_listview.setFocusable(false);
       News_listview.setFocusable(false);
       Dynamic_listview.setOnItemClickListener(this);
       News_listview.setOnItemClickListener(this);
       list.add("   活动");
       list.add("   新闻");
       activityTypeList.add("   全部");
       activityTypeList.add("   比赛");
       activityTypeList.add("   演出");
       activityTypeList.add("   报告");
       activityTypeList.add("   会议");
       activityTypeList.add("   参观");
       activityTypeList.add("   公益活动");
       activityTypeList.add("   其他");
       newsTypelist.add("   社团");
       newsTypelist.add("   社联");
        attendList.add("   已加入");
        attendList.add("   未加入");
        attendList.add("   全部");
        newsAttendList.add("   已加入");
        newsAttendList.add("   未加入");
        newsAttendList.add("   全部");
   }
    /**
     * 获取活动类型信息
     *
     * @param num
     */
    public void getTypeListInfo(int num,int status){
        if(!Preferences.isLogin()){
            typeLocation=num;
            attendLocation=status;
            RequestParams params = new RequestParams();
            params.put("trends",0);
            params.put("type",num);
            params.put("use_status",2);
            params.put("page",dynamicAdapter.getmPageIndex());
            RestClient.get(Constant.API_GET_CORPORATION_PARTICULARS, params, new AsyncHttpResponseHandler(mActivity, new JsonHttpResponseHandler() {
                @Override
                public void onFinish() {
                    super.onFinish();
                    pullToRefreshScrollView.onRefreshComplete();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        System.out.println("**********re"+response);
                        if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                            if (dynamicAdapter.getmPageIndex() == 1) {
                                dynamicList.clear();
                            }else {
                                if (dynamicList.size() < dynamicAdapter.getmPageSize()){
                                    ToastUtil.make(mActivity).show("亲，没有了");
                                }
                            }
                            JSONArray jsonArray = response.getJSONArray("data");
                            List<Dynamic> list = DynamicJSONConvert.convertJsonArrayToItemList(jsonArray);
                            dynamicList.addAll(list);
                            dynamicAdapter = new DynamicAdapter(mActivity, dynamicList);
                            Dynamic_listview.setAdapter(dynamicAdapter);
                            dynamicAdapter.increasePageIndex();
                            dynamicAdapter.notifyDataSetChanged();
                        }
                        else {
                            String msg = response.getString("msg");
                            if (dynamicAdapter.getmPageIndex() == 1){
                                dynamicList.clear();
                                dynamicAdapter.setDataSource(dynamicList);
                                Dynamic_listview.setAdapter(dynamicAdapter);
                                dynamicAdapter.increasePageIndex();
                                dynamicAdapter.notifyDataSetChanged();
                            }
                            ToastUtil.make(mActivity).show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }else {
            typeLocation=num;
        attendLocation=status;
        RequestParams params = new RequestParams();
        params.put("trends",0);
        params.put("type",num);
        params.put("use_status",status);
        params.put("user_id", Preferences.getUserId());
        params.put("page",dynamicAdapter.getmPageIndex());
        RestClient.get(Constant.API_GET_CORPORATION_PARTICULARS, params, new AsyncHttpResponseHandler(mActivity, new JsonHttpResponseHandler() {
                @Override
                public void onFinish() {
                    super.onFinish();
                    pullToRefreshScrollView.onRefreshComplete();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        System.out.println("**********re"+response);

                        if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                            if (dynamicAdapter.getmPageIndex() == 1) {
                                dynamicList.clear();
                            }else {
                                if (dynamicList.size() < dynamicAdapter.getmPageSize()){
                                    ToastUtil.make(mActivity).show("亲，没有了");
                                }
                            }
                            JSONArray jsonArray = response.getJSONArray("data");
                            List<Dynamic> list = DynamicJSONConvert.convertJsonArrayToItemList(jsonArray);
                            dynamicList.addAll(list);
                            dynamicAdapter = new DynamicAdapter(mActivity, dynamicList);
                            Dynamic_listview.setAdapter(dynamicAdapter);
                            dynamicAdapter.increasePageIndex();
                            dynamicAdapter.notifyDataSetChanged();
                        }
                        else {
                            String msg = response.getString("msg");
                            if (dynamicAdapter.getmPageIndex() == 1){
                                dynamicList.clear();
                                dynamicAdapter.setDataSource(dynamicList);
                                Dynamic_listview.setAdapter(dynamicAdapter);
                                dynamicAdapter.increasePageIndex();
                                dynamicAdapter.notifyDataSetChanged();
                            }
                            ToastUtil.make(mActivity).show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
    }
    /**
     * 获取参加/未参加活动信息
     *
     * @param num
     */
    public void getAttenedListInfo(int num){
            RequestParams params = new RequestParams();
            params.put("trends",0);
            params.put("type",typeLocation);
            params.put("use_status",num);
            params.put("user_id",Preferences.getUserId());
            params.put("page",dynamicAdapter.getmPageIndex());
            RestClient.get(Constant.API_GET_CORPORATION_PARTICULARS, params, new AsyncHttpResponseHandler(mActivity, new JsonHttpResponseHandler() {
                @Override
                public void onFinish() {
                    super.onFinish();
                    pullToRefreshScrollView.onRefreshComplete();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                            if (dynamicAdapter.getmPageIndex() == 1) {
                                dynamicList.clear();
                            }
                            JSONArray jsonArray = response.getJSONArray("data");
                            dynamicList.clear();
                            List<Dynamic> list1 = DynamicJSONConvert.convertJsonArrayToItemList(jsonArray);
                            dynamicList.addAll(list1);
                            if (list1.size() < dynamicAdapter.getmPageSize()) {
                                ToastUtil.make(mActivity).show("亲，没有了");
                            }
                            dynamicAdapter = new DynamicAdapter(mActivity, dynamicList);
                            Dynamic_listview.setAdapter(dynamicAdapter);
                            dynamicAdapter.increasePageIndex();
                            dynamicAdapter.notifyDataSetChanged();
                        } else {
                            String msg = response.getString("msg");
                            dynamicList.clear();
                            dynamicAdapter.setDataSource(dynamicList);
                            Dynamic_listview.setAdapter(dynamicAdapter);
                            dynamicAdapter.increasePageIndex();
                            dynamicAdapter.notifyDataSetChanged();
                            ToastUtil.make(mActivity).show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }));
    }
    /**
     * 获取新闻类型信息
     *
     * @param sort
     * @param status
     */
    public void getNewsListInfo(int sort,int status){
        newsType=sort;
        RequestParams params = new RequestParams();
        params.put("trends",1);
        params.put("type",sort);
        params.put("use_status",status);
        params.put("user_id", Preferences.getUserId());
        params.put("page", newAdapter.getmPageIndex());
        RestClient.get(Constant.API_GET_CORPORATION_PARTICULARS, params, new AsyncHttpResponseHandler(mActivity, new JsonHttpResponseHandler() {
                @Override
                public void onFinish() {
                    super.onFinish();
                    pullToRefreshScrollView.onRefreshComplete();
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        System.out.println("@@@@@@@@@@**" + response);
                        if (response.getInt("msg_code") == Constant.CODE_SUCCESS) {
                             if (newAdapter.getmPageIndex() == 1){
                                newsList.clear();
                                 JSONArray jsonArray = response.getJSONArray("data");

                                 List<News>  list = NewsJSONConvert.convertJsonArrayToItemList(jsonArray);
                                 newsList.addAll(list);
                                 newAdapter = new NewAdapter(mActivity, newsList);
                                 News_listview.setAdapter(newAdapter);
                                 newAdapter.increasePageIndex();
                                 newAdapter.notifyDataSetChanged();
                            }else {
                                 newAdapter.setmPageSize((newAdapter.getmPageIndex()-1)*10);
                                 if (newsList.size()!=newAdapter.getmPageSize()){
                                     ToastUtil.make(mActivity).show("亲，没有了");
                                     return ;
                                 }
                                 JSONArray jsonArray = response.getJSONArray("data");
                                 List<News>  list = NewsJSONConvert.convertJsonArrayToItemList(jsonArray);
                                 newsList.addAll(list);
                                 newAdapter = new NewAdapter(mActivity, newsList);
                                 News_listview.setAdapter(newAdapter);
                                 newAdapter.increasePageIndex();
                                 newAdapter.notifyDataSetChanged();
                             }

                        } else {
                            String msg = response.getString("msg");
                            if (newAdapter.getmPageIndex() == 1) {
                                newsList.clear();
                                newAdapter = new NewAdapter(mActivity, newsList);
                                News_listview.setAdapter(newAdapter);
                                newAdapter.increasePageIndex();
                                newAdapter.notifyDataSetChanged();
                            }
                            ToastUtil.make(mActivity).show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
    /**
     * 弹出窗口
     *
     */
    private void showPopupWindow()
    {
        popupWindow = new PopupWindow(mActivity);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(view.getWidth());
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setContentView(popview);
        popupWindow.showAsDropDown(DynamicTabItem_activity,0,0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                Dynamic_tabbar_activity_text.setSelected(false);
                Dynamic_tabbar_type_text.setSelected(false);
                Dynamic_tabbar_attended_text.setSelected(false);
            }
        });
        if (thread != null && !thread.isAlive())
        {thread.start();}
    }
    Thread thread = new Thread()
    {public void run()
    {while (true)
    {try {Thread.sleep(1000);}
    catch (InterruptedException e)
    {e.printStackTrace();}
        Log.e("44", "AAAAA");
        }
    };
    };
    @Override
    public void onClick(View v) {
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
        if (DynamicTabItem_public != null) {
            DynamicTabItem_public.setSelected(false);
        }
        DynamicTabItem_public = v;
        DynamicTabItem_public.setSelected(true);
        TextView textView=(TextView)popview.findViewById(R.id.backupBtn);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        switch (v.getId()) {
            case R.id.dynamic_tab_activity:
                /*初始化并显示"活动新闻"检索的popwindow*/
                ArrayAdapter<String> activityAdapter = new ArrayAdapter<String>(mActivity, R.layout.popwindow_listitem, list);
                listviews.setAdapter(activityAdapter);
                listviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (list.get(position).equals("   活动")) {
                            refreshStatus=0;
                            dynamicAdapter.setmPageIndex(1);
                            getTypeListInfo(100,2);
                            Dynamic_tabbar_type_text.setText("全部");
                            Dynamic_tabbar_attended_text.setText("全部");
                            News_listview.setVisibility(View.GONE);
                            Dynamic_listview.setVisibility(View.VISIBLE);
                            Dynamic_tabbar_activity_text.setText(R.string.dynamic_activity);
                            popupWindow.dismiss();
                        } else if (list.get(position).equals("   新闻")) {
                            refreshStatus=1;
                            newAdapter.setmPageIndex(1);
                            getNewsListInfo(1,0);
                            Dynamic_tabbar_type_text.setText("社联");
                            Dynamic_tabbar_attended_text.setText("全部");
                            Dynamic_listview.setVisibility(View.GONE);
                            News_listview.setVisibility(View.VISIBLE);
                            Dynamic_tabbar_activity_text.setText(R.string.dynamic_news);
                            popupWindow.dismiss();
                        }
                    }
                });
                showPopupWindow();
                break;
            case R.id.dynamic_tab_type:
                /*初始化并显示"类型"检索的popwindow*/
                if (Dynamic_tabbar_activity_text.getText().equals("活动")) {
                    ArrayAdapter<String> typeAapter = new ArrayAdapter<String>(mActivity, R.layout.popwindow_listitem, activityTypeList);
                    listviews.setAdapter(typeAapter);
                    listviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            {
                                if (activityTypeList.get(position).equals("   全部")) {
                                    Dynamic_tabbar_type_text.setText(activityTypeList.get(position).trim());
                                    Dynamic_tabbar_attended_text.setText("全部");
                                    dynamicAdapter.setmPageIndex(1);
                                    getTypeListInfo(100,2);
                                    popupWindow.dismiss();
                                } else if (activityTypeList.get(position).equals("   比赛")) {
                                    Dynamic_tabbar_type_text.setText(activityTypeList.get(position).trim());
                                    Dynamic_tabbar_attended_text.setText("全部");
                                    dynamicAdapter.setmPageIndex(1);
                                    getTypeListInfo(0,2);
                                    popupWindow.dismiss();
                                } else if (activityTypeList.get(position).equals("   演出")) {
                                    Dynamic_tabbar_type_text.setText(activityTypeList.get(position).trim());
                                    Dynamic_tabbar_attended_text.setText("全部");
                                    dynamicAdapter.setmPageIndex(1);
                                    getTypeListInfo(1,2);
                                    popupWindow.dismiss();
                                } else if (activityTypeList.get(position).equals("   报告")) {
                                    Dynamic_tabbar_type_text.setText(activityTypeList.get(position).trim());
                                    Dynamic_tabbar_attended_text.setText("全部");
                                    dynamicAdapter.setmPageIndex(1);
                                    getTypeListInfo(2,2);
                                    popupWindow.dismiss();
                                } else if (activityTypeList.get(position).equals("   会议")) {
                                    Dynamic_tabbar_type_text.setText(activityTypeList.get(position).trim());
                                    Dynamic_tabbar_attended_text.setText("全部");
                                    dynamicAdapter.setmPageIndex(1);
                                    getTypeListInfo(3,2);
                                    popupWindow.dismiss();
                                } else if (activityTypeList.get(position).equals("   参观")) {
                                    Dynamic_tabbar_type_text.setText(activityTypeList.get(position).trim());
                                    Dynamic_tabbar_attended_text.setText("全部");
                                    dynamicAdapter.setmPageIndex(1);
                                    getTypeListInfo(4,2);
                                    popupWindow.dismiss();
                                } else if (activityTypeList.get(position).equals("   公益活动")) {
                                    Dynamic_tabbar_type_text.setText(activityTypeList.get(position).trim());
                                    Dynamic_tabbar_attended_text.setText("全部");
                                    dynamicAdapter.setmPageIndex(1);
                                    getTypeListInfo(5,2);
                                    popupWindow.dismiss();
                                } else if (activityTypeList.get(position).equals("   其他")) {
                                    Dynamic_tabbar_type_text.setText(activityTypeList.get(position).trim());
                                    Dynamic_tabbar_attended_text.setText("全部");
                                    dynamicAdapter.setmPageIndex(1);
                                    getTypeListInfo(6,2);
                                    popupWindow.dismiss();
                                    Dynamic_tabbar_type_text.setSelected(false);
                                }
                            }
                        }
                    });
                    showPopupWindow();
                } else if (Dynamic_tabbar_activity_text.getText().equals("新闻")) {
                    ArrayAdapter<String> newstypeAapter = new ArrayAdapter<String>(mActivity, R.layout.popwindow_listitem, newsTypelist);
                    listviews.setAdapter(newstypeAapter);
                    listviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            {
                                if (newsTypelist.get(position).equals("   社团")) {
                                    if(!Preferences.isLogin()){
                                        AlertDialog.build(mContext, R.string.login_label_unlogin_msg,
                                                R.string.login_label_unlogin_ok, R.string.common_label_cancel,
                                                new AlertDialog.OnAlertDialogListener() {
                                                    @Override
                                                    public void onOk(AlertDialog alertDialogView) {
                                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                                        mContext.startActivity(intent);
                                                        Preferences.isRefreshing(true);
                                                        alertDialogView.dismiss();
                                                    }
                                                    @Override
                                                    public void onCancel(AlertDialog alertDialogView) {
                                                        alertDialogView.dismiss();
                                                    }
                                                }).show();
                                        return;
                                    }
                                    Dynamic_tabbar_type_text.setText(newsTypelist.get(position).trim());
                                    Dynamic_tabbar_attended_text.setText("全部");
                                    newAdapter.setmPageIndex(1);
                                    getNewsListInfo(0,0);
                                    popupWindow.dismiss();
                                } else if (newsTypelist.get(position).equals("   社联")) {
                                    Dynamic_tabbar_type_text.setText(newsTypelist.get(position).trim());
                                    newAdapter.setmPageIndex(1);
                                    getNewsListInfo(1,0);
                                    popupWindow.dismiss();
                                }
                            }
                        }
                    });
                    showPopupWindow();
                }
                break;
            case R.id.dynamic_tab_attended:
                /*初始化并显示"是否加入"检索的popwindow*/
                if (Dynamic_tabbar_activity_text.getText().equals("活动")) {
                    if(!Preferences.isLogin()){
                        AlertDialog.build(mContext, R.string.login_label_unlogin_msg,
                                R.string.login_label_unlogin_ok, R.string.common_label_cancel,
                                new AlertDialog.OnAlertDialogListener() {
                                    @Override
                                    public void onOk(AlertDialog alertDialogView) {
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        mContext.startActivity(intent);
                                        Preferences.isRefreshing(true);
                                        alertDialogView.dismiss();
                                    }

                                    @Override
                                    public void onCancel(AlertDialog alertDialogView) {
                                        alertDialogView.dismiss();
                                    }
                                }).show();
                        return;
                    }
                        ArrayAdapter<String> attendedAdapter = new ArrayAdapter<String>(mActivity, R.layout.popwindow_listitem, attendList);
                        listviews.setAdapter(attendedAdapter);
                        listviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (attendList.get(position).equals("   已加入")) {
                                Dynamic_tabbar_attended_text.setText(attendList.get(position).trim());
                                dynamicAdapter.setmPageIndex(1);
                                getAttenedListInfo(0);
                                popupWindow.dismiss();
                            } else if (attendList.get(position).equals("   未加入")) {
                                Dynamic_tabbar_attended_text.setText(attendList.get(position).trim());
                                dynamicAdapter.setmPageIndex(1);
                                getAttenedListInfo(1);
                                popupWindow.dismiss();
                            }
                            else if (attendList.get(position).equals("   全部")) {
                                Dynamic_tabbar_attended_text.setText(attendList.get(position).trim());
                                dynamicAdapter.setmPageIndex(1);
                                getAttenedListInfo(2);
                                popupWindow.dismiss();
                            }
                            attendLocation=position;
                        }
                    });
                    showPopupWindow();
                    break;
                }else if (Dynamic_tabbar_activity_text.getText().equals("新闻")&&Dynamic_tabbar_type_text.getText().equals("社团")) {
                    ArrayAdapter<String> attendedAdapter = new ArrayAdapter<String>(mActivity, R.layout.popwindow_listitem, newsAttendList);
                    listviews.setAdapter(attendedAdapter);
                    listviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (attendList.get(position).equals("   已加入")) {
                                Dynamic_tabbar_attended_text.setText(attendList.get(position).trim());
                                newAdapter.setmPageIndex(1);
                                getNewsListInfo(newsType,0);
                                popupWindow.dismiss();
                            } else if (attendList.get(position).equals("   未加入")) {
                                Dynamic_tabbar_attended_text.setText(attendList.get(position).trim());
                                newAdapter.setmPageIndex(1);
                                getNewsListInfo(newsType,1);
                                popupWindow.dismiss();
                            } else if (attendList.get(position).equals("   全部")) {
                                Dynamic_tabbar_attended_text.setText(attendList.get(position).trim());
                                newAdapter.setmPageIndex(1);
                                getNewsListInfo(newsType,2);
                                popupWindow.dismiss();
                            }
                            attendLocation=position;
                        }
                    });
                    showPopupWindow();
                    break;
                }
        }
        if (popupWindow==null){
            DynamicTabItem_public.setSelected(true);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.dynamic_listview:
                this.position = position;
                Intent dynamicIntent = new Intent(mActivity, DynamicDetailsActivity.class);
                dynamicIntent.putExtra("id",dynamicList.get(position).getId());
                startActivityForResult(dynamicIntent,100);
                break;
            case R.id.news_listview:
                Intent newsIntent = new Intent(mActivity, NewsDetailsActivity.class);
                newsIntent.putExtra("id",newsList.get(position).getId());
                System.out.println("********id***" + newsList.get(position).getId());
                startActivity(newsIntent);
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    public void onStart() {
        super.onStart();
        if (Preferences.getRefreshing()){
            pullToRefreshScrollView.doRefreshing(true);
            Preferences.isRefreshing(false);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK){
            int status = data.getIntExtra("status",-1);
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
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mActivity, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        if (refreshStatus == 0){
            dynamicAdapter.setmPageIndex(1);
            getTypeListInfo(typeLocation,attendLocation);
        }else{
            newAdapter.setmPageIndex(1);
            getNewsListInfo(newsType,attendLocation);
        }
    }
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        String label = DateUtils.formatDateTime(mActivity,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        pullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        if (refreshStatus == 0){
            getTypeListInfo(typeLocation,attendLocation);
        }else{
            getNewsListInfo(newsType,attendLocation);
        }
    }
}

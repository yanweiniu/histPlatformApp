package com.marchsoft.organization;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.utils.UrlAssert;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by FXB on 2016/2/28.
 */
public class AboutActivity extends BaseActivity implements PullToRefreshScrollView.OnRefreshListener2{
    private PullToRefreshScrollView mPullScrollView;
    private TextView aboutTitleTV;
    private WebView aboutTextWV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mPullScrollView=(PullToRefreshScrollView)findViewById(R.id.sv_about_us);
        aboutTitleTV=(TextView)findViewById(R.id.tv_aboutTitle);
        aboutTextWV=(WebView)findViewById(R.id.wv_about_us);
        mPullScrollView.setOnRefreshListener(this);
        mPullScrollView.doRefreshing(true);
    }
    private void getAboutData() {
        RequestParams params = new RequestParams();
        RestClient.get(Constant.API_GET_ABOUTUS, params, new AsyncHttpResponseHandler(getBaseContext(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if (response.getInt("msg_code")==Constant.CODE_SUCCESS){
                        JSONObject data=response.getJSONObject("data");
                        String title=data.getString("title");
                        String aboutContext=data.getString("about_us");
                        aboutTitleTV.setText(title);
                        aboutTextWV.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                mPullScrollView.onRefreshComplete();
                                super.onPageFinished(view, url);

                            }

                        });
                        if(UrlAssert.isUrl(aboutContext)){
                            aboutTextWV.loadUrl(aboutContext);
                        }else{

                            aboutTextWV.loadDataWithBaseURL(null,aboutContext,"text/html","utf-8",null);
                        }
                    }else{
                        ToastUtil.make(AboutActivity.this).show(response.getInt("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPullScrollView.onRefreshComplete();
            }
        }));
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getAboutData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }
}

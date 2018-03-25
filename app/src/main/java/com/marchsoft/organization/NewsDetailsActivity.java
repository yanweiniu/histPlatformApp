package com.marchsoft.organization;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.marchsoft.organization.convert.NewsJSONConvert;
import com.marchsoft.organization.http.AsyncHttpResponseHandler;
import com.marchsoft.organization.http.RequestParams;
import com.marchsoft.organization.http.RestClient;
import com.marchsoft.organization.model.News;
import com.marchsoft.organization.utils.Constant;
import com.marchsoft.organization.utils.ToastUtil;
import com.marchsoft.organization.utils.UrlAssert;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import static com.marchsoft.organization.R.id.organization_name;

/**
 * Created by Administrator on 2016/2/28 0028.
 */
public class NewsDetailsActivity extends BaseActivity implements PullToRefreshScrollView.OnRefreshListener2{
    private int newsId;
    WebView webView;
    private TextView newsTitle;
    private TextView organizationName;
    private TextView newsAuthor;
    private TextView newsDate;
    private PullToRefreshScrollView pullToRefreshScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details);
        newsId = getIntent().getIntExtra("id", 0);
        initHeader();
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setSupportZoom(true);
        pullToRefreshScrollView.doRefreshing(true);
        //getNewsDetails();

    }

    private void initHeader() {
        webView = (WebView) findViewById(R.id.webview);
        newsTitle = (TextView) findViewById(R.id.news_title);
        organizationName = (TextView) findViewById(organization_name);
        newsAuthor = (TextView) findViewById(R.id.news_author);
        newsDate = (TextView) findViewById(R.id.news_date);
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.news_scrollView);
        pullToRefreshScrollView.setOnRefreshListener(this);
    }

    private void getNewsDetails() {
        RequestParams params = new RequestParams();
        params.put("news_id",newsId);
        RestClient.get(Constant.API_GET_MAIN_NEWS_DETAILS,params,new AsyncHttpResponseHandler(this, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt("msg_code") == Constant.CODE_SUCCESS){
                        JSONObject jsonObject = response.getJSONObject("data").getJSONObject("news_info");
                        News news = NewsJSONConvert.convertJsonToItem(jsonObject);
                        String asName = news.getAsName();
                        SpannableString msp = new SpannableString(news.getAsName());
                        msp.setSpan(new RelativeSizeSpan(0.8f), 0, asName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dynamic_item_textcolor)), 0, asName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        newsTitle.setText(news.getTitle() + "　" );
                        newsTitle.append(msp);
                        newsAuthor.setText(news.getStudentName());
                        //organizationName.setText(news.getAsName());
                        newsDate.setText(news.getPublishDate());
                        Drawable author = getResources().getDrawable(R.mipmap.ic_author);
                        Drawable date = getResources().getDrawable(R.mipmap.ic_date);
                        author.setBounds(0, 0, author.getMinimumWidth(), author.getMinimumHeight());
                        date.setBounds(0, 0, date.getMinimumWidth(), date.getMinimumHeight());
                        newsDate.setCompoundDrawables(date, null, null, null);
                        newsAuthor.setCompoundDrawables(author, null, null, null);
                        String content =news.getContent();
                        webView.setWebViewClient(new WebViewClient(){
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                pullToRefreshScrollView.onRefreshComplete();
                                super.onPageFinished(view, url);
                            }
                        });
                        if (UrlAssert.isUrl(content)) {
                            webView.loadUrl(content);
                        } else {
                            webView.loadDataWithBaseURL(null, content, "text/html", "utf-8",
                                    null);
                        }
                    }else{
                        String msg = response.getString("msg");
                        ToastUtil.make(NewsDetailsActivity.this).show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                pullToRefreshScrollView.onRefreshComplete();
                super.onFinish();
            }
        }));


    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getNewsDetails();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }
}

package com.marchsoft.organization.http;
import com.loopj.android.http.AsyncHttpClient;
import com.marchsoft.organization.utils.Log;

public class RestClient {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		if (params == null) {
			params = new RequestParams();
		}
		Log.i("http request url", url + "?" + params.toString());
		client.get(url, params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		if (params == null) {
			params = new RequestParams();
		}
		Log.i("http request url", url + "?" + params.toString());
		client.post(url, params, responseHandler);
	}
}

package com.marchsoft.organization.http;

import com.marchsoft.organization.db.Preferences;

import java.util.Map;

public class RequestParams extends com.loopj.android.http.RequestParams {

    private static final long serialVersionUID = -5579602099273482314L;

    public RequestParams() {
        super();
        put("sign", "");
        put("version", "3.1.0");
        put("app_name", "android_orgnization");
        put("platform", "android");
        put("format", "json");
        put("token", Preferences.getAccessToken());
    }

    public RequestParams(Map<String, String> arg0) throws Exception {
        throw new Exception("不支持");
    }

    public RequestParams(Object... arg0) throws Exception {
        throw new Exception("不支持");
    }

    public RequestParams(String arg0, String arg1) throws Exception {
        throw new Exception("不支持");
    }

}

package com.marchsoft.organization.convert;

import com.marchsoft.organization.model.Dynamic;
import com.marchsoft.organization.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DynamicPageConvert {
    public static ArrayList<Dynamic> activityConvertJsonArrayToItemList(
            JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        ArrayList<Dynamic> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(activityonvertJsonToItem(jsonArray.getJSONObject(i)));
        }
        return list;
    }
    public static Dynamic activityonvertJsonToItem(JSONObject json)
            throws JSONException {
        Dynamic dynamic = new Dynamic();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+json.getString("name"));
        dynamic.setId(json.getInt("id"));
        dynamic.setTheme(json.getString("name"));
        dynamic.setDeclaration(json.optString("declaration"));
        /*dynamic.setSupercisor(json.optString("supervisor_id"));
        dynamic.setApplicationTime(json.optString("start_time"));*/
        dynamic.setPlace(json.optString("place"));
        dynamic.setRegistering(1);
        dynamic.setIcon(json.optString("object"));
        return dynamic;
    }

    public static ArrayList<News> newsConvertJsonArrayToItemList(
            JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        ArrayList<News> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(newsConvertJsonToItem(jsonArray.getJSONObject(i)));
        }
        return list;
    }
    public static News newsConvertJsonToItem(JSONObject json)
            throws JSONException {
        News news = new News();
        news.setId(json.getInt("id"));
        news.setTitle(json.getString("title"));
        news.setContent(json.optString("content"));
        //news.setReleaserId(json.optInt("releaser_id"));
        news.setPublishDate(json.optString("publish_date"));
        news.setAssociationId(json.optInt("association_id"));
        //news.setNackName("type");
        news.setPhoto(json.optString("photo"));
        return news;
    }
}

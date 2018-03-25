package com.marchsoft.organization.convert;

import com.marchsoft.organization.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsJSONConvert {
    public static ArrayList<News> convertJsonArrayToItemList(
            JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        ArrayList<News> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(convertJsonToItem(jsonArray.getJSONObject(i)));
        }

        return list;
    }

    public static News convertJsonToItem(JSONObject json)
            throws JSONException {
        News news = new News();
        news.setId(json.getInt("id"));
        news.setTitle(json.optString("title"));
        news.setContent(json.optString("content"));
        news.setPublishDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(json.optLong("publish_date") * 1000)));
        news.setTop(json.optInt("type"));
        news.setPhoto(json.optString("photo"));
        news.setAsName(json.optString("as_name"));
        news.setSupervisorId(json.optInt("supervisor_id"));
        news.setStudentName(json.optString("student_name"));
        news.setAssociationId(json.optInt("association_id"));
        return news;
    }
}

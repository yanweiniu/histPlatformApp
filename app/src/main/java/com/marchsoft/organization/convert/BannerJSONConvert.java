package com.marchsoft.organization.convert;
import com.marchsoft.organization.model.Banner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BannerJSONConvert {
    public static ArrayList<Banner> convertJsonArrayToItemList(
            JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        ArrayList<Banner> list = new ArrayList<Banner>();
        for (int i = 0; i < length; i++) {
            list.add(convertJsonToItem(jsonArray.getJSONObject(i)));
        }

        return list;
    }

    public static Banner convertJsonToItem(JSONObject json)
            throws JSONException {
        Banner banner =new Banner();
        banner.setId(json.getInt("id"));
        banner.setTitle(json.getString("title"));
        banner.setContent(json.getString("content"));
        banner.setPhoto(json.getString("photo"));
        banner.setPublicDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(json.optLong("publish_date") * 1000)));
        return banner;
    }
}

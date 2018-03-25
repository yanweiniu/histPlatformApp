package com.marchsoft.organization.convert;

import android.app.Application;
import android.widget.Toast;

import com.marchsoft.organization.BaseActivity;
import com.marchsoft.organization.model.Organization;
import com.marchsoft.organization.model.Organization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrganizationJSONConvert {
    public static ArrayList<Organization> convertJsonArrayToItemList(
            JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        ArrayList<Organization> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(convertJsonToItem(jsonArray.getJSONObject(i)));
        }

        return list;
    }

    public static Organization convertJsonToItem(JSONObject json)
            throws JSONException {
        Organization organization = new Organization();
        organization.setId(json.optInt("a_id"));
        organization.setName(json.optString("a_name"));
        organization.setIconAddress(json.optString("a_icon"));
        organization.setMemberCount(json.optInt("a_count"));
        organization.setViews(json.optInt("a_eye"));
        organization.setStatus(json.optInt("check"));
        return organization;
    }
}

package com.marchsoft.organization.convert;

import android.util.Log;

import com.marchsoft.organization.OrganizationDetailActivity;
import com.marchsoft.organization.model.OrganizationDetail;
import com.marchsoft.organization.model.OrganizationMemberBrefInfo;
import com.marchsoft.organization.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by wm on 16-3-21.
 */
public class OrganizationDetailJSONConvert {
//    public static OrganizationDetail convertJsonArrayToItem(JSONObject jsonObject) {
//        OrganizationDetail organizationDetail = null;
//        try {
//            organizationDetail = convertToItem(jsonArray.getJSONObject(0));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return organizationDetail;
//    }

    public static OrganizationDetail convertToItem(JSONObject jsonObject) throws JSONException {
        JSONArray viceArray;
        JSONArray memberArray;
        OrganizationDetail organizationDetail = new OrganizationDetail();
        organizationDetail.setOrganizationId(jsonObject.optInt("id"));
        organizationDetail.setOrganizationName(jsonObject.optString("as_name"));
        organizationDetail.setOrganizationIntroduction(jsonObject.optString("introduction"));
        organizationDetail.setDeclaration(jsonObject.optString("purpose"));
        organizationDetail.setCreateTime(dateConvert(jsonObject.optLong("set_time")));
        organizationDetail.setOrganizationIcon(jsonObject.optString("icon"));
        organizationDetail.setMemberCount(jsonObject.optInt("total"));
        organizationDetail.setPresidentIcon(jsonObject.optString("image"));
        organizationDetail.setPresentId(jsonObject.optInt("present_id"));
        organizationDetail.setIsMember(jsonObject.optInt("is_member"));
        if (jsonObject.opt("vice_president") == null || jsonObject.opt("vice_president").equals("null")){
            organizationDetail.setVicePresidentList(null);
        }else {
            organizationDetail.setVicePresidentList(convertToArray(jsonObject.optJSONArray("vice_president")));
        }
        if (jsonObject.opt("get_user") == null || jsonObject.opt("get_user").equals("null")){
            organizationDetail.setMemberList(null);
        }else {
            organizationDetail.setMemberList(convertToArray(jsonObject.optJSONArray("get_user")));
        }

        return organizationDetail;
    }

    public static String dateConvert(long createTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String creageDate = format.format(Long.parseLong(String.valueOf(createTime*1000)));
        return creageDate;
    }

    public static ArrayList<OrganizationMemberBrefInfo> convertToArray(JSONArray jsonArray) throws JSONException {
        ArrayList list = new ArrayList();
        if (jsonArray == null) {
            return null;
        } else {
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                list.add(convertToString((JSONObject) jsonArray.get(i)));
            }
            return list;
        }

    }

    public static OrganizationMemberBrefInfo convertToString(JSONObject jsonObject) {
        OrganizationMemberBrefInfo memberBrefInfo = new OrganizationMemberBrefInfo();
        memberBrefInfo.setUserId(jsonObject.optInt("user_id"));
        memberBrefInfo.setUserIcon(jsonObject.optString("image"));
        return memberBrefInfo;
    }
}
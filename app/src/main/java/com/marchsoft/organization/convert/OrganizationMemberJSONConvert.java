package com.marchsoft.organization.convert;

import com.marchsoft.organization.model.Organization;
import com.marchsoft.organization.model.OrganizationMember;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrganizationMemberJSONConvert {
    public static ArrayList<OrganizationMember> convertJsonArrayToItemList(
            JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        ArrayList<OrganizationMember> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(convertJsonToItem(jsonArray.getJSONObject(i)));
        }
        return list;
    }



    public static OrganizationMember convertJsonToItem(JSONObject json)
            throws JSONException {
        OrganizationMember organizationMember = new OrganizationMember();
        organizationMember.setUserId(json.optInt("user_id"));
        organizationMember.setIconAddress(json.optString("image"));
        organizationMember.setNickname(json.optString("nickname"));
        organizationMember.setStudentName(json.optString("student_name"));
        organizationMember.setSex(json.optInt("sex"));
        organizationMember.setStudentNum(String.valueOf(json.optInt("student_no")));
        if (json.opt("cell_phone") == null || json.opt("cell_phone").equals("null")){
            organizationMember.setPhone("未知");
        }else {
            organizationMember.setPhone(String.valueOf(json.optInt("cell_phone")));

        }
        organizationMember.setPositionId(json.optInt("position_id"));
        organizationMember.setJoinTime(json.optInt("initiation_time"));
        return organizationMember;
    }
}

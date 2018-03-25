package com.marchsoft.organization.convert;

import com.marchsoft.organization.model.LeaveMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LeaveMessageJSONConvert {
    public static ArrayList<LeaveMessage> convertJsonArrayToItemList(
            JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        ArrayList<LeaveMessage> list = new ArrayList<LeaveMessage>();
        for (int i = 0; i < length; i++) {
            list.add(convertJsonToItem(jsonArray.getJSONObject(i)));
        }

        return list;
    }

    public static LeaveMessage convertJsonToItem(JSONObject json)
            throws JSONException {
        LeaveMessage leaveMessage = new LeaveMessage();
        leaveMessage.setId(json.optInt("id"));
        leaveMessage.setAssociationId(json.optInt("association_id"));
        leaveMessage.setContent(json.optString("content"));
        leaveMessage.setUserId(json.optInt("user_id"));
        leaveMessage.setNickname(json.optString("nickname"));
        leaveMessage.setImage(json.optString("image"));
        leaveMessage.setPositionId(json.optInt("position_id"));
        leaveMessage.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(json.optLong("time") * 1000)));
        return leaveMessage;
    }
}

package com.marchsoft.organization.convert;
import com.marchsoft.organization.db.Preferences;
import com.marchsoft.organization.model.Dynamic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DynamicJSONConvert {
    public static ArrayList<Dynamic> convertJsonArrayToItemList(
            JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        ArrayList<Dynamic> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(convertJsonToItem(jsonArray.getJSONObject(i)));
        }

        return list;
    }

    public static Dynamic convertJsonToItem(JSONObject json)
            throws JSONException {

        Dynamic dynamic = new Dynamic();
        dynamic.setId(json.optInt("id"));
        dynamic.setName(json.optString("name"));
        dynamic.setTheme(json.optString("theme"));
        dynamic.setDeclaration(json.optString("declaration"));
        dynamic.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(json.optLong("start_time") * 1000)));
        dynamic.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(json.optLong("end_time")*1000)));
        dynamic.setFormat(json.optInt("format"));
        dynamic.setPlace(json.optString("place"));
        dynamic.setRegistering(json.optInt("registering"));
        dynamic.setSupervisorId(json.optInt("supervisor_id"));
        dynamic.setStudentName(json.optString("student_name"));
        dynamic.setImage(json.optString("image"));
        dynamic.setIcon(json.optString("icon"));
        dynamic.setAsNmae(json.optString("as_name"));
        if (!Preferences.isLogin()) {
            dynamic.setIsJoin(2);
        }else {
            dynamic.setIsJoin(json.optInt("is_join"));
        }
        dynamic.setAssociationId(json.optInt("association_id"));

        return dynamic;
    }
}

package com.marchsoft.organization.convert;
import com.marchsoft.organization.model.User;
import com.marchsoft.organization.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserJSONConvert {
    public static ArrayList<User> convertJsonArrayToItemList(
            JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        ArrayList<User> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(convertJsonToItem(jsonArray.getJSONObject(i)));
        }
        return list;
    }
    public static User convertJsonToItem(JSONObject json)
            throws JSONException {
        User user = new User();
        user.setmCell_phone(json.optString("cell_phone"));
        user.setmHome(json.optString("home"));
        String sex = String.valueOf(json.optInt("sex")).trim();
        if (Utils.isNumeric(sex)){
            user.setmSex(json.optInt("sex"));
        }else{
            if (sex.equals("男")){
                user.setmSex(0);
            }else  if (sex.equals("女")){
                user.setmSex(1);
            }else {
                user.setmSex(2);
            }
        }
        user.setmLocation(json.optString("location"));
        user.setmNickname(json.optString("nickname"));
        user.setmDepartment(json.optString("name"));
        user.setmClass(json.optString("class"));
        user.setmHeadImage(json.optString("image"));
        user.setmPhoto(json.optString("photo"));
        user.setmSign(json.optString("sign"));
        user.setmEmail(json.optString("email"));
        user.setmStudent_name(json.optString("student_name"));
        user.setmMajor(json.optString("major"));
        user.setmQQ(json.optInt("qq"));
        if(Utils.isNumeric(String.valueOf(json.optString("birthday")).trim())){
            user.setmBirthday(json.optString("birthday"));
        }else{
            user.setmBirthday("未知");
        }
        user.setmStatus(json.optInt("status"));
        user.setmToken(json.optString("token"));
        user.setmUserId(json.optInt("id"));
        return user;
    }
}

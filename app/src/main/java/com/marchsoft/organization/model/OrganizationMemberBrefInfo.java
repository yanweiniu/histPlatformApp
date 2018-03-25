package com.marchsoft.organization.model;

/**
 * Created by wm on 16-3-22.
 */
public class OrganizationMemberBrefInfo {
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    private int userId;
    private String userIcon;
}

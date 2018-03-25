package com.marchsoft.organization.model;

import java.util.ArrayList;

/**
 * Created by wm on 16-3-21.
 */
public class OrganizationDetail {
    private int organizationId;
    private String organizationName;
    private String organizationIntroduction;
    private String declaration;
    private String organizationIcon;
    private String createTime;
    private int memberCount;
    private String presidentIcon;
    private int presentId;
    private int isMember;
    private ArrayList vicePresidentList;
    private ArrayList memberList;

    public ArrayList getVicePresidentList() {
        return vicePresidentList;
    }

    public void setVicePresidentList(ArrayList vicePresidentList) {
        this.vicePresidentList = vicePresidentList;
    }

    public ArrayList getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList memberList) {
        this.memberList = memberList;
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationIntroduction() {
        return organizationIntroduction;
    }

    public void setOrganizationIntroduction(String organizationIntroduction) {
        this.organizationIntroduction = organizationIntroduction;
    }

    public int getPresentId() {
        return presentId;
    }

    public void setPresentId(int presentId) {
        this.presentId = presentId;
    }


    public String getOrganizationIcon() {
        return organizationIcon;
    }

    public void setOrganizationIcon(String organizationIcon) {
        this.organizationIcon = organizationIcon;
    }
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getPresidentIcon() {
        return presidentIcon;
    }

    public void setPresidentIcon(String presidentIcon) {
        this.presidentIcon = presidentIcon;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }



}
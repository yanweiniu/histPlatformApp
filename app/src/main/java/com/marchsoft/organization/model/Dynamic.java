package com.marchsoft.organization.model;

/**
 * Created by Administrator on 2016/2/23 0023.
 */
public class Dynamic {
    private int id;//活动id
    private String name;//活动名称
    private String theme;//活动主题
    private String declaration;//活动宣言
    private String startTime;//活动时间
    private String place;//活动地点
    private int registering;//报名参与人数
    private int supervisorId;//负责人id
    private String icon;//会标
    private String studentName;//负责人
    private int isJoin;//是否加入：0已报名，1已取消，2未报名
    private String asNmae;//社团名称
    private int format;//活动形式比赛（0），演出（1），报告（2），会议（3），参观（4），公益活动（5）其他（6）
    private String image;//负责人头像
    private int associationId;
    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getAssociationId() {
        return associationId;
    }

    public void setAssociationId(int associationId) {
        this.associationId = associationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getRegistering() {
        return registering;
    }

    public void setRegistering(int registering) {
        this.registering = registering;
    }

    public int getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(int supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }

    public String getAsNmae() {
        return asNmae;
    }

    public void setAsNmae(String asNmae) {
        this.asNmae = asNmae;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

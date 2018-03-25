package com.marchsoft.organization.model;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/7 0007.
 * 用来初始化省市区
 */

public class PCD implements Serializable{
    private int provinceIndex ;
    private int cityIndex ;
    private int districtIndex ;
    private String mCurrentProviceName;
    private String mCurrentCityName;
    private String mCurrentDistrictName ="";

    public String getmCurrentDistrictName() {
        return mCurrentDistrictName;
    }

    public void setmCurrentDistrictName(String mCurrentDistrictName) {
        this.mCurrentDistrictName = mCurrentDistrictName;
    }

    public int getProvinceIndex() {
        return provinceIndex;
    }

    public void setProvinceIndex(int provinceIndex) {
        this.provinceIndex = provinceIndex;
    }

    public int getCityIndex() {
        return cityIndex;
    }

    public void setCityIndex(int cityIndex) {
        this.cityIndex = cityIndex;
    }

    public int getDistrictIndex() {
        return districtIndex;
    }

    public void setDistrictIndex(int districtIndex) {
        this.districtIndex = districtIndex;
    }

    public String getmCurrentProviceName() {
        return mCurrentProviceName;
    }

    public void setmCurrentProviceName(String mCurrentProviceName) {
        this.mCurrentProviceName = mCurrentProviceName;
    }

    public String getmCurrentCityName() {
        return mCurrentCityName;
    }

    public void setmCurrentCityName(String mCurrentCityName) {
        this.mCurrentCityName = mCurrentCityName;
    }

    /**
     * 从json串中解析pcd对象
     *
     * @param json
     * @return
     */
    public static PCD fromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new Gson();
        PCD pcd = gson.fromJson(json, PCD.class);
        return pcd;
    }

    /**
     * 把pcd对象转化成json串
     *
     * @param pcd
     * @return
     */
    public static String toJson(PCD pcd) {
        Gson gson = new Gson();
        String pcdJson = gson.toJson(pcd);
        return pcdJson;
    }

}

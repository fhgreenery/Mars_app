package com.sjzc.fh.pedometer.database.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import cn.bmob.v3.BmobUser;

/**
 * Entity mapped to table "PERSONINFO_TABLE".
 */
public class PersonInfo extends BmobUser {

    private String mobile;
    private String name;
    private String sex;
    private String birthday;
    private String address;
    private String header_img_url;
    private Double homeLat;
    private Double homeLon;
    private String homeAddress;
    private Double companyLat;
    private Double companyLon;
    private String companyAddress;
    private String uid;
    private int integral;

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getCircle_header_bg_url() {
        return circle_header_bg_url;
    }

    public void setCircle_header_bg_url(String circle_header_bg_url) {
        this.circle_header_bg_url = circle_header_bg_url;
    }

    private String circle_header_bg_url;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public PersonInfo() {
    }

    public PersonInfo(String mobile) {
        this.mobile = mobile;
    }

    public PersonInfo(String mobile, String name, String sex, String birthday, String address, String header_img_url, Double homeLat, Double homeLon, String homeAddress, Double companyLat, Double companyLon, String companyAddress) {
        this.mobile = mobile;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.address = address;
        this.header_img_url = header_img_url;
        this.homeLat = homeLat;
        this.homeLon = homeLon;
        this.homeAddress = homeAddress;
        this.companyLat = companyLat;
        this.companyLon = companyLon;
        this.companyAddress = companyAddress;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeader_img_url() {
        return header_img_url;
    }

    public void setHeader_img_url(String header_img_url) {
        this.header_img_url = header_img_url;
    }

    public Double getHomeLat() {
        return homeLat;
    }

    public void setHomeLat(Double homeLat) {
        this.homeLat = homeLat;
    }

    public Double getHomeLon() {
        return homeLon;
    }

    public void setHomeLon(Double homeLon) {
        this.homeLon = homeLon;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Double getCompanyLat() {
        return companyLat;
    }

    public void setCompanyLat(Double companyLat) {
        this.companyLat = companyLat;
    }

    public Double getCompanyLon() {
        return companyLon;
    }

    public void setCompanyLon(Double companyLon) {
        this.companyLon = companyLon;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

}

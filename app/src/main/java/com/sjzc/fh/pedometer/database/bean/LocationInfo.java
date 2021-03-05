package com.sjzc.fh.pedometer.database.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.util.Comparator;

import cn.bmob.v3.BmobObject;

/**
 * Entity mapped to table "LOCATION_TABLE".
 */
public class LocationInfo extends BmobObject{

    private String nativePhoneNumber;
    private String lontitude;
    private String latitude;
    private String address;
    private String locationdescribe;
    private Long time;
    private String deviceId;

    public PersonInfo piInfo;

    public LocationInfo() {
    }

    public LocationInfo(String nativePhoneNumber, String lontitude, String latitude, String address, String locationdescribe, Long time, String deviceId) {
        this.nativePhoneNumber = nativePhoneNumber;
        this.lontitude = lontitude;
        this.latitude = latitude;
        this.address = address;
        this.locationdescribe = locationdescribe;
        this.time = time;
        this.deviceId = deviceId;
    }

    public String getNativePhoneNumber() {
        return nativePhoneNumber;
    }

    public void setNativePhoneNumber(String nativePhoneNumber) {
        this.nativePhoneNumber = nativePhoneNumber;
    }

    public String getLontitude() {
        return lontitude;
    }

    public void setLontitude(String lontitude) {
        this.lontitude = lontitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationdescribe() {
        return locationdescribe;
    }

    public void setLocationdescribe(String locationdescribe) {
        this.locationdescribe = locationdescribe;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public static class SortClass implements Comparator {
        public int compare(Object arg0,Object arg1){
            LocationInfo location0 = (LocationInfo)arg0;
            LocationInfo location1 = (LocationInfo)arg1;
            int flag = location0.getUpdatedAt().compareTo(location1.getUpdatedAt());
            return flag;
        }
    }
}

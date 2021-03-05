package com.sjzc.fh.pedometer.entity;

import com.sjzc.fh.pedometer.database.bean.PersonInfo;

import cn.bmob.v3.BmobObject;

/**
 * 计步信息
 */

public class StepInfo extends BmobObject{

    private int count;
    private String date;
    private int zannum;
    public PersonInfo personInfo;

    public int getZannum() {
        return zannum;
    }

    public void setZannum(int zannum) {
        this.zannum = zannum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // 设置分组的key
    public String groupKey(){
        return getCreatedAt().substring(0,10);
    }
}

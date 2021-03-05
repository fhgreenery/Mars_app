package com.sjzc.fh.pedometer.entity;

import com.sjzc.fh.pedometer.database.bean.PersonInfo;

import cn.bmob.v3.BmobObject;

/**
 *签到
 */

public class SignInfo extends BmobObject {

    public String date;
    public boolean hasSign;
    public PersonInfo personInfo;
}

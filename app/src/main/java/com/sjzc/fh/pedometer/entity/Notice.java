package com.sjzc.fh.pedometer.entity;

import cn.bmob.v3.BmobObject;

/**
 * 通知
 */

public class Notice extends BmobObject {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

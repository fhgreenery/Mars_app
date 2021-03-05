package com.sjzc.fh.pedometer.entity;

import com.sjzc.fh.pedometer.database.bean.PersonInfo;

import cn.bmob.v3.BmobObject;

/**
 * 评论
 */

public class CircleComment extends BmobObject {

    private String content;//评论内容

    private PersonInfo personInfo;//评论的用户，Pointer类型，一对一关系

    private CircleContent circleContent; //所评论的圈子内容，这里体现的是一对多的关系，一个评论只能属于一个圈子


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public CircleContent getCircleContent() {
        return circleContent;
    }

    public void setCircleContent(CircleContent circleContent) {
        this.circleContent = circleContent;
    }
}

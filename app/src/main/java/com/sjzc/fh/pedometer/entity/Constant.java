package com.sjzc.fh.pedometer.entity;

import com.sjzc.fh.pedometer.utils.SDCardUtils;

/**
 * 常量信息保存
 */

public interface Constant {

    String ROOT_PATH = String.format("%s%s", SDCardUtils.getESDString(), "/Zuji/");// 根目录
    String LOG_PATH = String.format("%slog/", ROOT_PATH);// 日志目录
    String AVATAR_PATH = String.format("%savatar/", ROOT_PATH);// 头像目录
    String CRASH_PATH = String.format("%scrash/", ROOT_PATH);// 异常信息的目录
    String SAVE_PIC = String.format("%ssavepic/", ROOT_PATH);// 图片保存的目录
    String SAVE_AUDIO = String.format("%ssaveaudio/", ROOT_PATH);// 音频保存的目录


    String TEMP_FILE_PATH = String.format("%stemp/", ROOT_PATH);// 临时文件存放的目录

    String BOMB_APP_ID = "a9f31dc59d63f44b91acfc9ff0b1595a" + "";//Bmob服务器接口
    String KEY_SPLASH_PIC_URL = "key_splash_pic_url";
    int SPLASH_PIC_URL_SAVE_TIME =  60 * 60 * 1; //1小时


    String KEY_JOKE_TEXT_URL = "key_joke_text_url";
    String KEY_JOKE_PIC_URL = "key_joke_pic_url";

    int JOKE_TEXT_OR_PIC_URL_SAVE_TIME =  24 * 60 * 60 * 1000; //24小时


    String KEY_LAST_UPDATE_CURR_PERSONINFO_TIME = "key_last_update_curr_personinfo_time";
    long UPDATE_CURR_PERSONINFO_INTERVAL = 5 * 60 * 1000;// 5分钟


    String KEY_LAST_CHECK_UPDATE_TIME = "key_last_check_update_time";
    long CHECK_UPDATE_INTERVAL = 10 * 60 * 1000;// 10分钟


    String KEY_LAST_SHOW_NOTICE_TIME = "key_last_show_notice_time";
    long SHOW_NOTICE_INTERVAL =  24 * 60 * 60 * 1000;// 24小时

    String KEY_SIGN_DATE= "key_sign_date";

    String KEY_CURR_SERVER_TIME = "key_curr_server_time";

    // 日志过期时间，默认为10天
    int LOG_EXPIRED_TIME = 10;
}

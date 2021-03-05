package com.sjzc.fh.pedometer.database;

import android.text.TextUtils;

import com.sjzc.fh.pedometer.database.dao.LocationInfoDao;
import com.sjzc.fh.pedometer.database.dao.LoginBeanDao;
import com.sjzc.fh.pedometer.database.dao.PushMessageDao;
import com.sjzc.fh.pedometer.utils.ConfigKey;
import com.sjzc.fh.pedometer.App;
import com.sjzc.fh.pedometer.database.bean.LocationInfo;
import com.sjzc.fh.pedometer.database.bean.LoginBean;
import com.sjzc.fh.pedometer.database.bean.PersonInfo;
import com.sjzc.fh.pedometer.database.bean.PushMessage;
import com.sjzc.fh.pedometer.database.dao.PersonInfoDao;

import java.util.List;

import static com.sjzc.fh.pedometer.App.aCache;


/**
 * 数据处理-增删改查
 */

public class DBHandler {
    private static LoginBeanDao loginDao = App.session.getLoginBeanDao();
    private static PushMessageDao pushMessageDao = App.session.getPushMessageDao();
    private static PersonInfoDao personInfoDao = App.session.getPersonInfoDao();
    private static LocationInfoDao locationInfoDao = App.session.getLocationInfoDao();

    public static void insertLoginInfo(LoginBean loginInfo) {
        loginDao.insertOrReplace(loginInfo);
    }

    public static LoginBean  getLoginInfo() {
        if (loginDao.loadAll() != null && loginDao.loadAll().size() > 0)
            for (LoginBean loginBean : loginDao.loadAll())
                if (loginBean.getLogining())
                    return loginBean;
        return null;
    }

    public static void clearLoginInfo() {
        loginDao.deleteAll();
    }

    public static void insertPushMessage(PushMessage pushMessage) {
        pushMessageDao.insertOrReplace(pushMessage);
    }


    public static List<PushMessage> getAllPushMessage() {
        return  pushMessageDao.loadAll();
    }

    public static void insertPesonInfo(PersonInfo personInfo) {
        personInfoDao.insertOrReplace(personInfo);
    }


    public static PersonInfo getCurrPesonInfo() {
        String mobile = aCache.getAsString(ConfigKey.KEY_CURR_LOGIN_MOBILE);
        if(TextUtils.isEmpty(mobile))
            return null;
        List<PersonInfo> personInfos  = personInfoDao.queryBuilder().where(PersonInfoDao.Properties.Mobile.eq(mobile)).list();
        if (personInfos != null && personInfos.size() > 0){
            return  personInfos.get(0);
        }
        return null;
    }


    public static void updatePesonInfo(PersonInfo personInfo) {
        personInfoDao.insertOrReplace(personInfo);
    }

    public static void insertLocationInfo(LocationInfo locationInfo) {
        locationInfoDao.insertOrReplace(locationInfo);
    }
    public static List<LocationInfo> getAllLocationInfo() {
        return  locationInfoDao.loadAll();
    }
}

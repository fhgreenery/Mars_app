package com.sjzc.fh.pedometer.database.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.sjzc.fh.pedometer.database.bean.LocationInfo;
import com.sjzc.fh.pedometer.database.bean.LoginBean;
import com.sjzc.fh.pedometer.database.bean.PushMessage;
import com.sjzc.fh.pedometer.database.bean.PersonInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig loginBeanDaoConfig;
    private final DaoConfig pushMessageDaoConfig;
    private final DaoConfig personInfoDaoConfig;
    private final DaoConfig locationInfoDaoConfig;

    private final LoginBeanDao loginBeanDao;
    private final PushMessageDao pushMessageDao;
    private final PersonInfoDao personInfoDao;
    private final LocationInfoDao locationInfoDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        loginBeanDaoConfig = daoConfigMap.get(LoginBeanDao.class).clone();
        loginBeanDaoConfig.initIdentityScope(type);

        pushMessageDaoConfig = daoConfigMap.get(PushMessageDao.class).clone();
        pushMessageDaoConfig.initIdentityScope(type);

        personInfoDaoConfig = daoConfigMap.get(PersonInfoDao.class).clone();
        personInfoDaoConfig.initIdentityScope(type);

        locationInfoDaoConfig = daoConfigMap.get(LocationInfoDao.class).clone();
        locationInfoDaoConfig.initIdentityScope(type);

        loginBeanDao = new LoginBeanDao(loginBeanDaoConfig, this);
        pushMessageDao = new PushMessageDao(pushMessageDaoConfig, this);
        personInfoDao = new PersonInfoDao(personInfoDaoConfig, this);
        locationInfoDao = new LocationInfoDao(locationInfoDaoConfig, this);

        registerDao(LoginBean.class, loginBeanDao);
        registerDao(PushMessage.class, pushMessageDao);
        registerDao(PersonInfo.class, personInfoDao);
        registerDao(LocationInfo.class, locationInfoDao);
    }
    
    public void clear() {
        loginBeanDaoConfig.getIdentityScope().clear();
        pushMessageDaoConfig.getIdentityScope().clear();
        personInfoDaoConfig.getIdentityScope().clear();
        locationInfoDaoConfig.getIdentityScope().clear();
    }

    public LoginBeanDao getLoginBeanDao() {
        return loginBeanDao;
    }

    public PushMessageDao getPushMessageDao() {
        return pushMessageDao;
    }

    public PersonInfoDao getPersonInfoDao() {
        return personInfoDao;
    }

    public LocationInfoDao getLocationInfoDao() {
        return locationInfoDao;
    }

}

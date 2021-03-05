package com.sjzc.fh.pedometer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.sjzc.fh.pedometer.ui.MainNewActivity;

public class MusicService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("dsds","onCreat");
        //创建通知栏通知实例
        Notification notification=new Notification();
        //设置图片
        notification.icon= com.sjzc.fh.pedometer.R.drawable.star;
        //设置内容
        notification.contentView=new RemoteViews(getPackageName(), com.sjzc.fh.pedometer.R.layout.notify);
        //设置点击通知栏通知时的跳转
        Intent intent=new Intent(this,MainNewActivity.class);
        notification.contentIntent= PendingIntent.getActivity(this,0,intent,0);

        //启动
        startForeground(1,notification); //这是第一个服务所有ID从1开始
    }

    @Override
    public void onDestroy() {
        Log.i("dsds","onDestory");
        super.onDestroy();
    }
}

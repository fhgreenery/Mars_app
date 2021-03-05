package com.sjzc.fh.pedometer.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


import com.facebook.stetho.common.LogUtil;
import com.sjzc.fh.pedometer.R;
import com.sjzc.fh.pedometer.ui.fragment.PrivateMessageLauncherFragment;
import com.sjzc.fh.pedometer.ui.fragment.RewardLauncherFragment;
import com.sjzc.fh.pedometer.adapter.BaseFragmentAdapter;
import com.sjzc.fh.pedometer.entity.Constant;
import com.sjzc.fh.pedometer.ui.fragment.LauncherBaseFragment;
import com.sjzc.fh.pedometer.ui.fragment.StereoscopicLauncherFragment;
import com.sjzc.fh.pedometer.utils.OtherUtil;
import com.sjzc.fh.pedometer.view.GuideViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import static com.sjzc.fh.pedometer.App.aCache;

import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;


/**
 * 缓冲界面 背景图片 实现逐字显示效果 获取登录时间
 */

public class SplashActivity extends FragmentActivity{
    private GuideViewPager vPager;			//	图片切换控件
    private List<LauncherBaseFragment> list = new ArrayList<LauncherBaseFragment>();
    private BaseFragmentAdapter adapter;	//适配器
    private View startView = null;
    private ImageView[] tips; //图片数组
    private int currentSelect;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startView = View.inflate(this, R.layout.splash, null);
        setContentView(startView);
//        if(OtherUtil.hasLogin()){
//            intent = new Intent(SplashActivity.this, MainNewActivity.class);
//        }
        //初试viewGroup控件
        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        //设置切换圆点图片
        tips = new ImageView[3];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LayoutParams(10, 10));//间距为10sp
            if (i == 0) {        //选中时为亮白
                imageView.setBackgroundResource(R.drawable.page_indicator_focused);
            } else {            //未选中为暗灰
                imageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            tips[i] = imageView;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 20;//设置左边距
            layoutParams.rightMargin = 20;//设置右边距
            group.addView(imageView, layoutParams);
        }

        //获取自定义viewpager-图片显示 然后设置背景图片
        vPager = (GuideViewPager) findViewById(R.id.viewpager_launcher);
        vPager.setBackGroud(BitmapFactory.decodeResource(getResources(), R.drawable.bg_reward_launcher));

        /**
         * 初始化三个fragment  并且添加到list中
         */
        RewardLauncherFragment rewardFragment = new RewardLauncherFragment();
        PrivateMessageLauncherFragment privateFragment = new PrivateMessageLauncherFragment();
        StereoscopicLauncherFragment stereoscopicFragment = new StereoscopicLauncherFragment();
        list.add(rewardFragment);
        list.add(privateFragment);
        list.add(stereoscopicFragment);

        adapter = new BaseFragmentAdapter(getSupportFragmentManager(), list);
        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(2);
        vPager.setCurrentItem(0);
        vPager.setOnPageChangeListener(changeListener);

        syncServerDate();
    }


    /**
     * 监听viewpager的移动
     */
    OnPageChangeListener changeListener=new OnPageChangeListener() {
        @Override
        public void onPageSelected(int index) {
            setImageBackground(index);//改变切换圆点的切换效果
            LauncherBaseFragment fragment=list.get(index);

            list.get(currentSelect).stopAnimation();//停止前一个页面的动画
            fragment.startAnimation();//开启当前页面的动画

            currentSelect=index;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}
        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };

    /**
     * 改变切换圆点的切换效果
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
        //点击"立即体验"按钮,跳转到用户登录界面
        ImageView experienceIV = (ImageView) findViewById(R.id.imgView_immediate_experience);
        experienceIV.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                intent = new Intent(SplashActivity.this, AnimationActivity.class);
                startActivity(intent);
                finish();

            }

        });
//        ImageView ivReward=(ImageView) findViewById(R.id.iv_reward);
//        ivReward.setOnClickListener(new OnClickListener(){
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                intent = new Intent(SplashActivity.this, MainNewActivity.class);
//                startActivity(intent);
//                finish();
//
//            }
//
//        });
    }

    private void syncServerDate() {
        Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long time, BmobException e) {
                if(e == null){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    final String date = formatter.format(new Date(time * 1000L));
                    LogUtil.i("Bmob","当前服务器时间为:" + date);
                    aCache.put(Constant.KEY_CURR_SERVER_TIME,date);
                }
            }
        });
    }

}

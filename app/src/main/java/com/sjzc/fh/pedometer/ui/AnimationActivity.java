package com.sjzc.fh.pedometer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjzc.fh.pedometer.R;

public class AnimationActivity extends Activity {

    private ImageView mLeft,mRight;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_img);

        mLeft= (ImageView) findViewById(R.id.image_left);
        mRight= (ImageView) findViewById(R.id.image_right);
//        mTextView= (TextView) findViewById(R.id.textView);


        //左图
        //创建一个Animation动画集合
        AnimationSet anim=new AnimationSet(true);
        /*
        1.确定类型： Absolute 相对于屏幕，Relative to self 相对自己 Relative to
        2.确定X的起始位置 0
        3.确定X的模式
        4.确定x的结束动画位置
        5. Y的同x一样
         */
        TranslateAnimation mytransLateanim=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f);

        //设置动画时间
        mytransLateanim.setDuration(3000);
        //确定动画开始的时间
        anim.setStartOffset(800);
        //添加动画
        anim.addAnimation(mytransLateanim);
        //设置停留在最后位置
        anim.setFillAfter(true);
         //启动动画
        mLeft.startAnimation(anim);

        //右图
        AnimationSet anim2=new AnimationSet(true);
        TranslateAnimation mytransLateanim2=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,+1f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f);

        //设置动画时间
        mytransLateanim2.setDuration(3000);
        //确定动画开始的时间
        anim2.setStartOffset(800);
        //添加动画
        anim2.addAnimation(mytransLateanim2);
        //设置停留在最后位置
        anim2.setFillAfter(true);
        //启动动画
        mRight.startAnimation(anim2);

        //字动画
        /*
        fromX  动画开始时X坐标的伸缩chic
        toX  动画结束时X坐标的伸缩尺寸
        FromX  动画开始时Y坐标的伸缩chic
        toY  动画结束时Y坐标的伸缩尺寸
         */
//        AnimationSet anim3=new AnimationSet(true);
//        ScaleAnimation myScaleanim=new ScaleAnimation(1f,3f,1f,3f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        myScaleanim.setDuration(1500);
//        AlphaAnimation myalphaanim=new AlphaAnimation(1,0.0001f);
//        myalphaanim.setDuration(1500);
//        anim3.addAnimation(myScaleanim); //添加动画
//        anim3.addAnimation(myalphaanim);//添加动画
//        anim3.setFillAfter(true);
//        mTextView.startAnimation(anim3);


        //动画结束自动跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(AnimationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        },4000);
    }



    /*\
    写动画主要步骤：
    1.创建一个Animations对象
    2.根据需要建立相对应的Animation对象
    3.根据对动画的需求，为Animation对象设置相应的数据
    4.将Animation对象添加到AninationSet对象种
    5.使用控件对象开始执行AnimationSet

     */
}

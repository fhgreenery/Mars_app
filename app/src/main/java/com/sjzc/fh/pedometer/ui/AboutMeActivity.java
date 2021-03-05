package com.sjzc.fh.pedometer.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjzc.fh.pedometer.utils.AppUtils;
import com.sjzc.fh.pedometer.base.BaseActivity;
import com.sjzc.fh.pedometer.utils.OtherUtil;
import com.sjzc.fh.pedometer.R;

/**
 * 关于我们
 */

public class AboutMeActivity extends BaseActivity {

    private TextView tvVersionName;
    private TextView tvSoftWareSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_me);

        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.title)).setText("关于");
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvVersionName= (TextView)findViewById(R.id.tv_version_name);
        String versionName = AppUtils.getVersionName();
        tvVersionName.setText("V "+versionName);

//        tvSoftWareSite = (TextView) findViewById(R.id.tv_software_site);
//        tvSoftWareSite.setText(getClickableSpan());
//        //设置超链接可点击
//        tvSoftWareSite.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 获取可点击的SpannableString
     * @return

    private SpannableString getClickableSpan() {
       SpannableString spannableString = new SpannableString("https://123.sogou.com/");

        return spannableString;
    }
*/
    public void shareClick(View view){

        OtherUtil.shareTextAndImage(this,"火星运动","一款具有社交功能的计步软件 \n", null);//分享图文
    }

}

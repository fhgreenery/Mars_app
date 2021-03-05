package com.sjzc.fh.pedometer.ui;

import android.os.Bundle;

import com.sjzc.fh.pedometer.R;
import com.sjzc.fh.pedometer.base.BaseActivity;
import com.sjzc.fh.pedometer.utils.ImageUtil;
import com.sjzc.fh.pedometer.utils.UIUtils;
import com.sjzc.fh.pedometer.widget.SuperImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class BigSingleImgActivity extends BaseActivity {

    @InjectView(R.id.iv_bigimg)
    SuperImageView ivBigimg;

    public static final String IMAGE_URL = "image_url";

    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.big_single_image);
        ButterKnife.inject(this);


        int sw = UIUtils.getScreenWidthAndHeight(this)[0];
       /* RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) ivBigimg.getLayoutParams();
        rlp.width = sw;
        rlp.height = sw * 3/2;
        ivBigimg.setLayoutParams(rlp);*/

        imgUrl = getIntent().getStringExtra(IMAGE_URL);
        ImageUtil.setDefaultImageView(ivBigimg,imgUrl, R.mipmap.bg_pic_def_rect,this);
    }

    @Override
    protected int getStateBarColor() {
        return R.color.color_transparent;
    }

}

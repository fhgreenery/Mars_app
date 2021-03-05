package com.sjzc.fh.pedometer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjzc.fh.pedometer.R;
import com.sjzc.fh.pedometer.base.BaseActivity;
import com.sjzc.fh.pedometer.database.bean.PersonInfo;
import com.sjzc.fh.pedometer.utils.ImageUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class UserInfoActivity extends BaseActivity {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.user_photo)
    ImageView userPhoto;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_gender)
    TextView tvGender;
    @InjectView(R.id.btn_message_board)
    Button btnMessageBoard;
//    @InjectView(R.id.rl_circle)
//    RelativeLayout rlCircle;
    @InjectView(R.id.tv_birthday)
    TextView tvBirthday;
    @InjectView(R.id.container)
    LinearLayout container;

    private PersonInfo personInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);
        ButterKnife.inject(this);

        initData();
    }


    private void initData() {
        title.setText("用户中心");
        personInfo = (PersonInfo) getIntent().getSerializableExtra("piInfo");
        if (null != personInfo) {
            ImageUtil.setCircleImageView(userPhoto, personInfo.getHeader_img_url(), R.mipmap.icon_user_def, this);

            if (!TextUtils.isEmpty(personInfo.getName()))
                tvName.setText(personInfo.getName());
            else
                tvName.setText("");

            if (!TextUtils.isEmpty(personInfo.getSex()))
                tvGender.setText(personInfo.getSex());
            else
                tvGender.setText("男");

            if (!TextUtils.isEmpty(personInfo.getBirthday())){
                tvBirthday.setText(personInfo.getBirthday());
            } else {
                tvBirthday.setText("未选择");
            }
        }
    }


    @OnClick({R.id.iv_back, R.id.btn_message_board})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_message_board:
                startActivity(new Intent(this, MessageBoardActivity.class).putExtra("piInfo", personInfo));
                break;
//            case R.id.rl_circle:
//                startActivity(new Intent(this, PrivateCircleActivity.class).putExtra("piInfo", personInfo));
//                break;
        }

    }

}
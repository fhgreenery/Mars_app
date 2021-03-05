package com.sjzc.fh.pedometer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.sjzc.fh.pedometer.R;
import com.sjzc.fh.pedometer.base.BaseActivity;
import com.sjzc.fh.pedometer.database.bean.PersonInfo;
import com.sjzc.fh.pedometer.utils.OtherUtil;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册
 */

public class RegistActivity extends BaseActivity implements View.OnClickListener{

    private EditText mEtName;
    private EditText mEtMobile;
    private EditText mEtPwd;
    private EditText mEtPincode;
    private Button  mBtnRegist;
    private Button  mBtnPincode;

    private String name,mobile ,authCode,pwd ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);

        initView();
        addListener();
    }

    private void addListener() {
        mBtnPincode.setOnClickListener(this);
        mBtnRegist.setOnClickListener(this);
    }

    private void initView() {
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtMobile = (EditText) findViewById(R.id.et_mobile);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mEtPincode = (EditText) findViewById(R.id.et_pincode);
        mBtnPincode = (Button) findViewById(R.id.btn_pincode);
        mBtnRegist = (Button) findViewById(R.id.btn_regist);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_regist:
                name  = mEtName.getText().toString().trim();
                mobile  = mEtMobile.getText().toString().trim();
                pwd = mEtPwd.getText().toString().trim();
                authCode = mEtPincode.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(mobile)){
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!OtherUtil.isMobile(mobile)){
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!OtherUtil.isPassword(pwd)){
                    Toast.makeText(this, "密码由6~16位数字和英文字母组成", Toast.LENGTH_SHORT).show();
                    return;
                }

              if(TextUtils.isEmpty(authCode)){
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                doRegist(name ,mobile,pwd);
            break;
            case com.sjzc.fh.pedometer.R.id.btn_pincode:
                mobile  = mEtMobile.getText().toString().trim();
                if(TextUtils.isEmpty(mobile)){
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取短信验证码
                BmobSMS.requestSMSCode(mobile, "regist", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if(e==null){
                            Toast.makeText(RegistActivity.this, "发送短信成功", Toast.LENGTH_SHORT).show();
                        }else{
                            toast("code ="+ e.getErrorCode()+"\nmsg = "+e.getLocalizedMessage());
                        }
                    }
                });
                break;
        }
    }

    //数据添加
    private void doRegist(String name,String mobile, String pwd) {
        showProgressDialog(false);
        PersonInfo piInfo = new PersonInfo();
        piInfo.setMobilePhoneNumber(mobile);
        piInfo.setUsername(mobile);
        piInfo.setPassword(pwd);
        piInfo.setName(name);
        piInfo.signUp(new SaveListener<PersonInfo>() {
            @Override
            public void done(PersonInfo user,BmobException e) {
                stopProgressDialog();
                if(e==null){
                    Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    RegistActivity.this.finish();
                }else{
                    toast("注册失败:" + e.getMessage());
                }
            }
        });
    }
}

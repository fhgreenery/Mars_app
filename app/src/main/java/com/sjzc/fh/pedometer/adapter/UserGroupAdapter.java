package com.sjzc.fh.pedometer.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sjzc.fh.pedometer.database.bean.PersonInfo;
import com.sjzc.fh.pedometer.utils.ImageUtil;
import com.sjzc.fh.pedometer.utils.PhoneUtil;
import com.sjzc.fh.pedometer.R;
import java.util.ArrayList;


public class UserGroupAdapter extends BaseQuickAdapter<PersonInfo,BaseViewHolder> {

    private Context context;

    public UserGroupAdapter(int layoutResId, ArrayList<PersonInfo> dataList, Context context) {
        super(layoutResId, dataList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, PersonInfo piInfo) {

        TextView tvName = baseViewHolder.getView(R.id.tv_user_name);
        if(!TextUtils.isEmpty(piInfo.getName()) && !TextUtils.equals("用户",piInfo.getName())){
            tvName.setText(piInfo.getName());
        }else if(!TextUtils.isEmpty(piInfo.getUsername())){
            tvName.setText(PhoneUtil.encryptTelNum(piInfo.getUsername()));
        }

        ImageView ivPic = baseViewHolder.getView(R.id.iv_header);
        ImageUtil.setCircleImageView(ivPic,piInfo.getHeader_img_url(), R.mipmap.icon_user_def,context);
    }
}

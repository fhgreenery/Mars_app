package com.sjzc.fh.pedometer.ui.pushmsg;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sjzc.fh.pedometer.R;
import com.sjzc.fh.pedometer.database.bean.PushMessage;
import com.sjzc.fh.pedometer.utils.DateTimeUtils;

import java.util.ArrayList;


public class MsgListAdapter extends BaseQuickAdapter<PushMessage,BaseViewHolder> {

    public MsgListAdapter(int layoutResId, ArrayList<PushMessage> dataList) {
        super(layoutResId, dataList);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final PushMessage msg) {

        baseViewHolder.setText(R.id.tv_content,msg.getContent());
        baseViewHolder.setText(R.id.tv_time, DateTimeUtils.getStringDateTime(msg.getTime()));

    }
}

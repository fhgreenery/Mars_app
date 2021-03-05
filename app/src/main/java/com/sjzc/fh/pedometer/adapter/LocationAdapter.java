package com.sjzc.fh.pedometer.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sjzc.fh.pedometer.database.bean.LocationInfo;
import com.sjzc.fh.pedometer.R;
import java.util.ArrayList;


public class LocationAdapter extends BaseQuickAdapter<LocationInfo,BaseViewHolder> {

    public LocationAdapter(int layoutResId, ArrayList<LocationInfo> dataList) {
        super(layoutResId, dataList);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LocationInfo locationInfo) {

        baseViewHolder.setText(R.id.tv_location,locationInfo.getAddress());
        baseViewHolder.setText(R.id.tv_location_desc,locationInfo.getLocationdescribe());
        baseViewHolder.setText(R.id.tv_time , locationInfo.getCreatedAt());

    }
}

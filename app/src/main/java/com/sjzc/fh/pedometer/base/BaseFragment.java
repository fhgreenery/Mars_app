package com.sjzc.fh.pedometer.base;


import android.app.Activity;

import com.jph.takephoto.app.TakePhotoFragment;
import com.sjzc.fh.pedometer.widget.BaseProgressDialog;


/**
 * Fragment实现了TakePhoto接口
 */

public abstract class BaseFragment extends TakePhotoFragment {

    private BaseProgressDialog mProgressDialog = null;
    /**
     * @author wulee
     */

    public void showProgressDialog(Activity activity, BaseProgressDialog.OnCancelListener cancelListener, boolean cancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog = new BaseProgressDialog(activity);
        if (cancelListener != null) {
            mProgressDialog.setOnCancelListener(cancelListener);
        }
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.show();
    }

    public void showProgressDialog(Activity activity, boolean cancelable) {
        showProgressDialog(activity,null, cancelable);
    }

    public void stopProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.stop();
        }
        mProgressDialog = null;
    }
}

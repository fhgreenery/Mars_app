package com.sjzc.fh.pedometer.widget;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * 若加载内容为空，显示进度条对话框
 */
public class BaseProgressDialog {
    //声明变量，用于接收参数等等
    private Context mContext;
    private View mChild;
    private boolean isShowing = false;
    private boolean cancelable = true;
    private OnCancelListener mOnCancelListener;
    //构建该类时获取调用对象Context，
    public BaseProgressDialog(Context context) {
        this.mContext = context;
        initChild();
    }
    //展示页面布局 属性和view
    public void show() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //获取到的view就是程序不包括标题栏的部分
        FrameLayout content = (FrameLayout)((Activity) mContext).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        if (mChild == null && !initChild()) {
            return;
        }
        isShowing = true;
        content.addView(mChild, params);
    }

    private boolean initChild() {
        FrameLayout root = new FrameLayout(mContext);
        root.setClickable(true);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            View view = inflater.inflate(com.sjzc.fh.pedometer.R.layout.base_progress_dialog, null);
            if (view != null) {
                root.addView(view, params);
                this.mChild = root;
                return true;
            }
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void stop() {
        if (!isShowing) {
            return;
        }
        FrameLayout content = (FrameLayout)((Activity) mContext).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        content.removeView(mChild);
        isShowing = false;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean cancelable() {
        return cancelable;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public boolean cancel() {
        if (!cancelable || !isShowing) {
            return false;
        }
        if (mOnCancelListener != null) {
            mOnCancelListener.onCancel();
        }
        stop();
        return true;
    }
    //设置取消监听
    public void setOnCancelListener(OnCancelListener cancelListener) {
        this.mOnCancelListener = cancelListener;
    }


    public interface OnCancelListener {
        void onCancel();
    }

}

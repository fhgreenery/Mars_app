package com.sjzc.fh.pedometer.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sjzc.fh.pedometer.service.AccelerometerSensorListener;
import com.sjzc.fh.pedometer.service.AccelerometerSensorService;
import com.sjzc.fh.pedometer.ui.StepHistoryActivity;
import com.sjzc.fh.pedometer.ui.weather.WeatherActivity;
import com.sjzc.fh.pedometer.service.ScreenService;
import com.sjzc.fh.pedometer.utils.Pedometer;
import com.sjzc.fh.pedometer.widget.BaseTitleLayout;
import com.sjzc.fh.pedometer.widget.TitleLayoutClickListener;
import com.sjzc.fh.pedometer.R;

import java.io.File;
import java.text.NumberFormat;

import cn.bmob.v3.update.BmobUpdateAgent;


/**
 * 计步
 */
public class ZujiFragment extends MainBaseFrag implements View.OnClickListener,Chronometer.OnChronometerTickListener, RadioGroup.OnCheckedChangeListener {

    private View mRootView;
    private Context mContext;
    private BaseTitleLayout titleLayout;
//    private AnimArcButtons menuBtns;
    private Pedometer pedometer;
    private long  currServerTime;

    //step
    public static final String ACTION_ON_STEP_COUNT_CHANGE = "action_on_step_count_change";
    private static final String TAG = ZujiFragment.class.getSimpleName();
    SharedPreferences mySharedPreferences;
    SharedPreferences.Editor editor;

    private View view;

    //子线程
    private Thread thread;

    //UI控件
    private TextView tvPercent;
    private ProgressBar pbPercent;
    private TextView tvGoal;
    private TextView tvSteps;
    private Button btReset;

    private Chronometer cmPasstime;//计时器
    private Button btControl;

    private TextView tvCalorie;
    private TextView tvDistance;
    private TextView tvSpeed;

    private TextView tvSex;
    private TextView tvHeight;
    private TextView tvWeight;
    private TextView tvAge;
    private TextView tvSensitive;
    private TextView tvLightive;
    private TextView tvSteplen;
    private RadioGroup rgMode;
    private RadioButton rbStepNormal;
    private RadioButton rbStepPocket;
    public static TextView tvLight;
    ////
////    public static ChartView cvLight;
//    //选择菜单
    private AlertDialog.Builder dialog;
    private NumberPicker numberPicker;
    private Button bt_weather;
    private Button bt_record;
    //
//
    private float calorie;//卡路里
    private float distance;//路程
    private float speed;//速度

    private String sex;//性别
    private float height;//身高
    private float weight;//体重
    private float steplen;//步长
    private int age;//年龄
    private float sensitive;//灵敏度
    private float lightive;//感光度
    //
    private int steps;//步数
    private int seconds;//秒数
    public static float LIGHT_BORDER = 20;//感光度个数，即感光度
    public static boolean isInPocketMode = false;//是否是口袋模式
    public static boolean isOpenMap = false;//地图是否同时开启了

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (mRootView == null){
            mRootView = inflater.inflate(R.layout.location_list_main,container,false);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null){
            parent.removeView(mRootView);
        }
        mSubThread();
        initView(mRootView);
        addListener();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext.startService(new Intent(mContext,ScreenService.class));
        BmobUpdateAgent.forceUpdate(mContext);
        pedometer = new Pedometer(mContext);
        pedometer.register();
    }

    //线程
    // 计步时会触发，同时设置相关UI
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {//创建子线程进行数据更新
            super.handleMessage(msg);
            steps = AccelerometerSensorListener.CURRENT_SETP;//通过传感器获得当前步数
            float percent = steps * 100 / pbPercent.getMax();//根据当前步数和目标步数设置进度
            tvPercent.setText(String.valueOf(percent) + "%");
            pbPercent.setProgress(steps);// 进度条设置
            tvSteps.setText("" + steps); //步数更新

            calAddData();
        };
    };

    /**
     * 计算卡路里，路程，均速等
     */

    protected void calAddData() {
        // TODO Auto-generated method stub
        distance = steps * steplen / (100);
        tvDistance.setText(distance + "");
        float msSpeed;

        if (seconds == 0) {
            msSpeed = 0;
        } else {
            msSpeed = distance / seconds;
        }
        float kmhSpeed = (float) (3.6 * msSpeed);
        speed = kmhSpeed;
        //保留小数点后两位
//        NumberFormat nf = NumberFormat.getNumberInstance();
//        nf.setMaximumFractionDigits(2);
//        nf.format(speed)
        tvSpeed.setText( speed+ "");

        calorie = (float) (weight * steps * steplen * 0.01 * 0.01);

        tvCalorie.setText(calorie + "");

    }


    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i(TAG, "onStart");

        restorePersonalData();
        initPersonalData();

    }

    // 读取sharepreference数据初始化相关配置
    private void restorePersonalData() {
        // TODO Auto-generated method stub
        mySharedPreferences =getActivity().getSharedPreferences(
                "personalData", Activity.MODE_PRIVATE);
        // editor = mySharedPreferences.edit();

        sex = mySharedPreferences.getString("sex", "男");
        height = mySharedPreferences.getFloat("height", 175);
        weight = mySharedPreferences.getFloat("weight", 65);
        steplen = mySharedPreferences.getFloat("steplen", 80);
        age = mySharedPreferences.getInt("age", 24);
        sensitive = mySharedPreferences.getFloat("sensitive", 8);
        lightive = mySharedPreferences.getFloat("lightive", 10);
        LIGHT_BORDER = lightive;

    }

    // 根据配置数据初始化UI显示
    private void initPersonalData() {
        tvSex.setText(sex);
        tvHeight.setText(height + "");
        tvWeight.setText(weight + "");
        tvSteplen.setText(steplen + "");
        tvAge.setText(age + "");
        tvSensitive.setText(sensitive + "");
        AccelerometerSensorListener.SENSITIVITY = sensitive;
        tvLightive.setText(lightive + "");
        LIGHT_BORDER = lightive;
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    // 保存相关配置到sharepreference
    private void savePersonalData() {
        // TODO Auto-generated method stub
        Log.i(TAG, "save data");

        editor = mySharedPreferences.edit();

        editor.putString("sex", sex);
        editor.putFloat("height", height);
        editor.putFloat("weight", weight);
        editor.putFloat("steplen", steplen);
        editor.putInt("age", age);
        editor.putFloat("sensitive", sensitive);
        editor.putFloat("lightive", lightive);
        editor.commit();
    }


    private void mSubThread() {
        if (thread == null) {
            thread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (AccelerometerSensorService.isRun) {
                            Message msg = new Message();
                            handler.sendMessage(msg);
                        }

                    }
                }
            });
            thread.start();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
//       if(menuBtns.isOpen())
//            menuBtns.closeMenu();

    }

    private void addListener() {
        titleLayout.setOnTitleClickListener(new TitleLayoutClickListener() {
            @Override
            public void onLeftClickListener() {
                super.onLeftClickListener();
                if(mListener != null){
                    mListener.OpenLeftMenu();
                }
            }
            @Override
            public void onRightImg1ClickListener() {
                super.onRightImg1ClickListener();
            }
        });

      /* menuBtns.setOnButtonClickListener((v, id) -> {
            switch (id){
                case 0:
                    startActivity(new Intent(mContext,WeatherActivity.class).putExtra("curr_time",currServerTime));
                    break;
                case 1:
                    startActivity(new Intent(mContext,StepHistoryActivity.class));
                    break;
                case 2:
                    break;
            }
        });
        */

    }

    private void initView(View view) {
        ImageView topHeaderIv = (ImageView) view.findViewById(R.id.ivstatebar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            topHeaderIv.setVisibility(View.VISIBLE);
        } else {
            topHeaderIv.setVisibility(View.GONE);
        }
        titleLayout = (BaseTitleLayout) view.findViewById(R.id.titlelayout);

//        menuBtns = (AnimArcButtons) view.findViewById(R.id.arc_menu_button);

        //step
        tvPercent = (TextView) view.findViewById(R.id.tv_percent);
        pbPercent = (ProgressBar) view.findViewById(R.id.pb_percent);
        tvGoal = (TextView) view.findViewById(R.id.tv_goal);
        tvGoal.setOnClickListener(this);
        tvSteps = (TextView) view.findViewById(R.id.tv_steps);
        tvSteps.setOnClickListener(this);
        btReset = (Button) view.findViewById(R.id.bt_reset);
        btReset.setOnClickListener(this);
        cmPasstime = (Chronometer) view.findViewById(R.id.cm_passtime);
        btControl = (Button) view.findViewById(R.id.bt_control);
        btControl.setOnClickListener(this);
        tvCalorie = (TextView) view.findViewById(R.id.tv_calorie);
        tvDistance = (TextView) view.findViewById(R.id.tv_distance);
        tvSpeed = (TextView) view.findViewById(R.id.tv_speed);

        tvSex = (TextView) view.findViewById(R.id.tv_sex);
        tvSex.setOnClickListener(this);
        tvHeight = (TextView) view.findViewById(R.id.tv_height);
        tvHeight.setOnClickListener(this);
        tvWeight = (TextView) view.findViewById(R.id.tv_weight);
        tvWeight.setOnClickListener(this);
        tvAge = (TextView) view.findViewById(R.id.tv_age);
        tvAge.setOnClickListener(this);
        tvSensitive = (TextView) view.findViewById(R.id.tv_sensitive);
        tvSensitive.setOnClickListener(this);
        tvLightive = (TextView) view.findViewById(R.id.tv_lightive);
        tvLightive.setOnClickListener(this);
        tvSteplen = (TextView) view.findViewById(R.id.tv_steplen);
        tvSteplen.setOnClickListener(this);
        rgMode = (RadioGroup)  view.findViewById(R.id.step_mode);
        rgMode.setOnCheckedChangeListener(this);
        rbStepNormal = (RadioButton) view.findViewById(R.id.step_normal);
        rbStepPocket = (RadioButton) view.findViewById(R.id.step_pocket);
//        tvLight = (TextView) findViewById(R.id.tv_light);
//
//        cvLight = (ChartView) view.findViewById(R.id.cv_light);

        pbPercent.setMax(10000);
        cmPasstime.setOnChronometerTickListener(this);
        bt_weather = (Button) view.findViewById(R.id.btn_weather);
        bt_weather.setOnClickListener(this);
        bt_record = (Button) view.findViewById(R.id.bt_record);
        bt_record.setOnClickListener(this);
    }
    //懒加载
   /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        } else {

        }
    }
*/

    /**
     * 分享功能
     */
    public void share(String imgPath, String msg) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        startActivity(Intent.createChooser(intent, "分享到"));//开启意图
    }

    /**
     * 编辑分享内容
     */

    public void shareMsg(final String imgPath) {
        final EditText editText = new EditText(getActivity());
        final StringBuffer msg = new StringBuffer();
        msg.append("你们在哪里呢？我刚刚走了" + steps + "步，总共" + distance + "米，消耗了" + calorie + "千卡卡路里！");

        editText.setText(msg.toString());
        new AlertDialog.Builder(getActivity()).setTitle("分享内容:")
                .setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        msg.setLength(0);
                        msg.append(editText.getText().toString().trim());
                        share(imgPath, msg.toString());
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(),
                AccelerometerSensorService.class);

        switch (view.getId()) {
            case R.id.tv_goal:
                // 设置目标
                final EditText editText = new EditText(getActivity());
                editText.setText(tvGoal.getText());
                new AlertDialog.Builder(getActivity())
                        .setTitle("请输入")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        tvGoal.setText(editText.getText()
                                                .toString().trim());
                                        pbPercent.setMax(Integer.parseInt(editText
                                                .getText().toString().trim()));
                                    }
                                }).setNegativeButton("取消", null).show();
                break;
            case R.id.bt_reset:
                // 重置按钮
                reset();
                if (isOpenMap) {
                    TraceFragment.bt_ctrlTrack.performClick();//同时关闭轨迹记录
                }
                break;
            case R.id.bt_control:
                // 控制按钮，开始，暂停，继续
                if (btControl.getText().equals("开始")) {
                    Toast.makeText(getActivity(), "已同时开启轨迹记录，若不需要可右滑点击停止",
                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "为获得更好的效果，请确认你的体重，步长等信息是正确的...", Toast.LENGTH_SHORT).show();
                    if (!TraceFragment.isRecording) {
                        TraceFragment.showflag = false;
                        TraceFragment.bt_ctrlTrack.performClick();// 模拟点击
                        TraceFragment.showflag = true;
                        isOpenMap = true;
                    }
                    getActivity().startService(intent);//开启加速传感器服务
                    cmPasstime.setBase(SystemClock.elapsedRealtime());//获取从设备boot后经历的时间值
                    cmPasstime.start();
                    btControl.setText("暂停");
                } else if (btControl.getText().equals("暂停")) {
                    getActivity().stopService(intent);
                    cmPasstime.stop();
                    btControl.setText("继续");
                } else if (btControl.getText().equals("继续")) {
                    getActivity().startService(intent);
                    cmPasstime.start();
                    btControl.setText("暂停");
                }

                break;
            case R.id.tv_sex:
                // 设置性别
                dialog = new AlertDialog.Builder(getActivity());
                final String[] sexlist = { "男", "女" };
                // 设置一个下拉的列表选择项
                dialog.setItems(sexlist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvSex.setText(sexlist[which]);
                        sex = sexlist[which];
                        savePersonalData();
                    }
                });
                dialog.show();
                break;
            case R.id.tv_age:
                // 设置年龄
                dialog = new AlertDialog.Builder(getActivity());
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(150);
                numberPicker.setValue(Integer.parseInt(tvAge.getText().toString()
                        .trim()));
                numberPicker.setMinValue(1);
                dialog.setView(numberPicker);
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                tvAge.setText(numberPicker.getValue() + "");
                                age = numberPicker.getValue();
                                savePersonalData();
                            }
                        });
                dialog.show();
                break;
            case R.id.tv_height:
                // 设置身高
                dialog = new AlertDialog.Builder(getActivity());
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(200);
                numberPicker.setValue((int) Float.parseFloat(tvHeight.getText()
                        .toString().trim()));
                numberPicker.setMinValue(20);
                dialog.setView(numberPicker);
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                tvHeight.setText(numberPicker.getValue() + "");
                                height = numberPicker.getValue();
                                savePersonalData();
                            }
                        });
                dialog.show();
                break;
            case R.id.tv_weight:
                // 设置体重
                dialog = new AlertDialog.Builder(getActivity());
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(200);
                numberPicker.setValue((int) Float.parseFloat(tvWeight.getText()
                        .toString().trim()));
                numberPicker.setMinValue(20);
                dialog.setView(numberPicker);
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                tvWeight.setText(numberPicker.getValue() + "");
                                weight = numberPicker.getValue();
                                savePersonalData();
                            }
                        });
                dialog.show();
                break;
            case R.id.tv_steplen:
                // 设置步长
                dialog = new AlertDialog.Builder(getActivity());
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(100);
                numberPicker.setValue((int) Float.parseFloat(tvSteplen.getText()
                        .toString().trim()));
                numberPicker.setMinValue(15);
                dialog.setView(numberPicker);
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                tvSteplen.setText(numberPicker.getValue() + "");
                                steplen = numberPicker.getValue();
                                savePersonalData();

                            }
                        });
                dialog.show();
                break;
            case R.id.tv_sensitive:
                // 设置灵敏度
                dialog = new AlertDialog.Builder(getActivity());
                numberPicker = new NumberPicker(getActivity());
                numberPicker.setFocusable(true);
                numberPicker.setFocusableInTouchMode(true);
                numberPicker.setMaxValue(10);
                numberPicker.setMinValue(1);
                numberPicker.setValue((int) Float.parseFloat(tvSensitive.getText()
                        .toString().trim()));
                dialog.setView(numberPicker);
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                tvSensitive.setText(numberPicker.getValue() + "");
                                sensitive = numberPicker.getValue();
                                AccelerometerSensorListener.SENSITIVITY = sensitive;
                                savePersonalData();
                            }
                        });
                dialog.show();
                break;
            case R.id.tv_lightive:
                // 设置光敏度
                final EditText editText1 = new EditText(getActivity());
                editText1.setText(tvLightive.getText());
                // 设置类型
                new AlertDialog.Builder(getActivity())
                        .setTitle("请输入")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText1)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        tvLightive.setText(editText1.getText()
                                                .toString().trim());
                                        lightive = Float.parseFloat(editText1
                                                .getText().toString().trim());
                                        LIGHT_BORDER = lightive;
                                        savePersonalData();

                                    }
                                }).setNegativeButton("取消", null).show();
                break;
            case R.id.tv_steps:
                // 分享
                if (steps >= 0) {
                    shareMsg(null);
                }
                break;
            case R.id.btn_weather:
                startActivity(new Intent(mContext,WeatherActivity.class).putExtra("curr_time",currServerTime));
                break;
            case R.id.bt_record:
                startActivity(new Intent(mContext,StepHistoryActivity.class));
                break;

        }

    }

    /**
     * 重置步数等信息
     */

    private void reset() {
        Intent intent = new Intent(getActivity(),
                AccelerometerSensorService.class);
        getActivity().stopService(intent);
        AccelerometerSensorListener.reset();
        steps = 0;
        seconds = 0;

        tvPercent.setText("0.0");
        pbPercent.setProgress(0);
        tvGoal.setText("1000");
        tvSteps.setText("0.0");
        // tvPasstime.setText("00:00:00");
        cmPasstime.setBase(SystemClock.elapsedRealtime());
        cmPasstime.stop();
        btControl.setText("开始");
        tvCalorie.setText("0.0");
        tvDistance.setText("0.0");
        tvSpeed.setText("0.0");

//        adjustLightive();

    }

    /**
     * 格式化时间显示
     */


    public void onChronometerTick(Chronometer arg0) {
        // TODO Auto-generated method stub
        seconds++;
        cmPasstime.setText(formatseconds());
    }

    public String formatseconds() {
        String hh = seconds / 3600 > 9 ? seconds / 3600 + "" : "0" + seconds
                / 3600;
        String mm = (seconds % 3600) / 60 > 9 ? (seconds % 3600) / 60 + ""
                : "0" + (seconds % 3600) / 60;
        String ss = (seconds % 3600) % 60 > 9 ? (seconds % 3600) % 60 + ""
                : "0" + (seconds % 3600) % 60;

        return hh + ":" + mm + ":" + ss;
    }

    /**
     * 调整光敏度
     */

//    public void adjustLightive() {
//        if (tvLightive == null) {
//            return;
//        }
//        if (LightSensorService.LIGHT == 0) {
//            return;
//        }
//        lightive = LightSensorService.LIGHT;
//        LIGHT_BORDER = lightive;
//        tvLightive.setText(lightive + "");
//        savePersonalData();
//    }

    /*
     * 计步模式改变时触发
     */

    @Override
    public void onCheckedChanged(RadioGroup group, int checkID) {

        if (checkID == rbStepPocket.getId()) {// 口袋模式
            isInPocketMode = true;

            // 自动调整光敏度
//            adjustLightive();

            Toast.makeText(getActivity(), "口袋模式已开启，光敏度已自动修正。",
                    Toast.LENGTH_LONG).show();
        } else if (checkID == rbStepNormal.getId()) {// 普通模式
            isInPocketMode = false;
        }

    }


    @Override
    public void onFragmentFirstSelected() {
        showProgressDialog(getActivity(),true);
      //  getLocationList(0, STATE_REFRESH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(pedometer!= null){
            pedometer.unRegister();
            pedometer = null;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public OnMenuBtnClickListener mListener;
    public  interface OnMenuBtnClickListener{
        //打开侧栏
        void OpenLeftMenu();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnMenuBtnClickListener){
            mListener = ((OnMenuBtnClickListener)activity);
        }
    }
}


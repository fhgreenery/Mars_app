package com.sjzc.fh.pedometer.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.common.LogUtil;
import com.sjzc.fh.pedometer.App;
import com.sjzc.fh.pedometer.R;
import com.sjzc.fh.pedometer.base.BaseActivity;
import com.sjzc.fh.pedometer.database.DBHandler;
import com.sjzc.fh.pedometer.database.bean.PersonInfo;
import com.sjzc.fh.pedometer.entity.Constant;
import com.sjzc.fh.pedometer.entity.SignInfo;
import com.sjzc.fh.pedometer.service.UploadLocationService;
import com.sjzc.fh.pedometer.ui.fragment.CircleFragment;
import com.sjzc.fh.pedometer.ui.fragment.MainBaseFrag;
import com.sjzc.fh.pedometer.ui.fragment.MusicFragment;
import com.sjzc.fh.pedometer.ui.fragment.TraceFragment;
import com.sjzc.fh.pedometer.ui.fragment.UserGroupFragment;
import com.sjzc.fh.pedometer.ui.fragment.ZujiFragment;
import com.sjzc.fh.pedometer.ui.pushmsg.PushMsgListActivity;
import com.sjzc.fh.pedometer.utils.AppUtils;
import com.sjzc.fh.pedometer.utils.ConfigKey;
import com.sjzc.fh.pedometer.utils.ImageUtil;
import com.sjzc.fh.pedometer.utils.LocationUtil;
import com.sjzc.fh.pedometer.utils.PhoneUtil;
import com.sjzc.fh.pedometer.widget.CoolImageView;
import com.zhouwei.blurlibrary.EasyBlur;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;

import static cn.bmob.v3.BmobUser.getCurrentUser;

/**
 * 不同fragment的切换
 * 设置侧滑显示个人中心布局
 */

public class MainNewActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener,NavigationView.OnNavigationItemSelectedListener,ZujiFragment.OnMenuBtnClickListener {
    //抽屉布局
    private  DrawerLayout mDrawerLayout;

    //fragment切换 RadioGroup和viewPager两种
    private MainFPagerAdaper mainFPagerAdaper;
    private ViewPager mViewPager;
    private RadioGroup mRg;

    //侧滑菜单 HeaderLayout与menu
    private NavigationView navigationView;
    private View menuHeaderView;
    private ImageView ivHeader;
    private TextView mTvName;
    private TextView mTvMobile;
    private TextView mTvSign;
    private TextView mTvIntegral;
    private TextView tvNewMsg;

    // 标示了当前位置
    private final int POS_ONE = 0, POS_TWO = 1, POS_THREE = 2, POS_FOUR = 3, POS_FIVE = 4;//
    // 是否已经展示过Fragment，默认是false，没有展示过
    private boolean[] hasFragSelected = new boolean[POS_FIVE + 1];
    private MainBaseFrag[] mainBaseFrags = new MainBaseFrag[POS_FIVE + 1];

    private long mLastClickReturnTime = 0l; // 记录上一次点击返回按钮的时间
    private Bitmap finalBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.sjzc.fh.pedometer.R.layout.main_new_activity);
        //页面显示 数据绑定 设置监听
        initView();
        initData();
        addListner();
        //页面切换
       int pos = 0;
        try {
            String posStr = App.aCache.getAsString(ConfigKey.KEY_MAIN_TAB_POS);
            if(!TextUtils.isEmpty(posStr))
                pos = Integer.parseInt(posStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        initSelectTab(pos);
        //sendFragmentFirstSelectedMsg(pos);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        initMenuHeaderInfo();
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                if (CloseMenu())
                    return true;
                if(System.currentTimeMillis() - mLastClickReturnTime > 1000L) {
                    mLastClickReturnTime = System.currentTimeMillis();
                    toast("再按一次退出程序");
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloseMenu();
        mDrawerLayout = null;
        if(!finalBitmap.isRecycled() ){
            finalBitmap.recycle();//回收图片所占的内存
            finalBitmap=null;
            System.gc();  //提醒系统及时回收
        }
    }
   private void initMenuHeaderInfo() {
       PersonInfo personalInfo = DBHandler.getCurrPesonInfo();
       if(null != personalInfo){
           if(!TextUtils.isEmpty(personalInfo.getName()))
               mTvName.setText(personalInfo.getName());
           else
               mTvName.setText("用户");
           mTvMobile.setText(PhoneUtil.encryptTelNum(personalInfo.getMobile()));
           ImageUtil.setCircleImageView(ivHeader,personalInfo.getHeader_img_url(), R.mipmap.icon_user_def,this);
       }
   }

    private void initView() {
        mDrawerLayout = (DrawerLayout)findViewById(com.sjzc.fh.pedometer.R.id.id_drawerLayout);
        //mDrawerLayout.setScrimColor(0x80000000); 设置边缘大小

        mViewPager = (ViewPager) findViewById(R.id.mviewpager);
        mRg = (RadioGroup) findViewById(R.id.mnc_rg);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //自定义menu菜单icon和title颜色
        int[][] states = new int[][]{
                new int[]{ -android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };
        final int[] colors = new int[]{
                ContextCompat.getColor(this, R.color.ctv_black_2),
                ContextCompat.getColor(this,R.color.colorAccent)
        };
        ColorStateList csl = new ColorStateList(states, colors);

        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);

       menuHeaderView = navigationView.inflateHeaderView(R.layout.nav_menu_header);
        //头像
        ivHeader = (ImageView) menuHeaderView.findViewById(R.id.circle_img_header);
        //用户名
        mTvName = (TextView) menuHeaderView.findViewById(R.id.tv_name);
        //手机号
        mTvMobile = (TextView) menuHeaderView.findViewById(R.id.tv_mobile);
        //打卡
        mTvSign = (TextView) menuHeaderView.findViewById(R.id.tv_sign);

        //天数
        mTvIntegral= (TextView) menuHeaderView.findViewById(R.id.tv_integral);
        LinearLayout llmsg = (LinearLayout) navigationView.getMenu().findItem(R.id.item_msg_board).getActionView();
        tvNewMsg = (TextView) llmsg.findViewById(R.id.tv_new_msg);

        final PersonInfo piInfo = BmobUser.getCurrentUser(PersonInfo.class);
        if(piInfo == null)
            return;
        mTvIntegral.setText(piInfo.getIntegral()+"");

        //签到实现
        String currDateStr = App.aCache.getAsString(Constant.KEY_CURR_SERVER_TIME);
        BmobQuery<SignInfo> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("personInfo",piInfo);

        BmobQuery<SignInfo> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("date",currDateStr);

        List<BmobQuery<SignInfo>> andQuerys = new ArrayList<>();
        andQuerys.add(query1);
        andQuerys.add(query2);

        BmobQuery<SignInfo> query = new BmobQuery<>();
        query.and(andQuerys);

        query.count(SignInfo.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if(e == null && count > 0){
                    mTvSign.setTextColor(ContextCompat.getColor(MainNewActivity.this, R.color.baseGrayNor));
                    mTvSign.setText("打卡成功");
                    mTvSign.setEnabled(false);
                }else{
                    mTvSign.setEnabled(true);
                }
            }
        });

        mTvSign.setOnClickListener(view -> Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long time, BmobException e) {
                if(e == null){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    final String date = formatter.format(new Date(time * 1000L));
                    LogUtil.i("bmob","当前服务器时间为:" + date);

                    SignInfo signInfo = new SignInfo();
                    signInfo.hasSign = true;
                    signInfo.date = date;
                    signInfo.personInfo = piInfo;
                    signInfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e == null){

                                final int integral = piInfo.getIntegral();
                                piInfo.setIntegral(integral+1);
                                piInfo.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e == null){
                                            App.aCache.put(Constant.KEY_SIGN_DATE,date);
                                            mTvSign.setTextColor(ContextCompat.getColor(MainNewActivity.this, com.sjzc.fh.pedometer.R.color.baseGrayNor));
                                            mTvSign.setText("已打卡");
                                            mTvSign.setEnabled(false);

                                            mTvIntegral.setText(Integer.valueOf(integral+1)+"");
                                           /* toast("打卡成功");
                                            if(integral==7){
                                                toast("您已连续坚持一周，再接再厉！");
                                            }else if(integral==14){
                                                toast("您已连续坚持两周，很棒呦！");
                                            }else if(integral==30){
                                                toast("您已连续坚持两周30天，加油！");
                                            }
                                            */
                                        }else{
                                            mTvSign.setEnabled(true);

                                            toast("打卡失败");
                                        }
                                    }
                                });

                            }else{
                                toast("打卡失败");
                                mTvSign.setEnabled(true);
                            }
                        }
                    });
                }else{
                    LogUtil.i("bmob","获取服务器时间失败:" + e.getMessage());
                }
            }
        }));
    }
    //导航栏菜单设置
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id ==  R.id.item_loginout) {
            showLogoutDialog();
        }else  if (id ==  R.id.item_pushmsg) {
            startActivity(new Intent(this,PushMsgListActivity.class));
        }else  if (id ==  R.id.item_msg_board) {
            PersonInfo personInfo = getCurrentUser(PersonInfo.class);
            if(personInfo != null){
                startActivity(new Intent(this,MessageBoardActivity.class).putExtra("piInfo",personInfo));
            }
        }else  if (id == R.id.item_feedback) {
            startActivity(new Intent(this,FeedBackActivity.class));


//        }else  if (id ==  R.id.item_setting) {
//            startActivity(new Intent(this,SettingActivity.class));
        }else  if (id ==  R.id.item_about) {
            startActivity(new Intent(this,AboutMeActivity.class));
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定要退出吗？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            App.aCache.put("has_login","no");
            LocationUtil.getInstance().stopGetLocation();
            AppUtils.AppExit(MainNewActivity.this);
            PersonInfo.logOut();
            startActivity(new Intent(MainNewActivity.this,LoginActivity.class));
            UploadLocationService.stopService();
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 关闭Menu
     */
    public boolean CloseMenu() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return true;
        }
        return false;
    }

    private void initData() {
        mainFPagerAdaper = new MainFPagerAdaper(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mainFPagerAdaper);//显示当前fragment
    }

    private void addListner() {
        mViewPager.setOnPageChangeListener(this);
        mRg.setOnCheckedChangeListener(this);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
            @Override
            public void onDrawerOpened(View drawerView) {
               // mHandler.sendEmptyMessage(MSG_QUERY_MESSAGE_COUNT);
            }
            @Override
            public void onDrawerClosed(View drawerView) {

            }
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        ivHeader.setOnClickListener(view -> startActivity(new Intent(MainNewActivity.this,PersonalInfoActivity.class)));
    }

   //实现页面切换
   //viewPager滑动显示fragment布局
    private void initSelectTab(int pos) {
        if (pos < POS_ONE || pos > POS_FIVE)
            return;
        RadioButton rb = findRadioButtonByPos(pos);//获取按钮控件
        if (rb != null) {
            rb.setChecked(true);//rb已经被选中
        }
        mViewPager.setCurrentItem(pos, false);
    }
    //选中按钮显示fragment布局
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rbnt_zuji:
                if (POS_ONE != mViewPager.getCurrentItem()) {
                    mViewPager.setCurrentItem(POS_ONE, false);
                }
                break;
            case R.id.rbnt_joke:
                if (POS_TWO != mViewPager.getCurrentItem()) {
                    mViewPager.setCurrentItem(POS_TWO, false);
                }
                break;
            case R.id.rbnt_user_group:
                if (POS_THREE != mViewPager.getCurrentItem()) {
                    mViewPager.setCurrentItem(POS_THREE, false);
                }
                break;
            case R.id.rbnt_news:
                if (POS_FOUR != mViewPager.getCurrentItem()) {
                    mViewPager.setCurrentItem(POS_FOUR, false);
                }
                break;
            case R.id.rbnt_circle:
                if (POS_FIVE != mViewPager.getCurrentItem()) {
                    mViewPager.setCurrentItem(POS_FIVE, false);
                }
                break;
        }
    }
    private RadioButton findRadioButtonByPos(int position) {
        switch (position) {
            case POS_ONE:
                return (RadioButton) mRg.findViewById(R.id.rbnt_zuji);//计步
            case POS_TWO:
                return (RadioButton) mRg.findViewById(R.id.rbnt_joke);//轨迹
            case POS_THREE:
                return (RadioButton) mRg.findViewById(R.id.rbnt_user_group);//好友
            case POS_FOUR:
                return (RadioButton) mRg.findViewById(R.id.rbnt_news);//音乐
            case POS_FIVE:
                return (RadioButton) mRg.findViewById(R.id.rbnt_circle);//运动圈
        }
        return null;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton rb = findRadioButtonByPos(position);
        if (rb != null) {
            rb.setChecked(true);
        }
        App.aCache.put(ConfigKey.KEY_MAIN_TAB_POS, position+"");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }




    /**
     * 打开左侧Menu的监听事件
     */
    @Override
    public void OpenLeftMenu() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    /**
     * 页面适配器
     */
    public class MainFPagerAdaper extends FragmentPagerAdapter {

        public MainFPagerAdaper(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment retFragment = null;
            switch (position) {
                case 0:
                    retFragment = new ZujiFragment();
                    mainBaseFrags[0] = (ZujiFragment) retFragment;

                    break;
                case 1:
                    retFragment = new TraceFragment();
                    mainBaseFrags[1] = (TraceFragment) retFragment;
                    break;
                case 2:
                    retFragment = new UserGroupFragment();
                    mainBaseFrags[2] = (UserGroupFragment) retFragment;
                    break;
                case 3:
                    retFragment = new MusicFragment();
                    mainBaseFrags[3] = (MusicFragment) retFragment;
                    break;
                case 4:
                    retFragment = new CircleFragment();
                    mainBaseFrags[4] = (CircleFragment) retFragment;
                    break;
            }
            return retFragment;
        }

        @Override
        public int getCount() {
            return POS_FIVE + 1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }

}

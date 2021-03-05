package com.sjzc.fh.pedometer.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.sjzc.fh.pedometer.R;
import com.sjzc.fh.pedometer.service.MusicService;
import com.sjzc.fh.pedometer.ui.List_main;
import android.view.View.OnClickListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public class MusicFragment extends MainBaseFrag  {
    private View view;
    private static int p=0;
    // private EditText mEditText;
    private  static Spinner mSpinner;
    private  static ArrayAdapter mAdapter;
    private  static TextView mName;
    private  static SeekBar mSeekBar;
    private static  String path;
    private  static MediaPlayer mMediaPlayer=new MediaPlayer();//多媒体播放类
    private  static boolean pause; //标记是否暂停
    private  static int position;//用于记录播放进度
    private static List<String> list;
    private  static List<String> name;
    private static AdapterViewFlipper mViewFlipper;
    private  static TextView mStarTime;
    private  static TextView mStopTime;
    private Intent intent;//用于开启前台服务

    private Deque<MediaStore.Audio> data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_play, container, false);

        list=new ArrayList<>();//用于存放音乐文件路径
        name=new ArrayList<String>();//用于存放歌曲名字

        intent=new Intent(getActivity(),MusicService.class);
        getActivity().startService(intent);//开启前台服务

        mName= (TextView) view.findViewById(R.id.tv_name);
        mSeekBar= (SeekBar)  view.findViewById(R.id.seekbar);
        mStarTime= (TextView)  view.findViewById(R.id.starTime);
        mStopTime= (TextView)  view.findViewById(R.id.stopTime);
        //设置电话监听
        TelephonyManager telephonyManager= (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);  //监听电话
        telephonyManager.listen(new  MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);

        search();
        init();
        mMediaPlayer.setOnCompletionListener(new mMediaPlayersetOnCompletionListener());

        //由于以下带码用到search()函数得到的数据所有要在调用search()之后执行，否则出错 //可能是由于线程的缘故
        mSpinner=(Spinner) view.findViewById(R.id.spinner);
        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,name);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        mSpinner.setAdapter(mAdapter);
        mSpinner.setSelection(0, false);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        mSpinner.setOnItemSelectedListener(new SpinnerListener());
        return view;
    }


    private String toTime(int time){  //将获取的歌曲时间毫秒转化为分钟
        int minute = time / 1000 / 60;
        int s = time / 1000 % 60;
        String mm = null;
        String ss = null;
        if(minute<10)mm = "0" + minute;
        else mm = minute + "";
        if(s <10)ss = "0" + s;
        else ss = "" + s;
        return mm + ":" + ss;
    }


    class mMediaPlayersetOnCompletionListener implements MediaPlayer.OnCompletionListener{ //一首歌播放结束监听

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            //自动循环播放
            if (p == (list.size() - 1)) p = 0;
            path = list.get(++p);
            if (path != null) {
                if (handler.post(updateThread)) {

                } else {
                    handler.post(updateThread);  //如果没开启多线程则开启  以更新进度条
                }
                play();
                mName.setText(name.get(p));
            }else {

                Toast.makeText(getActivity(),"没有歌曲了哦！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    Handler handler = new Handler(); //多线程
    Runnable updateThread = new Runnable(){
        public void run() {
            //获得歌曲现在播放位置并设置成播放进度条的值
            int max=mSeekBar.getMax();
            mSeekBar.setProgress((int)(max*(mMediaPlayer.getCurrentPosition()/(float) mMediaPlayer.getDuration())));//将歌曲进度转化为进度条进度
            mStarTime.setText(toTime(mMediaPlayer.getCurrentPosition())); //设置进度时间
            mStopTime.setText(toTime(mMediaPlayer.getDuration())); //设置最长时间
            //每次延迟100毫秒再启动线程
            handler.postDelayed(updateThread, 100); //单位毫秒
        }
    };



    private void init(){

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  //seekBar进度监听
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // fromUser判断是用户改变的滑块的值
                if (fromUser == true) {
                    position=progress;
                    int po=mSeekBar.getProgress();
                    mMediaPlayer.seekTo(po);

                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { //拖动结束调用
                seekBar.setProgress(seekBar.getProgress());

                int max=mMediaPlayer.getDuration();// 歌曲进度的最大值

                mMediaPlayer.seekTo((int)(max*(seekBar.getProgress()/(double)seekBar.getMax())));
            }
        });  //seekBar监听结束
    }//init


    public void search(){
        Cursor mAudioCursor = getActivity().getContentResolver().query(  //获取本地音乐清单
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,// 字段　没有字段　就是查询所有信息　相当于SQL语句中的　“ * ”
                null, // 查询条件
                null, // 条件的对应?的参数
                MediaStore.Audio.AudioColumns.TITLE);// 排序方式

        // 循环输出歌曲的信息
        //List<Map<String, Object>> mListData = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < mAudioCursor.getCount(); i++) {
            mAudioCursor.moveToNext();

            // 找到歌曲标题和总时间对应的列索引


            String filePath1=mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)); //得到歌曲路径
            Log.i("路径-------->",filePath1);
            list.add(filePath1);
            String str=getFileName(filePath1);
            String tilte= mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)); //歌曲名称
            Log.i("str----->",tilte);
            name.add(str); //保持歌曲名


        }

    }

    private final class MyPhoneListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {  //在来点时暂停播放，通话结束继续播放
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING: //来电话了
                    if(mMediaPlayer.isPlaying()){
                        position=mMediaPlayer.getCurrentPosition();//获取当前播放进度
                        mMediaPlayer.stop();//当播放器activity不在前台时暂停播放
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE: //结束通话
                    if(position>0&&path!=null){
                        play();
                        mMediaPlayer.seekTo(position); //
                        position=0;
                    }
                    break;
            }//switch
        }
    } //class

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //在这里处理加载数据等操作
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void play() {  //自定义播放函数
        mMediaPlayer.reset();//把各项参数恢复到初始状态
//        pauseButton.setImageResource(R.drawable.star);
        try {
            if(path!=null) {
                mMediaPlayer.setDataSource(path);//设置路径
                mMediaPlayer.prepare();//缓冲
                mMediaPlayer.setOnPreparedListener(new PrepareListener()); //传入position
            }else
            {
                Toast.makeText(getActivity(),"播放列表为空",Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final class PrepareListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) { //缓冲完时调用

            mMediaPlayer.start();//真正实现开始播放
        }
    }

    public String getFileName(String pathandname){ //通过绝对路径的到文件名

        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
        }
    }
//监听
@Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ImageButton upButton = (ImageButton) view.findViewById(R.id.upButton);
    ImageButton nextButton = (ImageButton) view.findViewById(R.id.nextButton);
    ImageButton pauseButton = (ImageButton) view.findViewById(R.id.pauseButton);
    ImageButton playButton = (ImageButton) view.findViewById(R.id.playButton);
    upButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(list!=null){
                if (p == 0) p = list.size();
                path = list.get(--p);
                if (path != null) {
                    if (handler.post(updateThread)) {

                    } else {
                        handler.post(updateThread);  //如果没开启多线程则开启  以更新进度条
                    }
                    play();
                    mName.setText(name.get(p));
                }else{
                    Toast.makeText(getActivity(),"没有歌曲了哦！",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(),"当前文件夹为空！",Toast.LENGTH_SHORT).show();
            }
        }
    });

    //next
   nextButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(list!=null) {
                if (p == (list.size() - 1)) p = 0;
                path = list.get(++p);
                if (path != null) {
                    if (handler.post(updateThread)) {

                    } else {
                        handler.post(updateThread);  //如果没开启多线程则开启  以更新进度条
                    }
                    play();
                    mName.setText(name.get(p));
                } else {
                    Toast.makeText(getActivity(), "没有歌曲了哦！", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getActivity(),"文件夹为空！",Toast.LENGTH_SHORT).show();
            }
        }
    });
    //pause start
    pauseButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();//暂停
                pause = true;//暂停标记
//                pauseButton.setImageResource(R.drawable.stop);

            } else {
                if (pause) { //如果被暂停过则继续播放
                    mMediaPlayer.start();
                    pause = false;
//                    pauseButton.setImageResource(R.drawable.star);
                }
            }
        }
    });
    //play
    playButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(list!=null) {
                path = list.get(p);
                play();//播放
                handler.post(updateThread);//启动线程
            }else {
                Toast.makeText(getActivity(),"当前文件夹还没有歌曲哦！",Toast.LENGTH_SHORT).show();
            }
        }
    });

}

    private class SpinnerListener implements AdapterView.OnItemSelectedListener { //spinner点击监听

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            p=i;
            path = list.get(p);
            if (handler.post(updateThread)) {

            } else {
                handler.post(updateThread);  //如果没开启多线程则开启  以更新进度条
            }
            play();
            mName.setText(name.get(p));

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }



   public void onActivityResult(int requestCode, int resultCode, Intent data) {  //接收数据
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==2){
            // na=data.getStringArrayExtra("da"); //得到歌词名数组
            path=data.getStringExtra("data");
            if (path != null) {
                play();
                mName.setText(getFileName(path));
//                mViewFlipper.startFlipping();//开始自动播放图片
                if (handler.post(updateThread)) {

                } else {
                    handler.post(updateThread);  //如果没开启多线程则开启  以更新进度条
                }
            }
        }
    }

    public void returnN(View v){ //点击事件  发送数据
        Intent in=new Intent(getActivity(),List_main.class);
        startActivityForResult(in,1);
    }
    @Override
    public void onFragmentFirstSelected() {
        showProgressDialog(getActivity(),true);
    }
}

package com.sjzc.fh.pedometer.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sjzc.fh.pedometer.R;
import com.sjzc.fh.pedometer.ui.fragment.MusicFragment;

import java.util.ArrayList;
import java.util.List;

public class List_main extends Activity{

    private static List<String> name,list;
    private static TextView mTextView_name;
    ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private static int p=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.sjzc.fh.pedometer.R.layout.activity_list_main);

        name=new ArrayList<>();
        list=new ArrayList<>();
        mTextView_name= (TextView) findViewById(R.id.text_title);
        listView= (ListView) findViewById(R.id.listView);

        search();
        //init();
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,name);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mTextView_name.setText(name.get(i));
                Intent intent=new Intent();
                intent.putExtra("data",list.get(i));
                setResult(2,intent);
                finish();
            }
        });

    }


    public void search() {
        Cursor mAudioCursor = this.getContentResolver().query(  //android获取SQLLite里面的本地音乐清单
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,// 字段　没有字段　就是查询所有信息　相当于SQL语句中的　“ * ”
                null, // 查询条件
                null, // 条件的对应?的参数
                MediaStore.Audio.AudioColumns.TITLE);// 排序方式

        // 循环输出歌曲的信息
        //List<Map<String, Object>> mListData = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < mAudioCursor.getCount(); i++) {
            mAudioCursor.moveToNext();
            String filePath1 = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)); //得到歌曲路径
            Log.i("路径-------->", filePath1);
            list.add(filePath1);
            String str = getFileName(filePath1);
            String tilte = mAudioCursor.getString(mAudioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)); //歌曲名称
            Log.i("str----->", tilte);
            name.add(str); //保持歌曲名
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


    public void gotomusic(View v){  //layout点击事件
        Intent intent=new Intent(List_main.this,MusicFragment.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //接收数据
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==2){

            mTextView_name.setText(getFileName(data.getStringExtra("path")));
        }
    }

}

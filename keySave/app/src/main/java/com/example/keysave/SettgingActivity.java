package com.example.keysave;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SettgingActivity extends AppCompatActivity {

    TextView about; //开启一个新的页面。
    TextView logindel; //小弹窗，确定是否退出登录。
    TextView clean;  //小弹窗，确定是否清除缓存。
    TextView lock;  //左右滑动开关设置，是否开启密码锁屏。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_settings);

        about = (TextView) findViewById(R.id.about);
        logindel = (TextView)findViewById(R.id.logindel);
        clean = (TextView)findViewById(R.id.clean);
        lock = (TextView)findViewById(R.id.lock);

        //about按钮动作事件
        /*
        * 1.开启一个新的aboutActivity,加载一个新的about页面。
        * 2.
        *
        * */
    }

}
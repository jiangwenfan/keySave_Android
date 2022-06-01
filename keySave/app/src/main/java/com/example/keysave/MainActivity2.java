package com.example.keysave;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.keysave.fragmentHandle.FragmentIndex;
import com.example.keysave.fragmentHandle.FragmentMe;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    FrameLayout fl_container;
    RelativeLayout rl_index;
    RelativeLayout rl_me;

    FragmentMe fragmentMe;
    FragmentIndex fragmentIndex;

    ActionBar supportActionBar; //标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      supportActionBar = getSupportActionBar();

        /*
        * 获取各种控件元素
        * */
        fl_container = findViewById(R.id.fl_container); //获取存放fragment的控件
        rl_index = findViewById(R.id.rl_index); //首页按钮
        rl_me = findViewById(R.id.rl_me); //个人中心按钮


        //设置点击方法
        rl_index.setOnClickListener(this);
        rl_me.setOnClickListener(this);

        //加载默认首页
        loadDefaultIndex();
    }

    /*
    * 基础OnClick类，实现这方法
    * */
    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            //设置首页按钮点击事件
            case R.id.rl_index:
                Log.d("debug-test", "index 页面被点击了");
                loadDefaultIndex();
                break;

            //设置个人中心按钮点击事件
            case R.id.rl_me:
                Log.d("debug-test", "me页面被电击了");
                //加载me页面
                if(fragmentMe == null){
                    fragmentMe = new FragmentMe();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,fragmentMe).commitAllowingStateLoss();
                //修改按钮颜色
                rl_index.setBackgroundColor(Color.parseColor("#E6E6FA"));
                rl_me.setBackgroundColor(Color.parseColor("#2E8B57"));
                //修改标题
                supportActionBar.setTitle("个人界面");
                break;
            default:
                Log.d("debug-test", "没有页面了..");
                break;
        }
    }


    public void loadDefaultIndex(){
        //加载聊天页面
        if(fragmentIndex == null){
            fragmentIndex = new FragmentIndex();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,fragmentIndex).commitAllowingStateLoss();
        //修改按钮颜色
        rl_index.setBackgroundColor(Color.parseColor("#2E8B57"));
        rl_me.setBackgroundColor(Color.parseColor("#E6E6FA"));
        //设置标题
        supportActionBar.setTitle("首页");
    }


}
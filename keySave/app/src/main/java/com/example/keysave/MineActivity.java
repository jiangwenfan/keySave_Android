package com.example.keysave;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MineActivity extends AppCompatActivity {

    ImageView photo; //头像
    Button edit; //编辑按钮
    Button setting; //获取设置按钮
    TextView input; //土司，功能未实现
    TextView output; //土司，功能未实现
    TextView logout; //退出。删除登录信息。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_mine);

        photo = (ImageView) findViewById(R.id.photo);
        edit = (Button)findViewById(R.id.edit);
        setting = (Button)findViewById(R.id.setting);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 1.开启设置页面的activity
                * */

            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 1.开启一个编辑个人资料的Activity页面。
                * 2.获取编辑后的文本
                * 3.设置到当前页面上
                * */
            }
        });
    }
}
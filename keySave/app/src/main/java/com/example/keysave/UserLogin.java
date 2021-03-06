package com.example.keysave;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.keysave.R;

public class UserLogin extends AppCompatActivity {
    /*
     * 用来处理登陆操作  登陆注册逻辑处理，进行基本用户认证
     *
     */
    private EditText et_phoneNumber;
    private CheckBox cb_yes;
    private Button bt_login;

    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_index); //加载登陆首页布局

        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        cb_yes = findViewById(R.id.cb_loginStatus);
        bt_login = findViewById(R.id.bt_login);



        //判断是否已经登陆过了
        SharedPreferences sharedPreferences = getSharedPreferences("logininfo", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "");
        String password = sharedPreferences.getString("password", "");
        if (!userName.equals("") && !password.equals("")) {
            //如果存在登陆数据，直接跳转到首页
            //Toast.makeText(getApplicationContext(),userName+":"+password,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
        }
        //Toast.makeText(getApplicationContext(),userName+":"+password,Toast.LENGTH_LONG).show();
    }


    public void loginCheck(View view) {
        phoneNumber = et_phoneNumber.getText().toString().trim();

        //Toast.makeText(getApplicationContext(), "点击了", Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),phoneNumber,Toast.LENGTH_LONG).show();
        //进行登陆操作
        //Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_LONG).show();
        //获取输入输入的电话号码，传到下个activity
        Intent intent = new Intent(getApplicationContext(), LoginCode.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }

}
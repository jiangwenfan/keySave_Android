package com.example.keysave;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.keysave.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginCode extends AppCompatActivity {

    String password = ""; //本地生成的验证码，   ---》 以后是从网络中获取验证码
    String phoneNumber; //传过来的phoneNumber

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_code); //加载验证码布局

        EditText ed_loginCode = findViewById(R.id.et_LoginCode);
        TextView tv_sendStatusMessage = findViewById(R.id.tv_sendStatusMessage);

        //获取上个activity中的数据，电话号码
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        Log.d("debug-test","[验证码页面]登陆首页穿过来的手机号码:"+phoneNumber);


        /*
        * 1.用随机数模拟，并自动输入
        *
        * 1.1 从网络获取验证码发送验证码,用户自己输入
        *
        * */
        //生产4位验证码
        String code = "";
        for(int i=0;i<4;i++){
            final double d = Math.random();
            final int i1 = (int) (d*10);
            String i1String = String.valueOf(i1);
            code += i1String;
        }
        //将模拟的验证码赋值给验证码变量
        password = code;
        //自动输入验证码
        ed_loginCode.setText(code);



        //2.更新页面发送成功的消息
        tv_sendStatusMessage.setText("已经发送至 "+phoneNumber+" 成功!");

        }


        /*
        * 点击登陆按钮，触发动作。
        *
        * 1.1 监听输入框 ----> 当前通过按钮触发
        * */
    public void ok(View view){
        //获取验证码
        EditText inputCode = findViewById(R.id.et_LoginCode);
        String inputCodeNum = inputCode.getText().toString().trim();

        //对比验证码
        if (inputCodeNum.equals(password)){
            //对比验证码成功!通过登陆

            /*
            //其他网络操作
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取userId
                    //http://119.29.194.108:8085/api/getUserId/?userPhone=18594822867
                    OkHttpClient client = new OkHttpClient();
//            RequestBody requestBody = new FormBody.Builder()
//                    .add("userPhone",phoneNumber)
//                    .build();
                    Request request = new Request.Builder()
                            .url("http://119.29.194.108:8085/api/getUserId/?userPhone="+phoneNumber)
                            .get()
                            .build();

                    //解析userId
                    try {
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        String userId = jsonObject.getString("userId");
                        //写入到本地
                        SharedPreferences sharedPreferences = getSharedPreferences("logininfo2",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId",userId);
                        editor.apply();

                        Toast.makeText(getApplication(),"获取成功",Toast.LENGTH_SHORT).show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            */
            //登陆成功，写入本地文件

            Log.d("debug-test","通过验证码对比了...");
            SharedPreferences sharedPreferences = getSharedPreferences("logininfo",MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("userName",phoneNumber);
            edit.putString("password",password);
            //edit.putString("userId","123456");  //根据电话号码获取从后端找到userId
            edit.putBoolean("status",true); //登陆状态
            edit.apply();
            Log.d("debug-test","登陆信息成功写入到了本地");

            //跳转到main activity
            Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
            startActivity(intent);

        }else{
            //弹出吐司，输入报错，继续输入
            Toast.makeText(getApplicationContext(),"输入的验证码有误!",Toast.LENGTH_LONG).show();
        }

    }
}
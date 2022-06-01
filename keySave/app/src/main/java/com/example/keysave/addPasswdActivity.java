package com.example.keysave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class addPasswdActivity extends AppCompatActivity {

    EditText siteName;
    EditText loginid;
    EditText passwd;
    Button addCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        siteName = (EditText) findViewById(R.id.siteName); //站点名
        loginid = (EditText) findViewById(R.id.siteId);
        passwd = (EditText) findViewById(R.id.passwd);
        addCommit = (Button) findViewById(R.id.addCommit);

        Log.d("debug-test","1.已经进入到了添加密码的activity页面中..");


    }

    Handler myHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                //发送添加成功发回状态码。
                Toast.makeText(getApplicationContext(), "密码添加成功!", Toast.LENGTH_LONG).show();
            }
        }
    };
    /*
    * 设置提交密码的事件
    * */
    public void submit(View v){
        Log.d("debug-test","2.点击了提交密码的按钮...");
        sendPost();
    }

    public void sendPost(){
        Log.d("debug-test","即将开启子线程发送密码到后端..");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://www.pwall.icu:8090/api/add";
                Log.d("debug-test","添加密码的api："+url);
                //post发送数据
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("siteName", siteName.getText().toString().trim())
                        .add("sitePasswd",passwd.getText().toString().trim())
                        .add("siteId", loginid.getText().toString().trim())
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                try {
                    Log.d("debug-test","即将使用post请求发送添加密码动作");
                    Response response = client.newCall(request).execute();
                    Log.d("debug-test","请求发送完毕");
                    String responseData = response.body().string();
                    Log.d("debug-test","发送添加密码请求结束,响应info:"+responseData);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("debug-test","请求报错了...");
                }


               Message message = new Message();
                message.what = 0;
                myHandler.sendMessage(message);
            }
        }).start();
        Log.d("debug-test","子线程添加密码成功!");
    }
}
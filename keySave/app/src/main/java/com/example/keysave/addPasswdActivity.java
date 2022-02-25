package com.example.keysave;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
        Log.d("111","adddd");
//        addCommit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //post 方式提交数据到api后端。
//                sendPost();
//                Log.d("111","点击了");
//            }
//        });
    }
    public void submit(View v){
        Log.d("111","点击了");
        sendPost();
    }
    public void sendPost(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://119.29.194.108:6070/api/add";
                Log.d("222",url);
                //post发送数据
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("siteName", "google")
                        .add("sitePasswdEncry", "admin123")
                        .add("siteId", "zhan210@gmail")
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    System.out.println(responseData);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //发送添加成功发回状态码。
                Toast.makeText(getApplicationContext(), "add ok!", Toast.LENGTH_LONG);
            }
        }).start();
        System.out.println("son");
    }
}
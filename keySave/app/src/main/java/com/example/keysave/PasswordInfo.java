package com.example.keysave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PasswordInfo extends AppCompatActivity {

    TextView siteId;
    TextView tv_id;
    TextView keyType;
    TextView siteName;
    TextView key;
    TextView algor;
    TextView backup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_info);

        //获取从上个item中传过来的id
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Log.d("debug-tets","传过来的id"+id);

        //获取布局中的元素
         siteId = findViewById(R.id.siteId);
         tv_id = findViewById(R.id.id);
         keyType =findViewById(R.id.keyType);
         siteName =findViewById(R.id.siteName);
         key =findViewById(R.id.key);
         algor =findViewById(R.id.algor);
         backup =findViewById(R.id.backup);


        //根据id获取详细信息
        getAllData(id);

    }
    //主线程接收消息，更新UI
    Handler myHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0:
                    HashMap<String,String> map = (HashMap<String, String>) msg.obj;
                    //更新UI
                    siteId.setText(map.get("siteId"));
                    tv_id.setText(map.get("id"));
                    keyType.setText(map.get("keyType"));
                    siteName.setText(map.get("siteName"));
                    key.setText("key");
                    algor.setText("algor");
                    backup.setText("backup");
                    break;
                default:
                    break;
            }
        }
    };
    public void getAllData(String id) {
        //根据id获取所有信息

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String url = "http://119.29.194.108:8090/api/getAll?id="+id;
                Request request = new Request.Builder().url(url).build();

                try {
                    Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    Log.d("debug-test","获取所有数据"+jsonData);

                    //解析数据
                    HashMap<String,String> data = parseAllData(jsonData);

                    //发送给主线程
                    Message message = new Message();
                    message.what = 0;
                    message.obj = data;
                    myHandler.sendMessage(message);
                    Log.d("debug-test","发送给主线完毕"+data);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }


    private HashMap<String, String> parseAllData(String responseData) {
        //解析所有详细信息

        HashMap<String, String> map = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(responseData);
            map.put("id", jsonObject.getString("id"));
            map.put("keyType", jsonObject.getString("keyType"));
            map.put("siteId", jsonObject.getString("siteId"));
            map.put("sitePasswd", jsonObject.getString("sitePasswd"));
            map.put("siteName", jsonObject.getString("siteName"));
            map.put("Key", jsonObject.getString("key"));
            map.put("algor", jsonObject.getString("algor"));
            map.put("backup", jsonObject.getString("backup"));

            Log.d("debug-test", "解析后的数据是:" + map);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
}
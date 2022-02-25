package com.example.keysave;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button addBt; //add passwd Button
    Button searchBt; //search a passwd
    EditText searchKey;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        addBt = findViewById(R.id.addBt);
        addBt.setOnClickListener(this);
        searchBt =  findViewById(R.id.searchBt);
        searchBt.setOnClickListener(this);

        //获取搜索关键字
        searchKey = findViewById(R.id.searchKey);

        //获取listview
        listView = findViewById(R.id.mainlv);
        //listView.setAdapter(new MyAdapter());
        //显示所有密码
        showAllPasswd();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBt:
                addPasswdHandle();
            case R.id.searchBt:
                searchPasswdHandle();
            default:
                break;
        }
    }

    //[2]获取子线程发送的消息
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //获取数据
            HashMap<String,String> strData = (HashMap<String, String>) msg.obj;

            if(msg.what == 0){
                //显示到首页。搜索获得的指定密码.//[3]更新listView

            }else if(msg.what == 1){
                //默认获取到该用户的账号密码信息
                ArrayList<Object> jsonList = (ArrayList<Object>) msg.obj;

                //2.通过listView显示到主页上
                PasswdAdapter adapter = new PasswdAdapter(getApplicationContext(),R.layout.passwdInfo_item,jsonList);

                //设置到lsitview上
                listView.setAdapter(adapter);

            }else if(msg.what == 2){

            }else{

            }
        }
    };


    /*
     * 首页显示所有密码
     * 1.子线程去获取所有密码的api
     * */
    private void showAllPasswd() {
        Log.d("index","create son thread ,get api");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("index","发送查询所有密码的api请求");

                String url="http://119.29.194.108:6070/api/all";
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("userName","jwf")
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                Response response = null;
                try {
                    Log.d("index","start send a post request ,拿到所有密码");
                    response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    ArrayList<Object>  jsonList = json(responseData); //解析之后的数组列表+哈希map数据

                    //发送到主线程
                    Message message = new Message();
                    message.what = 1;
                    message.obj = jsonList;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //解析json数据
            public ArrayList<Object> json(String responseData){
                ArrayList<Object> data = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(responseData);

                    for(int i=0;i<jsonArray.length();i++){
                        //解析json
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String siteId = jsonObject.getString("siteId");
                        String sitePasswdEncry = jsonObject.getString("sitePasswdEncry");
                        String siteName = jsonObject.getString("siteName");
                        String siteDomain = jsonObject.getString("siteDomain");
                        String siteLoginUrl = jsonObject.getString("siteLoginUrl");
                        String siteLoginArea = jsonObject.getString("siteLoginArea");
                        String key = jsonObject.getString("key");
                        String algor = jsonObject.getString("algor");

                        //存储到哈希map中
                        HashMap<String,String> item = new HashMap<>();
                        item.put("siteId",siteId);
                        item.put("sitePasswdEncry",sitePasswdEncry);
                        item.put("siteName",siteName);
                        item.put("siteDomain",siteDomain);
                        item.put("siteLoginUrl",siteLoginUrl);
                        item.put("siteLoginArea",siteLoginArea);
                        item.put("key",key);
                        item.put("algor",algor);

                        //存储到ArrayList
                        data.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }).start();

        String searchkey = "all";
        String url = "http://1.1.1.1/data.json?key=" + searchkey;
        Log.d("search:",url);

        getJsonData(url,1);

        Toast.makeText(getApplicationContext(),"发送成功!",Toast.LENGTH_LONG).show();
    }

    /*
     * 1.获取输入框的字符值
     * 2.把这个关键字以post的方式请求后端api,返回json数据
     * */
    private void searchPasswdHandle() {
        String searchkey = searchKey.getText().toString().trim();

        String url = "http://1.1.1.1/data.json?key=" + searchkey;
        Log.d("search:",url);

        getJsonData(url,0);

        Toast.makeText(getApplicationContext(),"发送成功!",Toast.LENGTH_LONG).show();
    }

    /*
     * 1.开启一个添加密码的activity
     * 2.
     * */
    private void addPasswdHandle() {
        //跳转activity
        Intent intent = new Intent(MainActivity.this, addPasswdActivity.class);
        startActivity(intent);
    }

    /*
    * 1.传入api加参数的url,传入发送消息的标志位。
    * 2.获取到的数据发给主线程。
    * */
    private void getJsonData(String url,int flag) {
        //获取json结果数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                //拼接url
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    //解析json数据
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //获取字段内容。
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        HashMap<String,String> info = new HashMap<>();
                        info.put("id",id);
                        info.put("name",name);
                        Log.d("get json:",info.toString());
                        //打印日志
                        Log.d("id is:", id);
                        Log.d("name is: ", name);

                        //发送到主线程
                        Message message =new Message();
                        message.what=flag;
                        message.obj = info;
                        handler.sendMessage(message);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public class MyAdapter  extends BaseAdapter {


        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //显示简单条目
//            TextView tv ;
//            if (convertView == null) {
//                //创建新的view 对象
//                tv = new TextView(MainActivity.this);
//
//                System.out.println("创建新的view 对象---"+position);
//            }else{
//
//                System.out.println("复用历史缓存 对象---"+position);
//                tv = (TextView) convertView;
//            }
//            tv.setText("哈哈哈"+position);
//            return tv;
            //显示复杂页面
            View view;
            if(convertView == null){
                view = View.inflate(getApplicationContext(),R.layout.passwdInfo_item,null); //将布局转换为view
            }else {
                view = convertView;
            }

            return view;
        }
    }

}


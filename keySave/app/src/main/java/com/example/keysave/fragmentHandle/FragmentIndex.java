package com.example.keysave.fragmentHandle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keysave.PasswdAdapter;
import com.example.keysave.PasswordInfo;
import com.example.keysave.R;
import com.example.keysave.addPasswdActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentIndex#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentIndex extends Fragment implements View.OnClickListener{

    TextView add; //add passwd Button
    EditText searchKey;
    ListView myListView;

    ArrayList<String> data; //listview数据
    ArrayAdapter<String> myArrayAdapter;



    //处理这个fragment的操作
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
         * 获取控件
         * */
        searchKey = view.findViewById(R.id.searchKey);   //获取搜索关键字
        myListView = view.findViewById(R.id.index_listview); //获取listview
        add = view.findViewById(R.id.add);

        add.setOnClickListener(this);
        searchKey.setOnClickListener(this);


        //显示所有密码
        showAllPasswd();


        //给listView注册上下文菜单
        registerForContextMenu(myListView);

    }

    /*
    * 上下文菜单
    * */
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //设置上下文菜单的样式
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_context,menu);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //return super.onContextItemSelected(item);
        //获取item数据，信息
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //对选中的选项进行响应
        switch (item.getItemId()){
            case R.id.delete:
                Log.d("debug-test","delete上下文菜单被点击了");
                Log.d("debug-test","--position-->"+menuInfo.position+"--id-->"+menuInfo.id);

                //根据索引删除数据
                String delData = data.remove(Integer.parseInt(String.valueOf(menuInfo.id)));
                Log.d("debug-test","删除后数据:"+data);

                //重新设置适配器
                myArrayAdapter.notifyDataSetChanged();
                myListView.setAdapter(myArrayAdapter);

                //显示
                Toast.makeText(getActivity(),"删除成功!",Toast.LENGTH_SHORT).show();

                return true;
            case R.id.test:
                Log.d("debug-test","test上下文菜单被点击了");
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //---->添加密码功能  1.开启一个添加密码的activity
            case R.id.add:
                Log.d("debug-test","添加密码按钮被点击了");
                //跳转activity
                Intent intent = new Intent(getActivity(), addPasswdActivity.class);
                startActivity(intent);
                break;
            case R.id.searchKey:
                Log.d("debug-test","搜索框点击了");
                //搜索框处理
                seachHandle();
                break;
            default:
                break;
        }

    }


    //[2]获取子线程发送的消息
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what == 0){
                    Log.d("debug-test","h");

            }else if(msg.what == 1){
                //首页默认获取到该用户啊所有的id和站点信息，进行UI界面更新
                Log.d("debug-test","进入到获取数据的主线程");
                ArrayList<HashMap<String,String>> jsonList = (ArrayList<HashMap<String, String>>) msg.obj;
                Log.d("debug-test","主线程收到数据:"+jsonList);

                //生成指定格式的ArrayList显示信息数据
                data = new ArrayList<>();
                for(int i=0;i<jsonList.size();i++){
                    HashMap<String,String> map= jsonList.get(i);
                    String messg = map.get("id")+" "+map.get("siteName")+" "+map.get("siteId");
                    data.add(messg);
                }

                //设置适配器到listviews上
                myArrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1, //显示的条目布局
                        data
                );
                myListView.setAdapter(myArrayAdapter);

                //设置每个item的点击动作
                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //显示每个密码的详细信息
                        Log.d("debug-test","item点击了");
                        Log.d("debug-test",""+i);

                        //get data
                        String itemData = data.get(i);
                        String[] itemDataSplit = itemData.split(" ");
                        Log.d("debug-test",""+itemDataSplit[0]);
                        //数据的表id
                        String dataId = itemDataSplit[0];


                        //进入显示密码详细信息的页面
                        Intent intent = new Intent(getActivity(), PasswordInfo.class);
                        intent.putExtra("id",dataId);
                        startActivity(intent);

                    }
                });
            }else if(msg.what == 10){
                //根据关键字搜索账号密码完成之后，更新UI界面
                Log.d("debug-test","进入到了handle主线程，准备更新UI界面");
                ArrayList<HashMap<String,String>> data = (ArrayList<HashMap<String, String>>) msg.obj;
                Log.d("debug-test","进入到了handle主线程,data"+data);

                //将数据组装成文本
                ArrayList resData = new ArrayList<>();
                for(int i=0;i<data.size();i++){
                    HashMap<String,String> map= data.get(i);
                    String messg = map.get("id")+" "+map.get("siteName")+" "+map.get("siteId");
                    resData.add(messg);
                }

                //设置适配器到listviews上
                myArrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1, //显示的条目布局
                        resData
                );
                myListView.setAdapter(myArrayAdapter);
            }else{
                Log.d("debug-test","hh");
            }
        }
    };

    /*
     * ------>  首页显示所有密码
     * */
    private void showAllPasswd() {
        Log.d("debug-test","进入主页，开始自动获取所有账号密码....");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("debug-test","进入子线程，开始进去首页密码获取...");

                String url="http://119.29.194.108:8090/api/show";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("debug-test","首页请求获取到响应数据:"+responseData);

                    ArrayList<HashMap<String,String>>  jsonList = indexDataJson(responseData); //解析之后的数组列表+哈希map数据
                    Log.d("debug-test","解析之后的数据"+jsonList);

                    //发送到主线程
                    Message message = new Message();
                    message.what = 1;
                    message.obj = jsonList;
                    handler.sendMessage(message);

                    Log.d("debug-test","发送到主线称成功!");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



            //解析首页json数据[xx]
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

    }
    public ArrayList<HashMap<String,String>> indexDataJson(String responseData){
        //解析并显示,index首页的数据。id和站点名
        ArrayList<HashMap<String,String>> data = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(responseData);
            for(int i=0;i<jsonArray.length();i++){
                HashMap<String,String> map = new HashMap<>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                map.put("id",jsonObject.getString("id"));
                map.put("siteId",jsonObject.getString("siteId"));
                map.put("siteName",jsonObject.getString("siteName"));
                data.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    /*
     *-------->搜索密码
     * */

    public void seachHandle() {
                String searchkey = searchKey.getText().toString().trim();
                Log.d("debug-test","输入的关键字是:"+searchkey);
                if(searchkey.length() != 0){
                    //长度不为0，进行模糊查询
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //开启子线程执行查询操作
                            Log.d("debug-test","开启了子线程进行查询操作...");

                            String url = "http://119.29.194.108:8090/api/select?siteName=" + searchkey;
                            Log.d("debug-test","搜索的api是:"+url);

                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(url)
                                    .get()
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();

                                String responseData = response.body().string().trim();
                                Log.d("debug-test","响应字符串:"+responseData);

                                //解析字符串
                                ArrayList<HashMap<String,String>>  jsonList = indexDataJson(responseData); //解析之后的数组列表+哈希map数据
                                Log.d("debug-test","解析之后的数据"+jsonList);


                                //发送到主线程
                                Message message = new Message();
                                message.what = 10;
                                message.obj = jsonList;
                                handler.sendMessage(message);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }











    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentIndex() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentIndex.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentIndex newInstance(String param1, String param2) {
        FragmentIndex fragment = new FragmentIndex();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_index, container, false);
    }



}
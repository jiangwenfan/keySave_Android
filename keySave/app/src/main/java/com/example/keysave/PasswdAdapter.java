package com.example.keysave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

public class PasswdAdapter extends ArrayAdapter<Object> {
    private int resourceId;

    //这个构造函数，接收:上下文，布局资源id,要显示的数组
    public PasswdAdapter(Context context, int textViewResourceId, List<Object> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        HashMap<String,String> item = (HashMap<String, String>) getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView siteName = (TextView) view.findViewById(R.id.siteName);
        TextView siteId = (TextView)view.findViewById(R.id.siteId);

        siteName.setText(item.get("siteName"));
        siteId.setText(item.get("siteId"));
        return view;
    }
}

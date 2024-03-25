package com.example.DogBreeds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class custombaseadapter extends BaseAdapter {
     Context context;
    ArrayList<String> district;
    LayoutInflater inflater;

    custombaseadapter(Context ctx, ArrayList<String>areaName){
           this.context=ctx;

            this.district= areaName;

        this.inflater= LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return district.size();
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
        convertView=inflater.inflate(R.layout.listlayout,null);
        TextView text=convertView.findViewById(R.id.listtext);
        text.setText(district.get(position));
        return convertView;
    }
}

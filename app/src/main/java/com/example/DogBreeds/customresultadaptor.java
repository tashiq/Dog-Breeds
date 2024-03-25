package com.example.DogBreeds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class customresultadaptor extends BaseAdapter {
    ArrayList<String> image;
    Context context;
    LayoutInflater inflater;

    customresultadaptor(Context ctx, ArrayList<String> ima){
        this.context=ctx;
        this.image=ima;
        this.inflater= LayoutInflater.from(ctx);
    }




    @Override
    public int getCount() {
        return image.size();
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
        convertView=inflater.inflate(R.layout.predictevertuserdetails,null);
        ImageView im=convertView.findViewById(R.id.rogimage);
        Picasso.get().load(image.get(position)).into(im);
        return null;
    }
}

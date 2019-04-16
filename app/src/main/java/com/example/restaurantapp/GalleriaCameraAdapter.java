package com.example.restaurantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleriaCameraAdapter extends BaseAdapter {

    private Context context;

    public GalleriaCameraAdapter(Context context) {

        this.context = context;

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.image_text_item, null);

        if (position == 0){
            ((ImageView) view.findViewById(R.id.itemImage)).setImageResource(R.drawable.round_photo_24);
            ((TextView) view.findViewById(R.id.itemText)).setText(R.string.galleryText);
        }else{
            ((ImageView) view.findViewById(R.id.itemImage)).setImageResource(R.drawable.round_camera_alt_24);
            ((TextView) view.findViewById(R.id.itemText)).setText(R.string.cameraText);
        }
        return view;
    }
}

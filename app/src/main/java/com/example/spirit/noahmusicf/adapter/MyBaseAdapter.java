package com.example.spirit.noahmusicf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private ArrayList<T> list;
    private Context context;

    public MyBaseAdapter(ArrayList<T> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getBaseView(context, position, convertView);
    }

    public abstract View getBaseView(Context context, int position, View convertView);
}

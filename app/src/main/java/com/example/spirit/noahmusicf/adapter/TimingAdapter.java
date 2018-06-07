package com.example.spirit.noahmusicf.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.modle.TimingBean;
import com.example.spirit.noahmusicf.utils.Util;

import java.util.ArrayList;

public class TimingAdapter extends MyBaseAdapter<TimingBean> {
    private ArrayList<TimingBean> list;
    private ArrayList<Boolean> isSelectedList;

    public TimingAdapter(ArrayList<TimingBean> list, Context context, ArrayList<Boolean>
            isSelectedList) {
        super(list, context);
        this.list = list;
        this.isSelectedList = isSelectedList;
    }

    @Override
    public View getBaseView(Context context, final int position, View convertView) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflate(R.layout.timing_item);
            holder = new ViewHolder();
            holder.ivSelected = convertView.findViewById(R.id.iv_selected);
            holder.tvFunction = convertView.findViewById(R.id.tv_function);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvFunction.setText(list.get(position).getTvFunction());
        holder.ivSelected.setImageResource(list.get(position).getIvSelected());
        Boolean isSelected = getIsSelectedList().get(position);
        holder.tvFunction.setSelected(isSelected);
        if (!isSelected) {
            holder.ivSelected.setVisibility(View.INVISIBLE);
        } else {
            holder.ivSelected.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvFunction;
        ImageView ivSelected;
    }

    private ArrayList<Boolean> getIsSelectedList() {
        return isSelectedList;
    }

    public void setIsSelectedList(ArrayList<Boolean> isSelectedList) {
        this.isSelectedList = isSelectedList;
    }
}

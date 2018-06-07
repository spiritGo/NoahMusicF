package com.example.spirit.noahmusicf.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.modle.ThemeListBean;

import java.util.ArrayList;

public class ThemeListAdapter extends MyBaseAdapter<ThemeListBean> {
    private ArrayList<ThemeListBean> list;
    private GradientDrawable gradientDrawable;
    private OnButtonClickListener listener;
    private ArrayList<GradientDrawable> gradientDrawables;

    public ThemeListAdapter(ArrayList<ThemeListBean> list, Context context) {
        super(list, context);
        this.list = list;
        gradientDrawables = new ArrayList<>();
    }

    @Override
    public View getBaseView(Context context, int position, View convertView) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.theme_list_item, null);
            holder = new ViewHolder();
            holder.view = convertView.findViewById(R.id.v_colorView);
            holder.textView = convertView.findViewById(R.id.tv_description);
            holder.button = convertView.findViewById(R.id.btn_description);
            convertView.setTag(holder);
            gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(5);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ThemeListBean themeListBean = list.get(position);
        Resources resources = context.getResources();

        try {
            gradientDrawable = gradientDrawables.get(position);
        } catch (IndexOutOfBoundsException e) {
            gradientDrawable.setColor(resources.getColor(themeListBean.getEgColor()));
            gradientDrawables.add(gradientDrawable);
        }

        holder.button.setBackgroundDrawable(gradientDrawables.get(position));
        holder.button.setText(themeListBean.getButtonText());
        holder.textView.setText(themeListBean.getDescription());
        holder.view.setBackgroundColor(resources.getColor(themeListBean.getEgColor()));

        holder.button.setTag(position);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onButtonClick((Integer) v.getTag());
                }
            }
        });

        return convertView;
    }

    public void setListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnButtonClickListener {
        void onButtonClick(int position);
    }

    class ViewHolder {
        View view;
        TextView textView;
        Button button;
    }
}

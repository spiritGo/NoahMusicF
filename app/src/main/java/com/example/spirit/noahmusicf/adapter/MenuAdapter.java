package com.example.spirit.noahmusicf.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.modle.ArrayData;
import com.example.spirit.noahmusicf.modle.MenuListBean;
import com.example.spirit.noahmusicf.utils.SPUtil;

import java.util.ArrayList;

public class MenuAdapter extends MyBaseAdapter<MenuListBean> {

    private ArrayList<MenuListBean> list;
    private int item = 0;
    private int color;
    private int gray70;

    public MenuAdapter(ArrayList<MenuListBean> list, Context context) {
        super(list, context);
        this.list = list;
    }

    @Override
    public View getBaseView(Context context, int position, View convertView) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.sliding_menu_list_item, null);
            holder = new ViewHolder();
            holder.ivIcon = convertView.findViewById(R.id.iv_icon);
            holder.tvTitle = convertView.findViewById(R.id.tv_listItemTitle);

            int egColor = ArrayData.getThemeListBeans().get(0).getEgColor();
            int theme = SPUtil.getInt(SPUtil.TITLE_COLOR, egColor);
            color = context.getResources().getColor(theme);
            gray70 = context.getResources().getColor(R.color.gray70);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MenuListBean menuListBean = list.get(position);
        holder.tvTitle.setText(menuListBean.getTitle());
        holder.ivIcon.setImageResource(menuListBean.getIcon());

        if (item==position) {
            holder.tvTitle.setTextColor(color);
            holder.ivIcon.setColorFilter(color);
        } else {
            holder.ivIcon.setColorFilter(gray70);
            holder.tvTitle.setTextColor(gray70);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

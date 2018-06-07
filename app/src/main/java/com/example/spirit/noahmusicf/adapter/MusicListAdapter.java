package com.example.spirit.noahmusicf.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.modle.MusicBean;
import com.example.spirit.noahmusicf.utils.SPUtil;

import java.util.ArrayList;

public class MusicListAdapter extends MyBaseAdapter<MusicBean> {

    private ArrayList<MusicBean> list;

    public MusicListAdapter(ArrayList<MusicBean> list, Context context) {
        super(list, context);
        this.list = list;
    }

    @Override
    public View getBaseView(Context context, int position, View convertView) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.music_list_item, null);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tv_musicTitle);
            holder.tvArtist = convertView.findViewById(R.id.tv_musicArtist);
            holder.imageView = convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvArtist.setText(list.get(position).getArtist());
        holder.imageView.setColorFilter(context.getResources().getColor(SPUtil.getInt
                ("titleColor", R.color.blue)));
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvArtist;
        ImageView imageView;
    }
}

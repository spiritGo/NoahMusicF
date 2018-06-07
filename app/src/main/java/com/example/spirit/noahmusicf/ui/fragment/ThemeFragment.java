package com.example.spirit.noahmusicf.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.adapter.ThemeListAdapter;
import com.example.spirit.noahmusicf.modle.ArrayData;
import com.example.spirit.noahmusicf.modle.ThemeListBean;
import com.example.spirit.noahmusicf.ui.activity.MainActivity;
import com.example.spirit.noahmusicf.utils.SPUtil;
import com.example.spirit.noahmusicf.utils.Util;

import java.util.ArrayList;

public class ThemeFragment extends Fragment {

    private ListView lvThemeList;
    private ArrayList<ThemeListBean> themeListBeans;
    private RelativeLayout rlTitle;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = Util.inflate(R.layout.fragment_theme);
        initView(view);
        initVariable();
        initUI();
        return view;
    }

    private void initVariable() {
        themeListBeans = ArrayData.getThemeListBeans();
    }

    private void initUI() {
        ThemeListAdapter themeListAdapter = new ThemeListAdapter(themeListBeans, getActivity());
        lvThemeList.setAdapter(themeListAdapter);

        themeListAdapter.setListener(new ThemeListAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int position) {
                changeColor(position);
            }
        });
    }

    private void changeColor(int position) {
        int egColor = themeListBeans.get(position).getEgColor();
        rlTitle.setBackgroundColor(getResources().getColor(egColor));
        SPUtil.putInt("titleColor", egColor);
        activity.getMenuAdapter().setColor(getResources().getColor(egColor));
        activity.getMenuAdapter().notifyDataSetChanged();
    }

    private void initView(View view) {
        lvThemeList = view.findViewById(R.id.lv_themeList);
        activity = (MainActivity) getActivity();
        rlTitle = activity.getRlTitle();
    }
}

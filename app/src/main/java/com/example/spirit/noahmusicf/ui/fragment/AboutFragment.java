package com.example.spirit.noahmusicf.ui.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.utils.Util;

public class AboutFragment extends Fragment {
    private TextView tvVersion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = Util.inflate(R.layout.fragment_about);
        initView(view);
        initUI();

        return view;
    }

    private void initUI() {
        tvVersion.setText(String.format("版本：%s\n\n作者：spiritGo\n\n邮箱：1194501891@qq.com",
                getVersionName()));
    }

    private String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getActivity().getPackageManager();

        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packInfo != null) {
            return packInfo.versionName;
        }

        return "";
    }

    private void initView(View view) {
        tvVersion = view.findViewById(R.id.tv_version);
    }
}

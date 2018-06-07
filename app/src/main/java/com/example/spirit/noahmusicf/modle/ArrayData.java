package com.example.spirit.noahmusicf.modle;

import android.support.v4.app.Fragment;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.ui.fragment.AboutFragment;
import com.example.spirit.noahmusicf.ui.fragment.MusicFragment;
import com.example.spirit.noahmusicf.ui.fragment.ThemeFragment;
import com.example.spirit.noahmusicf.ui.fragment.TimingCloseFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ArrayData {
    private static Map<String, Fragment> menuListTextMap;
    private static ArrayList<MenuListBean> menuListBeans;
    private static ArrayList<TimingBean> timingBeans;
    private static ArrayList<ThemeListBean> themeListBeans;

    public static Map<String, Fragment> getMenuListTextAndFragment() {
        if (menuListTextMap == null) {
            menuListTextMap = new LinkedHashMap<>();
            menuListTextMap.put("歌曲", new MusicFragment());
            menuListTextMap.put("定时关闭", new TimingCloseFragment());
            menuListTextMap.put("主题", new ThemeFragment());
            menuListTextMap.put("关于", new AboutFragment());
        }
        return menuListTextMap;
    }

    public static ArrayList<MenuListBean> getMenuListBeanList() {
        if (menuListBeans == null) {
            menuListBeans = new ArrayList<>();

            MenuListBean menuListBean0 = new MenuListBean();
            menuListBean0.setIcon(R.mipmap.pinpine70);
            menuListBean0.setTitle("歌曲");

            MenuListBean menuListBean3 = new MenuListBean();
            menuListBean3.setIcon(R.mipmap.timing);
            menuListBean3.setTitle("定时关闭");

            MenuListBean menuListBean4 = new MenuListBean();
            menuListBean4.setIcon(R.mipmap.theme);
            menuListBean4.setTitle("主题");

            MenuListBean menuListBean1 = new MenuListBean();
            menuListBean1.setIcon(R.mipmap.about);
            menuListBean1.setTitle("关于");

            menuListBeans.add(menuListBean0);
            menuListBeans.add(menuListBean3);
            menuListBeans.add(menuListBean4);
            menuListBeans.add(menuListBean1);
        }
        return menuListBeans;
    }

    public static ArrayList<TimingBean> getTimingBeanList() {
        if (timingBeans == null) {
            timingBeans = new ArrayList<>();

            TimingBean tenMin = new TimingBean();
            tenMin.setIvSelected(R.mipmap.selected);
            tenMin.setTvFunction(R.string.tenMin);
            timingBeans.add(tenMin);

            TimingBean twentyMin = new TimingBean();
            twentyMin.setIvSelected(R.mipmap.selected);
            twentyMin.setTvFunction(R.string.twentyMin);
            timingBeans.add(twentyMin);

            TimingBean thirtyMin = new TimingBean();
            thirtyMin.setIvSelected(R.mipmap.selected);
            thirtyMin.setTvFunction(R.string.thirtyMin);
            timingBeans.add(thirtyMin);

            TimingBean hour = new TimingBean();
            hour.setIvSelected(R.mipmap.selected);
            hour.setTvFunction(R.string.hour);
            timingBeans.add(hour);

            TimingBean custom = new TimingBean();
            custom.setIvSelected(R.mipmap.selected);
            custom.setTvFunction(R.string.custom);
            timingBeans.add(custom);
        }
        return timingBeans;
    }

    public static ArrayList<ThemeListBean> getThemeListBeans() {
        if (themeListBeans == null) {
            themeListBeans = new ArrayList<>();

            ThemeListBean blue = new ThemeListBean();
            blue.setButtonText("蓝色");
            blue.setDescription("蓝色");
            blue.setEgColor(R.color.blue);
            blue.setTheme(R.style.theme_blue);
            themeListBeans.add(blue);

            ThemeListBean pink = new ThemeListBean();
            pink.setButtonText("粉色");
            pink.setDescription("粉色");
            pink.setEgColor(R.color.pink);
            pink.setTheme(R.style.theme_pink);
            themeListBeans.add(pink);

            ThemeListBean colorPrimary = new ThemeListBean();
            colorPrimary.setButtonText("深蓝");
            colorPrimary.setDescription("深蓝");
            colorPrimary.setEgColor(R.color.colorPrimary);
            colorPrimary.setTheme(R.style.theme_pink);
            themeListBeans.add(colorPrimary);

            ThemeListBean aqua = new ThemeListBean();
            aqua.setButtonText("水绿色");
            aqua.setDescription("水绿色");
            aqua.setEgColor(R.color.aqua);
            aqua.setTheme(R.style.theme_pink);
            themeListBeans.add(aqua);

            ThemeListBean dimgray = new ThemeListBean();
            dimgray.setButtonText("暗淡灰色");
            dimgray.setDescription("暗淡灰色");
            dimgray.setEgColor(R.color.dimgray);
            dimgray.setTheme(R.style.theme_pink);
            themeListBeans.add(dimgray);
        }
        return themeListBeans;
    }
}

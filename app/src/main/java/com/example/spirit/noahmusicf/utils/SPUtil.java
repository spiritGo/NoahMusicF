package com.example.spirit.noahmusicf.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {

    final public static String CURRENT_MUSIC_ITEM="currentMusicItem";
    final public static String TITLE_COLOR="titleColor";

    private static SharedPreferences getSharedPreferences() {
        return Util.getContext().getSharedPreferences("info.txt", Context.MODE_PRIVATE);
    }

    public static void putInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }
}

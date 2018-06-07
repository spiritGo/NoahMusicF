package com.example.spirit.noahmusicf.ui;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class MyApplication extends Application {
    private static Context context;
    private static int tid;
    private static Handler handler;

    public static Context getContext() {
        return context;
    }

    public static int getTid() {
        return tid;
    }

    public static Handler getHandler() {
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        tid = android.os.Process.myTid();
        handler = new Handler();
    }
}

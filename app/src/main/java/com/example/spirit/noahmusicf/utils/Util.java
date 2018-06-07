package com.example.spirit.noahmusicf.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.spirit.noahmusicf.service.MusicService;
import com.example.spirit.noahmusicf.ui.MyApplication;

import java.util.ArrayList;

public class Util {

    public static Context getContext() {
        return MyApplication.getContext();
    }

    public static android.os.Handler getHandler() {
        return MyApplication.getHandler();
    }

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) getContext().getSystemService(Context
                .ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = null;
        if (myManager != null) {
            runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                    .getRunningServices(30);
            for (int i = 0; i < runningService.size(); i++) {
                if (runningService.get(i).service.getClassName().equals(ServiceName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static int dp2px(float dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    public static void runOnUI(Runnable r) {
        getHandler().post(r);
    }

    public static StringBuffer timeFormat(long second, String prompt) {
        StringBuffer timeStr = new StringBuffer();
        long hour = second / 3600;
        if (hour > 9) {
            timeStr.append(hour).append(":");
        } else if (hour > 0) {
            timeStr.append("0").append(hour).append(":");
        }
        long min = second % 3600 / 60;
        if (min > 9) {
            timeStr.append(min).append(":");
        } else {
            timeStr.append("0").append(min).append(":");
        }
        long s = second % 3600 % 60;
        if (s > 9) {
            timeStr.append(s);
        } else {
            timeStr.append("0").append(s);
        }
        timeStr.append(prompt);
        return timeStr;
    }

    public static Handler getMusicServiceHandler() {
        return MusicService.getHandler();
    }

    public static void runOnService(Runnable r) {
        getMusicServiceHandler().post(r);
    }
}

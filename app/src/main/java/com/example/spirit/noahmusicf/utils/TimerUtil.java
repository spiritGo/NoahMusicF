package com.example.spirit.noahmusicf.utils;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {
    private Timer timer;
    private static TimerUtil timerUtil;
    private long time = 0;
    private boolean isRunning = false;
    private int listItem = 0;

    public static TimerUtil getTimerUtil() {
        if (timerUtil == null) {
            synchronized (TimerUtil.class) {
                if (timerUtil == null) {
                    timerUtil = new TimerUtil();
                }
            }
        }
        return timerUtil;
    }

    public void timeSchedule(long delay, long period, final OnScheduleListener listener) {
        if (isRunning) {
            timer.cancel();
        }
        timer = new Timer();
        isRunning = true;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (time > 0) {
                    time--;
                    if (listener != null) {
                        listener.onSchedule(time);
                    }
                } else {
                    cancel();
                    if (listener != null) {
                        listener.onCancel();
                    }
                }

            }
        }, delay, period);
    }

    public interface OnScheduleListener {
        void onSchedule(long time);

        void onCancel();
    }


    public void cancel() {
        if (isRunning) {
            timer.cancel();
            timer.purge();
        }
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getListItem() {
        return listItem;
    }

    public void setListItem(int listItem) {
        this.listItem = listItem;
    }
}

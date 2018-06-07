package com.example.spirit.noahmusicf.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.widget.SeekBar;

import com.example.spirit.noahmusicf.modle.MusicBean;
import com.example.spirit.noahmusicf.utils.MediaPlayerUtil;
import com.example.spirit.noahmusicf.utils.MusicState;
import com.example.spirit.noahmusicf.utils.SPUtil;

import java.util.ArrayList;

public class MusicService extends Service {

    private MediaPlayerUtil playerUtil;
    private ArrayList<MusicBean> list;

    private static int currentMusicItem;
    private int count;
    private static Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        MusicBroadCast musicBroadCast = new MusicBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicState.PAUSE_ACTION);
        filter.addAction(MusicState.PLAY_ACTION);
        filter.addAction(MusicState.REPLAY_ACTION);
        filter.addAction(MusicState.STOP_ACTION);
        filter.addAction(MusicState.NEXT_ACTION);
        filter.addAction(MusicState.PREV_ACTION);
        registerReceiver(musicBroadCast, filter);

        playerUtil = MediaPlayerUtil.getMediaPlayerUtil();

        list = MediaPlayerUtil.getMusicList();

        final MediaPlayer player = playerUtil.getPlayer();
        count = playerUtil.getCount();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (playerUtil.isTimeOut()) {
                    playerUtil.musicPause();
                    playerUtil.setTimeOut(false);
                    playerUtil.playState(playerUtil.getPlay());
                } else {
                    next();
                }
                SPUtil.putInt(SPUtil.CURRENT_MUSIC_ITEM, currentMusicItem);
            }
        });

        if (playerUtil.getSeekBar() == null) {
            return;
        }

        playerUtil.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (count > 0) {
                    player.seekTo(seekBar.getProgress());
                }
            }
        });

        currentMusicItem = SPUtil.getInt(SPUtil.CURRENT_MUSIC_ITEM, 0);
        playerUtil.setNotification(list.get(currentMusicItem));

        playerUtil.musicPrepare(list.get(currentMusicItem));

        setBottomTitle();
    }

    public void setBottomTitle() {
        playerUtil.getTvTitle().setText(list.get(currentMusicItem).getTitle());
    }

    public static int getCurrentMusicItem() {
        return currentMusicItem;
    }

    public void next() {
        if (currentMusicItem >= count - 1) {
            currentMusicItem = 0;
        } else {
            currentMusicItem++;
        }
        playerUtil.next(list.get(currentMusicItem));
    }

    public void prev() {
        if (currentMusicItem <= 0) {
            currentMusicItem = count - 1;
        } else {
            currentMusicItem--;
        }
        playerUtil.prev(list.get(currentMusicItem));
    }

    class MusicBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (count > 0) {
                String action = intent.getAction();
                if (MusicState.PAUSE_ACTION.equals(action)) {
                    playerUtil.musicPause();
                    playerUtil.remoteViewSet(list.get(currentMusicItem));
                } else if (MusicState.PLAY_ACTION.equals(action)) {
                    playerUtil.musicStart();
                    playerUtil.remoteViewSet(list.get(currentMusicItem));
                } else if (MusicState.REPLAY_ACTION.equals(action)) {
                    currentMusicItem = intent.getIntExtra(MusicState.CURRENT_MUSIC_ITEM, 0);
                    if (list != null && list.size() > 0) {
                        playerUtil.musicReStart(list.get(currentMusicItem));
                    }
                } else if (MusicState.STOP_ACTION.equals(action)) {
                    playerUtil.musicStop();
                } else if (MusicState.NEXT_ACTION.equals(action)) {
                    next();
                } else if (MusicState.PREV_ACTION.equals(action)) {
                    prev();
                }
                playerUtil.playState(playerUtil.getPlay());
                SPUtil.putInt(SPUtil.CURRENT_MUSIC_ITEM, currentMusicItem);
            }
        }
    }

    public static Handler getHandler() {
        return handler;
    }
}

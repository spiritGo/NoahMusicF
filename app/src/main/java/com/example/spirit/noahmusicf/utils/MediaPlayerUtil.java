package com.example.spirit.noahmusicf.utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.modle.MusicBean;

import java.util.ArrayList;

public class MediaPlayerUtil {
    private static MediaPlayerUtil mediaPlayerUtil = null;
    private static ArrayList<MusicBean> musicList;
    private MediaPlayer player;
    private TextView tvTitle;
    private SeekBar seekBar;
    private boolean isTimeOut = false;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateProgress();
        }
    };
    private RemoteViews remoteViews;
    private ImageView play;
    private NotificationManager nM;
    private NotificationCompat.Builder builder;
    private View notification;

    public ImageView getPlay() {
        return play;
    }

    public void setPlay(ImageView play) {
        this.play = play;
    }

    private MediaPlayerUtil() {
        player = new MediaPlayer();
    }

    public static MediaPlayerUtil getMediaPlayerUtil() {
        if (mediaPlayerUtil == null) {
            synchronized (MediaPlayerUtil.class) {
                if (mediaPlayerUtil == null) {
                    mediaPlayerUtil = new MediaPlayerUtil();
                }
            }
        }
        return mediaPlayerUtil;
    }

    public static ArrayList<MusicBean> getMusicList() {
        if (musicList == null) {
            ContentResolver resolver = Util.getContext().getContentResolver();
            Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null, null, null, null);
            if (cursor == null) return null;
            musicList = new ArrayList<>();
            while (cursor.moveToNext()) {
                MusicBean musicBean = new MusicBean();
                musicBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore
                        .Audio.Media.TITLE)));
                musicBean.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore
                        .Audio.Media.ARTIST)));
                musicBean.setDuration(cursor.getString(cursor.getColumnIndex(MediaStore
                        .Audio.Media.DURATION)));
                musicBean.setPath(cursor.getString(cursor.getColumnIndex(MediaStore
                        .Audio.Media.DATA)));
                musicList.add(musicBean);
            }
            cursor.close();
        }
        return musicList;
    }

    public void musicStart() {
        if (!player.isPlaying()) {
            player.start();
            updateProgress();
        }
    }

    public int getCount() {
        ContentResolver resolver = Util.getContext().getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        if (cursor == null) return 0;
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void playOrPause(ImageView play) {
        if (musicList != null && musicList.size() > 0) {
            if (player.isPlaying()) {
                play.setSelected(false);
            } else {
                play.setSelected(true);
            }
        }
    }

    public void playState(ImageView play) {
        if (musicList != null && musicList.size() > 0) {
            if (player.isPlaying()) {
                play.setSelected(true);
            } else {
                play.setSelected(false);
            }
        }
    }

    public void musicStop() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
            seekBar = null;
            tvTitle = null;
            removeMessage();
        }
    }

    public void musicPause() {
        if (player.isPlaying()) {
            player.pause();
            removeMessage();
        }
    }

    public void musicReStart(MusicBean bean) {
        try {
            player.reset();
            player.setDataSource(bean.getPath());
            player.prepare();
            player.start();
            seekBar.setMax(Integer.parseInt(bean.getDuration()));
            getTvTitle().setText(bean.getTitle());
            updateProgress();
            setNotification(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void musicPrepare(MusicBean bean) {
        try {
            player.reset();
            player.setDataSource(bean.getPath());
            player.prepare();
            seekBar.setMax(Integer.parseInt(bean.getDuration()));
            getTvTitle().setText(bean.getTitle());
            updateProgress();
            setNotification(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void next(MusicBean bean) {
        musicReStart(bean);
    }

    public void prev(MusicBean bean) {
        musicReStart(bean);
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    private void updateProgress() {
        seekBar.setProgress(player.getCurrentPosition());
        handler.sendEmptyMessageDelayed(0, 350);
    }

    private void removeMessage() {
        handler.removeCallbacksAndMessages(null);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public void setTimeOut(boolean timeOut) {
        isTimeOut = timeOut;
    }

    public boolean isTimeOut() {
        return isTimeOut;
    }

    public void setNotification(MusicBean bean) {
        if (builder == null) {
            nM = (NotificationManager) Util.getContext().getSystemService(Context
                    .NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(Util.getContext());
            remoteViews = new RemoteViews(Util.getContext().getPackageName(), R.layout
                    .notifacation_layout);
            builder.setContent(remoteViews)
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.logo);
        }

        remoteViewSet(bean);
    }

    public void remoteViewSet(MusicBean bean) {
        remoteViews.setTextViewText(R.id.tv_title, bean.getTitle());
        remoteViews.setTextViewText(R.id.tv_musicArtist, bean.getArtist());

        PendingIntent prevIntent = PendingIntent.getBroadcast(Util.getContext(), 1, new Intent
                (MusicState.PREV_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent playIntent = null;
        if (!player.isPlaying()) {
            playIntent = PendingIntent.getBroadcast(Util.getContext(), 2, new Intent
                    (MusicState.PLAY_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            playIntent = PendingIntent.getBroadcast(Util.getContext(), 4, new Intent
                    (MusicState.PAUSE_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
        }
        PendingIntent nextIntent = PendingIntent.getBroadcast(Util.getContext(), 3, new Intent
                (MusicState.NEXT_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.prev, prevIntent);
        remoteViews.setOnClickPendingIntent(R.id.play, playIntent);
        remoteViews.setOnClickPendingIntent(R.id.next, nextIntent);

        remoteViewPlayState();

        if (nM != null) {
            nM.notify(0, builder.build());
        }
    }

    public void remoteViewPlayState() {
        if (!player.isPlaying()) {
            remoteViews.setImageViewResource(R.id.play, R.mipmap.play);
        } else {
            remoteViews.setImageViewResource(R.id.play, R.mipmap.pause);
        }
    }

}

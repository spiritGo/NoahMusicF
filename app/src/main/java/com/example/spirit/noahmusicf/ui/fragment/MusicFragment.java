package com.example.spirit.noahmusicf.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.adapter.MusicListAdapter;
import com.example.spirit.noahmusicf.modle.MusicBean;
import com.example.spirit.noahmusicf.service.MusicService;
import com.example.spirit.noahmusicf.utils.MediaPlayerUtil;
import com.example.spirit.noahmusicf.utils.MusicState;
import com.example.spirit.noahmusicf.utils.SPUtil;
import com.example.spirit.noahmusicf.utils.Util;

import java.util.ArrayList;

public class MusicFragment extends Fragment {

    private View view;
    private ListView lvMusic;
    private TextView tvMusicTitle;
    private SeekBar sbBar;
    private ImageView prev;
    private ImageView play;
    private ImageView next;
    private ArrayList<MusicBean> musicList;
    private MediaPlayerUtil playerUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_music, null);
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        initView();
        initVariable();
        startService();
        initUI();
        return view;
    }

    private void startService() {
        if (musicList.size() > 0) {
            if (!Util.isServiceRunning(MusicService.class.getName())) {
                getActivity().startService(new Intent(getActivity(), MusicService.class));
            }

            //MusicService.getMusicService().setBottomTitle();
            //System.out.println(MusicService.getCurrentMusicItem());
        }
    }

    private void initUI() {
        setColor();
        MusicListAdapter musicListAdapter = new MusicListAdapter(musicList, getActivity());
        lvMusic.setAdapter(musicListAdapter);
        rePlayMusic();
        next();
        prev();
        play();
    }

    private void initVariable() {
        playerUtil = MediaPlayerUtil.getMediaPlayerUtil();
        musicList = MediaPlayerUtil.getMusicList();
        playerUtil.setSeekBar(sbBar);
        playerUtil.setTvTitle(tvMusicTitle);

        playerUtil.setPlay(play);
    }

    private void play() {
        play.setSelected(MediaPlayerUtil.getMediaPlayerUtil().getPlayer().isPlaying());
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean playing = MediaPlayerUtil.getMediaPlayerUtil().isPlaying();
                if (playing) {
                    sendBroadCast(MusicState.PAUSE_ACTION);
                } else {
                    sendBroadCast(MusicState.PLAY_ACTION);
                }
                //playerUtil.playOrPause(play);
            }
        });
    }

    private void prev() {
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadCast(MusicState.PREV_ACTION);
            }
        });
    }

    private void sendBroadCast(String action) {
        Intent intent = new Intent(action);
        getActivity().sendBroadcast(intent);
    }

    private void next() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadCast(MusicState.NEXT_ACTION);
            }
        });
    }

    private void initView() {
        lvMusic = view.findViewById(R.id.lv_music);
        tvMusicTitle = view.findViewById(R.id.tv_musicBottomTitle);
        sbBar = view.findViewById(R.id.sb_bar);
        prev = view.findViewById(R.id.prev);
        play = view.findViewById(R.id.play);
        next = view.findViewById(R.id.next);
    }

    private void rePlayMusic() {
        lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MusicState.REPLAY_ACTION);
                intent.putExtra(MusicState.CURRENT_MUSIC_ITEM, position);
                getActivity().sendBroadcast(intent);
            }
        });
    }

    private void setColor() {
        int titleColor = SPUtil.getInt("titleColor", R.color.blue);
        int color = getResources().getColor(titleColor);
        prev.setColorFilter(color);
        play.setColorFilter(color);
        next.setColorFilter(color);
    }
}

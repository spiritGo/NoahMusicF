package com.example.spirit.noahmusicf.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.spirit.noahmusicf.R;
import com.example.spirit.noahmusicf.adapter.TimingAdapter;
import com.example.spirit.noahmusicf.modle.ArrayData;
import com.example.spirit.noahmusicf.modle.TimingBean;
import com.example.spirit.noahmusicf.service.MusicService;
import com.example.spirit.noahmusicf.utils.MediaPlayerUtil;
import com.example.spirit.noahmusicf.utils.MusicState;
import com.example.spirit.noahmusicf.utils.SPUtil;
import com.example.spirit.noahmusicf.utils.TimerUtil;
import com.example.spirit.noahmusicf.utils.Util;

import java.util.ArrayList;

public class TimingCloseFragment extends Fragment {

    private ListView lvTimingList;
    private View view;
    private ArrayList<TimingBean> timingBeanList;
    private RelativeLayout rlClose;
    private ArrayList<Boolean> isSelectedList;
    private TextView tvInfo;
    private TimerUtil timerUtil;
    private long timingTime = 0;
    private LinearLayout llTimingParent;
    private int width;
    private int height;
    private TimingAdapter timingAdapter;
    private RelativeLayout rl_stopPlay;
    final private int STOP_PLAY = 0;
    final private int EXIT_SOFT = 1;
    private int afterTimingState = STOP_PLAY;
    private TextView stopPlay;
    private RelativeLayout rl_finishPlay;
    private int playFinishVisible = View.VISIBLE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = Util.inflate(R.layout.fragment_timing);
        initView();
        initVariable();
        initUI();
        return view;
    }

    private void initUI() {
        isSelected(-1);
        timingAdapter = new TimingAdapter(timingBeanList, getActivity(), isSelectedList);
        timingAdapter.setIsSelectedList(isSelectedList);
        lvTimingList.setAdapter(timingAdapter);

        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        width = defaultDisplay.getWidth();
        height = defaultDisplay.getHeight();

        boolean running = timerUtil.isRunning();
        if (running) {
            rlCloseIsVisible(View.INVISIBLE, false, timerUtil.getListItem());
        }

        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlCloseIsVisible(View.VISIBLE, true, -1);
                timerUtil.cancel();
                tvInfo.setText(R.string.TimingNotOpen);
            }
        });

        lvTimingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                timerUtil.setListItem(position);
                rlCloseIsVisible(View.INVISIBLE, false, position);

                switch (position) {
                    case 0:
                        timingTime = 10 * 60;
                        showTime();
                        break;
                    case 1:
                        timingTime = 20 * 60;
                        showTime();
                        break;
                    case 2:
                        timingTime = 30 * 60;
                        showTime();
                        break;
                    case 3:
                        timingTime = 60 * 60;
                        showTime();
                        break;
                    case 4:
                        showPopWindow();
                        break;
                }
            }
        });


        View finishPlayPop = Util.inflate(R.layout.timing_finish_play_layout);
        final RelativeLayout rl_finishPlay = finishPlayPop.findViewById(R.id.rl_finishPlay);
        final RelativeLayout rl_exit = finishPlayPop.findViewById(R.id.rl_exit);
        final TextView tv_cancel = finishPlayPop.findViewById(R.id.tv_cancel);
        final PopupWindow popupWindow = new PopupWindow(finishPlayPop, LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popStyle);

        afterTimingState = SPUtil.getInt("afterTimingState", STOP_PLAY);
        if (afterTimingState == STOP_PLAY) {
            rlFinishPlayShow(rl_exit, rl_finishPlay, true);
        } else {
            rlFinishPlayShow(rl_exit, rl_finishPlay, false);
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });


        rl_stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.showAtLocation(llTimingParent, Gravity.BOTTOM, 0, 0);
                backgroundAlpha(0.5f);

                rl_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlFinishPlayShow(rl_exit, rl_finishPlay, false);
                        afterTimingState = EXIT_SOFT;
                        SPUtil.putInt("afterTimingState", TimingCloseFragment.this
                                .afterTimingState);

                        popupWindow.dismiss();
                    }
                });

                rl_finishPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlFinishPlayShow(rl_exit, rl_finishPlay, true);
                        afterTimingState = STOP_PLAY;
                        SPUtil.putInt("afterTimingState", TimingCloseFragment.this
                                .afterTimingState);
                        popupWindow.dismiss();
                    }
                });

                //System.out.println(SPUtil.getInt("afterTimingState",afterTimingState));

                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        playFinishVisible = SPUtil.getInt("playFinishVisible", View.VISIBLE);
        //System.out.println(playFinishVisible+"playFinishVisible");
        this.rl_finishPlay.getChildAt(1).setVisibility(playFinishVisible);

        this.rl_finishPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playFinishVisible = TimingCloseFragment.this.rl_finishPlay.getChildAt(1)
                        .getVisibility();
                if (playFinishVisible == View.VISIBLE) {
                    playFinishVisible = View.INVISIBLE;
                } else {
                    playFinishVisible = View.VISIBLE;
                }
                TimingCloseFragment.this.rl_finishPlay.getChildAt(1).setVisibility
                        (playFinishVisible);
                SPUtil.putInt("playFinishVisible", playFinishVisible);
            }
        });
    }

    private void rlFinishPlayShow(RelativeLayout rl_exit, RelativeLayout rl_finishPlay, boolean
            isFinishPlay) {
        rl_exit.getChildAt(0).setSelected(!isFinishPlay);
        rl_finishPlay.getChildAt(0).setSelected(isFinishPlay);

        if (isFinishPlay) {
            rl_exit.getChildAt(1).setVisibility(View.INVISIBLE);
            rl_finishPlay.getChildAt(1).setVisibility(View.VISIBLE);
            stopPlay.setText(((TextView) rl_finishPlay.getChildAt(0)).getText());
        } else {
            rl_exit.getChildAt(1).setVisibility(View.VISIBLE);
            rl_finishPlay.getChildAt(1).setVisibility(View.INVISIBLE);
            stopPlay.setText(((TextView) rl_exit.getChildAt(0)).getText());
        }
    }

    private void rlCloseIsVisible(int invisible, boolean b, int listItem) {
        rlClose.getChildAt(1).setVisibility(invisible);
        rlClose.getChildAt(0).setSelected(b);

        isSelected(listItem);
        timingAdapter.setIsSelectedList(isSelectedList);
        timingAdapter.notifyDataSetChanged();
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    private void showTime() {
        timerUtil.setTime(timingTime);

        timerUtil.timeSchedule(0, 1000, new TimerUtil.OnScheduleListener() {
            @Override
            public void onSchedule(final long time) {
                Util.runOnService(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tvInfo.setText(Util.timeFormat(time, getString(R.string
                                    .willStopPlay)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onCancel() {
                boolean playing = MediaPlayerUtil.getMediaPlayerUtil().isPlaying();
                if (playing) {
                    if (afterTimingState == STOP_PLAY) {
                        if (playFinishVisible == View.VISIBLE) {
                            MediaPlayerUtil.getMediaPlayerUtil().setTimeOut(true);
                        } else {
                            getActivity().sendBroadcast(new Intent(MusicState.PAUSE_ACTION));
                        }
                    } else {
                        if (playFinishVisible == View.VISIBLE) {
                            MediaPlayerUtil.getMediaPlayerUtil().setTimeOut(true);
                        } else {
                            getActivity().finish();
                            getActivity().stopService(new Intent(getActivity(), MusicService
                                    .class));
                        }
                    }
                    //rlCloseIsVisible(View.VISIBLE, true, -1);
                }
            }
        });
    }

    private void showPopWindow() {
        View popLayout = View.inflate(getActivity(), R.layout
                .timer_pop_window_layout, null);
        final TimePicker tp_time = popLayout.findViewById(R.id.tp_time);
        Button btn_cancel = popLayout.findViewById(R.id.btn_cancel);
        Button btn_ok = popLayout.findViewById(R.id.btn_ok);
        tp_time.setIs24HourView(true);
        tp_time.setCurrentHour(0);
        tp_time.setCurrentMinute(0);


        final PopupWindow popupWindow = new PopupWindow(popLayout, width, (int)
                (height * 0.6f));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.popStyle);
        popupWindow.showAtLocation(llTimingParent, Gravity.BOTTOM, 0, 0);

        backgroundAlpha(0.5f);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = tp_time.getCurrentHour();
                int minute = tp_time.getCurrentMinute();
                timingTime = hour * 60 * 60 + minute * 60;
                showTime();
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void isSelected(int position) {
        for (int i = 0; i < timingBeanList.size(); i++) {
            if (isSelectedList.size() != timingBeanList.size()) {
                isSelectedList.add(i, false);
            } else {
                isSelectedList.set(i, false);
            }
            if (position >= 0) {
                isSelectedList.set(position, true);
            }
        }
    }

    private void initVariable() {
        timingBeanList = ArrayData.getTimingBeanList();
        isSelectedList = new ArrayList<>();
        timerUtil = TimerUtil.getTimerUtil();
    }

    private void initView() {
        lvTimingList = view.findViewById(R.id.lv_timingList);
        rlClose = view.findViewById(R.id.rl_close);
        tvInfo = view.findViewById(R.id.tv_info);
        llTimingParent = view.findViewById(R.id.ll_timingParent);
        rl_stopPlay = view.findViewById(R.id.rl_stopPlay);
        rl_finishPlay = view.findViewById(R.id.rl_finishPlay);
        stopPlay = (TextView) rl_stopPlay.getChildAt(1);
    }


}

package com.kasiengao.ksgframe.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.kaisengao.base.util.TimeUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.listener.OnTimerUpdateListener;
import com.ksg.ksgplayer.player.IPlayer;

/**
 * @ClassName: SmallControllerCover
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/28 15:21
 * @Description: 小型控制器
 */
public class SmallControllerCover extends BaseControllerCover implements View.OnClickListener, OnTimerUpdateListener, SeekBar.OnSeekBarChangeListener {

    private AppCompatSeekBar mProgress;

    private ProgressBar mBottomProgress;

    private AppCompatImageView mPlayState;

    private AppCompatImageView mFullscreen;

    private AppCompatTextView mCurrTime;

    private AppCompatTextView mDurationTime;

    public SmallControllerCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_small_controller, null);
    }

    /**
     * InitViews
     */
    @Override
    public void initViews() {
        super.initViews();
        // Views
        this.mProgress = findViewById(R.id.cover_controller_seek);
        this.mBottomProgress = findViewById(R.id.cover_controller_bottom_seek);
        this.mCurrTime = findViewById(R.id.cover_controller_curr_time);
        this.mDurationTime = findViewById(R.id.cover_controller_duration_time);
        this.mPlayState = findViewById(R.id.cover_controller_play);
        this.mFullscreen = findViewById(R.id.cover_controller_fullscreen);
        // OnClicks
        this.mPlayState.setOnClickListener(this);
        this.mFullscreen.setOnClickListener(this);
        // SeekBar
        this.mProgress.setOnSeekBarChangeListener(this);
    }

    /**
     * View 与页面视图绑定
     */
    @Override
    public void onCoverViewBind() {
        super.onCoverViewBind();
        // 初始状态
        if (getPlayerStateGetter() != null) {
            this.setPlayState(getPlayerStateGetter().getState());
        }
        // 手势滑动 默认关闭
        Bundle bundle = BundlePool.obtain();
        bundle.putBoolean(EventKey.BOOL_DATA, false);
        this.notifyPrivateEvent(
                CoverConstant.CoverKey.KEY_GESTURE,
                CoverConstant.PrivateEvent.CODE_GESTURE_SLIDE_ENABLED,
                bundle);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        super.onPlayerEvent(eventCode, bundle);
        if (eventCode == OnPlayerListener.PLAYER_EVENT_ON_STATE_CHANGE) {
            // 播放状态改变
            this.setPlayState(bundle.getInt(EventKey.INT_DATA));
        }
    }

    /**
     * 设置状态
     */
    private void setPlayState(int state) {
        if (state == IPlayer.STATE_PAUSE) {
            this.mPlayState.setSelected(false);
        } else if (state == IPlayer.STATE_START) {
            this.mPlayState.setSelected(true);
        }
    }

    /**
     * 播放状态
     */
    @Override
    protected void onSwitchPlayState() {
        super.onSwitchPlayState();
        boolean selected = mPlayState.isSelected();
        if (selected) {
            this.requestPause(null);
        } else {
            this.requestResume(null);
        }
        this.mPlayState.setSelected(!selected);
    }

    /**
     * onClick
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cover_controller_play) {
            // 播放状态
            this.onSwitchPlayState();
        } else if (id == R.id.cover_controller_fullscreen) {
            // 进入全屏
            this.notifyCoverEvent(CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_ENTER, null);
        }
    }

    /**
     * 跳转进度
     */
    @Override
    protected void onSeek() {
        super.onSeek();
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_DATA, mProgress.getProgress());
        this.requestSeek(bundle);
    }

    /**
     * 设置 播放进度Time
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    @Override
    protected void setSeekProgressTime(long curr, long duration) {
        super.setSeekProgressTime(curr, duration);
        this.mCurrTime.setText(TimeUtil.getTimeFormatText(mTimeFormat, curr));
        this.mDurationTime.setText(TimeUtil.getTimeFormatText(mTimeFormat, duration));
    }

    /**
     * 设置 播放进度
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    @Override
    protected void setSeekProgress(long curr, long duration) {
        super.setSeekProgress(curr, duration);
        this.mProgress.setMax((int) duration);
        this.mProgress.setProgress((int) curr);
        float secondProgress = mBufferPercentage * 1.0f / 100 * duration;
        // 缓冲进度
        this.mProgress.setSecondaryProgress((int) secondProgress);
    }

    /**
     * 设置 底部播放进度
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    @Override
    protected void setBottomProgress(long curr, long duration) {
        super.setBottomProgress(curr, duration);
        this.mBottomProgress.setMax((int) duration);
        this.mBottomProgress.setProgress((int) curr);
    }

    /**
     * 控制器
     */
    @Override
    protected void onController(boolean isShow) {
        super.onController(isShow);
        this.mPlayState.setVisibility(isShow ? View.VISIBLE : View.GONE);
        this.mCurrTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
        this.mDurationTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
        this.mFullscreen.setVisibility(isShow ? View.VISIBLE : View.GONE);
        this.mProgress.setVisibility(isShow ? View.VISIBLE : View.GONE);
        this.mBottomProgress.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    /**
     * 获取组件等级
     *
     * @return level
     */
    @Override
    public int getCoverLevel() {
        return levelMedium(20);
    }
}
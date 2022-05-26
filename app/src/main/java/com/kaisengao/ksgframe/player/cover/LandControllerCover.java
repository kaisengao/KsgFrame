package com.kaisengao.ksgframe.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.kaisengao.base.util.TextUtil;
import com.kaisengao.base.util.TimeUtil;
import com.kaisengao.ksgframe.R;
import com.kaisengao.ksgframe.common.widget.PlayStateView;
import com.kaisengao.ksgframe.constant.CoverConstant;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.IPlayer;

/**
 * @ClassName: Landscape
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/31 16:25
 * @Description: 横屏控制器
 */
public class LandControllerCover extends BaseControllerCover implements RadioGroup.OnCheckedChangeListener {

    private View mControllerTop;

    private View mControllerBottom;

    private AppCompatSeekBar mProgress;

    private AppCompatTextView mCurrTime;

    private AppCompatTextView mDurationTime;

    private PlayStateView mPlayState;

    private AppCompatImageView mDanmakuState;

    private AppCompatTextView mSpeed;

    private RadioGroup mSpeeds;

    private RadioButton mDefaultSpeed;

    public LandControllerCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_land_controller, null);
    }

    /**
     * InitViews
     */
    @Override
    public void initViews() {
        super.initViews();
        // Views
        this.mControllerTop = findViewById(R.id.cover_controller_top);
        this.mControllerBottom = findViewById(R.id.cover_controller_bottom);
        this.mProgress = findViewById(R.id.cover_controller_seek);
        this.mCurrTime = findViewById(R.id.cover_controller_curr_time);
        this.mDurationTime = findViewById(R.id.cover_controller_duration_time);
        this.mPlayState = findViewById(R.id.cover_controller_play);
        AppCompatImageView fullscreen = findViewById(R.id.cover_controller_fullscreen);
        this.mDanmakuState = findViewById(R.id.cover_controller_danmaku);
        this.mSpeed = findViewById(R.id.cover_controller_speed);
        this.mSpeeds = findViewById(R.id.cover_controller_speeds);
        this.mDefaultSpeed = findViewById(R.id.speed_100);
        // OnClicks
        findViewById(R.id.cover_controller_back).setOnClickListener(this);
        this.mPlayState.setOnClickListener(this);
        fullscreen.setOnClickListener(this);
        this.mSpeed.setOnClickListener(this);
        this.mSpeeds.setOnClickListener(this);
        this.mDanmakuState.setOnClickListener(this);
        // SeekBar
        this.mProgress.setOnSeekBarChangeListener(this);
        // RadioGroup
        this.mSpeeds.setOnCheckedChangeListener(this);
        // Init
        fullscreen.setSelected(true);
        this.mDanmakuState.setSelected(true);
    }

    /**
     * View 与页面视图绑定
     */
    @Override
    public void onCoverViewBind() {
        super.onCoverViewBind();
        // 初始状态
        if (getPlayerInfoGetter() != null) {
            this.setPlayState(getPlayerInfoGetter().getState());
            // 倍速
            float speed = getPlayerInfoGetter().getSpeed();
            if (speed == 1) {
                this.mDefaultSpeed.setChecked(true);
                this.mSpeed.setSelected(false);
                this.mSpeed.setText(TextUtil.getString(R.string.controller_speed));
            } else {
                this.mSpeed.setSelected(true);
                this.mSpeed.setText(String.format("%sX", speed));
            }
        }
        // 开启手势滑动
        this.setGestureEnabled(true, true, true);
    }

    @Override
    public void onCoverViewUnBind() {
        super.onCoverViewUnBind();
        // 关闭手势
        this.setGestureEnabled(false, false, false);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        super.onPlayerEvent(eventCode, bundle);
        switch (eventCode) {
            case OnPlayerListener.PLAYER_EVENT_ON_STATE_CHANGE:
                // 播放状态改变
                this.setPlayState(bundle.getInt(EventKey.INT_DATA));
                break;
            case OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                // 恢复倍数
                this.mSpeed.setSelected(false);
                this.mSpeed.setText(R.string.controller_speed);
                // 全屏切换
                this.notifyCoverEvent(CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_EXIT, null);
                break;
            default:
                break;
        }
    }

    /**
     * 设置状态
     */
    private void setPlayState(int state) {
        if (state == IPlayer.STATE_PAUSE) {
            this.mPlayState.switchToStart();
            this.mPlayState.setSelected(false);
        } else if (state == IPlayer.STATE_START) {
            this.mPlayState.switchToPause();
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
     * 倍速
     *
     * @param checkedId checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        float speed;
        if (checkedId == R.id.speed_75) {
            speed = 0.75f;
        } else if (checkedId == R.id.speed_125) {
            speed = 1.25f;
        } else if (checkedId == R.id.speed_150) {
            speed = 1.5f;
        } else if (checkedId == R.id.speed_200) {
            speed = 2f;
        } else {
            speed = 1.0f;
        }
        Bundle obtain = BundlePool.obtain();
        obtain.putFloat(EventKey.FLOAT_DATA, speed);
        this.requestSpeed(obtain);
        this.mSpeeds.setVisibility(View.GONE);
        this.mSpeed.setSelected(true);
        this.mSpeed.setText(String.format("%sX", speed));
    }

    /**
     * onClick
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cover_controller_back) {
            // Back
            this.notifyCoverEvent(CoverConstant.CoverEvent.CODE_REQUEST_BACK, null);
        } else if (id == R.id.cover_controller_play) {
            // 播放状态
            this.onSwitchPlayState();
        } else if (id == R.id.cover_controller_fullscreen) {
            // 退出全屏
            this.notifyCoverEvent(CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_EXIT, null);
        } else if (id == R.id.cover_controller_danmaku) {
            // 弹幕状态
            if (mDanmakuState.isSelected()) {
                this.notifyPrivateEvent(CoverConstant.CoverKey.KEY_DANMAKU,
                        CoverConstant.PrivateEvent.CODE_REQUEST_DANMAKU_CLOSE, null);
            } else {
                this.notifyPrivateEvent(CoverConstant.CoverKey.KEY_DANMAKU,
                        CoverConstant.PrivateEvent.CODE_REQUEST_DANMAKU_OPEN, null);
            }
            this.mDanmakuState.setSelected(!mDanmakuState.isSelected());
        } else if (id == R.id.cover_controller_speed) {
            // 隐藏控制器
            this.onHideController();
            // Show倍速菜单
            this.mSpeeds.setVisibility(View.VISIBLE);
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
     * 控制器 显示/隐藏
     */
    @Override
    protected void onSwitchController() {
        if (mSpeeds.getVisibility() == View.VISIBLE) {
            this.mSpeeds.setVisibility(View.GONE);
        } else {
            super.onSwitchController();
        }
    }

    /**
     * 控制器
     */
    @Override
    protected void onController(boolean isShow) {
        super.onController(isShow);
        this.mSpeeds.setVisibility(View.GONE);
        this.mControllerTop.setVisibility(isShow ? View.VISIBLE : View.GONE);
        this.mControllerBottom.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取组件等级
     *
     * @return level
     */
    @Override
    public int getCoverLevel() {
        return levelMedium(30);
    }


}
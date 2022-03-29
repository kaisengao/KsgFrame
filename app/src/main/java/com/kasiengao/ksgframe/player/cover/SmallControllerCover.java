package com.kasiengao.ksgframe.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.kaisengao.base.configure.ThreadPool;
import com.kaisengao.base.util.TimeUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.ksg.ksgplayer.cover.BaseCover;
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
public class SmallControllerCover extends BaseCover implements View.OnClickListener, OnTimerUpdateListener, SeekBar.OnSeekBarChangeListener {

    private static final int CONTROLLER_DELAY = 4000;

    private AppCompatSeekBar mProgress;

    private ProgressBar mBottomProgress;

    private AppCompatImageView mPlayState;

    private AppCompatImageView mFullscreen;

    private AppCompatTextView mCurrTime;

    private AppCompatTextView mDurationTime;

    private long mBufferPercentage;

    private boolean mControllerShow = true;

    private boolean mTimerUpdatePause = false;

    private String mTimeFormat;

    private ThreadPool.MainThreadHandler mHandler;

    public SmallControllerCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_small_controller, null);
    }

    /**
     * View 与页面视图绑定
     */
    @Override
    public void onCoverViewBind() {
        // Views
        this.mProgress = findViewById(R.id.cover_controller_seek);
        this.mBottomProgress = findViewById(R.id.cover_controller_bottom_seek);
        this.mCurrTime = findViewById(R.id.cover_controller_curr_time);
        this.mDurationTime = findViewById(R.id.cover_controller_duration_time);
        this.mPlayState = findViewById(R.id.cover_controller_play_state);
        this.mFullscreen = findViewById(R.id.cover_controller_fullscreen);
        // OnClicks
        this.mPlayState.setOnClickListener(this);
        this.mFullscreen.setOnClickListener(this);
        this.findViewById(R.id.cover_controller).setOnClickListener(this);
        // SeekBar
        this.mProgress.setOnSeekBarChangeListener(this);
        // Handler
        this.mHandler = ThreadPool.MainThreadHandler.getInstance();
    }

    /**
     * View 与页面视图解绑
     */
    @Override
    public void onCoverViewUnBind() {

    }

    /**
     * 值 过滤器
     *
     * @return {"Key1","Key2","Key3"}
     */
    @Override
    public String[] getValueFilters() {
        return new String[]{};
    }

    /**
     * 值 事件
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void onValueEvent(String key, Object value) {
    }

    /**
     * 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
                // 新的播放地址 初始化数据
                this.mTimeFormat = null;
                this.mBufferPercentage = 0;
                this.onRenewUi(0, 0);
                break;
            case OnPlayerListener.PLAYER_EVENT_ON_PREPARED:
                // 视频准备
            case OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                // 播放结束
                // 隐藏控制器
                this.onHideController();
                break;
            case OnPlayerListener.PLAYER_EVENT_ON_STATUS_CHANGE:
                // 播放状态改变
                int state = bundle.getInt(EventKey.INT_DATA);
                if (state == IPlayer.STATE_PAUSE) {
                    this.mPlayState.setSelected(false);
                } else if (state == IPlayer.STATE_START) {
                    this.mPlayState.setSelected(true);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 错误事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    /**
     * Cover事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {

    }

    /**
     * Cover私有事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onPrivateEvent(int eventCode, Bundle bundle) {

    }

    /**
     * 生产者事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onProducerEvent(int eventCode, Bundle bundle) {

    }

    /**
     * 生产者事件 数据
     *
     * @param key  key
     * @param data data
     */
    @Override
    public void onProducerData(String key, Object data) {

    }

    /**
     * onClick
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cover_controller_play_state) {
            // 播放状态
            this.onSwitchPlayState();
        } else if (id == R.id.cover_controller_fullscreen) {
            // 全屏切换
            this.notifyCoverEvent(CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_TOGGLE, null);
            // 隐藏控制器
            this.onHideController();
        } else if (id == R.id.cover_controller) {
            // 控制器 显示/隐藏
            if (mControllerShow) {
                this.onHideController();
            } else {
                this.onShowController();
            }
        }
    }

    /**
     * 播放状态
     */
    private void onSwitchPlayState() {
        boolean selected = mPlayState.isSelected();
        if (selected) {
            this.requestPause(null);
        } else {
            this.requestResume(null);
        }
        this.mPlayState.setSelected(!selected);
    }

    /**
     * @param curr             当前进度
     * @param duration         总进度
     * @param bufferPercentage 缓冲进度
     */
    @Override
    public void onTimerUpdate(long curr, long duration, long bufferPercentage) {
        // 验证是否暂停了进度更新
        if (mTimerUpdatePause) {
            return;
        }
        // 缓冲进度
        this.mBufferPercentage = bufferPercentage;
        // 时长格式 00：00 or 00:00:00
        if (TextUtils.isEmpty(mTimeFormat) || duration != mProgress.getMax()) {
            this.mTimeFormat = TimeUtil.getFormat(duration);
        }
        // 更新Ui
        this.onRenewUi(curr, duration);
    }

    /**
     * 更新Ui
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    private void onRenewUi(long curr, long duration) {
        // 更新进度
        this.setSeekProgressTime(curr, duration);
        this.setSeekProgress(curr, duration);
        this.setBottomProgress(curr, duration);
    }

    /**
     * 设置 播放进度Time
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    private void setSeekProgressTime(long curr, long duration) {
        this.mCurrTime.setText(TimeUtil.getTime(mTimeFormat, curr));
        this.mDurationTime.setText(TimeUtil.getTime(mTimeFormat, duration));
    }

    /**
     * 设置 播放进度
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    private void setSeekProgress(long curr, long duration) {
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
    private void setBottomProgress(long curr, long duration) {
        this.mBottomProgress.setMax((int) duration);
        this.mBottomProgress.setProgress((int) curr);
    }

    /**
     * Seek事件
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // 更新Ui
        if (fromUser) {
            this.onRenewUi(progress, seekBar.getMax());
        }
    }

    /**
     * Seek事件
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // 暂停进度更新
        this.mTimerUpdatePause = true;
        // 暂停控制器的计时器
        if (mControllerShow) {
            onStopTimerController();
        }
    }

    /**
     * Seek事件
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // 恢复控制器的计时器
        if (mControllerShow) {
            onStartTimerController();
        }
        // 跳转进度
        this.mHandler.removeCallbacks(mSeekEventRunnable);
        this.mHandler.post(mSeekEventRunnable, 300);
    }

    /**
     * Seek跳转进度
     */
    private final Runnable mSeekEventRunnable = () -> {
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_DATA, mProgress.getProgress());
        this.requestSeek(bundle);
        // 开放进度更新
        this.mTimerUpdatePause = false;
    };

    /**
     * 显示 控制器
     */
    private void onShowController() {
        this.onController(true);
        this.onStartTimerController();
    }

    /**
     * 隐藏 控制器
     */
    private void onHideController() {
        this.onController(false);
        this.onStopTimerController();
    }


    /**
     * 计时器 隐藏控制器 开始
     */
    private void onStartTimerController() {
        this.onStopTimerController();
        this.mHandler.post(mControllerRunnable, CONTROLLER_DELAY);
    }

    /**
     * 计时器 隐藏控制器 停止
     */
    private void onStopTimerController() {
        this.mHandler.removeCallbacks(mControllerRunnable);
    }

    /**
     * 计时器 控制器
     */
    private final Runnable mControllerRunnable = this::onHideController;

    /**
     * 控制器
     */
    private void onController(boolean isShow) {
        this.mControllerShow = isShow;
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
        return levelMedium(10);
    }


}
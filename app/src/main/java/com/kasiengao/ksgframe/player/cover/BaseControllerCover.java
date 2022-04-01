package com.kasiengao.ksgframe.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.kaisengao.base.configure.ThreadPool;
import com.kaisengao.base.util.TimeUtil;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.ksg.ksgplayer.cover.BaseCover;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.listener.OnTimerUpdateListener;

/**
 * @ClassName: BaseControllerCover
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/31 17:15
 * @Description: 抽离一些公共方法
 */
public abstract class BaseControllerCover extends BaseCover implements View.OnClickListener, OnTimerUpdateListener, SeekBar.OnSeekBarChangeListener {

    private static final int CONTROLLER_DELAY = 4000;

    protected long mBufferPercentage;

    protected boolean mControllerShow = true;

    protected boolean mTimerUpdatePause = false;

    protected String mTimeFormat;

    protected ThreadPool.MainThreadHandler mHandler;

    private final Runnable mSeekRunnable = this::onSeek;

    private final Runnable mControllerRunnable = this::onHideController;

    public BaseControllerCover(Context context) {
        super(context);
    }

    /**
     * InitViews
     */
    @Override
    public void initViews() {
        // Handler
        this.mHandler = ThreadPool.MainThreadHandler.getInstance();
    }

    /**
     * View 与页面视图绑定
     */
    @Override
    public void onCoverViewBind() {
        if (mControllerShow) {
            this.onStartTimerController();
        }
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
        return new String[]{
                CoverConstant.ValueKey.KEY_SWITCH_CONTROLLER,
                CoverConstant.ValueKey.KEY_HIDE_CONTROLLER,
                CoverConstant.ValueKey.KEY_SWITCH_PLAY,
        };
    }

    /**
     * 值 事件
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void onValueEvent(String key, Object value) {
        switch (key) {
            case CoverConstant.ValueKey.KEY_SWITCH_CONTROLLER:
                if (value != null) {
                    if ((boolean) value) {
                        this.onShowController();
                    } else {
                        this.onHideController();
                    }
                } else {
                    // 控制器 显示/隐藏
                    this.onSwitchController();
                }
                break;
            case CoverConstant.ValueKey.KEY_HIDE_CONTROLLER:
                // 隐藏控制器
                this.onHideController();
                break;
            case CoverConstant.ValueKey.KEY_SWITCH_PLAY:
                // 播放状态 播放/暂停
                this.onSwitchPlayState();
                break;
            default:
                break;
        }
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
                // 隐藏控制器
                this.onHideController();
                break;
            case OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                // 恢复倍数
                Bundle obtain = BundlePool.obtain();
                obtain.putFloat(EventKey.FLOAT_DATA, 1.0f);
                this.requestSpeed(obtain);
                // 隐藏控制器
                this.onHideController();
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
        // 进度自动更新状态
        if (eventCode == CoverConstant.CoverEvent.CODE_REQUEST_TIMER_UPDATE_STATE) {
            if (bundle != null) {
                this.onRenewUi(bundle.getLong(EventKey.LONG_ARG1), bundle.getLong(EventKey.LONG_ARG2));
                this.mTimerUpdatePause = bundle.getBoolean(EventKey.BOOL_DATA);
            }
        }
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
        this.mTimeFormat = TimeUtil.getFormat(duration);
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
    protected void setSeekProgressTime(long curr, long duration) {

    }

    /**
     * 设置 播放进度
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    protected void setSeekProgress(long curr, long duration) {

    }

    /**
     * 设置 底部播放进度
     *
     * @param curr     播放进度
     * @param duration 总进度
     */
    protected void setBottomProgress(long curr, long duration) {

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
        this.mHandler.removeCallbacks(mSeekRunnable);
        this.mHandler.post(mSeekRunnable, 300);
    }

    /**
     * 跳转进度
     */
    protected void onSeek() {
        // 开放进度更新
        this.mTimerUpdatePause = false;
    }

    /**
     * 播放状态
     */
    protected void onSwitchPlayState() {

    }

    /**
     * 控制器 显示/隐藏
     */
    protected void onSwitchController() {
        if (mControllerShow) {
            this.onHideController();
        } else {
            this.onShowController();
        }
    }

    /**
     * 显示 控制器
     */
    protected void onShowController() {
        this.onController(true);
        this.onStartTimerController();
    }

    /**
     * 隐藏 控制器
     */
    protected void onHideController() {
        this.onController(false);
        this.onStopTimerController();
    }

    /**
     * 计时器 隐藏控制器 开始
     */
    protected void onStartTimerController() {
        this.onStopTimerController();
        this.mHandler.post(mControllerRunnable, CONTROLLER_DELAY);
    }

    /**
     * 计时器 隐藏控制器 停止
     */
    protected void onStopTimerController() {
        this.mHandler.removeCallbacks(mControllerRunnable);
    }

    /**
     * 控制器
     */
    protected void onController(boolean isShow) {
        this.mControllerShow = isShow;
        this.getValuePool().putObject(CoverConstant.ValueKey.KEY_SWITCH_CONTROLLER, isShow, true, false);
    }

    /**
     * 释放
     */
    @Override
    public void release() {
        super.release();
        this.onStopTimerController();
        this.mHandler.removeCallbacks(mSeekRunnable);
    }
}
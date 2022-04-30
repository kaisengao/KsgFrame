package com.kasiengao.ksgframe.player.cover;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.kaisengao.base.configure.ActivityManager;
import com.kaisengao.base.configure.ThreadPool;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.util.VibratorUtil;
import com.kasiengao.ksgframe.common.widget.GestureTipsView;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.ksg.ksgplayer.cover.BaseCover;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.helper.GestureTouchHelper;
import com.ksg.ksgplayer.listener.OnTouchGestureListener;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.state.PlayerStateGetter;

/**
 * @ClassName: GestureCover
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/28 13:02
 * @Description: 手势操作
 */
public class GestureCover extends BaseCover implements View.OnTouchListener, OnTouchGestureListener, View.OnLayoutChangeListener {

    private long mSlidProgress;

    private float mDefaultSpeed;

    private LinearLayout mGestureTipsRoot;

    private LinearLayout mGestureTipsSlidingRoot;

    private LinearLayout mGestureTipsSpeedRoot;

    private AppCompatImageView mGestureIcon;

    private ProgressBar mGestureProgress;

    private AppCompatTextView mGestureSlidingTime;

    private ProgressBar mGestureSlidingProgress;

    private GestureTipsView mGestureTipsView;

    private GestureTouchHelper mGestureTouchHelper;

    private ThreadPool.MainThreadHandler mHandler;

    private final Bundle seekBundle = BundlePool.obtain();

    private final Runnable mSeekRunnable = this::onSeek;

    public GestureCover(Context context) {
        super(context);

    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_gesture, null);
    }

    /**
     * InitViews
     */
    @Override
    public void initViews() {
        // 手势操作 提示View
        this.mGestureTipsView = new GestureTipsView();
        // 手势操作帮助类
        Activity activity = ActivityManager.getInstance().currentActivity();
        this.mGestureTouchHelper = new GestureTouchHelper(activity, this);
        //  Views
        this.mGestureTipsRoot = findViewById(R.id.cover_gesture);
        this.mGestureTipsSlidingRoot = findViewById(R.id.cover_gesture_sliding);
        this.mGestureTipsSpeedRoot = findViewById(R.id.cover_gesture_speed);
        this.mGestureIcon = findViewById(R.id.cover_gesture_icon);
        this.mGestureProgress = findViewById(R.id.cover_gesture_progress);
        this.mGestureSlidingTime = findViewById(R.id.cover_gesture_sliding_time);
        this.mGestureSlidingProgress = findViewById(R.id.cover_gesture_sliding_progress);
        // OnTouch事件
        this.getCoverView().setOnTouchListener(this);
        // Handler
        this.mHandler = ThreadPool.MainThreadHandler.getInstance();
    }

    /**
     * View 与页面视图绑定
     */
    @Override
    public void onCoverViewBind() {
        this.getCoverView().addOnLayoutChangeListener(this);
    }

    /**
     * View 与页面视图解绑
     */
    @Override
    public void onCoverViewUnBind() {
        this.getCoverView().removeOnLayoutChangeListener(this);
    }

    /**
     * 视频总时长
     */
    private long getDuration() {
        PlayerStateGetter playerStateGetter = getPlayerStateGetter();
        return playerStateGetter == null ? 0 : playerStateGetter.getDuration();
    }

    /**
     * 视频播放进度
     */
    private long getProgress() {
        PlayerStateGetter playerStateGetter = getPlayerStateGetter();
        return playerStateGetter == null ? 0 : playerStateGetter.getProgress();
    }

    /**
     * onTouch
     *
     * @param v     v
     * @param event event
     * @return boolean
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.mGestureTouchHelper.onTouch(event);
    }

    /**
     * Down事件
     */
    @Override
    public void onDown() {
        // 初始化基参
        this.mGestureTouchHelper.setParams(getProgress(), getDuration());
        this.mGestureSlidingProgress.setMax((int) getDuration());
    }

    /**
     * 单击
     */
    @Override
    public void onSingleTapGesture() {
        this.getValuePool().putObject(CoverConstant.ValueKey.KEY_GESTURE_SINGLE_TAP, null);
    }

    /**
     * 双击
     */
    @Override
    public void onDoubleTapGesture() {
        this.getValuePool().putObject(CoverConstant.ValueKey.KEY_GESTURE_DOUBLE_TAB, null, false);
    }

    /**
     * 长按
     */
    @Override
    public void onLongPress() {
        // 倍速播放
        if (getPlayerStateGetter() != null && getPlayerStateGetter().getState() == IPlayer.STATE_START) {
            this.mDefaultSpeed = getPlayerStateGetter().getSpeed();
            // 提示
            this.mGestureTipsView
                    .setRooView(mGestureTipsSpeedRoot)
                    .show();
            // 震动
            VibratorUtil.vibrate(100);
            // 通知 控制器 隐藏
            this.getValuePool().putObject(CoverConstant.ValueKey.KEY_HIDE_CONTROLLER, null);
            // 在原有速度的基础上x2
            Bundle obtain = BundlePool.obtain();
            obtain.putFloat(EventKey.FLOAT_DATA, mDefaultSpeed * 2.0f);
            this.requestSpeed(obtain);
        }
    }

    /**
     * 长按结束
     */
    @Override
    public void onLongPressEnd() {
        this.mGestureTipsView.dismiss();
        // 停止倍速
        Bundle obtain = BundlePool.obtain();
        obtain.putFloat(EventKey.FLOAT_DATA, mDefaultSpeed);
        this.requestSpeed(obtain);
    }

    /**
     * 亮度
     *
     * @param percent 百分比
     */
    @Override
    public void onSlideBrightness(int percent) {
        // 提示
        this.mGestureTipsView
                .setRooView(mGestureTipsRoot)
                .setBrightnessIcon(mGestureIcon, percent)
                .setProgress(mGestureProgress, percent)
                .show();
    }

    /**
     * 音量
     *
     * @param percent 百分比
     */
    @Override
    public void onSlideVolume(int percent) {
        // 提示
        this.mGestureTipsView
                .setRooView(mGestureTipsRoot)
                .setVolumeIcon(mGestureIcon, percent)
                .setProgress(mGestureProgress, percent)
                .show();
    }

    /**
     * 快进退
     *
     * @param percent 百分比
     * @param time    时间
     */
    @Override
    public void onSlideSeek(float percent, String time) {
        // 提示
        this.mGestureTipsView
                .setRooView(mGestureTipsSlidingRoot)
                .setProgress(mGestureSlidingProgress, (int) percent)
                .setTime(mGestureSlidingTime, time)
                .show();
        // 通知 通知 滑动进度且停止进度自动更新
        this.notifySlideSeek(true);
    }

    /**
     * 滑动结束 快进退
     */
    @Override
    public void onSlideEndSeek() {
        this.mGestureTipsView.dismiss();
        // 跳转进度
        if (mGestureTouchHelper.getSlideProgress() >= 0) {
            this.mHandler.removeCallbacks(mSeekRunnable);
            this.mHandler.post(mSeekRunnable, 300);
        }
        // 通知 滑动进度且恢复进度自动更新
        this.notifySlideSeek(false);
    }

    /**
     * 滑动结束
     */
    @Override
    public void onSlideEnd() {
        this.mGestureTipsView.dismiss();
    }

    /**
     * 通知 滑动进度
     *
     * @param timerUpdatePause 进度自动更新状态
     */
    private void notifySlideSeek(boolean timerUpdatePause) {
        this.mSlidProgress = mGestureTouchHelper.getSlideProgress();
        seekBundle.putLong(EventKey.LONG_ARG1, mSlidProgress);
        seekBundle.putLong(EventKey.LONG_ARG2, mGestureTouchHelper.getDuration());
        seekBundle.putBoolean(EventKey.BOOL_DATA, timerUpdatePause);
        this.notifyCoverEvent(CoverConstant.CoverEvent.CODE_REQUEST_TIMER_UPDATE_STATE, seekBundle);
    }

    /**
     * 跳转进度
     */
    protected void onSeek() {
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_DATA, mSlidProgress);
        this.requestSeek(bundle);
    }


    /**
     * 布局改变事件
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        this.mGestureTouchHelper.setView(v.getWidth(), v.getHeight());
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
        if (eventCode == CoverConstant.PrivateEvent.CODE_GESTURE_SLIDE_ENABLED) {
            // 手势滑动 开启/关闭
            this.mGestureTouchHelper.setGestureSlideEnabled(bundle.getBoolean(EventKey.BOOL_DATA, true));
        }
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
     * 获取组件等级
     *
     * @return level
     */
    @Override
    public int getCoverLevel() {
        return levelLow(5);
    }

    /**
     * 释放
     */
    @Override
    public void release() {
        super.release();
        this.mHandler.removeCallbacks(mSeekRunnable);
    }
}

package com.kasiengao.ksgframe.ui.trainee.player.cover;

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

import com.kaisengao.base.configure.ThreadPool;
import com.kaisengao.base.util.CommonUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.widget.GestureTipsView;
import com.ksg.ksgplayer.assist.DataInter;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.helper.GestureTouchHelper;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.listener.OnTouchGestureListener;
import com.ksg.ksgplayer.receiver.BaseCover;
import com.ksg.ksgplayer.receiver.IReceiverGroup;
import com.ksg.ksgplayer.receiver.PlayerStateGetter;

/**
 * @ClassName: GestureCover
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/28 13:02
 * @Description: 手势操作 Cover
 */
public class GestureCover extends BaseCover implements View.OnTouchListener, OnTouchGestureListener {

    private LinearLayout mGestureTipsRoot;

    private LinearLayout mGestureTipsSlidingRoot;

    private AppCompatImageView mGestureIcon;

    private ProgressBar mGestureProgress;

    private AppCompatImageView mGestureSlidingStatus;

    private AppCompatTextView mGestureSlidingTime;

    private long mSlidProgress;

    private final GestureTipsView mGestureTipsView;

    private final GestureTouchHelper mGestureTouchHelper;

    private ThreadPool.MainThreadHandler mHandler;

    private final Bundle seekBundle = BundlePool.obtain();

    public GestureCover(Context context) {
        super(context);
        Activity activity = CommonUtil.scanForActivity(getContext());
        // 手势操作 提示View
        this.mGestureTipsView = new GestureTipsView();
        // 手势操作帮助类
        this.mGestureTouchHelper = new GestureTouchHelper(activity, this);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_gesture, null);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        this.mGestureTipsRoot = findViewById(R.id.cover_gesture);
        this.mGestureTipsSlidingRoot = findViewById(R.id.cover_gesture_sliding);
        this.mGestureIcon = findViewById(R.id.cover_gesture_icon);
        this.mGestureProgress = findViewById(R.id.cover_gesture_progress);
        this.mGestureSlidingStatus = findViewById(R.id.cover_gesture_sliding_status);
        this.mGestureSlidingTime = findViewById(R.id.cover_gesture_sliding_time);
        // 获取视图宽高
        this.getScreenSize();
        // OnTouch事件
        this.getView().setOnTouchListener(this);
        // Handler
        this.mHandler = ThreadPool.MainThreadHandler.getInstance();
        // 组件间通信
        this.getGroupValue().registerOnGroupValueUpdateListener(mGroupValueUpdateListener);
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
        // 组件间通信
        this.getGroupValue().unregisterOnGroupValueUpdateListener(mGroupValueUpdateListener);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        // 数据源
        if (eventCode == OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET) {
            DataSource dataSource = (DataSource) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
            if (dataSource != null) {
                // 配置 直播页面不可 快进退
                this.mGestureTouchHelper.setSlideHEnabled(!dataSource.isLive());
            }
        }
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.mGestureTouchHelper.onTouch(event);
    }

    /**
     * 组件间通信
     */
    private final IReceiverGroup.OnGroupValueUpdateListener mGroupValueUpdateListener = new IReceiverGroup.OnGroupValueUpdateListener() {
        @Override
        public String[] filterKeys() {
            return new String[]{
                    DataInter.Key.KEY_SCREEN_ORIENTATION,
                    DataInter.Key.KEY_FULLSCREEN,
            };
        }

        @Override
        public void onValueUpdate(String key, Object value) {
            switch (key) {
                case DataInter.Key.KEY_SCREEN_ORIENTATION:
                    // 横竖屏切换 重新获取视图宽高
                    getScreenSize();
                    break;
                case DataInter.Key.KEY_FULLSCREEN:
                    // 全屏状态切换 重新获取视图宽高
                    getScreenSize();
                    // 是否开启滑动手势 (全屏开启)
                    mGestureTouchHelper.setSlideEnabled((boolean) value);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 亮度手势，手指在Layout左半部上下滑动时候调用
     *
     * @param percent 百分比
     */
    @Override
    public void onBrightnessGesture(int percent) {
        // 提示
        mGestureTipsView
                .setRooView(mGestureTipsRoot)
                .setBrightnessIcon(mGestureIcon, percent)
                .setProgress(mGestureProgress, percent)
                .show();
    }

    /**
     * 音量手势，手指在Layout右半部上下滑动时候调用
     *
     * @param percent 百分比
     */
    @Override
    public void onVolumeGesture(int percent) {
        // 提示
        mGestureTipsView
                .setRooView(mGestureTipsRoot)
                .setVolumeIcon(mGestureIcon, percent)
                .setProgress(mGestureProgress, percent)
                .show();
    }

    /**
     * 快进快退手势，手指在Layout左右滑动的时候调用
     *
     * @param percent 百分比
     * @param time    时间
     */
    @Override
    public void onSeekGesture(float percent, String time) {
        // 提示
        mGestureTipsView
                .setRooView(mGestureTipsSlidingRoot)
                .setSliding(mGestureSlidingStatus, percent)
                .setTime(mGestureSlidingTime, time)
                .show();
        // 通知 ControllerCover 自动更新进度
        notifyReceiverControllerSeek(true);
    }

    /**
     * 单击手势，确认是单击的时候调用
     */
    @Override
    public void onSingleTapGesture() {
        // 组件间通信 通知 ControllerCover
        this.getGroupValue().putString(DataInter.Key.KEY_CONTROLLER_STATUS, "");
    }

    /**
     * 双击手势，确认是双击的时候调用
     */
    @Override
    public void onDoubleTapGesture() {
        // 组件间通信 通知 ControllerCover
        this.getGroupValue().putString(DataInter.Key.KEY_CONTROLLER_PLAY_STATUS, "");
    }

    /**
     * 按下手势，第一根手指按下时候调用
     */
    @Override
    public void onDown() {
        // 初始化基参
        this.mGestureTouchHelper.setParams(getProgress(), getDuration());
    }

    /**
     * 快进后退手势 滑动结束
     */
    @Override
    public void onSeekEndGesture() {
        this.mGestureTipsView.dismiss();
        // 更新进度
        if (mGestureTouchHelper.getSlideProgress() >= 0) {
            mHandler.removeCallbacks(mSeekEventRunnable);
            mHandler.post(mSeekEventRunnable, 300);
        }
        // 通知 ControllerCover 自动更新进度
        notifyReceiverControllerSeek(false);
    }

    /**
     * 滑动结束
     */
    @Override
    public void onEndGesture() {
        mGestureTipsView.dismiss();
    }

    /**
     * 通知 ControllerCover 自动更新进度
     *
     * @param timerUpdatePause 暂停
     */
    private void notifyReceiverControllerSeek(boolean timerUpdatePause) {
        this.mSlidProgress = mGestureTouchHelper.getSlideProgress();
        seekBundle.putLong(EventKey.LONG_ARG1, mSlidProgress);
        seekBundle.putLong(EventKey.LONG_ARG2, mGestureTouchHelper.getDuration());
        seekBundle.putBoolean(EventKey.BOOL_DATA, timerUpdatePause);
        notifyReceiverPrivateEvent(
                DataInter.ReceiverKey.KEY_CONTROLLER_COVER,
                DataInter.PrivateEvent.EVENT_CODE_GESTURE_SLIDE_SEEK,
                seekBundle);
    }

    /**
     * 进度跳转
     */
    private final Runnable mSeekEventRunnable = () -> {
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_DATA, mSlidProgress);
        this.requestSeek(bundle);
    };

    /**
     * 获取屏幕真实宽高
     */
    private void getScreenSize() {
        this.getView().addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            this.mGestureTouchHelper.setView(v.getWidth(), v.getHeight());
        });
    }

    @Override
    public int getCoverLevel() {
        return levelHigh(1);
    }
}

package com.kasiengao.ksgframe.java.player.cover;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.kasiengao.base.configure.ThreadPool;
import com.kasiengao.base.util.CommonUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.widget.GestureTipsView;
import com.ksg.ksgplayer.assist.DataInter;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.helper.GestureTouchHelper;
import com.ksg.ksgplayer.listener.OnTouchGestureListener;
import com.ksg.ksgplayer.receiver.BaseCover;
import com.ksg.ksgplayer.receiver.IReceiverGroup;
import com.ksg.ksgplayer.receiver.PlayerStateGetter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ClassName: GestureCover
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/28 13:02
 * @Description: 手势操作 Cover
 */
public class GestureCover extends BaseCover implements View.OnTouchListener {

    private Unbinder mBind;

    @BindView(R.id.cover_gesture)
    LinearLayout mGestureTipsRoot;
    @BindView(R.id.cover_gesture_sliding)
    LinearLayout mGestureTipsSlidingRoot;
    @BindView(R.id.cover_gesture_icon)
    AppCompatImageView mGestureIcon;
    @BindView(R.id.cover_gesture_progress)
    ProgressBar mGestureProgress;
    @BindView(R.id.cover_gesture_sliding_status)
    AppCompatImageView mGestureSlidingStatus;
    @BindView(R.id.cover_gesture_sliding_time)
    AppCompatTextView mGestureSlidingTime;

    private long mSlidProgress;

    private final GestureTipsView mGestureTipsView;

    private final GestureTouchHelper mGestureTouchHelper;

    private ThreadPool.MainThreadHandler mHandler;

    public GestureCover(Context context) {
        super(context);
        Activity activity = CommonUtil.scanForActivity(getContext());
        // 手势操作 提示View
        this.mGestureTipsView = new GestureTipsView();
        // 手势操作帮助类
        this.mGestureTouchHelper = new GestureTouchHelper(activity);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_gesture, null);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        this.mBind = ButterKnife.bind(this, getView());
        // 获取视图宽高
        this.getScreenSize();
        // OnTouch事件
        this.getView().setOnTouchListener(this);
        // Handler
        this.mHandler = ThreadPool.MainThreadHandler.getInstance();
        // 注册手势事件
        this.mGestureTouchHelper.setOnTouchGestureListener(mTouchGestureListener);
        // 组件间通信
        this.getGroupValue().registerOnGroupValueUpdateListener(mGroupValueUpdateListener);
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
        this.mBind.unbind();
        // 取消手势事件
        this.mGestureTouchHelper.setOnTouchGestureListener(null);
        // 组件间通信
        this.getGroupValue().unregisterOnGroupValueUpdateListener(mGroupValueUpdateListener);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {

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
    private IReceiverGroup.OnGroupValueUpdateListener mGroupValueUpdateListener = new IReceiverGroup.OnGroupValueUpdateListener() {
        @Override
        public String[] filterKeys() {
            return new String[]{
                    DataInter.Key.KEY_FULLSCREEN
            };
        }

        @Override
        public void onValueUpdate(String key, Object value) {
            // 全屏状态切换
            if (DataInter.Key.KEY_FULLSCREEN.equals(key)) {
                boolean fullScreen = (boolean) value;
                // 获取视图宽高
                getScreenSize();
                // 是否开启滑动手势 (全屏开启)
                mGestureTouchHelper.setSlideEnabled(fullScreen);
            }
        }
    };

    /**
     * 手势操作事件
     */
    private OnTouchGestureListener mTouchGestureListener = new OnTouchGestureListener() {

        final Bundle controllerBundle = BundlePool.obtain();

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
         * @param time 时间
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
            notifyReceiverController(true);
        }

        /**
         * 单击手势，确认是单击的时候调用
         */
        @Override
        public void onSingleTapGesture() {
            // 组件间通信 通知 ControllerCover
            getGroupValue().putString(DataInter.Key.KEY_CONTROLLER_STATUS, "");
        }

        /**
         * 双击手势，确认是双击的时候调用
         */
        @Override
        public void onDoubleTapGesture() {
            // 组件间通信 通知 ControllerCover
            getGroupValue().putString(DataInter.Key.KEY_CONTROLLER_PLAY_STATUS, "");
        }

        /**
         * 按下手势，第一根手指按下时候调用
         */
        @Override
        public void onDown() {
            // 初始化基参
            mGestureTouchHelper.setParams(getProgress(), getDuration());
        }

        /**
         * 快进后退手势 滑动结束
         */
        @Override
        public void onSeekEndGesture() {
            mGestureTipsView.dismiss();
            // 更新进度
            if (mGestureTouchHelper.getSlideProgress() >= 0) {
                mHandler.removeCallbacks(mSeekEventRunnable);
                mHandler.post(mSeekEventRunnable, 300);
            }
            // 通知 ControllerCover 自动更新进度
            notifyReceiverController(false);
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
         * @param  timerUpdatePause 暂停
         */
        private void notifyReceiverController(boolean timerUpdatePause) {
            mSlidProgress = mGestureTouchHelper.getSlideProgress();
            controllerBundle.putLong(EventKey.LONG_ARG1, mSlidProgress);
            controllerBundle.putLong(EventKey.LONG_ARG2, mGestureTouchHelper.getDuration());
            controllerBundle.putBoolean(EventKey.BOOL_DATA, timerUpdatePause);
            notifyReceiverPrivateEvent(
                    DataInter.ReceiverKey.KEY_CONTROLLER_COVER,
                    DataInter.PrivateEvent.EVENT_CODE_GESTURE_SLIDE_SEEK,
                    controllerBundle);
        }
    };

    /**
     * 进度跳转
     */
    private Runnable mSeekEventRunnable = () -> {
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_DATA, mSlidProgress);
        this.requestSeek(bundle);
    };

    /**
     * 获取屏幕真实宽高
     */
    private void getScreenSize() {
        // 竖屏获取当前view的宽高
        this.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGestureTouchHelper.setView(getView().getWidth(), getView().getHeight());
                getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public int getCoverLevel() {
        return super.levelHigh(1);
    }
}

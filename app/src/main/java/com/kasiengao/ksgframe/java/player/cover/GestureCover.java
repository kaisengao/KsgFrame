package com.kasiengao.ksgframe.java.player.cover;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatImageView;

import com.kasiengao.base.util.CommonUtil;
import com.kasiengao.base.util.ToastUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.widget.GestureTipsView;
import com.ksg.ksgplayer.assist.DataInter;
import com.ksg.ksgplayer.helper.BrightnessHelper;
import com.ksg.ksgplayer.helper.GestureTouchHelper;
import com.ksg.ksgplayer.helper.VolumeHelper;
import com.ksg.ksgplayer.listener.OnTouchGestureListener;
import com.ksg.ksgplayer.receiver.BaseCover;
import com.ksg.ksgplayer.receiver.IReceiverGroup;

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
    @BindView(R.id.cover_gesture_icon)
    AppCompatImageView mGestureIcon;
    @BindView(R.id.cover_gesture_progress)
    ProgressBar mGestureProgress;

    private int mScreenWidth, mScreenHeight;

    private int mMaxAppLight;

    private int mCurrentVolume;

    private boolean mIsLandscape;

    private final Activity mActivity;

    private final VolumeHelper mVolumeHelper;

    private final GestureTipsView mGestureTipsView;

    private final GestureTouchHelper mGestureTouchHelper;

    public GestureCover(Context context) {
        super(context);
        this.mActivity = CommonUtil.scanForActivity(getContext());
        // 音频帮助类
        this.mVolumeHelper = new VolumeHelper(getContext());
        // 手势操作 提示View
        this.mGestureTipsView = new GestureTipsView();
        // 手势操作帮助类
        this.mGestureTouchHelper = new GestureTouchHelper(getContext());
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
        this.getScreenSize(getContext());
        // OnTouch事件
        this.getView().setOnTouchListener(this);
        // setRooView
        this.mGestureTipsView.setRooView(mGestureTipsRoot);
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
                    DataInter.Key.KEY_IS_LANDSCAPE
            };
        }

        @Override
        public void onValueUpdate(String key, Object value) {
            switch (key) {
                case DataInter.Key.KEY_IS_LANDSCAPE:
                    mIsLandscape = (boolean) value;
                    // 获取视图宽高
                    getScreenSize(getContext());
                    // 横竖屏切换
                    mGestureTouchHelper.setSlidingEnable(mIsLandscape);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 手势操作事件
     */
    private OnTouchGestureListener mTouchGestureListener = new OnTouchGestureListener() {

        /**
         * 亮度手势，手指在Layout左半部上下滑动时候调用
         *
         * @param percent 百分比
         */
        @Override
        public void onBrightnessGesture(float percent) {
            // 新的亮度
            int newBrightness = (int) (percent / (mScreenHeight - 30 * 2) * 100 + mMaxAppLight);
            // 百分比计算
            int brightness = BrightnessHelper.setAppLight100(mActivity, newBrightness);
            // 提示
            mGestureTipsView
                    .setBrightnessIcon(mGestureIcon, brightness)
                    .setProgress(mGestureProgress, brightness)
                    .show();
        }

        /**
         * 音量手势，手指在Layout右半部上下滑动时候调用
         *
         * @param percent 百分比
         */
        @Override
        public void onVolumeGesture(float percent) {
            // 新的音量
            int newVolume = (int) (percent / (mScreenHeight - 30 * 2) * 100 + mCurrentVolume);
            // 百分比计算
            int volume = mVolumeHelper.setVoice100(newVolume);
            // 提示
            mGestureTipsView
                    .setVolumeIcon(mGestureIcon, volume)
                    .setProgress(mGestureProgress, volume)
                    .show();
        }

        /**
         * 快进快退手势，手指在Layout左右滑动的时候调用
         *
         * @param percent 百分比
         */
        @Override
        public void onSlidingGesture(float percent) {
            ToastUtil.showShort("onSlidingGesture");
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
            // 初始化音频数据
            mCurrentVolume = mVolumeHelper.get100CurrentVolume();
            // 初始化亮度数据
            mMaxAppLight = BrightnessHelper.getAppLight100(mActivity);
        }

        /**
         * 快进后退手势 滑动结束
         */
        @Override
        public void onSlidingEndGesture() {
            ToastUtil.showShort("onSlidingEndGesture");
        }

        /**
         * 滑动结束
         */
        @Override
        public void onEndGesture() {
            mGestureTipsView.dismiss();
        }
    };

    /**
     * 获取屏幕真实宽高
     *
     * @param context context
     */
    private void getScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null && mIsLandscape) {
            final Display display = windowManager.getDefaultDisplay();
            Point outPoint = new Point();
            display.getRealSize(outPoint);
            // 手机屏幕真实宽度
            this.mScreenWidth = outPoint.x;
            // 手机屏幕真实高度
            this.mScreenHeight = outPoint.y;
            // 配置宽高
            this.mGestureTouchHelper.setWidthHeight(mScreenWidth, mScreenHeight);
        } else {
            // 竖屏获取当前view的宽高
            this.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mScreenWidth = getView().getWidth();
                    mScreenHeight = getView().getHeight();
                    mGestureTouchHelper.setWidthHeight(mScreenWidth, mScreenHeight);
                    getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    @Override
    public int getCoverLevel() {
        return super.levelHigh(1);
    }
}

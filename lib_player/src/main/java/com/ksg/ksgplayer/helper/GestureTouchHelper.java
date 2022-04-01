package com.ksg.ksgplayer.helper;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ksg.ksgplayer.listener.OnTouchGestureListener;
import com.ksg.ksgplayer.utils.TimeUtil;

/**
 * @author kaisengao
 * @create: 2019/3/7 13:44
 * @describe: 手势 事件帮助类
 */
public class GestureTouchHelper extends GestureDetector.SimpleOnGestureListener {

    private final Activity mActivity;

    private int mViewWidth, mViewHeight;

    private int mMaxAppLight;

    private int mCurrentVolume;

    private long mProgress;

    private long mDuration;

    private long mSlideProgress;

    private String mTimeFormat;

    private boolean mSlipRegion;

    private boolean mFirstTouch;

    private boolean mLongPress;

    private boolean mSlideSeek;

    private boolean mSlideVolume;

    private boolean mSlideBrightness;

    private boolean mGestureEnabled = true;

    private final VolumeHelper mVolumeHelper;

    private final GestureDetector mGestureDetector;

    private final OnTouchGestureListener mTouchGestureListener;

    /**
     * @param context              context
     * @param touchGestureListener 手势监听回调
     */
    public GestureTouchHelper(Activity context, OnTouchGestureListener touchGestureListener) {
        this.mActivity = context;
        this.mTouchGestureListener = touchGestureListener;
        // 音频帮助类
        this.mVolumeHelper = new VolumeHelper(context);
        // GestureDetector
        this.mGestureDetector = new GestureDetector(context, this);
    }

    /**
     * 设置 宽高
     */
    public void setView(int viewWidth, int viewHeight) {
        this.mViewWidth = viewWidth;
        this.mViewHeight = viewHeight;
    }

    /**
     * 设置 是否开启手势调节，默认开启
     */
    public void setGestureEnabled(boolean gestureEnabled) {
        this.mGestureEnabled = gestureEnabled;
    }

    /**
     * 初始化基参
     */
    public void setParams(long progress, long duration) {
        this.mSlideProgress = 0;
        // 初始化进度
        this.mProgress = progress;
        // 初始化总时长
        this.mDuration = duration;
        // 初始化音频数据
        this.mCurrentVolume = mVolumeHelper.get100CurrentVolume();
        // 初始化亮度数据
        this.mMaxAppLight = BrightnessHelper.getAppLight100(mActivity);
        // 初始化时间格式
        this.mTimeFormat = TimeUtil.getFormat(mDuration);
    }

    /**
     * 视频总时长
     */
    public long getDuration() {
        return mDuration;
    }

    /**
     * 滑动的进度
     */
    public long getSlideProgress() {
        return mSlideProgress;
    }

    /**
     * View Touch事件
     *
     * @param event event
     * @return boolean
     */
    public boolean onTouch(MotionEvent event) {
        // 验证是否开启了手势
        if (!mGestureEnabled) {
            return mGestureDetector.onTouchEvent(event);
        }
        // 监听Up事件
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mLongPress) {
                // 长按事件
                this.onLongPressEnd();
            } else if (mSlideSeek) {
                // 滑动进度事件
                this.onSlideEndSeek();
            } else {
                // 滑动其他事件
                this.onSlideEnd();
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * Down事件
     */
    @Override
    public boolean onDown(MotionEvent e) {
        // 初始化基参
        this.mSlipRegion = false;
        this.mFirstTouch = true;
        this.mLongPress = false;
        this.mSlideSeek = false;
        this.mSlideVolume = false;
        this.mSlideBrightness = false;
        // 回调Down事件
        this.mTouchGestureListener.onDown();
        // 是否开启手势
        return mGestureEnabled;
    }

    /**
     * 单击
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        this.mTouchGestureListener.onSingleTapGesture();
        return super.onSingleTapConfirmed(e);
    }

    /**
     * 双击
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        this.mTouchGestureListener.onDoubleTapGesture();
        return super.onDoubleTap(e);
    }

    /**
     * 长按
     */
    @Override
    public void onLongPress(MotionEvent e) {
        this.mLongPress = true;
        this.mTouchGestureListener.onLongPress();
    }

    /**
     * 长按结束
     */
    public void onLongPressEnd() {
        this.mLongPress = false;
        this.mTouchGestureListener.onLongPressEnd();
    }

    /**
     * @param e1        The first down motion event that started the scrolling.
     * @param e2        The move motion event that triggered the current onScroll.
     * @param distanceX The distance along the X axis(轴) that has been scrolled since the last call to onScroll. This is NOT the distance between e1 and e2.
     * @param distanceY The distance along the Y axis that has been scrolled since the last call to onScroll. This is NOT the distance between e1 and e2.
     *                  无论是用手拖动view，或者是以抛的动作滚动，都会多次触发 ,这个方法在ACTION_MOVE动作发生时就会触发 参看GestureDetector的onTouchEvent方法源码
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // 验证是否开启了手势 或 滑动手势
        if (!mGestureEnabled) {
            return true;
        }
        float deltaX = e1.getX() - e2.getX();
        float deltaY = e1.getY() - e2.getY();
        if (mFirstTouch) {
            this.mSlideSeek = Math.abs(distanceX) >= Math.abs(distanceY);
            if (!mSlideSeek) {
                //半屏宽度
                int halfScreen = mViewWidth / 2;
                if (e2.getX() > halfScreen) {
                    this.mSlideVolume = true;
                } else {
                    this.mSlideBrightness = true;
                }
            }
            this.mFirstTouch = false;
        }

        if (mSlideSeek) {
            // 快进退
            this.slideToSeek(e1.getX(), deltaX);
        } else if (mSlideBrightness) {
            // 亮度
            this.slideBrightness(deltaY);
        } else if (mSlideVolume) {
            // 音量
            this.slideVolume(deltaY);
        }

        return true;
    }

    /**
     * 亮度
     *
     * @param deltaY deltaY
     */
    private void slideBrightness(float deltaY) {
        // 新的亮度
        int newBrightness = (int) (deltaY / (mViewHeight - 30 * 2) * 100 + mMaxAppLight);
        // 百分比计算
        int brightness = BrightnessHelper.setAppLight100(mActivity, newBrightness);
        // 回调
        this.mTouchGestureListener.onSlideBrightness(brightness);
    }

    /**
     * 音量
     *
     * @param deltaY deltaY
     */
    private void slideVolume(float deltaY) {
        // 新的音量
        int newVolume = (int) (deltaY / (mViewHeight - 30 * 2) * 100 + mCurrentVolume);
        // 百分比计算
        int volume = mVolumeHelper.setVoice100(newVolume);
        // 回调
        this.mTouchGestureListener.onSlideVolume(volume);
    }

    /**
     * 快进后退
     *
     * @param deltaX deltaX
     */
    private void slideToSeek(float e1x, float deltaX) {
        // 计算滑动区域 左侧百分之%07的位置开始计算 小于不算
        //            右侧百分之%93的位置开始计算 大于不算
        this.mSlipRegion = (e1x > (mViewWidth * 0.07) && e1x < (mViewWidth * 0.93));
        // 验证是否符合区域
        if (this.mSlipRegion) {
            deltaX = -deltaX;
            // 计算手势滑动产生的新进度
            this.mSlideProgress = (long) (deltaX / mViewWidth * 200000 + mProgress);
            this.mSlideProgress = mSlideProgress <= 0 ? 0 : mSlideProgress;
            this.mSlideProgress = Math.min(mSlideProgress, mDuration);
            // 时间转换
            String curr = TimeUtil.getTime(mTimeFormat, mSlideProgress);
            String duration = TimeUtil.getTime(mTimeFormat, mDuration);
            String time = curr + " / " + duration;
            // 回调
            this.mTouchGestureListener.onSlideSeek(mSlideProgress, time);
        }
    }

    /**
     * 滑动结束
     */
    private void onSlideEnd() {
        this.mTouchGestureListener.onSlideEnd();
    }

    /**
     * 滑动结束 快进退
     */
    private void onSlideEndSeek() {
        if (mSlipRegion) {
            this.mTouchGestureListener.onSlideEndSeek();
        }
    }
}

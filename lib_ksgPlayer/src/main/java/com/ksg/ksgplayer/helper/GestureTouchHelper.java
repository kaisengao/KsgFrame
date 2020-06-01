package com.ksg.ksgplayer.helper;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.kasiengao.base.util.TimeUtil;
import com.ksg.ksgplayer.listener.OnTouchGestureListener;

/**
 * @author kaisengao
 * @create: 2019/3/7 13:44
 * @describe: 手势 事件帮助类
 */
public class GestureTouchHelper extends GestureDetector.SimpleOnGestureListener {

    private Activity mActivity;

    private int mViewWidth, mViewHeight;

    private int mMaxAppLight;

    private int mCurrentVolume;

    private long mProgress;

    private long mDuration;

    private long mSlideProgress;

    private String mTimeFormat;

    private boolean mSlipRegion;

    private boolean mFirstTouch;

    private boolean mChangeVolume;

    private boolean mChangePosition;

    private boolean mChangeBrightness;

    private boolean mIsSlideEnabled = false;

    private boolean mIsGestureEnabled = true;

    private final VolumeHelper mVolumeHelper;

    private final GestureDetector mGestureDetector;

    private OnTouchGestureListener mOnTouchGestureListener;

    public GestureTouchHelper(Activity context) {
        this.mActivity = context;
        // 音频帮助类
        this.mVolumeHelper = new VolumeHelper(context);
        // GestureDetector
        this.mGestureDetector = new GestureDetector(context, this);
    }

    /**
     * 设置  View宽
     */
    public void setView(int viewWidth, int viewHeight) {
        this.mViewWidth = viewWidth;
        this.mViewHeight = viewHeight;
    }

    /**
     * 设置 是否可以手势调节进度，音量，亮度功能，默认关闭
     */
    public void setSlideEnabled(boolean enableInNormal) {
        this.mIsSlideEnabled = enableInNormal;
    }

    /**
     * 设置 是否开启手势调节进度，音量，亮度，单击，双击，默认开启
     */
    public void setGestureEnabled(boolean gestureEnabled) {
        this.mIsGestureEnabled = gestureEnabled;
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
        this.mMaxAppLight = BrightnessHelper.getAppLight100(this.mActivity);
        // 初始化时间格式
        this.mTimeFormat = TimeUtil.getFormat(mDuration);
    }

    /**
     * 手势回调监听
     *
     * @param onTouchGestureListener onTouchGestureListener
     */
    public void setOnTouchGestureListener(OnTouchGestureListener onTouchGestureListener) {
        this.mOnTouchGestureListener = onTouchGestureListener;
    }

    public long getDuration() {
        return mDuration;
    }

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
        // 监听Up事件
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 1、验证是否开启了滑动手势
            if (mIsSlideEnabled) {
                // 2、验证是横向滑动结束还是普通滑动事件
                if (mChangePosition) {
                    this.onSeekEndGesture();
                } else {
                    this.onEndGesture();
                }
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 单击手势，确认是单击的时候调用
     */
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (mOnTouchGestureListener != null) {
            mOnTouchGestureListener.onSingleTapGesture();
        }
        return super.onSingleTapConfirmed(e);
    }

    /**
     * 双击手势，确认是双击的时候调用
     */
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (mOnTouchGestureListener != null) {
            mOnTouchGestureListener.onDoubleTapGesture();
        }
        return super.onDoubleTap(e);
    }

    /**
     * 按下手势，第一根手指按下时候调用
     */
    @Override
    public boolean onDown(MotionEvent e) {
        // 初始化基参
        this.mSlipRegion = false;
        this.mFirstTouch = true;
        this.mChangeVolume = false;
        this.mChangePosition = false;
        this.mChangeBrightness = false;
        // 回调Down事件
        if (mOnTouchGestureListener != null) {
            mOnTouchGestureListener.onDown();
        }
        // 是否开启手势
        return mIsGestureEnabled;
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
        if (!mIsGestureEnabled || !mIsSlideEnabled) {
            return true;
        }
        float deltaX = e1.getX() - e2.getX();
        float deltaY = e1.getY() - e2.getY();
        if (mFirstTouch) {
            this.mChangePosition = Math.abs(distanceX) >= Math.abs(distanceY);
            if (!mChangePosition) {
                //半屏宽度
                int halfScreen = mViewWidth / 2;
                if (e2.getX() > halfScreen) {
                    this.mChangeVolume = true;
                } else {
                    this.mChangeBrightness = true;
                }
            }
            this.mFirstTouch = false;
        }

        if (mChangePosition) {
            // 快进后退
            this.slideToChangePosition(e1.getX(), deltaX);
        } else if (mChangeBrightness) {
            // 亮度
            this.slideToChangeBrightness(deltaY);
        } else if (mChangeVolume) {
            // 音量
            this.slideToChangeVolume(deltaY);
        }

        return true;
    }

    /**
     * 快进后退
     *
     * @param deltaX deltaX
     */
    private void slideToChangePosition(float e1x, float deltaX) {
        if (mOnTouchGestureListener != null) {
            // 计算滑动区域 左侧百分之%07的位置开始计算 小于不算
            //             右侧百分之%93的位置开始计算 大于不算
            this.mSlipRegion = (e1x > (mViewWidth * 0.07) && e1x < (mViewWidth * 0.93));
            // 验证是否符合区域
            if (this.mSlipRegion) {
                deltaX = -deltaX;
                // 计算手势滑动产生的新进度
                mSlideProgress = (long) (deltaX / mViewWidth * 120000 + mProgress);
                mSlideProgress = mSlideProgress <= 0 ? 0 : mSlideProgress;
                mSlideProgress = Math.min(mSlideProgress, mDuration);
                // 时间转换
                String curr = TimeUtil.getTime(mTimeFormat, mSlideProgress);
                String duration = TimeUtil.getTime(mTimeFormat, mDuration);
                String time = curr + " / " + duration;
                // 回调
                this.mOnTouchGestureListener.onSeekGesture(deltaX, time);
            }
        }
    }

    /**
     * 亮度
     *
     * @param deltaY deltaY
     */
    private void slideToChangeBrightness(float deltaY) {
        if (mOnTouchGestureListener != null) {
            // 新的亮度
            int newBrightness = (int) (deltaY / (mViewHeight - 30 * 2) * 100 + mMaxAppLight);
            // 百分比计算
            int brightness = BrightnessHelper.setAppLight100(mActivity, newBrightness);
            // 回调
            this.mOnTouchGestureListener.onBrightnessGesture(brightness);
        }
    }

    /**
     * 音量
     *
     * @param deltaY deltaY
     */
    private void slideToChangeVolume(float deltaY) {
        if (mOnTouchGestureListener != null) {
            // 新的音量
            int newVolume = (int) (deltaY / (mViewHeight - 30 * 2) * 100 + mCurrentVolume);
            // 百分比计算
            int volume = mVolumeHelper.setVoice100(newVolume);
            // 回调
            this.mOnTouchGestureListener.onVolumeGesture(volume);
        }
    }

    /**
     * 快进后退手势 滑动结束
     */
    private void onSeekEndGesture() {
        if (mOnTouchGestureListener != null && mSlipRegion) {
            this.mOnTouchGestureListener.onSeekEndGesture();
        }
    }

    /**
     * 滑动结束
     */
    private void onEndGesture() {
        if (mOnTouchGestureListener != null) {
            this.mOnTouchGestureListener.onEndGesture();
        }
    }
}

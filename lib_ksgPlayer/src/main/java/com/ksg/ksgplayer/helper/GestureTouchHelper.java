package com.ksg.ksgplayer.helper;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ksg.ksgplayer.listener.OnTouchGestureListener;

/**
 * @author kaisengao
 * @create: 2019/3/7 13:44
 * @describe: 手势 事件帮助类
 */
public class GestureTouchHelper extends GestureDetector.SimpleOnGestureListener {

    private int mWidth;

    private int mHeight;

    private boolean mFirstTouch;

    private boolean mVerticalSlide;

    private boolean mHorizontalSlide;

    private boolean mGestureEnable = true;

    private boolean mSlidingEnable = false;

    private GestureDetector mGestureDetector;

    private OnTouchGestureListener mOnTouchGestureListener;

    public GestureTouchHelper(Context context) {
        this.mGestureDetector = new GestureDetector(context, this);
    }

    public void setWidthHeight(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void setGestureEnable(boolean enable) {
        this.mGestureEnable = enable;
    }

    public void setSlidingEnable(boolean slidingEnable) {
        this.mSlidingEnable = slidingEnable;
    }

    public void setOnTouchGestureListener(OnTouchGestureListener onTouchGestureListener) {
        this.mOnTouchGestureListener = onTouchGestureListener;
    }

    public boolean onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mSlidingEnable) {
                if (mHorizontalSlide) {
                    onSlidingEndGesture();
                } else {
                    onEndGesture();
                }
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (mOnTouchGestureListener != null) {
            mOnTouchGestureListener.onSingleTapGesture();
        }
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (mOnTouchGestureListener != null) {
            mOnTouchGestureListener.onDoubleTapGesture();
        }
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mFirstTouch = true;
        mHorizontalSlide = false;
        if (mOnTouchGestureListener != null) {
            mOnTouchGestureListener.onDown();
        }
        return mGestureEnable;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!mGestureEnable || !mSlidingEnable) {
            return true;
        }

        if (mOnTouchGestureListener != null) {

            float deltaY = e1.getY() - e2.getY();

            if (mFirstTouch) {
                mHorizontalSlide = Math.abs(distanceX) >= Math.abs(distanceY);
                mVerticalSlide = e1.getRawX() >= mWidth / 2;
                mFirstTouch = false;
            }

            if (mHorizontalSlide) {
                // 除5避免滑动太快 伪阻尼
                float percent = (e2.getX() - e1.getX()) / 5;
                mOnTouchGestureListener.onSlidingGesture(percent);
            } else {
                if (Math.abs(deltaY) > mHeight) {
                    return true;
                }
                if (mVerticalSlide) {
                    mOnTouchGestureListener.onVolumeGesture(e1.getY() - e2.getY());
                } else {
                    mOnTouchGestureListener.onBrightnessGesture(e1.getY() - e2.getY());
                }
            }

            return true;
        }
        return true;
    }

    private void onEndGesture() {
        if (mOnTouchGestureListener != null) {
            mOnTouchGestureListener.onEndGesture();
        }
    }

    private void onSlidingEndGesture() {
        if (mOnTouchGestureListener != null) {
            mOnTouchGestureListener.onSlidingEndGesture();
        }
    }
}

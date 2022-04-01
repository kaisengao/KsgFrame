package com.kasiengao.ksgframe.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.core.content.ContextCompat;

import com.kasiengao.ksgframe.R;

/**
 * @ClassName: SlideLayout
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/29 20:33
 * @Description: 向右滑动关闭界面
 */
public class SlidingLayout extends FrameLayout {

    /**
     * 页面边缘阴影的宽度默认值
     */
    private static final int SHADOW_WIDTH = 16;
    /**
     * 页面边缘阴影的宽度
     */
    private int mShadowWidth;
    private int mInterceptDownX;
    private int mLastInterceptX;
    private int mLastInterceptY;
    private int mTouchDownX;
    private int mLastTouchX;
    private int mLastTouchY;
    private boolean isConsumed = true;
    /**
     * 页面边缘的阴影图
     */
    private Drawable mLeftShadow;

    private Scroller mScroller;

    private OnSlidingListener mSlidingListener;

    public SlidingLayout(Context context) {
        this(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // InitView
        this.initView();
    }

    /**
     * InitView
     */
    private void initView() {
        this.mScroller = new Scroller(getContext());
        this.mLeftShadow = ContextCompat.getDrawable(getContext(), R.drawable.shape_left_shadow);
        this.mShadowWidth = SHADOW_WIDTH * ((int) getResources().getDisplayMetrics().density);
    }

    /**
     * 事件
     */
    public void setSlidingListener(OnSlidingListener slidingListener) {
        this.mSlidingListener = slidingListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                this.mInterceptDownX = x;
                this.mLastInterceptX = x;
                this.mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastInterceptX;
                int deltaY = y - mLastInterceptY;

                if (mInterceptDownX < (getWidth() / 2f) && Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                this.mLastInterceptX = x;
                this.mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                this.mInterceptDownX = mLastInterceptX = mLastInterceptY = 0;
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.mTouchDownX = x;
                this.mLastTouchX = x;
                this.mLastTouchY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastTouchX;
                int deltaY = y - mLastTouchY;

                if (!isConsumed && mTouchDownX < (getWidth() / 2f) && Math.abs(deltaX) > Math.abs(deltaY)) {
                    this.isConsumed = true;
                }

                if (mLastTouchX<=0){
                    this.mLastTouchX = (int) ev.getX();
                }

                if (isConsumed) {
                    int rightMovedX = mLastTouchX - (int) ev.getX();
                    // 左侧即将滑出屏幕
                    if (getScrollX() + rightMovedX >= 0) {
                        this.scrollTo(0, 0);
                    } else {
                        this.scrollBy(rightMovedX, 0);
                    }
                }
                this.mLastTouchX = x;
                this.mLastTouchY = y;
                break;
            case MotionEvent.ACTION_UP:
                this.isConsumed = false;
                this.mTouchDownX = mLastTouchX = mLastTouchY = 0;
                // 根据手指释放时的位置决定回弹还是关闭
                if (-getScrollX() < getWidth() / 2) {
                    this.scrollBack();
                } else {
                    this.scrollClose();
                }
                break;
        }
        return true;
    }

    /**
     * 滑动返回
     */
    private void scrollBack() {
        int startX = getScrollX();
        int dx = -getScrollX();
        this.mScroller.startScroll(startX, 0, dx, 0, 300);
        this.invalidate();
    }

    /**
     * 滑动关闭
     */
    private void scrollClose() {
        int startX = getScrollX();
        int dx = -getScrollX() - getWidth();
        this.mScroller.startScroll(startX, 0, dx, 0, 300);
        this.invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            this.scrollTo(mScroller.getCurrX(), 0);
            this.postInvalidate();
        } else if (-getScrollX() >= getWidth()) {
            this.close();
            this.mScroller.startScroll(0, 0, 0, 0, 300);
            this.invalidate();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.drawShadow(canvas);
    }

    /**
     * 绘制边缘的阴影
     */
    private void drawShadow(Canvas canvas) {
        mLeftShadow.setBounds(0, 0, mShadowWidth, getHeight());
        canvas.save();
        canvas.translate(-mShadowWidth, 0);
        this.mLeftShadow.draw(canvas);
        canvas.restore();
    }

    /**
     * 关闭
     */
    public void close() {
        if (mSlidingListener != null) {
            this.mSlidingListener.scrollClose();
        }
    }


    public interface OnSlidingListener {

        /**
         * 滑动关闭
         */
        void scrollClose();
    }
}
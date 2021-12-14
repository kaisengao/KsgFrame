package com.kasiengao.ksgframe.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @ClassName: ViewPagerEx
 * @Author: KaiSenGao
 * @CreateDate: 2019-07-04 16:54
 * @Description: viewPager 禁止左右滑动
 */
public class ViewPagerEx extends ViewPager {

    private boolean mCanScroll = true;

    public ViewPagerEx(Context context) {
        super(context);
    }

    public ViewPagerEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mCanScroll && super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mCanScroll && super.onTouchEvent(ev);
    }

    public void setScrollable(boolean scrollable) {
        this.mCanScroll = scrollable;
    }

}

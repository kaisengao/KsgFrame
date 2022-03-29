package com.kasiengao.ksgframe.common.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * @ClassName: FabScrollBehavior
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/29 13:00
 * @Description: 滚动隐藏 Fab
 */
public class FabScrollBehavior extends FloatingActionButton.Behavior {

    private int mTranslationY = 0;

    private int mCurrTranslationY = 0;

    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        this.mTranslationY = child.getHeight() + getMarginBottom(child);
        // 确保滚动方向为垂直方向
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        if (dyConsumed > 0) {
            // 向上滑动
            if (mCurrTranslationY <= mTranslationY) {
                this.mCurrTranslationY = mCurrTranslationY + 10;
                child.setTranslationY(mCurrTranslationY);
            }
        } else {
            // 向上滑动
            if (mCurrTranslationY >= 0) {
                this.mCurrTranslationY = mCurrTranslationY - 10;
                child.setTranslationY(mCurrTranslationY);
            }
        }
    }

    private int getMarginBottom(View view) {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }
}
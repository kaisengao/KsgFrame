package com.kasiengao.ksgframe.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @ClassName: PlayerContainerView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/27 16:57
 * @Description: 播放器容器 View 最高优先级
 */
public class PlayerContainerView extends FrameLayout {

    private boolean mIntercept;

    public PlayerContainerView(@NonNull Context context) {
        this(context, null);
    }

    public PlayerContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIntercept(boolean intercept) {
        this.mIntercept = intercept;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 拦截所有事件
        if (mIntercept) {
            return true;
        }
        return super.onTouchEvent(event);
    }
}

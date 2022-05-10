package com.kasiengao.ksgframe.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * @ClassName: CNestedScrollView
 * @Author: KaiSenGao
 * @CreateDate: 2022/5/10 18:02
 * @Description:
 */
public class CNestedScrollView extends NestedScrollView {

    private int mScrollTop;

    public CNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public CNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
        super.onScrollChanged(left, top, oldLeft, oldTop);
        this.mScrollTop = top;
    }

    public int getScrollTop() {
        return mScrollTop;
    }
}
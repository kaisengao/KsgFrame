package com.kaisengao.ksgframe.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

/**
 * @ClassName: CLinearLayout
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/30 16:33
 * @Description:
 */
public class CLinearLayout extends LinearLayout {

    public CLinearLayout(Context context) {
        this(context, null);
    }

    public CLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 更新宽度
     */
    @Keep
    public void setWidth(float width) {
        this.getLayoutParams().width = (int) width;
        this.requestLayout();
    }

    /**
     * 更新高度
     */
    @Keep
    public void setHeight(float height) {
        this.getLayoutParams().height = (int) height;
        this.requestLayout();
    }
}
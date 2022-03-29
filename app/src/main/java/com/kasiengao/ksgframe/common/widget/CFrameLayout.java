package com.kasiengao.ksgframe.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Keep;

/**
 * @ClassName: CFrameLayout
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/29 16:55
 * @Description:
 */
public class CFrameLayout extends FrameLayout {

    public CFrameLayout(Context context) {
        super(context);
    }

    public CFrameLayout(Context context, AttributeSet attrs) {
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
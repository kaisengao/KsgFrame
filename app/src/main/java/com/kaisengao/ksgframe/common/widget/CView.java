package com.kaisengao.ksgframe.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Keep;

/**
 * @ClassName: CView
 * @Author: KaiSenGao
 * @CreateDate: 2022/5/10 19:04
 * @Description:
 */
public class CView extends View {

    public CView(Context context) {
        this(context, null);
    }

    public CView(Context context, AttributeSet attrs) {
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
package com.kaisengao.ksgframe.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @ClassName: BlurImageView
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/29 18:53
 * @Description:
 */
public class CImageView extends AppCompatImageView {

    public CImageView(@NonNull Context context) {
        this(context, null);
    }

    public CImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
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
package com.kasiengao.ksgframe.common.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.kaisengao.base.util.GlideUtil;

/**
 * @ClassName: BlurImageView
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/29 18:53
 * @Description: 毛玻璃
 */
public class BlurImageView extends AppCompatImageView {

    public BlurImageView(@NonNull Context context) {
        super(context);
    }

    public BlurImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置 毛玻璃
     */
    public void setImageBlur(@Nullable Drawable drawable) {
        GlideUtil.loadImageBlur(getContext(), drawable, this, 25);
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
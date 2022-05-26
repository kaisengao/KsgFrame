package com.kaisengao.ksgframe.common.fish.anim.base;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @ClassName: IAnimListener
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/2 16:36
 * @Description:
 */
public interface ISurfaceAnim {

    /**
     * 设置 RootView 的宽高
     *
     * @param viewWidth  宽
     * @param viewHeight 高
     */
    void setRootViewWH(int viewWidth, int viewHeight);

    /**
     * 动画结束标识
     */
    boolean isAnimEnd();

    /**
     * 绘制
     */
    void draw(Canvas canvas, Paint paint);

    /**
     * 暂停
     */
    void pause();

    /**
     * 释放资源
     */
    void release();
}

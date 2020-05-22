package com.ksg.ksgplayer.render;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;

import com.ksg.ksgplayer.player.BaseInternalPlayer;

/**
 * @ClassName: IRenderView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 10:51
 * @Description:
 */
public interface IRenderView {

    /**
     * 关联BaseInternalPlayer
     */
    void attachToPlayer(@NonNull BaseInternalPlayer player);

    /**
     * 设置视频宽高
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    void setVideoSize(int videoWidth, int videoHeight);

    /**
     * 设置视频旋转角度
     *
     * @param degree 角度值
     */
    void setVideoRotation(int degree);

    /**
     * 获取真实的RenderView
     */
    View getView();

    /**
     * 截图
     */
    Bitmap doScreenShot();

    /**
     * 释放资源
     */
    void release();
}

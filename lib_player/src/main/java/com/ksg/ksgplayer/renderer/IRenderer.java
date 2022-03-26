package com.ksg.ksgplayer.renderer;

import android.view.View;

import com.ksg.ksgplayer.player.IPlayer;

/**
 * @ClassName: IRender
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 10:59
 * @Description: 画面渲染接口类
 */
public interface IRenderer {

    /**
     * 自定义
     */
    int RENDER_TYPE_CUSTOM = -1;

    /**
     * TextureView
     */
    int RENDER_TYPE_TEXTURE_VIEW = 0;

    /**
     * SurfaceView
     */
    int RENDER_TYPE_SURFACE_VIEW = 1;

    /**
     * 设置 Callback
     *
     * @param callback {@link Callback}
     */
    void setCallback(Callback callback);

    /**
     * 设置视频旋转角度
     *
     * @param degree 角度
     */
    void setVideoRotation(int degree);

    /**
     * 设置视频采样率
     *
     * @param videoSarNum videoSarNum
     * @param videoSarDen videoSarDen
     */
    void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen);

    /**
     * 设置画面宽高比
     * <p>
     * {@link AspectRatio#AspectRatio_16_9}
     * {@link AspectRatio#AspectRatio_4_3}
     * {@link AspectRatio#AspectRatio_FIT_PARENT}
     * {@link AspectRatio#AspectRatio_FILL_PARENT}
     * {@link AspectRatio#AspectRatio_MATCH_PARENT}
     * {@link AspectRatio#AspectRatio_ORIGIN}
     *
     * @param aspectRatio {@link AspectRatio}
     */
    void updateAspectRatio(AspectRatio aspectRatio);

    /**
     * 设置画面宽高
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    void updateVideoSize(int videoWidth, int videoHeight);

    /**
     * 返回 渲染器View
     *
     * @return View
     */
    View getRendererView();

    /**
     * 释放资源
     */
    void release();

    /**
     * 是否释放了资源
     *
     * @return boolean
     */
    boolean isReleased();

    /**
     * Holder
     */
    interface Holder {

        /**
         * 绑定播放器
         *
         * @param player {@link IPlayer}
         */
        void bindPlayer(IPlayer player);
    }

    /**
     * Callback
     */
    interface Callback {

        /**
         * Surface 创建
         */
        void onSurfaceCreated(Holder holder, int width, int height);

        /**
         * Surface 改变
         */
        void onSurfaceChanged(Holder holder, int format, int width, int height);

        /**
         * Surface 销毁
         */
        void onSurfaceDestroy(Holder holder);
    }
}

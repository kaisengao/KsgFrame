package com.ksg.ksgplayer.render;

import android.view.View;

import com.ksg.ksgplayer.player.IKsgPlayer;

/**
 * @ClassName: IRender
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 10:59
 * @Description: 画面渲染接口类
 */
public interface IRender {

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
     * 设置 视图渲染 Callback
     *
     * @param renderCallback {@link IRenderCallback}
     */
    void setRenderCallback(IRenderCallback renderCallback);

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
     * 返回 视图View
     *
     * @return View
     */
    View getRenderView();

    /**
     * 销毁视图资源
     */
    void release();

    /**
     * 是否销毁了视图资源
     *
     * @return boolean
     */
    boolean isReleased();

    /**
     * 绑定视图
     */
    interface IRenderHolder {
        /**
         * 绑定视图
         *
         * @param player {@link IKsgPlayer}
         */
        void bindPlayer(IKsgPlayer player);
    }

    /**
     * 视图渲染回调
     */
    interface IRenderCallback {
        void onSurfaceCreated(IRenderHolder renderHolder, int width, int height);

        void onSurfaceChanged(IRenderHolder renderHolder, int format, int width, int height);

        void onSurfaceDestroy(IRenderHolder renderHolder);
    }
}

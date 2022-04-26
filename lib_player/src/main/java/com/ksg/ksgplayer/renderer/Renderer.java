package com.ksg.ksgplayer.renderer;

import android.view.View;
import android.view.ViewGroup;

import com.ksg.ksgplayer.config.AspectRatio;
import com.ksg.ksgplayer.proxy.PlayerProxy;
import com.ksg.ksgplayer.renderer.filter.base.GLFilter;

/**
 * @ClassName: IRender
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 10:59
 * @Description: 画面渲染接口类
 */
public interface Renderer {

    /**
     * 绑定 播放器
     *
     * @param playerProxy 播放器代理类
     */
    void bindPlayer(PlayerProxy playerProxy);

    /**
     * 设置 回调事件
     *
     * @param listener listener
     */
    void setRendererListener(RendererListener listener);

    /**
     * 设置 画面宽高
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    void setVideoSize(int videoWidth, int videoHeight);

    /**
     * 设置 画面旋转角度
     *
     * @param degree 角度
     */
    void setRotationDegrees(int degree);

    /**
     * 获取 画面旋转角度
     *
     * @return degree 角度
     */
    int getRotationDegrees();

    /**
     * 设置 视频采样率
     *
     * @param videoSarNum videoSarNum
     * @param videoSarDen videoSarDen
     */
    void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen);

    /**
     * 设置 画面比例
     *
     * @param aspectRatio {@link AspectRatio}
     */
    default void setAspectRatio(int aspectRatio) {
        // 1、调整View去适应比例变化
        this.changeLayoutParams(aspectRatio);
        // 2、设置比例
        this.setViewAspectRatio(aspectRatio);
    }

    /**
     * 调整View去适应比例变化
     *
     * @param aspectRatio {@link AspectRatio}
     */
    default void changeLayoutParams(int aspectRatio) {
        boolean changed = (aspectRatio != AspectRatio.RATIO_DEFAULT);
        int params = changed ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
        ViewGroup.LayoutParams layoutParams = getRendererView().getLayoutParams();
        layoutParams.width = params;
        layoutParams.height = params;
        this.getRendererView().setLayoutParams(layoutParams);
    }

    /**
     * 设置 画面比例
     *
     * @param aspectRatio {@link AspectRatio}
     */
    void setViewAspectRatio(int aspectRatio);

    /**
     * 设置 自定义画面比例
     *
     * @param customAspectRatio 自定义比例 （例：16/9 = 1.77）
     */
    void setCustomAspectRatio(int customAspectRatio);

    /**
     * 设置 滤镜 (仅GLSurfaceView可用)
     *
     * @param filter GLFilter
     */
    void setGLFilter(GLFilter filter);

    /**
     * 返回 渲染器View
     *
     * @return View
     */
    View getRendererView();

    /**
     * 截图
     */
    boolean onShotPic();

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
}

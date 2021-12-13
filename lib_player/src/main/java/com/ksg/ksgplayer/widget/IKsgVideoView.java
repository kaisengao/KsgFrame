package com.ksg.ksgplayer.widget;

import android.view.ViewGroup;

import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.KsgVideoPlayer;
import com.ksg.ksgplayer.render.IRender;

/**
 * @ClassName: IKsgVideoView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/26 10:29
 * @Description: 播放器View
 */
public interface IKsgVideoView {

    /**
     * 返回 播放器对象
     *
     * @return {@link KsgVideoPlayer}
     */
    KsgVideoPlayer getVideoPlayer();

    /**
     * 容器
     *
     * @param userContainer ViewGroup
     */
    void attachContainer(ViewGroup userContainer);

    /**
     * 容器
     *
     * @param userContainer ViewGroup
     * @param updateRender  是否更新渲染view
     */
    void attachContainer(ViewGroup userContainer, boolean updateRender);

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    void setDataSource(DataSource dataSource);

    /**
     * 设置渲染视图类型
     *
     * @param renderType {@link IRender}
     */
    void setRenderType(int renderType);

    /**
     * 设置（播放器）解码器
     *
     * @param decoderView {@link BaseInternalPlayer}
     */
    boolean setDecoderView(BaseInternalPlayer decoderView);

    /**
     * 获取播放状态
     *
     * @return 播放状态 true 播放 反之
     */
    boolean isPlaying();

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    void seekTo(long msc);

    /**
     * 播放
     */
    void start();

    /**
     * start
     *
     * @param msc 在指定的位置开始播放
     */
    void start(long msc);

    /**
     * 暂停
     */
    void pause();

    /**
     * 继续播放
     */
    void resume();

    /**
     * 停止
     */
    void stop();

    /**
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    void replay(long msc);

    /**
     * 重置播放器
     */
    void reset();

    /**
     * 释放播放器
     */
    void release();

    /**
     * 销毁资源
     */
    void destroy();

    /**
     * 播放状态
     *
     * @return boolean
     */
    boolean isInPlaybackState();
}

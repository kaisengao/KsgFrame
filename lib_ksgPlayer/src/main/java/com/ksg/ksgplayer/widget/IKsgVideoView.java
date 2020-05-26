package com.ksg.ksgplayer.widget;

import android.view.ViewGroup;

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
    void setDataSource(String dataSource);

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
    void setDecoderView(BaseInternalPlayer decoderView);
}

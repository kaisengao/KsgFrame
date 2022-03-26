package com.ksg.ksgplayer;

import android.view.ViewGroup;

import com.ksg.ksgplayer.player.BasePlayer;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.renderer.IRenderer;

/**
 * @ClassName: IKsgVideoPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:09
 * @Description: 播放器
 */
public interface IKsgVideoPlayer extends IPlayer {

    /**
     * 绑定 视图容器
     *
     * @param container container
     */
    void bindContainer(ViewGroup container);

    /**
     * 绑定 视图容器
     *
     * @param container      container
     * @param updateRenderer 更新渲染器
     */
    void bindContainer(ViewGroup container, boolean updateRenderer);

    /**
     * 设置（播放器）解码器
     *
     * @param decoderView {@link BasePlayer}
     */
    boolean setDecoderView(BasePlayer decoderView);

    /**
     * 设置渲染器类型
     *
     * @param rendererType {@link IRenderer}
     */
    void setRendererType(int rendererType);
}

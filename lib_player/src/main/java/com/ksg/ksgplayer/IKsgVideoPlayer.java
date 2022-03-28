package com.ksg.ksgplayer;

import android.view.ViewGroup;

import com.ksg.ksgplayer.cover.ICoverManager;
import com.ksg.ksgplayer.listener.OnCoverEventListener;
import com.ksg.ksgplayer.player.BasePlayer;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.producer.BaseEventProducer;
import com.ksg.ksgplayer.renderer.IRenderer;

import java.util.List;

/**
 * @ClassName: IKsgVideoPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:09
 * @Description: 播放器
 */
public interface IKsgVideoPlayer extends IPlayer {

    /**
     * 设置 覆盖组件管理器
     *
     * @param coverManager coverManager
     */

    void setCoverManager(ICoverManager coverManager);

    /**
     * 添加自定义事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */

    void addEventProducer(BaseEventProducer eventProducer);

    /**
     * 移除一个事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */

    void removeEventProducer(BaseEventProducer eventProducer);

    /**
     * 返回事件生产者集合 便于控制
     *
     * @return List
     */

    List<BaseEventProducer> getEventProducers();

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
     * 解绑 视图容器
     */
    void unbindContainer();

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

    /**
     * 设置 Cover组件回调事件
     *
     * @param coverEventListener coverEventListener
     */
    void setCoverEventListener(OnCoverEventListener coverEventListener);
}

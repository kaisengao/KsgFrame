package com.ksg.ksgplayer;

import android.view.ViewGroup;

import androidx.annotation.ColorRes;

import com.ksg.ksgplayer.cover.ICoverManager;
import com.ksg.ksgplayer.listener.OnCoverEventListener;
import com.ksg.ksgplayer.listener.OnRendererListener;
import com.ksg.ksgplayer.player.BasePlayer;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.producer.BaseEventProducer;
import com.ksg.ksgplayer.renderer.Renderer;
import com.ksg.ksgplayer.renderer.glrender.GLViewRender;
import com.ksg.ksgplayer.renderer.view.KsgGLSurfaceView;

import java.util.List;

/**
 * @ClassName: IKsgVideoPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:09
 * @Description: 播放器
 */
public interface IKsgVideoPlayer extends IPlayer {

    /**
     * 设置 背景颜色
     *
     * @param res res
     */
    void setBackgroundColor(@ColorRes int res);

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
     * 设置 GLViewRender (仅GLSurfaceView可用)
     *
     * @param viewRender {@link GLViewRender}
     * @param modeSize   {@link KsgGLSurfaceView} 测量模式
     */
    void setGLViewRender(GLViewRender viewRender, int modeSize);

    /**
     * 设置 解码器
     *
     * @param decoderView {@link BasePlayer}
     */
    boolean setDecoderView(BasePlayer decoderView);

    /**
     * 获取 解码器
     *
     * @return {@link BasePlayer}
     */
    BasePlayer getDecoderView();

    /**
     * 设置 渲染器类型
     *
     * @param rendererType {@link RendererType}
     */
    void setRendererType(int rendererType);

    /**
     * 获取 渲染器类型
     *
     * @return {@link RendererType}
     */
    int getRendererType();

    /**
     * 设置 渲染器
     *
     * @param rendererType {@link Renderer}
     */
    void setRenderer(int rendererType);

    /**
     * 获取 渲染器
     */
    Renderer getRenderer();

    /**
     * 设置 Cover组件事件
     *
     * @param coverEventListener coverEventListener
     */
    void setCoverEventListener(OnCoverEventListener coverEventListener);

    /**
     * 设置 渲染器事件
     *
     * @param rendererListener rendererListener
     */
    void setRendererListener(OnRendererListener rendererListener);
}

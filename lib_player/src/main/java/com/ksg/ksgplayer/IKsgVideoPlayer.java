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
     * 设置 渲染器
     *
     * @param rendererType {@link Renderer}
     */
    void setRendererType(int rendererType);

    /**
     * 获取 渲染器
     */
    Renderer getRenderer();

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
     * 设置 画面比例
     *
     * @param aspectRatio {@link AspectRatio}
     */
    void setAspectRatio(int aspectRatio);

    /**
     * 设置 自定义画面比例
     *
     * @param customAspectRatio 自定义比例 （例：16/9 = 1.77）
     */
    void setCustomAspectRatio(int customAspectRatio);

    /**
     * 截图
     *
     * @param shotHigh 高清/普通
     * @describe: 使用此方法后监听 {@link OnRendererListener}事件获取截图
     */
    boolean onShotPic(boolean shotHigh);

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

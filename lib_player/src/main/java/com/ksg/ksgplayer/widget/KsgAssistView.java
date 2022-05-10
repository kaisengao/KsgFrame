package com.ksg.ksgplayer.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;

import com.ksg.ksgplayer.IKsgVideoPlayer;
import com.ksg.ksgplayer.KsgVideoPlayer;
import com.ksg.ksgplayer.cover.ICoverManager;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.listener.OnCoverEventListener;
import com.ksg.ksgplayer.listener.OnErrorListener;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.listener.OnRendererListener;
import com.ksg.ksgplayer.player.BasePlayer;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.producer.BaseEventProducer;
import com.ksg.ksgplayer.renderer.Renderer;
import com.ksg.ksgplayer.renderer.RendererType;
import com.ksg.ksgplayer.renderer.glrender.GLViewRender;
import com.ksg.ksgplayer.renderer.view.KsgGLSurfaceView;

import java.util.List;

/**
 * @ClassName: KsgAssistView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 16:17
 * @Description: 辅助播放器
 */
public class KsgAssistView implements IKsgVideoView {

    private Context mContext;

    private KsgVideoPlayer mPlayer;

    public KsgAssistView(Context context) {
        this.mContext = context;
        // Init Player
        this.initPlayer();
    }

    /**
     * 返回 播放器对象
     *
     * @return {@link KsgVideoPlayer}
     */
    @Override
    public final IKsgVideoPlayer getPlayer() {
        return mPlayer;
    }

    /**
     * 设置 背景颜色
     *
     * @param color color
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public void setBackgroundColor(@ColorInt int color) {
        this.mPlayer.setBackgroundColor(color);
    }

    /**
     * 设置 覆盖组件管理器
     *
     * @param coverManager coverManager
     */
    @Override
    public void setCoverManager(ICoverManager coverManager) {
        this.mPlayer.setCoverManager(coverManager);
    }

    /**
     * 添加自定义事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    @Override
    public void addEventProducer(BaseEventProducer eventProducer) {
        this.mPlayer.addEventProducer(eventProducer);
    }

    /**
     * 移除一个事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    @Override
    public void removeEventProducer(BaseEventProducer eventProducer) {
        this.mPlayer.removeEventProducer(eventProducer);
    }

    /**
     * 返回事件生产者集合 便于控制
     *
     * @return List
     */
    @Override
    public List<BaseEventProducer> getEventProducers() {
        return mPlayer.getEventProducers();
    }

    /**
     * 绑定 视图容器
     *
     * @param container container
     */
    @Override
    public void bindContainer(ViewGroup container) {
        this.bindContainer(container, false);
    }

    /**
     * 绑定 视图容器
     *
     * @param container      container
     * @param updateRenderer 更新渲染器
     */
    @Override
    public void bindContainer(ViewGroup container, boolean updateRenderer) {
        // 更新渲染器
        if (updateRenderer) {
            // 释放渲染器
            this.mPlayer.releaseRenderer();
            // 更新Render
            this.setRenderer(getRendererType());
        }
        // 绑定
        this.mPlayer.bindContainer(container, updateRenderer);
    }

    /**
     * 解绑 视图容器
     */
    @Override
    public void unbindContainer() {
        this.mPlayer.unbindContainer();
    }

    /**
     * 设置 GLViewRender (仅GLSurfaceView可用)
     *
     * @param viewRender {@link GLViewRender}
     * @param modeSize   {@link KsgGLSurfaceView} 测量模式
     */
    @Override
    public void setGLViewRender(GLViewRender viewRender, int modeSize) {
        this.mPlayer.setGLViewRender(viewRender, modeSize);
    }

    /**
     * 设置 解码器
     *
     * @param decoderView {@link BasePlayer}
     */
    @Override
    public boolean setDecoderView(BasePlayer decoderView) {
        return mPlayer.setDecoderView(decoderView);
    }

    /**
     * 返回 解码器
     *
     * @return {@link BasePlayer}
     */
    @Override
    public BasePlayer getDecoderView() {
        return mPlayer.getDecoderView();
    }

    /**
     * 设置 渲染器类型
     *
     * @param rendererType {@link RendererType}
     */
    @Override
    public void setRendererType(int rendererType) {
        this.mPlayer.setRendererType(rendererType);
    }

    /**
     * 获取 渲染器类型
     *
     * @return {@link RendererType}
     */
    @Override
    public int getRendererType() {
        return mPlayer.getRendererType();
    }

    /**
     * 设置 渲染器
     *
     * @param rendererType {@link Renderer}
     */
    @Override
    public void setRenderer(int rendererType) {
        this.mPlayer.setRenderer(rendererType);
    }

    /**
     * 获取 渲染器
     *
     * @return {@link Renderer}
     */
    @Override
    public Renderer getRenderer() {
        return mPlayer.getRenderer();
    }

    /**
     * 是否处于播放
     *
     * @return boolean
     */
    @Override
    public boolean isItPlaying() {
        return mPlayer.isItPlaying();
    }

    /**
     * Init Player
     */
    @Override
    public void initPlayer() {
        this.mPlayer = new KsgVideoPlayer(mContext);
    }

    /**
     * 获取当前state
     * {@link IPlayer#STATE_DESTROY}
     * {@link IPlayer#STATE_ERROR}
     * {@link IPlayer#STATE_IDLE}
     * {@link IPlayer#STATE_INIT}
     * {@link IPlayer#STATE_PREPARED}
     * {@link IPlayer#STATE_START}
     * {@link IPlayer#STATE_PAUSE}
     * {@link IPlayer#STATE_STOP}
     * {@link IPlayer#STATE_COMPLETE}
     */
    @Override
    public int getState() {
        return mPlayer.getState();
    }

    /**
     * 自定义消息
     *
     * @param code   code
     * @param bundle bundle
     */
    public void option(int code, Bundle bundle) {
        this.mPlayer.option(code, bundle);
    }

    /**
     * 设置数据源
     *
     * @param dataSource 数据源
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        this.mPlayer.setDataSource(dataSource);
    }

    /**
     * 设置渲染器
     *
     * @param surface surface
     */
    @Override
    public void setSurface(Surface surface) {
        this.mPlayer.setSurface(surface);
    }

    /**
     * 设置渲染器
     *
     * @param holder surfaceHolder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        this.mPlayer.setDisplay(holder);
    }

    /**
     * 获取渲染器
     *
     * @return View
     */
    @Override
    public View getCustomRenderer() {
        return mPlayer.getCustomRenderer();
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float left, float right) {
        this.mPlayer.setVolume(left, right);
    }

    /**
     * 设置循环播放
     */
    @Override
    public void setLooping(boolean looping) {
        this.mPlayer.setLooping(looping);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        this.mPlayer.setSpeed(speed);
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        return mPlayer.getSpeed();
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        return mPlayer.getTcpSpeed();
    }

    /**
     * 获取缓冲进度百分比
     */
    @Override
    public int getBufferPercentage() {
        return mPlayer.getBufferPercentage();
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        return mPlayer.getDuration();
    }

    /**
     * 获取播放状态
     */
    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    /**
     * 跳到指定位置
     */
    @Override
    public void seekTo(long msc) {
        this.mPlayer.seekTo(msc);
    }


    /**
     * 播放
     */
    @Override
    public void start() {
        this.mPlayer.start();
    }

    /**
     * 播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void start(long msc) {
        this.mPlayer.start(msc);
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        this.mPlayer.pause();
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        this.mPlayer.resume();
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        this.mPlayer.stop();
    }

    /**
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void replay(long msc) {
        this.mPlayer.replay(msc);
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        this.mPlayer.reset();
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        this.mPlayer.release();
    }

    /**
     * 设置 播放事件
     *
     * @param playerListener playerListener
     */

    @Override
    public void setPlayerListener(OnPlayerListener playerListener) {
        this.mPlayer.setPlayerListener(playerListener);
    }

    /**
     * 设置 错误事件
     *
     * @param errorListener errorListener
     */
    @Override
    public void setErrorListener(OnErrorListener errorListener) {
        this.mPlayer.setErrorListener(errorListener);
    }

    /**
     * 设置 Cover组件事件
     *
     * @param coverEventListener coverEventListener
     */
    @Override
    public void setCoverEventListener(OnCoverEventListener coverEventListener) {
        this.mPlayer.setCoverEventListener(coverEventListener);
    }

    /**
     * 设置 渲染器事件
     *
     * @param rendererListener rendererListener
     */
    @Override
    public void setRendererListener(OnRendererListener rendererListener) {
        this.mPlayer.setRendererListener(rendererListener);
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.mContext = null;
        if (mPlayer != null) {
            this.mPlayer.destroy();
            this.mPlayer = null;
        }
    }
}

package com.ksg.ksgplayer.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import com.ksg.ksgplayer.KsgVideoPlayer;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.listener.OnErrorListener;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.BasePlayer;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.renderer.IRenderer;

/**
 * @ClassName: KsgAssistView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 16:17
 * @Description: 辅助播放器
 */
public class KsgAssistView implements IKsgVideoView {

    private Context mContext;

    private int mDefaultRendererType;

    private boolean mRendererTypeChange;

    private DataSource mDataSource;

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
    public final KsgVideoPlayer getPlayer() {
        return mPlayer;
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
        // 验证是否更新Render 或者 是否强制更新Render
        if (updateRenderer || isNeedForceUpdateRender()) {
            // 释放Render
            this.releaseRenderer();
            // 更新Render
            this.updateRenderer();
        }
        // 添加容器
        this.mPlayer.bindContainer(container, updateRenderer);
    }

    /**
     * 设置（播放器）解码器
     *
     * @param decoderView {@link BasePlayer}
     */
    @Override
    public boolean setDecoderView(BasePlayer decoderView) {
        return mPlayer.setDecoderView(decoderView);
    }

    /**
     * 返回 （播放器）解码器
     *
     * @return {@link BasePlayer}
     */
    public BasePlayer getDecoderView() {
        return mPlayer.getDecoderView();
    }

    /**
     * 设置渲染器类型
     *
     * @param rendererType {@link IRenderer}
     */
    @Override
    public void setRendererType(int rendererType) {
        this.mRendererTypeChange = mDefaultRendererType != rendererType;
        this.mDefaultRendererType = rendererType;
        this.updateRenderer();
    }

    /**
     * 验证是否需要强制更新渲染器
     *
     * @return boolean
     */
    private boolean isNeedForceUpdateRender() {
        return mPlayer.getIRenderer() == null || mPlayer.getIRenderer().isReleased() || mRendererTypeChange;
    }

    /**
     * 更新渲染器
     */
    private void updateRenderer() {
        if (isNeedForceUpdateRender()) {
            this.mRendererTypeChange = false;
            this.mPlayer.setRendererType(mDefaultRendererType);
        }
    }

    /**
     * 释放渲染器
     */
    private void releaseRenderer() {
        if (mPlayer.getIRenderer() != null) {
            this.mPlayer.getIRenderer().setCallback(null);
            this.mPlayer.releaseRenderer();
        }
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
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        // 设置数据源
        this.mDataSource = dataSource;
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
    public View getRenderer() {
        return mPlayer.getRenderer();
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
        this.start(0, false);
    }

    /**
     * 播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void start(long msc) {
        this.start(msc, false);
    }

    /**
     * 播放
     *
     * @param msc          在指定的位置开始播放
     * @param updateRender 是否更新Render
     */
    public void start(long msc, boolean updateRender) {
        if (updateRender) {
            // 释放Render
            this.releaseRenderer();
            // 更新Render
            this.updateRenderer();
        }
        // 播放
        if (mDataSource != null) {
            this.mPlayer.setDataSource(mDataSource);
            this.mPlayer.start(msc);
        }
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

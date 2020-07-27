package com.ksg.ksgplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ksg.ksgplayer.config.KsgPlayerConfig;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.KsgVideoPlayer;
import com.ksg.ksgplayer.render.IRender;

/**
 * @ClassName: KsgVideoView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 14:44
 * @Description: 播放器View
 */
public class KsgVideoView extends FrameLayout implements IKsgVideoView {

    private int mDefaultRenderType;

    private KsgVideoPlayer mVideoPlayer;

    public KsgVideoView(@NonNull Context context) {
        this(context, null);
    }

    public KsgVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 播放器
        this.mVideoPlayer = new KsgVideoPlayer(context);
        // 默认添加容器
        this.attachContainer(this);
        // 默认使用的视图类型
        this.mDefaultRenderType = KsgPlayerConfig.getInstance().getDefaultRenderType();
    }

    /**
     * 返回 播放器对象
     *
     * @return {@link KsgVideoPlayer}
     */
    @Override
    public final KsgVideoPlayer getVideoPlayer() {
        return this.mVideoPlayer;
    }

    /**
     * 容器
     *
     * @param userContainer ViewGroup
     */
    @Override
    public final void attachContainer(ViewGroup userContainer) {
        this.mVideoPlayer.attachContainer(this);
    }

    /**
     * 容器
     *
     * @param userContainer ViewGroup
     * @param updateRender  是否更新渲染view
     */
    @Override
    public final void attachContainer(ViewGroup userContainer, boolean updateRender) {
        this.mVideoPlayer.attachContainer(this, updateRender);
    }

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    @Override
    public final void setDataSource(DataSource dataSource) {
        // 销毁视图资源
        this.mVideoPlayer.releaseRender();
        // 设置渲染视图
        this.setRenderType(this.mDefaultRenderType);
        // 设置数据源
        this.mVideoPlayer.setDataSource(dataSource);
    }

    /**
     * 设置渲染视图类型
     *
     * @param renderType {@link IRender}
     */
    @Override
    public final void setRenderType(int renderType) {
        IRender mRender = this.mVideoPlayer.getRender();
        // 验证重复视图类型
        if (this.mDefaultRenderType == renderType && mRender != null && !mRender.isReleased()) {
            return;
        }
        this.mVideoPlayer.setRenderType(renderType);
    }

    /**
     * 设置（播放器）解码器
     *
     * @param decoderView {@link BaseInternalPlayer}
     */
    @Override
    public final boolean setDecoderView(BaseInternalPlayer decoderView) {
        return this.mVideoPlayer.setDecoderView(decoderView);
    }

    /**
     * 返回 （播放器）解码器
     *
     * @return {@link BaseInternalPlayer}
     */
    public BaseInternalPlayer getDecoderView() {
        return this.mVideoPlayer.getDecoderView();
    }

    /**
     * 获取播放状态
     *
     * @return 播放状态 true 播放 反之
     */
    @Override
    public boolean isPlaying() {
        return this.mVideoPlayer.isPlaying();
    }

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    @Override
    public void seekTo(long msc) {
        this.mVideoPlayer.seekTo(msc);
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        this.mVideoPlayer.start();
    }

    /**
     * 播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void start(long msc) {
        this.mVideoPlayer.start(msc);
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        this.mVideoPlayer.pause();
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        this.mVideoPlayer.resume();
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        this.mVideoPlayer.stop();
    }

    /**
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void replay(long msc) {
        this.mVideoPlayer.replay(msc);
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        this.mVideoPlayer.reset();
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        this.mVideoPlayer.release();
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.mVideoPlayer.destroy();
        this.mVideoPlayer = null;
    }

    /**
     * 播放状态
     *
     * @return boolean
     */
    @Override
    public boolean isInPlaybackState() {
        return this.mVideoPlayer.isInPlaybackState();
    }
}

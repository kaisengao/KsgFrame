package com.ksg.ksgplayer.widget;

import android.content.Context;
import android.view.ViewGroup;

import com.ksg.ksgplayer.config.KsgPlayerConfig;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.KsgVideoPlayer;
import com.ksg.ksgplayer.render.IRender;

/**
 * @ClassName: KsgAssistView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 16:17
 * @Description: 辅助播放器
 */
public class KsgAssistView implements IKsgVideoView {

    private int mDefaultRenderType;

    private boolean mRenderTypeChange;

    private DataSource mDataSource;

    private KsgVideoPlayer mVideoPlayer;

    public KsgAssistView(Context context) {
        // 播放器
        this.mVideoPlayer = new KsgVideoPlayer(context);
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
    public void attachContainer(ViewGroup userContainer) {
        this.attachContainer(userContainer, false);
    }

    /**
     * 容器
     *
     * @param userContainer ViewGroup
     * @param updateRender  是否更新渲染view
     */
    @Override
    public void attachContainer(ViewGroup userContainer, boolean updateRender) {
        // 验证是否更新Render 或者 是否强制更新Render
        if (updateRender || isNeedForceUpdateRender()) {
            // 释放Render
            this.releaseRender();
            // 更新Render
            this.updateRender();
        }
        // 添加容器
        this.mVideoPlayer.attachContainer(userContainer, updateRender);
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
     * 设置渲染视图类型
     *
     * @param renderType {@link IRender}
     */
    @Override
    public void setRenderType(int renderType) {
        this.mRenderTypeChange = this.mDefaultRenderType != renderType;
        this.mDefaultRenderType = renderType;
        this.updateRender();
    }

    /**
     * 验证是否需要强制更新Render
     *
     * @return boolean
     */
    private boolean isNeedForceUpdateRender() {
        return this.mVideoPlayer.getRender() == null || this.mVideoPlayer.getRender().isReleased() || mRenderTypeChange;
    }

    /**
     * 更新Render
     */
    private void updateRender() {
        if (isNeedForceUpdateRender()) {
            this.mRenderTypeChange = false;
            this.mVideoPlayer.setRenderType(mDefaultRenderType);
        }
    }

    /**
     * 释放Render
     */
    private void releaseRender() {
        if (this.mVideoPlayer.getRender() != null) {
            this.mVideoPlayer.getRender().setRenderCallback(null);
            this.mVideoPlayer.releaseRender();
        }
    }

    /**
     * 设置（播放器）解码器
     *
     * @param decoderView {@link BaseInternalPlayer}
     */
    @Override
    public boolean setDecoderView(BaseInternalPlayer decoderView) {
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
            this.releaseRender();
            // 更新Render
            this.updateRender();
        }
        // 播放
        if (mDataSource != null) {
            this.mVideoPlayer.setDataSource(mDataSource);
            this.mVideoPlayer.start(msc);
        }
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

package com.ksg.ksgplayer.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ksg.ksgplayer.player.IKagVideoPlayer;
import com.ksg.ksgplayer.player.KsgVideoPlayer;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.render.IRender;

/**
 * @ClassName: KsgVideoView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 14:44
 * @Description: 播放器View
 */
public class KsgVideoView extends FrameLayout implements IKagVideoPlayer {

    private KsgVideoPlayer mVideoPlayer;

    public KsgVideoView(@NonNull Context context) {
        this(context, null);
    }

    public KsgVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.mVideoPlayer = new KsgVideoPlayer(context);

        this.attachContainer(this);
    }

    public KsgVideoPlayer getVideoPlayer() {
        return mVideoPlayer;
    }

    @Override
    public void attachContainer(ViewGroup userContainer) {
        this.mVideoPlayer.attachContainer(this);
    }

    @Override
    public void attachContainer(ViewGroup userContainer, boolean updateRender) {
        this.mVideoPlayer.attachContainer(this, updateRender);
    }

    @Override
    public int getState() {
        return this.mVideoPlayer.getState();
    }

    @Override
    public void option(int code, Bundle bundle) {
        this.mVideoPlayer.option(code, bundle);
    }

    @Override
    public void setDataSource(String dataSource) {
        // 销毁视图资源
        this.mVideoPlayer.releaseRender();
        // 设置渲染视图
        this.setRenderType(this.mVideoPlayer.mRenderType);
        // 设置数据源
        this.mVideoPlayer.setDataSource(dataSource);
    }

    /**
     * 设置渲染视图类型
     *
     * @param renderType {@link IRender}
     */
    @Override
    public void setRenderType(int renderType) {
        int mRenderType = this.mVideoPlayer.mRenderType;
        IRender mRender = this.mVideoPlayer.mRender;
        // 验证重复视图类型
        if (mRenderType == renderType && mRender != null && !mRender.isReleased()) {
            return;
        }
        this.mVideoPlayer.setRenderType(renderType);
    }

    @Override
    public void setDecoderView(BaseInternalPlayer decoderView) {
        this.mVideoPlayer.setDecoderView(decoderView);
    }

    @Override
    public void setVolume(float left, float right) {
        this.mVideoPlayer.setVolume(left, right);
    }

    @Override
    public void setLooping(boolean isLooping) {
        this.mVideoPlayer.setLooping(isLooping);
    }

    @Override
    public void setSpeed(float speed) {
        this.mVideoPlayer.setSpeed(speed);
    }

    @Override
    public float getSpeed() {
        return this.mVideoPlayer.getSpeed();
    }

    @Override
    public long getTcpSpeed() {
        return this.mVideoPlayer.getTcpSpeed();
    }

    @Override
    public int getBufferedPercentage() {
        return this.mVideoPlayer.getBufferedPercentage();
    }

    @Override
    public long getCurrentPosition() {
        return this.mVideoPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return this.mVideoPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return this.mVideoPlayer.isPlaying();
    }

    @Override
    public void seekTo(int msc) {
        this.mVideoPlayer.seekTo(msc);
    }

    @Override
    public void start() {
        this.mVideoPlayer.start();
    }

    @Override
    public void start(long msc) {
        this.mVideoPlayer.start(msc);
    }

    @Override
    public void pause() {
        this.mVideoPlayer.pause();
    }

    @Override
    public void resume() {
        this.mVideoPlayer.resume();
    }

    @Override
    public void stop() {
        this.mVideoPlayer.stop();
    }

    @Override
    public void rePlay(int msc) {
        this.mVideoPlayer.rePlay(msc);
    }

    @Override
    public void reset() {
        this.mVideoPlayer.reset();
    }

    @Override
    public void release() {
        this.mVideoPlayer.release();
    }

    @Override
    public void destroy() {
        this.mVideoPlayer.destroy();
        this.mVideoPlayer = null;
    }
}

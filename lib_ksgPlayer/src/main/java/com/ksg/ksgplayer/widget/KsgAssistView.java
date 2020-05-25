package com.ksg.ksgplayer.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.IKagVideoPlayer;
import com.ksg.ksgplayer.player.KsgVideoPlayer;
import com.ksg.ksgplayer.render.IRender;

/**
 * @ClassName: KsgAssistView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 16:17
 * @Description:
 */
public class KsgAssistView implements IKagVideoPlayer {

    private final Context mContext;

    private KsgVideoPlayer mVideoPlayer;

    private String mDataSource;

    private boolean mRenderTypeChange;

    public KsgAssistView(Context context) {
        this.mContext = context;
        this.mVideoPlayer = new KsgVideoPlayer(context);
    }

    public KsgVideoPlayer getVideoPlayer() {
        return mVideoPlayer;
    }

    @Override
    public void attachContainer(ViewGroup userContainer) {
        this.attachContainer(userContainer, false);
    }

    @Override
    public void attachContainer(ViewGroup userContainer, boolean updateRender) {

        if (updateRender || isNeedForceUpdateRender()) {
            releaseRender();
            //update render view.
            updateRender();
        }

        this.mVideoPlayer.attachContainer(userContainer, updateRender);
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
        mRenderTypeChange = this.mVideoPlayer.mRenderType != renderType;
        this.mVideoPlayer.mRenderType = renderType;
        updateRender();
    }

    private boolean isNeedForceUpdateRender() {
        return this.mVideoPlayer.mRender == null || this.mVideoPlayer.mRender.isReleased() || mRenderTypeChange;
    }

    private void updateRender() {
        if (isNeedForceUpdateRender()) {
            mRenderTypeChange = false;
            this.mVideoPlayer.setRenderType(this.mVideoPlayer.mRenderType);
        }
    }

    private void releaseRender() {
        if (this.mVideoPlayer.mRender != null) {
            this.mVideoPlayer.mRender.setRenderCallback(null);
            this.mVideoPlayer.mRender.release();
        }
        this.mVideoPlayer.mRender = null;
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

    public void play() {
        play(false);
    }

    public void play(boolean updateRender) {
        if (updateRender) {
            releaseRender();
            updateRender();
        }
        if (mDataSource != null) {
            onInternalSetDataSource(mDataSource);
            onInternalStart();
        }
    }

    private void onInternalSetDataSource(String dataSource) {
        this.mVideoPlayer.setDataSource(dataSource);
    }

    private void onInternalStart(int msc) {
        this.mVideoPlayer.start(msc);
    }

    private void onInternalStart() {
        this.mVideoPlayer.start();
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
        if (mDataSource != null) {
            onInternalSetDataSource(mDataSource);
            onInternalStart(msc);
        }
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
        releaseRender();
        this.mVideoPlayer.destroy();
        this.mVideoPlayer = null;
    }

}

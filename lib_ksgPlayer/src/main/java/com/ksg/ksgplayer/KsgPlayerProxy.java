package com.ksg.ksgplayer;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.ksg.ksgplayer.cache.PlayValueGetter;
import com.ksg.ksgplayer.cache.progress.ProgressCache;
import com.ksg.ksgplayer.config.PlayerConfig;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.player.IKsgPlayerProxy;
import com.ksg.ksgplayer.widget.KsgVideoPlayer;

/**
 * @ClassName: KsgPlayerProxy
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:23
 * @Description: 播放器代理
 */
public final class KsgPlayerProxy implements IKsgPlayer {

    /**
     * 视频播放地址
     */
    private String mDataSource;

    /**
     * 播放器
     */
    private BaseInternalPlayer mInternalPlayer;

    /**
     * 播放进度缓存
     */
    private IKsgPlayerProxy mProgressCache;

    /**
     * 播放器状态事件
     */
    private OnPlayerEventListener mOnPlayerEventListener;

    /**
     * 播放器错误事件
     */
    private OnErrorEventListener mOnErrorEventListener;

    public KsgPlayerProxy() {
        // 初始化 播放进度缓存代理
        this.initProgressCache();
    }

    /**
     * 初始化 播放进度缓存代理
     */
    private void initProgressCache() {
        // 验证是否开启本地播放进度缓存
        if (PlayerConfig.getInstance().isPlayProgressCache()) {
            this.mProgressCache = new ProgressCache(new PlayValueGetter() {
                @Override
                public long getCurrentPosition() {
                    return KsgPlayerProxy.this.getCurrentPosition();
                }

                @Override
                public int getBufferedPercentage() {
                    return KsgPlayerProxy.this.getBufferedPercentage();
                }

                @Override
                public long getDuration() {
                    return KsgPlayerProxy.this.getDuration();
                }

                @Override
                public int getState() {
                    return KsgPlayerProxy.this.getState();
                }
            });
        }
    }

    /**
     * 设置（播放器）解码器
     *
     * @param decoderView {@link BaseInternalPlayer}
     */
    public void setDecoderView(BaseInternalPlayer decoderView) {
        this.destroy();
        this.mInternalPlayer = decoderView;
    }

    /**
     * 返回 （播放器）解码器
     *
     * @return {@link BaseInternalPlayer}
     */
    public BaseInternalPlayer getDecoderView() {
        return this.mInternalPlayer;
    }

    /**
     * 获取当前state
     *
     * @return 返回状态
     */
    @Override
    public int getState() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getState();
        }
        return 0;
    }

    /**
     * 初始化 播放器
     */
    @Override
    public void initPlayer() {

    }

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    @Override
    public void setDataSource(String dataSource) {
        this.mDataSource = dataSource;
        // 重置 事件监听
        this.resetListener();
        // 设置 事件监听
        this.initListener();
        // 设置 播放地址
        this.interPlayerSetDataSource(dataSource);
    }

    /**
     * 设置 播放地址
     */
    private void interPlayerSetDataSource(String dataSource) {
        // 验证 播放进度缓存
        if (isProgressCacheOpen()) {
            this.mProgressCache.onDataSourceReady(dataSource);
        }
        // 播放器配置播放地址
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setDataSource(dataSource);
        }
    }

    /**
     * 配置 事件监听
     */
    private void initListener() {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setOnPlayerEventListener(mInternalPlayerEventListener);
            this.mInternalPlayer.setOnErrorEventListener(mInternalErrorEventListener);
        }
    }

    /**
     * 重置 事件监听
     */
    private void resetListener() {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setOnPlayerEventListener(null);
            this.mInternalPlayer.setOnErrorEventListener(null);
        }
    }

    /**
     * 自定义事件
     *
     * @param code   code
     * @param bundle bundle
     */
    @Override
    public void option(int code, Bundle bundle) {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.option(code, bundle);
        }
    }

    /**
     * 设置渲染视频的View,主要用于TextureView
     *
     * @param surface surface
     */
    @Override
    public void setSurface(Surface surface) {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setSurface(surface);
        }
    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     *
     * @param holder surfaceHolder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setDisplay(holder);
        }
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float v1, float v2) {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setVolume(v1, v2);
        }
    }

    /**
     * 设置是否循环播放
     */
    @Override
    public void setLooping(boolean isLooping) {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setLooping(isLooping);
        }
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setSpeed(speed);
        }
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getSpeed();
        }
        return 0;
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getTcpSpeed();
        }
        return 0;
    }

    /**
     * 获取缓冲进度百分比
     *
     * @return 缓冲进度
     */
    @Override
    public int getBufferedPercentage() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getBufferedPercentage();
        }
        return 0;
    }

    /**
     * 获取当前播放的位置
     *
     * @return 播放进度
     */
    @Override
    public long getCurrentPosition() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 获取视频总时长
     *
     * @return 总时长
     */
    @Override
    public long getDuration() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getDuration();
        }
        return 0;
    }

    /**
     * 获取播放状态
     *
     * @return 播放状态 true 播放 反之
     */
    @Override
    public boolean isPlaying() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        // 播放
        this.start(getProgressCache());
    }

    /**
     * 获取播放记录缓存
     *
     * @return cache
     */
    private long getProgressCache() {
        if (isProgressCacheOpen()) {
            return this.mProgressCache.getRecord(mDataSource);
        }
        return 0;
    }

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    @Override
    public void seekTo(int msc) {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.seekTo(msc);
        }
    }

    /**
     * 准备开始播放（异步）
     */
    @Override
    public void prepareAsync() {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.prepareAsync();
        }
    }

    /**
     * start
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void start(long msc) {
        if (isPlayerAvailable()) {
            this.prepareAsync();
            this.mInternalPlayer.start(msc);
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.pause();
        }
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.resume();
        }
    }

    /**
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    public void rePlay(int msc) {
        if (mDataSource != null && isPlayerAvailable()) {
            this.interPlayerSetDataSource(mDataSource);
            this.mInternalPlayer.start(msc);
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        // 验证 播放进度缓存
        if (isProgressCacheOpen()) {
            this.mProgressCache.onIntentStop();
        }
        if (isPlayerAvailable()) {
            this.mInternalPlayer.stop();
        }
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        // 验证 播放进度缓存
        if (isProgressCacheOpen()) {
            this.mProgressCache.onIntentReset();
        }
        if (isPlayerAvailable()) {
            this.mInternalPlayer.reset();
        }
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.release();
        }
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        if (isProgressCacheOpen()) {
            this.mProgressCache.onIntentDestroy();
        }
        if (isPlayerAvailable()) {
            this.mInternalPlayer.destroy();
        }
        // 销毁 事件监听
        this.resetListener();
    }

    /**
     * 播放器非空验证
     *
     * @return boolean
     */
    private boolean isPlayerAvailable() {
        return this.mInternalPlayer != null;
    }

    /**
     * 验证 是否开启了播放进度缓存
     *
     * @return boolean
     */
    private boolean isProgressCacheOpen() {
        return PlayerConfig.getInstance().isPlayProgressCache() && this.mProgressCache != null;
    }

    /**
     * 播放器的基础事件
     */
    private OnPlayerEventListener mInternalPlayerEventListener = new OnPlayerEventListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            // 验证 播放进度缓存
            if (isProgressCacheOpen()) {
                mProgressCache.onPlayerEvent(eventCode, bundle);
            }
            // 回调 播放器的基础事件
            callBackPlayEventListener(eventCode, bundle);
        }
    };

    /**
     * 播放器的错误事件
     */
    private OnErrorEventListener mInternalErrorEventListener = new OnErrorEventListener() {
        @Override
        public void onErrorEvent(int eventCode, Bundle bundle) {
            // 回调 播放器的错误事件
            callBackErrorEventListener(eventCode, bundle);
        }
    };

    /**
     * 回调 播放器的基础事件 {@link KsgVideoPlayer}
     */
    private void callBackPlayEventListener(int eventCode, Bundle bundle) {
        if (mOnPlayerEventListener != null) {
            this.mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
        }
    }

    /**
     * 回调 播放器的基础事件 {@link KsgVideoPlayer}
     */
    private void callBackErrorEventListener(int eventCode, Bundle bundle) {
        if (mOnErrorEventListener != null) {
            this.mOnErrorEventListener.onErrorEvent(eventCode, bundle);
        }
    }

    /**
     * 设置 播放器状态事件
     *
     * @param onPlayerEventListener onPlayerEventListener
     */
    @Override
    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    /**
     * 设置 播放器错误事件
     *
     * @param onErrorEventListener onErrorEventListener
     */
    @Override
    public void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        this.mOnErrorEventListener = onErrorEventListener;
    }

}

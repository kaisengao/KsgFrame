package com.ksg.ksgplayer.proxy;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.ksg.ksgplayer.KsgVideoPlayer;
import com.ksg.ksgplayer.cache.IPlaybackCache;
import com.ksg.ksgplayer.cache.PlaybackCache;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorListener;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.BasePlayer;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.state.PlayerStateGetter;

/**
 * @ClassName: PlayerProxy
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 15:25
 * @Description: 播放器代理
 */
public class PlayerProxy implements IPlayer {

    private float mVolumeLeft = -1, mVolumeRight = -1;

    private boolean isBuffering;

    /**
     * 视频播放地址
     */
    private DataSource mDataSource;

    /**
     * 播放器
     */
    private BasePlayer mBasePlayer;

    /**
     * 播放缓存
     */
    private IPlaybackCache mPlaybackCache;

    /**
     * 进度更新计时代理
     */
    private TimerCounterProxy mTimerCounterProxy;

    /**
     * 播放器状态事件
     */
    private OnPlayerListener mPlayerListener;

    /**
     * 播放器错误事件
     */
    private OnErrorListener mErrorListener;

    public PlayerProxy() {
        // Init 播放缓存
        this.initPlaybackCache();
        // Init 进度更新计时代理
        this.initTimerCounterProxy();

    }

    /**
     * Init 播放缓存
     */
    private void initPlaybackCache() {
        this.mPlaybackCache = new PlaybackCache();
        this.mPlaybackCache.bindPlayStateGetter(new PlayerStateGetter() {

            @Override
            public int getState() {
                return PlayerProxy.this.getState();
            }

            @Override
            public long getProgress() {
                return getCurrentPosition();
            }

            @Override
            public long getDuration() {
                return PlayerProxy.this.getDuration();
            }

            @Override
            public int getBufferPercentage() {
                return PlayerProxy.this.getBufferPercentage();
            }

            @Override
            public boolean isBuffering() {
                return isBuffering;
            }
        });
    }

    /**
     * Init 进度更新计时代理
     */
    private void initTimerCounterProxy() {
        // 初始化 进度更新计时代理
        this.mTimerCounterProxy = new TimerCounterProxy(1000);
    }

    /**
     * 进度更新计时代理状态
     *
     * @param useProxy 开/关
     */
    public void setUseTimerProxy(boolean useProxy) {
        this.mTimerCounterProxy.setUseProxy(useProxy);
    }

    /**
     * 设置（播放器）解码器
     *
     * @param decoderView {@link BasePlayer}
     */
    public void setDecoderView(BasePlayer decoderView) {
        this.resetPlayer();
        this.mBasePlayer = decoderView;
    }

    /**
     * 返回 （播放器）解码器
     *
     * @return {@link BasePlayer}
     */
    public BasePlayer getDecoderView() {
        return mBasePlayer;
    }

    /**
     * 是否处于播放
     *
     * @return boolean
     */
    @Override
    public boolean isItPlaying() {
        return mBasePlayer.isItPlaying();
    }

    /**
     * 获取当前state
     *
     * @return 返回状态
     */
    @Override
    public int getState() {
        if (isPlayerAvailable()) {
            return mBasePlayer.getState();
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
    public void setDataSource(DataSource dataSource) {
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
    private void interPlayerSetDataSource(DataSource dataSource) {
        // 缓存 设置视频播放地址
        this.mPlaybackCache.setDataSource(dataSource);
        // 设置视频播放地址
        if (isPlayerAvailable()) {
            this.mBasePlayer.setDataSource(dataSource);
        }
    }

    /**
     * 配置 事件监听
     */
    private void initListener() {
        this.mTimerCounterProxy.setTimerCounterListener(mTimerCounterListener);
        if (isPlayerAvailable()) {
            this.mBasePlayer.setPlayerListener(mInternalPlayerEventListener);
            this.mBasePlayer.setErrorListener(mInternalErrorEventListener);
        }
    }

    /**
     * 重置 事件监听
     */
    private void resetListener() {
        this.mTimerCounterProxy.setTimerCounterListener(null);
        if (isPlayerAvailable()) {
            this.mBasePlayer.setPlayerListener(null);
            this.mBasePlayer.setErrorListener(null);
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
            this.mBasePlayer.option(code, bundle);
        }
    }

    /**
     * 设置渲染器
     *
     * @param surface surface
     */
    @Override
    public void setSurface(Surface surface) {
        if (isPlayerAvailable()) {
            this.mBasePlayer.setSurface(surface);
        }
    }

    /**
     * 设置渲染器
     *
     * @param holder surfaceHolder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        if (isPlayerAvailable()) {
            this.mBasePlayer.setDisplay(holder);
        }
    }

    /**
     * 获取渲染器
     */
    @Override
    public View getRenderer() {
        if (isPlayerAvailable()) {
            return mBasePlayer.getRenderer();
        }
        return null;
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float left, float right) {
        this.mVolumeLeft = left;
        this.mVolumeRight = right;
        if (isPlayerAvailable()) {
            this.mBasePlayer.setVolume(left, right);
        }
    }

    /**
     * 设置是否循环播放
     */
    @Override
    public void setLooping(boolean looping) {
        if (isPlayerAvailable()) {
            this.mBasePlayer.setLooping(looping);
        }
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        if (isPlayerAvailable()) {
            this.mBasePlayer.setSpeed(speed);
        }
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        if (isPlayerAvailable()) {
            return this.mBasePlayer.getSpeed();
        }
        return 0;
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        if (isPlayerAvailable()) {
            return this.mBasePlayer.getTcpSpeed();
        }
        return 0;
    }

    /**
     * 获取缓冲进度百分比
     *
     * @return 缓冲进度
     */
    @Override
    public int getBufferPercentage() {
        if (isPlayerAvailable()) {
            return this.mBasePlayer.getBufferPercentage();
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
            return this.mBasePlayer.getCurrentPosition();
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
            return this.mBasePlayer.getDuration();
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
            return this.mBasePlayer.isPlaying();
        }
        return false;
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        // 播放
        this.start(mPlaybackCache.getProgressCache(mDataSource));
    }

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    @Override
    public void seekTo(long msc) {
        if (isPlayerAvailable()) {
            this.mBasePlayer.seekTo(msc);
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
            this.mBasePlayer.start(msc);
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        if (isPlayerAvailable()) {
            this.mBasePlayer.pause();
        }
        this.mPlaybackCache.onIntentPause();
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        if (isPlayerAvailable()) {
            this.mBasePlayer.resume();
        }
    }

    /**
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    public void replay(long msc) {
        if (mDataSource != null && isPlayerAvailable()) {
            this.interPlayerSetDataSource(mDataSource);
            this.mBasePlayer.start(msc);
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        if (isPlayerAvailable()) {
            this.mBasePlayer.stop();
        }
        this.mPlaybackCache.onIntentStop();
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        if (isPlayerAvailable()) {
            this.mBasePlayer.reset();
        }
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        if (isPlayerAvailable()) {
            this.mBasePlayer.release();
        }
    }

    /**
     * 重置
     */
    private void resetPlayer() {
        if (isPlayerAvailable()) {
            this.mBasePlayer.destroy();
            this.mBasePlayer = null;
        }
        // 重置 事件监听
        this.resetListener();
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        // 重置
        this.resetPlayer();
        // 释放资源
        if (mTimerCounterProxy != null) {
            this.mTimerCounterProxy.destroy();
            this.mTimerCounterProxy = null;
        }
        if (mPlaybackCache != null) {
            this.mPlaybackCache.onIntentDestroy();
            this.mPlaybackCache = null;
        }
    }

    /**
     * 播放器非空验证
     *
     * @return boolean
     */
    private boolean isPlayerAvailable() {
        return mBasePlayer != null;
    }

    /**
     * 进度更新计时代理
     */
    private final TimerCounterProxy.OnTimerCounterListener mTimerCounterListener = new TimerCounterProxy.OnTimerCounterListener() {
        @Override
        public void onCounter() {
            long curr = getCurrentPosition();
            long duration = getDuration();
            long bufferPercentage = getBufferPercentage();
            if (duration <= 0) {
                return;
            }
            onTimerUpdateEvent(curr, duration, bufferPercentage);
        }
    };

    /**
     * @param curr             播放进度
     * @param duration         播放总时长
     * @param bufferPercentage 缓冲进度
     */
    private void onTimerUpdateEvent(long curr, long duration, long bufferPercentage) {
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_ARG1, curr);
        bundle.putLong(EventKey.LONG_ARG2, duration);
        bundle.putLong(EventKey.LONG_ARG3, bufferPercentage);
        this.callBackPlayEventListener(OnPlayerListener.PLAYER_EVENT_ON_TIMER_UPDATE, bundle);
    }

    /**
     * 播放事件
     */
    private final OnPlayerListener mInternalPlayerEventListener = new OnPlayerListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            mTimerCounterProxy.proxyPlayEvent(eventCode, bundle);
            // 事件
            switch (eventCode) {
                case OnPlayerListener.PLAYER_EVENT_ON_PREPARED:
                    if (mVolumeLeft >= 0 || mVolumeRight >= 0) {
                        mBasePlayer.setVolume(mVolumeLeft, mVolumeRight);
                    }
                    break;
                case OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                    long duration = getDuration();
                    long bufferPercentage = getBufferPercentage();
                    if (duration > 0) {
                        onTimerUpdateEvent(duration, duration, bufferPercentage);
                    }
                    break;
                case OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_START:
                    // 事件 开始缓冲
                    isBuffering = true;
                    break;
                case OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_END:
                    // 事件 结束缓冲
                    isBuffering = false;
                    break;
                default:
                    break;
            }
            // 回调
            mPlaybackCache.onPlayerEvent(eventCode, bundle);
            // 回调
            callBackPlayEventListener(eventCode, bundle);
        }
    };

    /**
     * 错误事件
     */
    private final OnErrorListener mInternalErrorEventListener = new OnErrorListener() {
        @Override
        public void onErrorEvent(int eventCode, Bundle bundle) {
            mTimerCounterProxy.proxyErrorEvent(eventCode, bundle);
            // 回调
            mPlaybackCache.onErrorEvent(eventCode, bundle);
            // 回调
            callBackErrorEventListener(eventCode, bundle);
        }
    };

    /**
     * 回调 播放事件 {@link KsgVideoPlayer}
     */
    private void callBackPlayEventListener(int eventCode, Bundle bundle) {
        if (mPlayerListener != null) {
            this.mPlayerListener.onPlayerEvent(eventCode, bundle);
        }
    }

    /**
     * 回调 错误事件 {@link KsgVideoPlayer}
     */
    private void callBackErrorEventListener(int eventCode, Bundle bundle) {
        if (mErrorListener != null) {
            this.mErrorListener.onErrorEvent(eventCode, bundle);
        }
    }

    /**
     * 设置 播放事件
     *
     * @param playerListener OnPlayerListener
     */
    public void setPlayerListener(OnPlayerListener playerListener) {
        this.mPlayerListener = playerListener;
    }

    /**
     * 设置 错误事件
     *
     * @param errorListener OnErrorListener
     */
    public void setErrorListener(OnErrorListener errorListener) {
        this.mErrorListener = errorListener;
    }
}
package com.kasiengao.ksgframe.java.player.player;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.kaisengao.base.util.KLog;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.IKsgPlayer;

import org.jetbrains.annotations.NotNull;

import static com.google.android.exoplayer2.upstream.DataSource.Factory;

/**
 * @ClassName: KsgExoPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/11/3 10:22
 * @Description: ExoPlayer Google播放器
 */
public class KsgExoPlayer extends BaseInternalPlayer {

    private long mStartSeekPos = -1;

    private boolean isPreparing = true;

    private boolean isBuffering = false;

    private boolean isPendingSeek = false;

    private SimpleExoPlayer mExoPlayer;

    private DefaultBandwidthMeter mBandwidthMeter;

    public KsgExoPlayer(Context context) {
        super(context);
        // 初始化通知
        this.updateStatus(IKsgPlayer.STATE_INITIALIZED);
    }

    /**
     * 初始化 播放器
     */
    @Override
    public void initPlayer() {
        this.mExoPlayer = new SimpleExoPlayer
                .Builder(mContext, new DefaultRenderersFactory(mContext))
                .setTrackSelector(new DefaultTrackSelector(mContext)).build();
        this.mBandwidthMeter = new DefaultBandwidthMeter.Builder(mContext).build();
    }

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        try {
            // 验证初始化
            if (mExoPlayer == null) {
                this.initPlayer();
            } else {
                this.stop();
                this.reset();
                this.release();
            }
            // 播放准备
            this.updateStatus(IKsgPlayer.STATE_INITIALIZED);
            // 事件监听
            this.mExoPlayer.addListener(mEventListener);
            this.mExoPlayer.addVideoListener(mVideoListener);
            // dataSource
            Uri videoUri = Uri.parse(dataSource.getUrl());
            // if not setting, use default user-agent
            String userAgent = Util.getUserAgent(mContext, mContext.getPackageName());
            // create DefaultDataSourceFactory
            Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent, mBandwidthMeter);
            // create MediaSource
            MediaSource mediaSource = getMediaSource(videoUri, dataSourceFactory);
            // Prepare the player with the source.
            this.isPreparing = true;
            this.mExoPlayer.prepare(mediaSource);
            this.mExoPlayer.setPlayWhenReady(false);
            // 发送数据源事件
            Bundle bundle = BundlePool.obtain();
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
            this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET, bundle);
        } catch (Exception e) {
            this.updateStatus(IKsgPlayer.STATE_ERROR);
            this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_DATA_SOURCE, e.getMessage());
        }
    }

    private MediaSource getMediaSource(Uri uri, Factory dataSourceFactory) {
        int contentType = Util.inferContentType(uri);
        switch (contentType) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
            default:
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        }
    }

    /**
     * 设置渲染视频的View,主要用于TextureView
     *
     * @param surface surface
     */
    @Override
    public void setSurface(Surface surface) {
        this.mExoPlayer.setVideoSurface(surface);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SURFACE_UPDATE, null);
    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     *
     * @param holder surfaceHolder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        this.mExoPlayer.setVideoSurfaceHolder(holder);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SURFACE_HOLDER_UPDATE, null);
    }

    /**
     * 设置 播放器自定义的视图
     *
     * @return View
     */
    @Override
    public View getRenderView() {
        return null;
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float left, float right) {
        this.mExoPlayer.setVolume(left);
    }

    /**
     * 设置是否循环播放
     */
    @Override
    public void setLooping(boolean isLooping) {
        this.mExoPlayer.setRepeatMode(isLooping ? ExoPlayer.REPEAT_MODE_ONE : ExoPlayer.REPEAT_MODE_OFF);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        PlaybackParameters parameters = new PlaybackParameters(speed, 1f);
        this.mExoPlayer.setPlaybackParameters(parameters);
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        return mExoPlayer.getPlaybackParameters().speed;
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        return 0;
    }

    /**
     * 获取缓冲进度百分比
     *
     * @return 缓冲进度
     */
    @Override
    public int getBufferPercentage() {
        return mBufferPercentage;
    }

    /**
     * 获取当前播放的位置
     *
     * @return 播放进度
     */
    @Override
    public long getCurrentPosition() {
        return mExoPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     *
     * @return 总时长
     */
    @Override
    public long getDuration() {
        return mExoPlayer.getDuration();
    }

    /**
     * 获取播放状态
     *
     * @return 播放状态 true 播放 反之
     */
    @Override
    public boolean isPlaying() {
        return mExoPlayer.isPlaying();
    }

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    @Override
    public void seekTo(long msc) {
        if (msc < 0) {
            return;
        }
        int state = getState();
        if (state == IKsgPlayer.STATE_PREPARED
                || state == IKsgPlayer.STATE_STARTED
                || state == IKsgPlayer.STATE_PAUSED
                || state == IKsgPlayer.STATE_PLAYBACK_COMPLETE) {
            this.isPendingSeek = true;
            // 计算百分比
            float percent = ((float) msc) / getDuration();
            // 计算当前位置
            int currentPosition = (int) (getDuration() * percent);
            try {
                this.mExoPlayer.seekTo(currentPosition);
            } catch (IllegalStateException e) {
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_SEEK, null);
            }
            // seekTo
            Bundle bundle = BundlePool.obtain();
            bundle.putLong(EventKey.LONG_DATA, msc);
            this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_TO, bundle);
        }
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        try {
            this.mExoPlayer.setPlayWhenReady(true);
            // 开始播放
            this.updateStatus(IKsgPlayer.STATE_STARTED);
            this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_START, null);
        } catch (Exception e) {
            this.updateStatus(IKsgPlayer.STATE_ERROR);
            this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_START, e.getMessage());
        }
    }

    /**
     * start
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void start(long msc) {
        if (msc > 0) {
            this.mStartSeekPos = msc;
        }
        // start
        this.start();
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        int state = getState();
        if (state != IKsgPlayer.STATE_END
                && state != IKsgPlayer.STATE_ERROR
                && state != IKsgPlayer.STATE_IDLE
                && state != IKsgPlayer.STATE_INITIALIZED
                && state != IKsgPlayer.STATE_PAUSED
                && state != IKsgPlayer.STATE_STOPPED) {
            try {
                this.mExoPlayer.setPlayWhenReady(false);
            } catch (IllegalStateException e) {
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_PAUSE, e.getMessage());
            }
            // 暂停播放
            this.updateStatus(IKsgPlayer.STATE_PAUSED);
            this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE, null);
        }
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        if (getState() == IKsgPlayer.STATE_PAUSED) {
            try {
                this.mExoPlayer.setPlayWhenReady(true);
            } catch (IllegalStateException e) {
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_RESUME, e.getMessage());
            }
            // 继续播放
            this.updateStatus(IKsgPlayer.STATE_STARTED);
            this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESUME, null);
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        this.isPreparing = true;
        this.isBuffering = false;
        int state = getState();
        if (state == IKsgPlayer.STATE_PREPARED
                || state == IKsgPlayer.STATE_STARTED
                || state == IKsgPlayer.STATE_PAUSED
                || state == IKsgPlayer.STATE_PLAYBACK_COMPLETE) {
            try {
                this.mExoPlayer.stop();
            } catch (IllegalStateException e) {
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_STOP, e.getMessage());
            }
            // 停止播放
            this.updateStatus(IKsgPlayer.STATE_STOPPED);
            this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_STOP, null);
        }
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        this.stop();
        this.updateStatus(IKsgPlayer.STATE_IDLE);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESET, null);
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        if (mExoPlayer == null) {
            return;
        }
        this.isPreparing = true;
        this.isBuffering = false;
        this.mExoPlayer.removeListener(mEventListener);
        this.mExoPlayer.removeVideoListener(mVideoListener);
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.stop();
        this.release();
        this.mExoPlayer = null;
        // 销毁
        this.updateStatus(IKsgPlayer.STATE_END);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY, null);
    }

    /**
     * 播放信息事件
     */
    private final VideoListener mVideoListener = new VideoListener() {

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            Bundle bundle = BundlePool.obtain();
            bundle.putInt(EventKey.INT_ARG1, width);
            bundle.putInt(EventKey.INT_ARG2, height);
            bundle.putInt(EventKey.INT_ARG3, 0);
            bundle.putInt(EventKey.INT_ARG4, 0);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE, bundle);
        }

        @Override
        public void onRenderedFirstFrame() {
            KLog.d("onRenderedFirstFrame");
            updateStatus(IKsgPlayer.STATE_STARTED);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null);
        }
    };

    /**
     * 播放事件
     */
    private final Player.EventListener mEventListener = new Player.EventListener() {

        @Override
        public void onTracksChanged(@NotNull TrackGroupArray trackGroups, @NotNull TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            int bufferPercentage = mExoPlayer.getBufferedPercentage();
            if (!isLoading) {
                setBufferPercentage(bufferPercentage);
            }
            KLog.d("onLoadingChanged : " + isLoading + ", bufferPercentage = " + bufferPercentage);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            KLog.d("onPlayerStateChanged : playWhenReady = " + playWhenReady
                    + ", playbackState = " + playbackState);

            if (!isPreparing) {
                if (playWhenReady) {
                    updateStatus(IKsgPlayer.STATE_STARTED);
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESUME, null);
                } else {
                    updateStatus(IKsgPlayer.STATE_PAUSED);
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE, null);
                }
            }

            if (isPreparing) {
                switch (playbackState) {
                    case Player.STATE_READY:
                        isPreparing = false;
                        Format format = mExoPlayer.getVideoFormat();
                        Bundle bundle = BundlePool.obtain();
                        if (format != null) {
                            bundle.putInt(EventKey.INT_ARG1, format.width);
                            bundle.putInt(EventKey.INT_ARG2, format.height);
                        }
                        updateStatus(IKsgPlayer.STATE_PREPARED);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED, bundle);

                        if (playWhenReady) {
                            updateStatus(STATE_STARTED);
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_START, null);
                        }

                        if (mStartSeekPos > 0) {
                            mExoPlayer.seekTo(mStartSeekPos);
                            mStartSeekPos = -1;
                        }
                        break;
                }
            }

            if (isBuffering) {
                switch (playbackState) {
                    case Player.STATE_READY:
                    case Player.STATE_ENDED:
                        long bitrateEstimate = mBandwidthMeter.getBitrateEstimate();
                        KLog.d("buffer_end, BandWidth : " + bitrateEstimate);
                        isBuffering = false;
                        Bundle bundle = BundlePool.obtain();
                        bundle.putLong(EventKey.LONG_DATA, bitrateEstimate);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END, bundle);
                        break;
                }
            }

            if (isPendingSeek) {
                switch (playbackState) {
                    case Player.STATE_READY:
                        isPendingSeek = false;
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE, null);
                        break;
                }
            }

            if (!isPreparing) {
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        long bitrateEstimate = mBandwidthMeter.getBitrateEstimate();
                        KLog.d("buffer_start, BandWidth : " + bitrateEstimate);
                        isBuffering = true;
                        Bundle bundle = BundlePool.obtain();
                        bundle.putLong(EventKey.LONG_DATA, bitrateEstimate);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START, bundle);
                        break;
                    case Player.STATE_ENDED:
                        updateStatus(IKsgPlayer.STATE_PLAYBACK_COMPLETE);
                        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE, null);
                        break;
                }
            }

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onPlayerError(@NotNull ExoPlaybackException error) {
            KLog.e(error.getMessage() == null ? "" : error.getMessage());
            switch (error.type) {
                case ExoPlaybackException.TYPE_SOURCE:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_IO, null);
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_COMMON, null);
                    break;
                case ExoPlaybackException.TYPE_REMOTE:
                case ExoPlaybackException.TYPE_OUT_OF_MEMORY:
                case ExoPlaybackException.TYPE_UNEXPECTED:
                default:
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, null);
                    break;
            }
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            KLog.d("onPlaybackParametersChanged : " + playbackParameters.toString());
        }
    };
}
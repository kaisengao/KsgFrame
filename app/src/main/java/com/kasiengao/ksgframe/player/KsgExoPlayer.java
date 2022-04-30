package com.kasiengao.ksgframe.player;

import static com.google.android.exoplayer2.Player.DISCONTINUITY_REASON_SEEK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoSize;
import com.kaisengao.base.util.FileUtil;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorListener;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.BasePlayer;
import com.ksg.ksgplayer.player.IPlayer;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @ClassName: KsgExoPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/11/3 10:22
 * @Description: ExoPlayer Google播放器
 */
public class KsgExoPlayer extends BasePlayer {

    private long mStartPos = -1;

    private int mLastPlaybackState;

    private boolean isPreparing = true;

    private boolean isBuffering = false;

    private boolean isLastPlayWhenReady;

    private DataSource mDataSource;

    private ExoPlayer mExoPlayer;

    private HttpProxyCacheServer mCacheServer;

    public KsgExoPlayer(Context context) {
        super(context);
        // 初始化通知
        this.updateState(IPlayer.STATE_IDLE);
        this.mLastPlaybackState = Player.STATE_IDLE;
    }

    /**
     * Init Player
     */
    @Override
    public void initPlayer() {
        @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER;
        DefaultRenderersFactory rendererFactory = new DefaultRenderersFactory(mContext);
        rendererFactory.setExtensionRendererMode(extensionRendererMode);
        this.mExoPlayer = new ExoPlayer.Builder(mContext, rendererFactory)
                .setLooper(Looper.myLooper())
                .setTrackSelector(new DefaultTrackSelector(mContext))
                .setLoadControl(new DefaultLoadControl())
                .build();
    }

    /**
     * Get Player
     *
     * @return ExoPlayer
     */
    public ExoPlayer getExoPlayer() {
        return mExoPlayer;
    }

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        this.mDataSource = dataSource;
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
            this.updateState(IPlayer.STATE_INIT);
            // 事件监听
            this.mExoPlayer.addListener(mListener);
            // Uri
            Uri videoUri;
            // VideoUrl
            String videoUrl = dataSource.getUrl();
            // Http or File
            if ((videoUrl.startsWith("http://") || videoUrl.startsWith("https://")) && dataSource.isVideoCache()) {
                videoUri = Uri.parse(getCacheServer(dataSource).getProxyUrl(videoUrl));
            } else {
                videoUri = Uri.parse(videoUrl);
            }
            // if not setting, use default user-agent
            String userAgent = Util.getUserAgent(mContext, mContext.getPackageName());
            // create DefaultDataSourceFactory
            DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(mContext, new DefaultHttpDataSource.Factory().setUserAgent(userAgent));
            // create MediaSource
            MediaSource mediaSource = getMediaSource(videoUri, dataSourceFactory);
            // Prepare the player with the source.
            this.isPreparing = true;
            this.mExoPlayer.setMediaSource(mediaSource);
            this.mExoPlayer.prepare();
            this.mExoPlayer.setPlayWhenReady(false);
            // 发送数据源事件
            Bundle bundle = BundlePool.obtain();
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_DATA_SOURCE_SET, bundle);
        } catch (Exception e) {
            this.updateState(IPlayer.STATE_ERROR);
            this.sendErrorEvent(OnErrorListener.ERROR_EVENT_DATA_SOURCE, e.getMessage());
        }
    }

    private MediaSource getMediaSource(Uri uri, DefaultDataSource.Factory dataSourceFactory) {
        int contentType = Util.inferContentType(uri);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        switch (contentType) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
            case C.TYPE_RTSP:
            case C.TYPE_OTHER:
            default:
                return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
        }
    }

    public HttpProxyCacheServer getCacheServer(DataSource dataSource) {
        if (mCacheServer == null) {
            this.mCacheServer = new HttpProxyCacheServer.Builder(mContext)
                    .cacheDirectory(new File(FileUtil.getFileDir(mContext, dataSource.getVideoCacheFileName())))
                    .maxCacheFilesCount(dataSource.getMaxVideoCacheCount())
                    .build();
        }
        return mCacheServer;
    }

    /**
     * 设置渲染器
     *
     * @param surface surface
     */
    @Override
    public void setSurface(Surface surface) {
        this.mExoPlayer.setVideoSurface(surface);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SURFACE_UPDATE, null);
    }

    /**
     * 设置渲染器
     *
     * @param holder surfaceHolder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        this.mExoPlayer.setVideoSurfaceHolder(holder);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SURFACE_HOLDER_UPDATE, null);
    }

    /**
     * 设置 播放器自定义的视图
     *
     * @return View
     */
    @Override
    public View getCustomRenderer() {
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
    public void setLooping(boolean looping) {
        this.mExoPlayer.setRepeatMode(looping ? ExoPlayer.REPEAT_MODE_ONE : ExoPlayer.REPEAT_MODE_OFF);
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
        int state = mExoPlayer.getPlaybackState();
        switch (state) {
            case Player.STATE_BUFFERING:
            case Player.STATE_READY:
                return mExoPlayer.getPlayWhenReady();
            case Player.STATE_IDLE:
            case Player.STATE_ENDED:
            default:
                return false;
        }
    }

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    @Override
    public void seekTo(long msc) {
        // 计算百分比
        float percent = ((float) msc) / getDuration();
        // 计算当前位置
        int currentPosition = (int) (getDuration() * percent);
        // Seek
        this.mExoPlayer.seekTo(currentPosition);
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_DATA, msc);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SEEK_TO, bundle);
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        this.mExoPlayer.setPlayWhenReady(true);
        this.updateState(IPlayer.STATE_START);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_START, null);
    }

    /**
     * start
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void start(long msc) {
        this.mStartPos = msc;
        this.start();
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        int state = getState();
        if (state != IPlayer.STATE_DESTROY
                && state != IPlayer.STATE_ERROR
                && state != IPlayer.STATE_IDLE
                && state != IPlayer.STATE_INIT
                && state != IPlayer.STATE_PAUSE
                && state != IPlayer.STATE_STOP
                && state != IPlayer.STATE_COMPLETE) {
            this.mExoPlayer.setPlayWhenReady(false);
            this.updateState(IPlayer.STATE_PAUSE);
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PAUSE, null);
        }
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        if (getState() == IPlayer.STATE_PAUSE) {
            this.mExoPlayer.setPlayWhenReady(true);
            this.updateState(IPlayer.STATE_START);
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_RESUME, null);
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        int state = getState();
        if (state == IPlayer.STATE_PREPARED
                || state == IPlayer.STATE_START
                || state == IPlayer.STATE_PAUSE
                || state == IPlayer.STATE_COMPLETE) {
            this.mExoPlayer.stop();
            this.updateState(IPlayer.STATE_STOP);
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_STOP, null);
        }
    }

    /**
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void replay(long msc) {
        if (mDataSource != null) {
            this.setDataSource(mDataSource);
            this.start(msc);
        }
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        this.stop();
        this.updateState(IPlayer.STATE_IDLE);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_RESET, null);
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        if (mExoPlayer == null) {
            return;
        }
        this.mExoPlayer.clearVideoSurface();
        this.mExoPlayer.removeListener(mListener);
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.release();
        this.mExoPlayer.release();
        this.updateState(IPlayer.STATE_DESTROY);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_DESTROY, null);
    }

    /**
     * 事件
     */
    private final Player.Listener mListener = new Player.Listener() {

        @Override
        public void onIsLoadingChanged(boolean isLoading) {
            // 缓冲进度
            if (!isLoading) {
                setBufferPercentage(mExoPlayer.getBufferedPercentage());
            }
        }

        @Override
        public void onPlaybackStateChanged(int playbackState) {
            onPlayWhenReadyChanged(isLastPlayWhenReady, playbackState);
        }

        @SuppressLint("SwitchIntDef")
        @Override
        public void onPlayWhenReadyChanged(boolean playWhenReady, int playbackState) {
            // 重新播放状态顺序为：STATE_IDLE -》STATE_BUFFERING -》STATE_READY
            // 缓冲时顺序为：STATE_BUFFERING -》STATE_READY
            if (isLastPlayWhenReady != playWhenReady || mLastPlaybackState != playbackState) {
                if (isBuffering) {
                    switch (playbackState) {
                        case Player.STATE_ENDED:
                        case Player.STATE_READY:
                            sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_END, null);
                            isBuffering = false;
                            break;
                    }
                }

                if (isPreparing) {
                    switch (playbackState) {
                        case Player.STATE_READY:
                            updateState(IPlayer.STATE_PREPARED);
                            sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PREPARED, null);
                            if (playWhenReady) {
                                updateState(STATE_START);
                                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_START, null);
                            }
                            if (mStartPos > 0 && mExoPlayer.getDuration() > 0) {
                                seekTo(mStartPos);
                                mStartPos = -1;
                            }
                            isPreparing = false;
                            break;
                        case Player.STATE_ENDED:
                            break;
                    }
                }

                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_START, null);
                        isBuffering = true;
                        break;
                    case Player.STATE_ENDED:
                        updateState(IPlayer.STATE_COMPLETE);
                        sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE, null);
                        break;
                    default:
                        break;
                }
            }
            isLastPlayWhenReady = playWhenReady;
            mLastPlaybackState = playbackState;
        }

        @Override
        public void onPlayerError(PlaybackException error) {
            updateState(IPlayer.STATE_ERROR);
            sendErrorEvent(OnErrorListener.ERROR_EVENT_UNKNOWN, error.errorCode + "|" + error.getErrorCodeName());
        }

        @Override
        public void onPositionDiscontinuity(@NotNull Player.PositionInfo oldPosition, @NotNull Player.PositionInfo newPosition, int reason) {
            if (reason == DISCONTINUITY_REASON_SEEK) {
                Bundle bundle = BundlePool.obtain();
                bundle.putLong(EventKey.LONG_DATA, newPosition.positionMs);
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SEEK_COMPLETE, bundle);
            }
        }

        @Override
        public void onVideoSizeChanged(VideoSize videoSize) {
            Bundle bundle = BundlePool.obtain();
            bundle.putInt(EventKey.INT_ARG1, videoSize.width);
            bundle.putInt(EventKey.INT_ARG2, videoSize.height);
            sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE, bundle);
        }

        @Override
        public void onRenderedFirstFrame() {
            sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null);
        }
    };
}
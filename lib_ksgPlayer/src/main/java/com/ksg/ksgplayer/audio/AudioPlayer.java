package com.ksg.ksgplayer.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.kaisengao.base.util.KLog;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.IKsgPlayer;

/**
 * @ClassName: AudioPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/9/2 13:12
 * @Description: MediaPlayer 音频播放器
 */
public class AudioPlayer extends BaseInternalPlayer {

    private int mTargetState;

    private MediaPlayer mMediaPlayer;

    public AudioPlayer(Context context) {
        super(context);
        // 初始化通知
        this.updateStatus(IKsgPlayer.STATE_INITIALIZED);
    }

    /**
     * 初始化 播放器
     */
    @Override
    public void initPlayer() {
        this.mMediaPlayer = new MediaPlayer();
    }

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        try {
            if (this.mMediaPlayer == null) {
                this.initPlayer();
            } else {
                this.stop();
                this.reset();
                this.release();
            }
            // 事件监听
            this.mMediaPlayer.setOnInfoListener(mInfoListener);
            this.mMediaPlayer.setOnPreparedListener(mPreparedListener);
            this.mMediaPlayer.setOnCompletionListener(mCompletionListener);
            this.mMediaPlayer.setOnErrorListener(mErrorListener);
            this.mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            this.mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            this.updateStatus(STATE_INITIALIZED);
            // dataSource
            this.mMediaPlayer.setDataSource(dataSource.getUrl());
            this.mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.mMediaPlayer.prepareAsync();
            // 发送数据源事件
            Bundle bundle = BundlePool.obtain();
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
            this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET, bundle);
        } catch (Exception e) {
            this.mTargetState = IKsgPlayer.STATE_ERROR;
            this.updateStatus(IKsgPlayer.STATE_ERROR);
            this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_DATA_SOURCE, e.getMessage());
        }
    }

    /**
     * 设置渲染视频的View,主要用于TextureView
     *
     * @param surface surface
     */
    @Override
    public void setSurface(Surface surface) {

    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     *
     * @param holder surfaceHolder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {

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
        this.mMediaPlayer.setVolume(left, right);
    }

    /**
     * 设置是否循环播放
     */
    @Override
    public void setLooping(boolean isLooping) {
        this.mMediaPlayer.setLooping(isLooping);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {

    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        return 0;
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
        return this.mBufferPercentage;
    }

    /**
     * 获取当前播放的位置
     *
     * @return 播放进度
     */
    @Override
    public long getCurrentPosition() {
        if (getState() == IKsgPlayer.STATE_PREPARED) {
            return mMediaPlayer.getCurrentPosition();
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
        if (getState() == IKsgPlayer.STATE_PREPARED) {
            return mMediaPlayer.getDuration();
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
        return this.mMediaPlayer.isPlaying();
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
            // 计算百分比
            float percent = ((float) msc) / getDuration();
            // 计算当前位置
            int currentPosition = (int) (getDuration() * percent);
            try {
                this.mMediaPlayer.seekTo(currentPosition);
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
        int state = getState();
        if (state == IKsgPlayer.STATE_PREPARED
                || state == IKsgPlayer.STATE_PAUSED
                || state == IKsgPlayer.STATE_PLAYBACK_COMPLETE
                || state == IKsgPlayer.STATE_STOPPED) {
            try {
                this.mMediaPlayer.start();
                // 开始播放
                this.updateStatus(IKsgPlayer.STATE_STARTED);
                this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_START, null);
            } catch (Exception e) {
                this.mTargetState = IKsgPlayer.STATE_ERROR;
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_START, e.getMessage());
            }
        }
        this.mTargetState = IKsgPlayer.STATE_STARTED;
    }

    /**
     * start
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void start(long msc) {
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
                this.mMediaPlayer.pause();
                this.updateStatus(IKsgPlayer.STATE_PAUSED);
                this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE, null);
            } catch (IllegalStateException e) {
                this.mTargetState = IKsgPlayer.STATE_ERROR;
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_PAUSE, e.getMessage());
            }
        }
        this.mTargetState = IKsgPlayer.STATE_PAUSED;
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        if (getState() == IKsgPlayer.STATE_PAUSED) {
            try {
                this.mMediaPlayer.start();
                this.updateStatus(IKsgPlayer.STATE_STARTED);
                this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESUME, null);
            } catch (IllegalStateException e) {
                this.mTargetState = IKsgPlayer.STATE_ERROR;
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_RESUME, e.getMessage());
            }
        }
        this.mTargetState = IKsgPlayer.STATE_STARTED;
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        int state = getState();
        if (state == IKsgPlayer.STATE_PREPARED
                || state == IKsgPlayer.STATE_STARTED
                || state == IKsgPlayer.STATE_PAUSED
                || state == IKsgPlayer.STATE_PLAYBACK_COMPLETE) {
            try {
                this.mMediaPlayer.stop();
                this.updateStatus(IKsgPlayer.STATE_STOPPED);
                this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_STOP, null);
            } catch (IllegalStateException e) {
                this.mTargetState = IKsgPlayer.STATE_ERROR;
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_STOP, e.getMessage());
            }
        }
        this.mTargetState = IKsgPlayer.STATE_STOPPED;
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        this.mMediaPlayer.reset();
        this.mTargetState = IKsgPlayer.STATE_IDLE;
        this.updateStatus(IKsgPlayer.STATE_IDLE);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESET, null);
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        if (mMediaPlayer == null) {
            return;
        }
        this.mMediaPlayer.setOnPreparedListener(null);
        this.mMediaPlayer.setOnVideoSizeChangedListener(null);
        this.mMediaPlayer.setOnCompletionListener(null);
        this.mMediaPlayer.setOnErrorListener(null);
        this.mMediaPlayer.setOnInfoListener(null);
        this.mMediaPlayer.setOnBufferingUpdateListener(null);
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.stop();
        this.release();
        this.mMediaPlayer = null;
        // 销毁
        this.updateStatus(IKsgPlayer.STATE_END);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY, null);
    }

    /**
     * 播放器 准备完成 事件
     */
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            updateStatus(STATE_PREPARED);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED, null);

            KLog.d("Prepared: " + mTargetState);
            // 准备完成 判断当前状态类型
            if (mTargetState == STATE_STARTED) {
                start();
            } else if (mTargetState == STATE_PAUSED) {
                pause();
            } else if (mTargetState == STATE_STOPPED
                    || mTargetState == STATE_IDLE) {
                reset();
            }
        }
    };

    /**
     * 播放器 信息与警告 事件
     */
    private MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    KLog.d("MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    KLog.d("MEDIA_INFO_VIDEO_RENDERING_START");
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    KLog.d("MEDIA_INFO_BUFFERING_START:");
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START, null);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    KLog.d("MEDIA_INFO_BUFFERING_END:");
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END, null);
                    break;
                case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                    KLog.d("MEDIA_INFO_BAD_INTERLEAVING:");
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BAD_INTERLEAVING, null);
                    break;
                case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    KLog.d("MEDIA_INFO_NOT_SEEKABLE:");
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_NOT_SEEK_ABLE, null);
                    break;
                case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                    KLog.d("MEDIA_INFO_METADATA_UPDATE:");
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_METADATA_UPDATE, null);
                    break;
                case MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                    KLog.d("MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_UNSUPPORTED_SUBTITLE, null);
                    break;
                case MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                    KLog.d("MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SUBTITLE_TIMED_OUT, null);
                    break;
            }
            return false;
        }
    };

    /**
     * 播放器 缓冲进度 事件
     */
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            setBufferPercentage(percent);
        }
    };

    /**
     * 播放器 Seek结束 事件
     */
    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mediaPlayer) {
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE, null);
        }
    };
    /**
     * 播放器 结束 事件
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mTargetState = IKsgPlayer.STATE_PLAYBACK_COMPLETE;
            updateStatus(IKsgPlayer.STATE_PLAYBACK_COMPLETE);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE, null);
        }
    };

    /**
     * 播放器 Error 事件
     */
    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            KLog.d("Error: " + what + "," + extra);

            int eventCode = OnErrorEventListener.ERROR_EVENT_COMMON;

            switch (what) {
                case MediaPlayer.MEDIA_ERROR_IO:
                    eventCode = OnErrorEventListener.ERROR_EVENT_IO;
                    break;
                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                    eventCode = OnErrorEventListener.ERROR_EVENT_MALFORMED;
                    break;
                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    eventCode = OnErrorEventListener.ERROR_EVENT_TIMED_OUT;
                    break;
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    eventCode = OnErrorEventListener.ERROR_EVENT_UNKNOWN;
                    break;
                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                    eventCode = OnErrorEventListener.ERROR_EVENT_UNSUPPORTED;
                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    eventCode = OnErrorEventListener.ERROR_EVENT_SERVER_DIED;
                    break;
                case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                    eventCode = OnErrorEventListener.ERROR_EVENT_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK;
                    break;
            }

            mTargetState = IKsgPlayer.STATE_ERROR;
            updateStatus(IKsgPlayer.STATE_ERROR);
            submitErrorEvent(eventCode, null);
            return true;
        }
    };
}

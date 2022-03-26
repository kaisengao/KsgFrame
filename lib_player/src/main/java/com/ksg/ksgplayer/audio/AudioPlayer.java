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
import com.ksg.ksgplayer.listener.OnErrorListener;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.BasePlayer;
import com.ksg.ksgplayer.player.IPlayer;

/**
 * @ClassName: AudioPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/9/2 13:12
 * @Description: MediaPlayer 音频播放器
 */
public class AudioPlayer extends BasePlayer {

    private int mTargetState;

    private MediaPlayer mMediaPlayer;

    public AudioPlayer(Context context) {
        super(context);
        // 初始化通知
        this.updateStatus(IPlayer.STATE_INIT);
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
            this.updateStatus(STATE_INIT);
            // dataSource
            this.mMediaPlayer.setDataSource(dataSource.getUrl());
            this.mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.mMediaPlayer.prepareAsync();
            // 发送数据源事件
            Bundle bundle = BundlePool.obtain();
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_DATA_SOURCE_SET, bundle);
        } catch (Exception e) {
            this.mTargetState = IPlayer.STATE_ERROR;
            this.updateStatus(IPlayer.STATE_ERROR);
            this.sendErrorEvent(OnErrorListener.ERROR_EVENT_DATA_SOURCE, e.getMessage());
        }
    }

    /**
     * 设置渲染器
     *
     * @param surface surface
     */
    @Override
    public void setSurface(Surface surface) {

    }

    /**
     * 设置渲染器
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
    public View getRenderer() {
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
    public void setLooping(boolean looping) {
        this.mMediaPlayer.setLooping(looping);
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
        if (getState() == IPlayer.STATE_PREPARED) {
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
        if (getState() == IPlayer.STATE_PREPARED) {
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
        if (state == IPlayer.STATE_PREPARED
                || state == IPlayer.STATE_START
                || state == IPlayer.STATE_PAUSE
                || state == IPlayer.STATE_COMPLETE) {
            // 计算百分比
            float percent = ((float) msc) / getDuration();
            // 计算当前位置
            int currentPosition = (int) (getDuration() * percent);
            try {
                this.mMediaPlayer.seekTo(currentPosition);
            } catch (IllegalStateException e) {
                this.updateStatus(IPlayer.STATE_ERROR);
                this.sendErrorEvent(OnErrorListener.ERROR_EVENT_SEEK, null);
            }
            // seekTo
            Bundle bundle = BundlePool.obtain();
            bundle.putLong(EventKey.LONG_DATA, msc);
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SEEK_TO, bundle);
        }
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        int state = getState();
        if (state == IPlayer.STATE_PREPARED
                || state == IPlayer.STATE_PAUSE
                || state == IPlayer.STATE_COMPLETE
                || state == IPlayer.STATE_STOP) {
            try {
                this.mMediaPlayer.start();
                // 开始播放
                this.updateStatus(IPlayer.STATE_START);
                this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_START, null);
            } catch (Exception e) {
                this.mTargetState = IPlayer.STATE_ERROR;
                this.updateStatus(IPlayer.STATE_ERROR);
                this.sendErrorEvent(OnErrorListener.ERROR_EVENT_START, e.getMessage());
            }
        }
        this.mTargetState = IPlayer.STATE_START;
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
        if (state != IPlayer.STATE_DESTROY
                && state != IPlayer.STATE_ERROR
                && state != IPlayer.STATE_IDLE
                && state != IPlayer.STATE_INIT
                && state != IPlayer.STATE_PAUSE
                && state != IPlayer.STATE_STOP) {
            try {
                this.mMediaPlayer.pause();
                this.updateStatus(IPlayer.STATE_PAUSE);
                this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PAUSE, null);
            } catch (IllegalStateException e) {
                this.mTargetState = IPlayer.STATE_ERROR;
                this.updateStatus(IPlayer.STATE_ERROR);
                this.sendErrorEvent(OnErrorListener.ERROR_EVENT_PAUSE, e.getMessage());
            }
        }
        this.mTargetState = IPlayer.STATE_PAUSE;
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        if (getState() == IPlayer.STATE_PAUSE) {
            try {
                this.mMediaPlayer.start();
                this.updateStatus(IPlayer.STATE_START);
                this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_RESUME, null);
            } catch (IllegalStateException e) {
                this.mTargetState = IPlayer.STATE_ERROR;
                this.updateStatus(IPlayer.STATE_ERROR);
                this.sendErrorEvent(OnErrorListener.ERROR_EVENT_RESUME, e.getMessage());
            }
        }
        this.mTargetState = IPlayer.STATE_START;
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
            try {
                this.mMediaPlayer.stop();
                this.updateStatus(IPlayer.STATE_STOP);
                this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_STOP, null);
            } catch (IllegalStateException e) {
                this.mTargetState = IPlayer.STATE_ERROR;
                this.updateStatus(IPlayer.STATE_ERROR);
                this.sendErrorEvent(OnErrorListener.ERROR_EVENT_STOP, e.getMessage());
            }
        }
        this.mTargetState = IPlayer.STATE_STOP;
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        this.mMediaPlayer.reset();
        this.mTargetState = IPlayer.STATE_IDLE;
        this.updateStatus(IPlayer.STATE_IDLE);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_RESET, null);
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
        this.updateStatus(IPlayer.STATE_DESTROY);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_DESTROY, null);
    }

    /**
     * 播放器 准备完成 事件
     */
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            updateStatus(STATE_PREPARED);
            sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PREPARED, null);

            KLog.d("Prepared: " + mTargetState);
            // 准备完成 判断当前状态类型
            if (mTargetState == STATE_START) {
                start();
            } else if (mTargetState == STATE_PAUSE) {
                pause();
            } else if (mTargetState == STATE_STOP
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
                    sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    KLog.d("MEDIA_INFO_BUFFERING_START:");
                    sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_START, null);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    KLog.d("MEDIA_INFO_BUFFERING_END:");
                    sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_END, null);
                    break;
                case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                    KLog.d("MEDIA_INFO_BAD_INTERLEAVING:");
                    sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_BAD_INTERLEAVING, null);
                    break;
                case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    KLog.d("MEDIA_INFO_NOT_SEEKABLE:");
                    sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_NOT_SEEK_ABLE, null);
                    break;
                case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                    KLog.d("MEDIA_INFO_METADATA_UPDATE:");
                    sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_METADATA_UPDATE, null);
                    break;
                case MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                    KLog.d("MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                    sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_UNSUPPORTED_SUBTITLE, null);
                    break;
                case MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                    KLog.d("MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                    sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SUBTITLE_TIMED_OUT, null);
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
            sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SEEK_COMPLETE, null);
        }
    };
    /**
     * 播放器 结束 事件
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mTargetState = IPlayer.STATE_COMPLETE;
            updateStatus(IPlayer.STATE_COMPLETE);
            sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE, null);
        }
    };

    /**
     * 播放器 Error 事件
     */
    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            KLog.d("Error: " + what + "," + extra);

            int eventCode = OnErrorListener.ERROR_EVENT_COMMON;

            switch (what) {
                case MediaPlayer.MEDIA_ERROR_IO:
                    eventCode = OnErrorListener.ERROR_EVENT_IO;
                    break;
                case MediaPlayer.MEDIA_ERROR_MALFORMED:
                    eventCode = OnErrorListener.ERROR_EVENT_MALFORMED;
                    break;
                case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                    eventCode = OnErrorListener.ERROR_EVENT_TIMED_OUT;
                    break;
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    eventCode = OnErrorListener.ERROR_EVENT_UNKNOWN;
                    break;
                case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                    eventCode = OnErrorListener.ERROR_EVENT_UNSUPPORTED;
                    break;
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    eventCode = OnErrorListener.ERROR_EVENT_SERVER_DIED;
                    break;
                case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                    eventCode = OnErrorListener.ERROR_EVENT_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK;
                    break;
            }

            mTargetState = IPlayer.STATE_ERROR;
            updateStatus(IPlayer.STATE_ERROR);
            sendErrorEvent(eventCode, null);
            return true;
        }
    };
}

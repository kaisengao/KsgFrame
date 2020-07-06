package com.kasiengao.ksgframe.java.player.player;

import android.content.Context;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.kasiengao.base.util.KLog;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.IKsgPlayer;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @ClassName: KsgMediaPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/21 16:19
 * @Description: IjkMediaPlayer 播放器
 */
public class KsgIjkPlayer extends BaseInternalPlayer {

    private long mStartSeekPos;

    private int mVideoWidth, mVideoHeight;

    private IjkMediaPlayer mMediaPlayer;

    public KsgIjkPlayer(Context context) {
        super(context);
        // 初始化通知
        this.updateStatus(IKsgPlayer.STATE_INITIALIZED);
    }

    static {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    /**
     * 初始化 播放器
     */
    @Override
    public void initPlayer() {
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        // ijkPlayer支持硬解码和软解码。 0表示使用av解码器，1表示使用媒体解码器。
        // 软解码时不会旋转视频角度这时需要你通过onInfo的what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED去获取角度，自己旋转画面。
        // 或者开启硬解硬解码，不过硬解码容易造成黑屏无声（硬件兼容问题），下面是设置硬解码相关的代码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-hevc", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
        // 某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容，
        // 视频压缩过于厉害，seek只支持关键帧，出现这个情况就是原始的视频文件中i 帧比较少
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        // 为嵌入式系统打开声音库
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
        // 跳帧处理,放CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1L);
        // 0为一进入就播放,1为进入时不播放
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        // 超时
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 10000000);
        // 播放重连次数
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 3);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        // 环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);

        this.mMediaPlayer = ijkMediaPlayer;
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
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            this.updateStatus(STATE_INITIALIZED);
            // dataSource
            this.mMediaPlayer.setDataSource(dataSource.getUrl());
            // 发送数据源事件
            Bundle bundle = BundlePool.obtain();
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
            this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET, bundle);
        } catch (Exception e) {
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
        if (this.mMediaPlayer == null) {
            this.initPlayer();
        }
        this.mMediaPlayer.setSurface(surface);
    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     *
     * @param holder surfaceHolder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        this.mMediaPlayer.setDisplay(holder);
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
        this.mMediaPlayer.setSpeed(speed);
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        return this.mMediaPlayer.getSpeed(0);
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        return this.mMediaPlayer.getTcpSpeed();
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
        return this.mMediaPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     *
     * @return 总时长
     */
    @Override
    public long getDuration() {
        return this.mMediaPlayer.getDuration();
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
     * 准备开始播放（异步）
     */
    @Override
    public void prepareAsync() {
        // 播放器准备
        try {
            this.mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            this.updateStatus(IKsgPlayer.STATE_ERROR);
            this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_PREPARE_ASYNC, e.getMessage());
        }
        // 播放准备
        this.updateStatus(IKsgPlayer.STATE_PREPARED);
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
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_START, e.getMessage());
            }
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
                this.mMediaPlayer.pause();
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
                this.mMediaPlayer.start();
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
        int state = getState();
        if (state == IKsgPlayer.STATE_PREPARED
                || state == IKsgPlayer.STATE_STARTED
                || state == IKsgPlayer.STATE_PAUSED
                || state == IKsgPlayer.STATE_PLAYBACK_COMPLETE) {
            try {
                this.mMediaPlayer.stop();
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
        this.mMediaPlayer.reset();
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
    private IMediaPlayer.OnPreparedListener mPreparedListener = iMediaPlayer -> {

        this.mVideoWidth = iMediaPlayer.getVideoWidth();
        this.mVideoHeight = iMediaPlayer.getVideoHeight();

        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_ARG1, mVideoWidth);
        bundle.putInt(EventKey.INT_ARG2, mVideoHeight);

        // 译码器准备完成
        this.updateStatus(STATE_PREPARED);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED, bundle);

        long seekToPosition = this.mStartSeekPos;
        if (seekToPosition != 0) {
            this.mMediaPlayer.seekTo(seekToPosition);
            this.mStartSeekPos = 0;
        }
    };

    /**
     * 播放器 信息与警告 事件
     */
    private IMediaPlayer.OnInfoListener mInfoListener = (iMediaPlayer, what, extra) -> {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                KLog.d("MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                KLog.d("MEDIA_INFO_VIDEO_RENDERING_START");
                this.mStartSeekPos = 0;
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                KLog.d("MEDIA_INFO_BUFFERING_START:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START, null);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                KLog.d("MEDIA_INFO_BUFFERING_END:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END, null);
                break;
            case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                //not support
                break;
            case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                KLog.d("MEDIA_INFO_BAD_INTERLEAVING:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BAD_INTERLEAVING, null);
                break;
            case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                KLog.d("MEDIA_INFO_NOT_SEEKABLE:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_NOT_SEEK_ABLE, null);
                break;
            case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                KLog.d("MEDIA_INFO_METADATA_UPDATE:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_METADATA_UPDATE, null);
                break;
            case IMediaPlayer.MEDIA_INFO_TIMED_TEXT_ERROR:
                KLog.d("MEDIA_INFO_TIMED_TEXT_ERROR:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_TIMED_TEXT_ERROR, null);
                break;
            case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                KLog.d("MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_UNSUPPORTED_SUBTITLE, null);
                break;
            case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                KLog.d("MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SUBTITLE_TIMED_OUT, null);
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                KLog.d("MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + extra);
                Bundle bundle = BundlePool.obtain();
                bundle.putInt(EventKey.INT_DATA, extra);
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED, bundle);
                break;
            case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                KLog.d("MEDIA_INFO_AUDIO_RENDERING_START:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_RENDER_START, null);
                break;
            case IMediaPlayer.MEDIA_INFO_AUDIO_DECODED_START:
                KLog.d("MEDIA_INFO_AUDIO_DECODED_START:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_DECODER_START, null);
                break;
            case IMediaPlayer.MEDIA_INFO_AUDIO_SEEK_RENDERING_START:
                KLog.d("MEDIA_INFO_AUDIO_SEEK_RENDERING_START:");
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_SEEK_RENDERING_START, null);
                break;
        }
        return true;
    };

    /**
     * 播放器 缓冲进度 事件
     */
    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = (iMediaPlayer, percent) -> {
        // 缓冲进度 (不知道为啥他的进度只显示99)
        this.setBufferPercentage(percent >= 99 ? 100 : percent);
    };

    /**
     * 播放器 Seek结束 事件
     */
    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = mp -> {
        // SeekComplete
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE, null);
    };

    /**
     * 播放器 视频大小改变 事件
     */
    private IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = (iMediaPlayer, width, height, sarNum, sarDen) -> {
        mVideoWidth = iMediaPlayer.getVideoWidth();
        mVideoHeight = iMediaPlayer.getVideoHeight();
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_ARG1, mVideoWidth);
        bundle.putInt(EventKey.INT_ARG2, mVideoHeight);
        bundle.putInt(EventKey.INT_ARG3, sarNum);
        bundle.putInt(EventKey.INT_ARG4, sarDen);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE, bundle);
    };

    /**
     * 播放器 结束 事件
     */
    private IMediaPlayer.OnCompletionListener mCompletionListener = iMediaPlayer -> {
        this.updateStatus(IKsgPlayer.STATE_PLAYBACK_COMPLETE);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE, null);
    };

    /**
     * 播放器 Error 事件
     */
    private IMediaPlayer.OnErrorListener mErrorListener = (iMediaPlayer, framework_err, impl_err) -> {
        this.updateStatus(IKsgPlayer.STATE_ERROR);
        this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, null);
        return true;
    };

}

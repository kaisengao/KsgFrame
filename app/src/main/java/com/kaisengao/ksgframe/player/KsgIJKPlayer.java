package com.kaisengao.ksgframe.player;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorListener;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.BasePlayer;
import com.ksg.ksgplayer.player.IPlayer;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @ClassName: KsgIJKPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/21 16:19
 * @Description: IjkMediaPlayer Bilibili播放器
 */
public class KsgIJKPlayer extends BasePlayer {

    private long mStartPos = -1;

    private DataSource mDataSource;

    private IjkMediaPlayer mMediaPlayer;

    public KsgIJKPlayer(Context context) {
        super(context);
        // 初始化通知
        this.updateState(IPlayer.STATE_IDLE);
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
        this.mDataSource = dataSource;
        try {
            if (this.mMediaPlayer == null) {
                this.initPlayer();
            } else {
                this.stop();
                this.reset();
                this.release();
            }
            // 播放准备
            this.updateState(IPlayer.STATE_INIT);
            // 事件监听
            this.mMediaPlayer.setOnPreparedListener(mPreparedListener);
            this.mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            this.mMediaPlayer.setOnCompletionListener(mCompletionListener);
            this.mMediaPlayer.setOnErrorListener(mErrorListener);
            this.mMediaPlayer.setOnInfoListener(mInfoListener);
            this.mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            this.mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            // DataSource
            this.mMediaPlayer.setDataSource(dataSource.getUrl());
            this.mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            this.mMediaPlayer.setScreenOnWhilePlaying(true);
            this.mMediaPlayer.prepareAsync();
            // 发送数据源事件
            Bundle bundle = BundlePool.obtain();
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_DATA_SOURCE_SET, bundle);
        } catch (Exception e) {
            this.updateState(IPlayer.STATE_ERROR);
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
        this.mMediaPlayer.setSurface(surface);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SURFACE_UPDATE, null);
    }

    /**
     * 设置渲染器
     *
     * @param holder surfaceHolder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        this.mMediaPlayer.setDisplay(holder);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SURFACE_HOLDER_UPDATE, null);
    }

    /**
     * 渲染器 改变事件
     */
    @Override
    public void onSurfaceChanged() {

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
        // 计算百分比
        float percent = ((float) msc) / getDuration();
        // 计算当前位置
        int currentPosition = (int) (getDuration() * percent);
        // Seek
        this.mMediaPlayer.seekTo(currentPosition);
        Bundle bundle = BundlePool.obtain();
        bundle.putLong(EventKey.LONG_DATA, msc);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SEEK_TO, bundle);
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        this.mMediaPlayer.start();
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
            this.mMediaPlayer.pause();
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
            this.mMediaPlayer.start();
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
            this.mMediaPlayer.stop();
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
        this.release();
        this.mMediaPlayer.reset();
        this.updateState(IPlayer.STATE_IDLE);
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
        this.release();
        this.mMediaPlayer.release();
        this.updateState(IPlayer.STATE_DESTROY);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_DESTROY, null);
    }

    /**
     * 事件 准备完成
     */
    private final IMediaPlayer.OnPreparedListener mPreparedListener = iMediaPlayer -> {
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_ARG1, iMediaPlayer.getVideoWidth());
        bundle.putInt(EventKey.INT_ARG2, iMediaPlayer.getVideoHeight());
        this.updateState(IPlayer.STATE_PREPARED);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PREPARED, bundle);
        if (mStartPos > 0 && mMediaPlayer.getDuration() > 0) {
            this.seekTo(mStartPos);
            this.mStartPos = -1;
        }
        if (iMediaPlayer.isPlaying()) {
            this.updateState(STATE_START);
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_START, null);
        }
    };

    /**
     * 事件 信息与警告
     */
    private final IMediaPlayer.OnInfoListener mInfoListener = (iMediaPlayer, what, extra) -> {
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_START, null);
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_END, null);
                break;
            case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                //not support
                break;
            case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_BAD_INTERLEAVING, null);
                break;
            case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_NOT_SEEK_ABLE, null);
                break;
            case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_METADATA_UPDATE, null);
                break;
            case IMediaPlayer.MEDIA_INFO_TIMED_TEXT_ERROR:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_TIMED_TEXT_ERROR, null);
                break;
            case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_UNSUPPORTED_SUBTITLE, null);
                break;
            case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SUBTITLE_TIMED_OUT, null);
                break;
            case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                Bundle bundle = BundlePool.obtain();
                bundle.putInt(EventKey.INT_DATA, extra);
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED, bundle);
                break;
            case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_AUDIO_RENDER_START, null);
                break;
            case IMediaPlayer.MEDIA_INFO_AUDIO_DECODED_START:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_AUDIO_DECODER_START, null);
                break;
            case IMediaPlayer.MEDIA_INFO_AUDIO_SEEK_RENDERING_START:
                sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_AUDIO_SEEK_RENDERING_START, null);
                break;
        }
        return true;
    };

    /**
     * 事件 缓冲进度
     */
    private final IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = (iMediaPlayer, percent) -> {
        // 缓冲进度 (不知道为啥他的进度只显示99)
        this.setBufferPercentage(percent > 95 ? 100 : percent);
    };

    /**
     * 事件 Seek结束
     */
    private final IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = mp -> {
        // SeekComplete
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SEEK_COMPLETE, null);
    };

    /**
     * 事件 视频大小改变
     */
    private final IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = (iMediaPlayer, width, height, sarNum, sarDen) -> {
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_ARG1, iMediaPlayer.getVideoWidth());
        bundle.putInt(EventKey.INT_ARG2, iMediaPlayer.getVideoHeight());
        bundle.putInt(EventKey.INT_ARG3, sarNum);
        bundle.putInt(EventKey.INT_ARG4, sarDen);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE, bundle);
    };

    /**
     * 事件 播放结束
     */
    private final IMediaPlayer.OnCompletionListener mCompletionListener = iMediaPlayer -> {
        this.updateState(IPlayer.STATE_COMPLETE);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE, null);
    };

    /**
     * 事件 Error
     */
    private final IMediaPlayer.OnErrorListener mErrorListener = (iMediaPlayer, framework_err, impl_err) -> {
        this.updateState(IPlayer.STATE_ERROR);
        this.sendErrorEvent(OnErrorListener.ERROR_EVENT_UNKNOWN, null);
        return true;
    };
}

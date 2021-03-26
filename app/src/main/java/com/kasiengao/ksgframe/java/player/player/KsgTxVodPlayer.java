package com.kasiengao.ksgframe.java.player.player;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import com.kasiengao.ksgframe.R;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * @ClassName: KsgTxVodPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/7/6 13:08
 * @Description: 腾讯云点播 播放器
 */
public class KsgTxVodPlayer extends BaseInternalPlayer {

    private long mStartSeekPos;

    private long mCurrentPosition;

    private long mDuration;

    private DataSource mDataSource;

    private TXVodPlayer mVodPlayer;

    private TXCloudVideoView mVideoView;

    public KsgTxVodPlayer(Context context) {
        super(context);
        // 初始化通知
        this.updateStatus(IKsgPlayer.STATE_INITIALIZED);
    }

    /**
     * 初始化 播放器
     */
    @Override
    public void initPlayer() {

        TXVodPlayer vodPlayer = new TXVodPlayer(mContext);

        TXVodPlayConfig mPlayConfig = new TXVodPlayConfig();
        mPlayConfig.setCacheFolderPath(getInnerSDCardPath() + "/liveCache");
        mPlayConfig.setMaxCacheItems(5);

        vodPlayer.setConfig(mPlayConfig);

        vodPlayer.setPlayerView(getVideoView());

        vodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);

        vodPlayer.enableHardwareDecode(true);

        this.mVodPlayer = vodPlayer;
    }

    /**
     * 创建 TXCloudVideoView
     *
     * @return TXCloudVideoView
     */
    private TXCloudVideoView getVideoView() {
        if (mVideoView == null) {
            View inflate = View.inflate(mContext, R.layout.layout_cloud_video_view, null);
            this.mVideoView = inflate.findViewById(R.id.tx_video_view);
        }
        return this.mVideoView;
    }

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        // 验证初始化
        if (mVodPlayer == null) {
            this.initPlayer();
        } else {
            this.stop();
            this.reset();
            this.release();
        }
        // dataSource
        this.mDataSource = dataSource;
        // 事件监听
        this.mVodPlayer.setVodListener(mITXVodPlayListener);
        // 发送数据源
        Bundle bundle = BundlePool.obtain();
        bundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET, bundle);
        // 播放准备
        this.updateStatus(IKsgPlayer.STATE_PREPARED);
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
        return this.getVideoView();
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float left, float right) {

    }

    /**
     * 设置是否循环播放
     */
    @Override
    public void setLooping(boolean isLooping) {
        this.mVodPlayer.setLoop(isLooping);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        this.mVodPlayer.setRate(speed);
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
        return this.mCurrentPosition;
    }

    /**
     * 获取视频总时长
     *
     * @return 总时长
     */
    @Override
    public long getDuration() {
        return this.mDuration;
    }

    /**
     * 获取播放状态
     *
     * @return 播放状态 true 播放 反之
     */
    @Override
    public boolean isPlaying() {
        return this.mVodPlayer.isPlaying();
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
            // 指定的位置
            this.mCurrentPosition = msc;
            // 计算百分比
            float percent = ((float) (msc / 1000)) / getDuration();
            // 计算要跳转的位置
            int seek = (int) (getDuration() * percent);
            try {
                this.mVodPlayer.seek(seek);
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
                this.mVodPlayer.setAutoPlay(true);
                int result = mVodPlayer.startPlay(mDataSource.getUrl());
                if (result == 0) {
                    // 开始播放通知
                    this.updateStatus(IKsgPlayer.STATE_STARTED);
                    this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_START, null);
                } else {
                    this.updateStatus(IKsgPlayer.STATE_ERROR);
                    this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_START, "播放地址异常!");
                    Toast.makeText(mContext, "播放地址异常!", Toast.LENGTH_SHORT).show();
                }
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
                this.mVodPlayer.pause();
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
                this.mVodPlayer.resume();
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
                this.mVodPlayer.stopPlay(true);
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
        this.updateStatus(IKsgPlayer.STATE_IDLE);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESET, null);
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        this.mVodPlayer.setVodListener(null);
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.stop();
        this.release();
        this.mVodPlayer = null;
        if (mVideoView != null) {
            this.mVideoView.onDestroy();
            this.mVideoView = null;
        }
        // 销毁
        this.updateStatus(IKsgPlayer.STATE_END);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY, null);
    }

    /**
     * 获取手机缓存路径
     */
    private String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 设置播放器的回调
     */
    private ITXVodPlayListener mITXVodPlayListener = new ITXVodPlayListener() {
        /**`
         * 点播播放器回调
         *
         * @param txVodPlayer TXVodPlayer
         * @param event  事件id.id类型请参考 {@linkplain TXLiveConstants#PLAY_EVT_CONNECT_SUCC 播放事件列表}.
         * @param param Bundle
         */
        @Override
        public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle param) {
            switch (event) {
                // 2013 点播准备完成
                case TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED:
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED, null);
                    break;
                // 2007 缓冲开始
                case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START, null);
                    break;
                // 2014 缓冲结束
                case TXLiveConstants.PLAY_EVT_VOD_LOADING_END:
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END, null);
                    break;
                // 2004 视频播放开始，如果有转菊花什么的这个时候该停了
                case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null);
                    break;
                // 2005 视频播放进度，会通知当前播放进度、加载进度 和总体时长
                case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                    // 播放进度
                    mCurrentPosition = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS);
                    // 视频总长
                    mDuration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION_MS);
                    // 加载进度
                    setBufferPercentage(param.getInt(TXLiveConstants.EVT_PLAYABLE_DURATION_MS));
                    // 在指定的位置播放
                    if (mStartSeekPos != 0) {
                        seekTo(mStartSeekPos);
                        mStartSeekPos = 0;
                    }
                    break;
                // 2006 视频播放结束
                case TXLiveConstants.PLAY_EVT_PLAY_END:
                    stop();
                    updateStatus(IKsgPlayer.STATE_PLAYBACK_COMPLETE);
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE, null);
                    break;
                default:
                    if (event < 0) {
                        updateStatus(IKsgPlayer.STATE_ERROR);
                        submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, param.getString(TXLiveConstants.EVT_DESCRIPTION));
                    }
                    break;
            }
        }

        @Override
        public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

        }
    };
}

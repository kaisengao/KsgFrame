package com.kasiengao.ksgframe.java.player.player;

import android.content.Context;
import android.os.Bundle;
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
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * @ClassName: KsgTxLivePlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/7/6 15:53
 * @Description: 腾讯云直播 播放器
 */
public class KsgTxLivePlayer extends BaseInternalPlayer {

    private DataSource mDataSource;

    private TXLivePlayer mLivePlayer;

    private TXCloudVideoView mVideoView;

    public KsgTxLivePlayer(Context context) {
        super(context);
        // 初始化通知
        this.updateStatus(IKsgPlayer.STATE_INITIALIZED);
    }

    /**
     * 初始化 播放器
     */
    @Override
    public void initPlayer() {

        TXLivePlayer livePlayer = new TXLivePlayer(mContext);

        TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();
        // 自动模式
        mPlayConfig.setAutoAdjustCacheTime(true);
        mPlayConfig.setMinAutoAdjustCacheTime(1);
        mPlayConfig.setMaxAutoAdjustCacheTime(5);

        livePlayer.setConfig(mPlayConfig);

        livePlayer.setPlayerView(getVideoView());

        livePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);

        this.mLivePlayer = livePlayer;
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
        if (mLivePlayer == null) {
            this.initPlayer();
        } else {
            this.stop();
            this.reset();
            this.release();
        }
        // dataSource
        this.mDataSource = dataSource;
        // 事件监听
        this.mLivePlayer.setPlayListener(mITXLivePlayListener);
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
        return 0;
    }

    /**
     * 获取视频总时长
     *
     * @return 总时长
     */
    @Override
    public long getDuration() {
        return 0;
    }

    /**
     * 获取播放状态
     *
     * @return 播放状态 true 播放 反之
     */
    @Override
    public boolean isPlaying() {
        return this.mLivePlayer.isPlaying();
    }

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    @Override
    public void seekTo(long msc) {

    }

    /**
     * 准备开始播放（异步）
     */
    @Override
    public void prepareAsync() {
        // 播放器准备
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
                int result = mLivePlayer.startPlay(mDataSource.getUrl(), getPlayType(mDataSource.getUrl()));
                if (result == 0) {
                    // 开始播放通知
                    this.updateStatus(IKsgPlayer.STATE_STARTED);
                    this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_START, null);
                } else {
                    this.updateStatus(IKsgPlayer.STATE_ERROR);
                    this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, "播放地址异常!");
                    Toast.makeText(mContext, "播放地址异常!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_START, e.getMessage());
            }
        }
    }

    private int getPlayType(String playUrl) {

        int playType;

        String http = "http://", https = "https://", flv = ".flv", m3u8 = ".m3u8";

        boolean isHttp = playUrl.startsWith(http) || playUrl.startsWith(https);

        boolean isFlv = playUrl.contains(flv);

        boolean isM3u8 = playUrl.contains(m3u8);

        if (mDataSource.getLiveType() != -1) {
            playType = mDataSource.getLiveType();
        } else if (isHttp && isFlv) {
            playType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
        } else if (isHttp && isM3u8) {
            playType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
        } else {
            playType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
        }

        return playType;
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
                this.mLivePlayer.pause();
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
                this.mLivePlayer.resume();
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
                this.mLivePlayer.stopPlay(true);
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
        this.mLivePlayer.setPlayListener(null);
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.stop();
        this.release();
        this.mLivePlayer = null;
        if (mVideoView != null) {
            this.mVideoView.onDestroy();
            this.mVideoView = null;
        }
        // 销毁
        this.updateStatus(IKsgPlayer.STATE_END);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY, null);
    }

    /**
     * -----直播状态-----
     * 不要在收到 PLAY_LOADING 后隐藏播放画面因为 PLAY_LOADING -> PLAY_BEGIN 的时间长短是不确定的，
     * 可能是 5s 也可能是 5ms，有些客户考虑在 LOADING 时隐藏画面，
     * BEGIN 时显示画面，会造成严重的画面闪烁（尤其是直播场景下）。
     * 推荐的做法是在视频播放画面上叠加一个半透明的 loading 动画。
     * -----判断直播已结束-----
     * 基于各种标准的实现原理不同，很多直播流通常没有结束事件（2006）抛出，
     * 此时可预期的表现是：主播结束推流后，SDK 会很快发现数据流拉取失败（WARNING_RECONNECT），
     * 然后开始重试，直至三次重试失败后抛出 PLAY_ERR_NET_DISCONNECT 事件。
     * 所以 2006 和 -2301 都要监听，用来作为直播结束的判定事件。
     */
    private ITXLivePlayListener mITXLivePlayListener = new ITXLivePlayListener() {
        /**
         * 直播播放器回调
         *
         * @param event 事件id.id类型请参考 {@linkplain TXLiveConstants#PUSH_EVT_CONNECT_SUCC 播放事件列表}.
         * @param param bundle
         */
        @Override
        public void onPlayEvent(int event, Bundle param) {
            switch (event) {
                // 2003  渲染首个视频数据包(IDR)
                case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED, null);
                    break;
                // 2004  PLAY_EVT_PLAY_BEGIN 视频播放开始，如果有转菊花什么的这个时候该停了
                case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null);
                    break;
                // 2007  视频播放 loading，如果能够恢复，之后会有 BEGIN 事件
                case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START, null);
                    break;
                // -2301 网络断连,且经多次重连亦不能恢复,更多重试请自行重启播放
                case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                    stop();
                    updateStatus(IKsgPlayer.STATE_ERROR);
                    submitErrorEvent(OnErrorEventListener.ERROR_PLAY_ERR_NET_DISCONNECT, "网络断连，且经多次重连亦不能恢复，更多重试请自行重启播放");
                    break;
                // 2006 视频播放结束
                case TXLiveConstants.PLAY_EVT_PLAY_END:
                    stop();
                    updateStatus(IKsgPlayer.STATE_PLAYBACK_COMPLETE);
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE, null);
                    break;

                case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION:
                    int width = param.getInt(TXLiveConstants.EVT_PARAM1, 0);
                    int height = param.getInt(TXLiveConstants.EVT_PARAM2, 0);
                    if (width > 0 && height > 0) {
                        float ratioInt = 1.3f;

                        float ratio = (float) height / width;
                        // PC上混流后的宽高比为4:5，这种情况下填充模式会把左右的小主播窗口截掉一部分，用适应模式比较合适
                        if (ratio > ratioInt) {
                            mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                        } else {
                            mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
                        }
                    }
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
        public void onNetStatus(Bundle bundle) {

        }
    };
}


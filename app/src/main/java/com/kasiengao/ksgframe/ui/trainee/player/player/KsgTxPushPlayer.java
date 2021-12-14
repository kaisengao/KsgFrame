package com.kasiengao.ksgframe.ui.trainee.player.player;

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
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * @ClassName: KsgTxPushPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2019-07-29 14:40
 * @Description: 腾讯云直播(推流)
 */
public final class KsgTxPushPlayer extends BaseInternalPlayer {

    private DataSource mDataSource;

    private TXLivePusher mLivePusher;

    private TXCloudVideoView mVideoView;

    public KsgTxPushPlayer(Context context) {
        super(context);
        // 初始化通知
        this.updateStatus(IKsgPlayer.STATE_INITIALIZED);
    }

    /**
     * 初始化 播放器
     */
    @Override
    public void initPlayer() {
        TXLivePusher livePusher = new TXLivePusher(mContext);

        TXLivePushConfig pushConfig = new TXLivePushConfig();
        // 设置是否允许双指手势放大预览画面
        pushConfig.setEnableZoom(true);
        // 设置视频编码 GOP
        pushConfig.setVideoEncodeGop(2);

        pushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);

        livePusher.setConfig(pushConfig);

        livePusher.startCameraPreview(getVideoView());

        // 中档 高清画面质量
        livePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, false, false);
        // 中档 美颜
        livePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_NATURE, 5, 5, 5);

        this.mLivePusher = livePusher;
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
        if (mLivePusher == null) {
            this.initPlayer();
        } else {
            this.stop();
            this.reset();
            this.release();
        }
        // 直播类型
        dataSource.setLive(true);
        // dataSource
        this.mDataSource = dataSource;
        // 事件监听
        this.mLivePusher.setPushListener(mLivePushListener);
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
        return this.mLivePusher.isPushing();
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
     * 播放
     */
    @Override
    public void start() {
        if (getState() == IKsgPlayer.STATE_PREPARED
                || getState() == IKsgPlayer.STATE_PAUSED
                || getState() == IKsgPlayer.STATE_PLAYBACK_COMPLETE) {
            int result = mLivePusher.startPusher(mDataSource.getUrl().trim());
            if (result == 0) {
                // 开始播放通知
                this.updateStatus(IKsgPlayer.STATE_STARTED);
                this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_START, null);
            } else if (result == -5) {
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, "license 校验失败");
                Toast.makeText(mContext, "license 校验失败", Toast.LENGTH_SHORT).show();
            } else {
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, "推流地址异常!");
                Toast.makeText(mContext, "推流地址异常!", Toast.LENGTH_SHORT).show();
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
                this.mLivePusher.pausePusher();
                this.updateStatus(IKsgPlayer.STATE_PAUSED);
                this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE, null);
            } catch (IllegalStateException e) {
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_PAUSE, e.getMessage());
            }
        }
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        if (getState() == IKsgPlayer.STATE_PAUSED) {
            try {
                this.mLivePusher.resumePusher();
                this.updateStatus(IKsgPlayer.STATE_STARTED);
                this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESUME, null);
            } catch (IllegalStateException e) {
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_RESUME, e.getMessage());
            }
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        if (getState() == IKsgPlayer.STATE_PREPARED
                || getState() == IKsgPlayer.STATE_STARTED
                || getState() == IKsgPlayer.STATE_PAUSED
                || getState() == IKsgPlayer.STATE_PLAYBACK_COMPLETE) {
            try {
                this.mLivePusher.stopPusher();
                this.mLivePusher.stopCameraPreview(true);
                this.updateStatus(IKsgPlayer.STATE_STOPPED);
                this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_STOP, null);
            } catch (IllegalStateException e) {
                this.updateStatus(IKsgPlayer.STATE_ERROR);
                this.submitErrorEvent(OnErrorEventListener.ERROR_EVENT_STOP, e.getMessage());
            }
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
        if (mLivePusher != null) {
            this.mLivePusher.setPushListener(null);
        }
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.stop();
        this.release();
        this.mLivePusher = null;
        if (mVideoView != null) {
            this.mVideoView.onDestroy();
            this.mVideoView = null;
        }
        // 销毁
        this.updateStatus(IKsgPlayer.STATE_END);
        this.submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY, null);
    }

    /**
     * 自定义事件
     *
     * @param code   code
     * @param bundle bundle
     */
    @Override
    public void option(int code, Bundle bundle) {
        super.option(code, bundle);

        switch (code) {
            case PlayerEvent.EVENT_CODE_REQUEST_SWITCH_CAMERA:
                // 前后置摄像头
                this.mLivePusher.switchCamera();
                break;
            case PlayerEvent.EVENT_CODE_REQUEST_BEAUTY_ON:
                // 中档 美颜
                this.mLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_NATURE, 5, 5, 5);
                break;
            case PlayerEvent.EVENT_CODE_REQUEST_BEAUTY_OFF:
                // 关闭 美颜
                this.mLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_NATURE, 0, 0, 0);
                break;
            case PlayerEvent.EVENT_CODE_REQUEST_START_LINK_MIC:
                // 连麦，设置推流Quality为VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER
                this.mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER, true, false);
                break;
            case PlayerEvent.EVENT_CODE_REQUEST_STOP_LINK_MIC:
                // 没有播放流了，切换推流回直播模式
                this.mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, false, false);

                TXLivePushConfig pushConfig = mLivePusher.getConfig();
                pushConfig.setVideoEncodeGop(2);
                this.mLivePusher.setConfig(pushConfig);
                break;
            case PlayerEvent.EVENT_CODE_REQUEST_ROTATION_FOR:
                // Activity 的旋转方向，配置推流器
                // 自动旋转打开，Activity随手机方向旋转之后，需要改变推流方向
                int mobileRotation = bundle.getInt(EventKey.INT_DATA, -1);
                this.setRotationForActivity(mobileRotation);
                break;
            default:
                break;
        }
    }

    /**
     * 根据当前 Activity 的旋转方向，配置推流器
     */
    private void setRotationForActivity(int mobileRotation) {
        // 自动旋转打开，Activity随手机方向旋转之后，需要改变推流方向
        switch (mobileRotation) {
            case Surface.ROTATION_0:
                this.mLivePusher.setRenderRotation(0);
                break;
            case Surface.ROTATION_180:
                this.mLivePusher.setRenderRotation(-180);
                break;
            case Surface.ROTATION_90:
                this.mLivePusher.setRenderRotation(-90);
                break;
            case Surface.ROTATION_270:
                this.mLivePusher.setRenderRotation(-270);
                break;
            default:
                break;
        }
    }

    /**
     * -----推流状态-----
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
    private ITXLivePushListener mLivePushListener = new ITXLivePushListener() {

        /**
         * 直播播放器回调
         *
         * @param event 事件id.id类型请参考 {@linkplain TXLiveConstants#PUSH_EVT_CONNECT_SUCC 播放事件列表}.
         * @param param bundle
         */

        @Override
        public void onPushEvent(int event, Bundle param) {
            switch (event) {
                // 渲染首个视频数据包(IDR)
                case TXLiveConstants.PUSH_EVT_CONNECT_SUCC:
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED, null);
                    break;
                // PLAY_EVT_PLAY_BEGIN 视频播放开始，如果有转菊花什么的这个时候该停了
                case TXLiveConstants.PUSH_EVT_PUSH_BEGIN:
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null);
                    break;
                // 网络断连,且经多次重连亦不能恢复,更多重试请自行重启播放
                case TXLiveConstants.PUSH_ERR_NET_DISCONNECT:
                case TXLiveConstants.PUSH_ERR_INVALID_ADDRESS:
                case TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL:
                case TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL:
                    stop();
                    updateStatus(IKsgPlayer.STATE_ERROR);
                    submitErrorEvent(OnErrorEventListener.ERROR_PLAY_ERR_NET_DISCONNECT, "出现错误，请重新开播！");
                    break;
                case TXLiveConstants.PUSH_WARNING_NET_BUSY:
                    // 您当前的网络环境不佳，请尽快更换网络保证正常直播
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_NET_BUSY, null);
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

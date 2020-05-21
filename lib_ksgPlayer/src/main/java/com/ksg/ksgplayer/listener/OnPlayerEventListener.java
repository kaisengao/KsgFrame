package com.ksg.ksgplayer.listener;

import android.os.Bundle;

import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.proxy.TimerCounterProxy;

/**
 * @author kaisengao
 * @create: 2019/1/7 14:25
 * @describe: 播放器的播放、准备事件
 */
public interface OnPlayerEventListener {

    /**
     * 直播
     */
    int PLAYER_EVENT_ON_LIVE = -99000;

    /**
     * 设置数据源
     */
    int PLAYER_EVENT_ON_DATA_SOURCE_SET = -99001;

    /**
     * start {@link IKsgPlayer#start()}
     */
    int PLAYER_EVENT_ON_START = -99002;

    /**
     * pause {@link IKsgPlayer#pause()}
     */
    int PLAYER_EVENT_ON_PAUSE = -99003;

    /**
     * resume {@link IKsgPlayer#resume()}
     */
    int PLAYER_EVENT_ON_RESUME = -99004;

    /**
     * stop {@link IKsgPlayer#stop()}
     */
    int PLAYER_EVENT_ON_STOP = -99005;

    /**
     * reset {@link IKsgPlayer#reset()}
     */
    int PLAYER_EVENT_ON_RESET = -99006;

    /**
     * destroy {@link IKsgPlayer#destroy()}
     */
    int PLAYER_EVENT_ON_DESTROY = -99007;

    /**
     * 开始缓冲流
     */
    int PLAYER_EVENT_ON_BUFFERING_START = -99008;

    /**
     * 缓冲流结束
     */
    int PLAYER_EVENT_ON_BUFFERING_END = -99009;

    /**
     * seekTo {@link IKsgPlayer#seekTo(int)}
     */
    int PLAYER_EVENT_ON_SEEK_TO = -99010;

    /**
     * 开始渲染视频
     */
    int PLAYER_EVENT_ON_VIDEO_RENDER_START = -99011;

    /**
     * 播放完成
     */
    int PLAYER_EVENT_ON_PLAY_COMPLETE = -99012;

    /**
     * 视频大小变化
     */
    int PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE = -99013;

    /**
     * 译码器准备
     */
    int PLAYER_EVENT_ON_PREPARED = -99014;

    /**
     * 定时器更新 {@link TimerCounterProxy}
     * 如果计时器停止,你不能接收这个事件代码.
     */
    int PLAYER_EVENT_ON_TIMER_UPDATE = -99015;

    /**
     * 播放进度更新 {@link OnPlaybackProgressListener}
     */
    int PLAYER_EVENT_ON_PLAYBACK_PROGRESS = -99016;

    /**
     * 播放状态更新
     */
    int PLAYER_EVENT_ON_STATUS_CHANGE = -99017;

    /**
     * 网络环境不佳警告
     */
    int PLAYER_EVENT_ON_NET_BUSY = -99018;

    /**
     * 发送事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onPlayerEvent(int eventCode, Bundle bundle);
}

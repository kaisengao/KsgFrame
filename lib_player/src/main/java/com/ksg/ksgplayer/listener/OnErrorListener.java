package com.ksg.ksgplayer.listener;

import android.os.Bundle;

/**
 * @ClassName: OnErrorEventListener
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:22
 * @Description: 错误事件
 */
public interface OnErrorListener {

    /**
     * 网络断连，且经多次重连亦不能恢复，更多重试请自行重启播放
     */
    int ERROR_PLAY_ERR_NET_DISCONNECT = -87999;

    /**
     * 未知的播放错误
     */
    int ERROR_EVENT_UNKNOWN = -88000;

    /**
     * 播放器准备出错
     */
    int ERROR_EVENT_PREPARE_ASYNC = 88001;

    /**
     * 播放器 Start出错
     */
    int ERROR_EVENT_START = 88002;

    /**
     * 播放器 Seek出错
     */
    int ERROR_EVENT_SEEK = 88003;

    /**
     * 播放器 Pause出错
     */
    int ERROR_EVENT_PAUSE = 88004;

    /**
     * 播放器 Resume出错
     */
    int ERROR_EVENT_RESUME = 88005;

    /**
     * 播放器 Stop出错
     */
    int ERROR_EVENT_STOP = 88006;

    /**
     * 播放器 DataSource出错
     */
    int ERROR_EVENT_DATA_SOURCE = 88007;

    //A error that causes a play to terminate
    int ERROR_EVENT_COMMON = -88011;

    int ERROR_EVENT_SERVER_DIED = -88013;

    int ERROR_EVENT_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = -88014;

    int ERROR_EVENT_IO = -88015;

    int ERROR_EVENT_MALFORMED = -88016;

    int ERROR_EVENT_UNSUPPORTED = -88017;

    int ERROR_EVENT_TIMED_OUT = -88018;

    /**
     * 发送错误信息
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onErrorEvent(int eventCode, Bundle bundle);
}

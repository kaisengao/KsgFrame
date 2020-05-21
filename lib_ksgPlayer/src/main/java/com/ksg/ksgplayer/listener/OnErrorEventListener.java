package com.ksg.ksgplayer.listener;

import android.os.Bundle;

/**
 * @author kaisengao
 * @create: 2019/2/28 10:57
 * @describe: 播放器的错误事件
 */
public interface OnErrorEventListener {

    /**
     * 未知的播放错误
     */
    int ERROR_EVENT_UNKNOWN = -88001;

    /**
     * 网络断连，且经多次重连亦不能恢复，更多重试请自行重启播放
     */
    int ERROR_PLAY_ERR_NET_DISCONNECT = -88002;

    /**
     * 发送错误信息
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onErrorEvent(int eventCode, Bundle bundle);
}

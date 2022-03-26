package com.ksg.ksgplayer.listener;

import android.os.Bundle;

import com.ksg.ksgplayer.cover.ICoverManager;

/**
 * @ClassName: OnProducerEventListener
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 15:52
 * @Description: 生产者事件
 */
public interface OnProducerSenderListener {

    /**
     * 发送事件
     *
     * @param eventCode      code
     * @param bundle         bundle
     * @param receiverFilter 过滤器
     */
    void sendEvent(int eventCode, Bundle bundle, ICoverManager.OnCoverFilter receiverFilter);

    /**
     * 发送Object事件
     *
     * @param key            key
     * @param value          value
     * @param receiverFilter 过滤器
     */
    void sendObject(String key, Object value, ICoverManager.OnCoverFilter receiverFilter);
}

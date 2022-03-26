package com.ksg.ksgplayer.producer;

import android.os.Bundle;

import com.ksg.ksgplayer.listener.OnProducerSenderListener;
import com.ksg.ksgplayer.cover.ICoverManager;

/**
 * @ClassName: ProducerEventSender
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 15:47
 * @Description: 生产者事件发送者
 */
public final class ProducerSender {

    private final OnProducerSenderListener mProducerEventListener;

    public ProducerSender(OnProducerSenderListener producerEventListener) {
        this.mProducerEventListener = producerEventListener;
    }

    /**
     * 发送事件
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    public void sendEvent(int eventCode, Bundle bundle) {
        this.sendEvent(eventCode, bundle, null);
    }

    /**
     * 发送事件
     *
     * @param eventCode      code
     * @param bundle         bundle
     * @param receiverFilter 过滤器
     */
    public void sendEvent(int eventCode, Bundle bundle, ICoverManager.OnCoverFilter receiverFilter) {
        if (mProducerEventListener != null) {
            this.mProducerEventListener.sendEvent(eventCode, bundle, receiverFilter);
        }
    }

    /**
     * 发送事件
     *
     * @param key   key
     * @param value value
     */
    public void sendObject(String key, Object value) {
        this.sendObject(key, value, null);
    }

    /**
     * 发送事件
     *
     * @param key            key
     * @param value          value
     * @param receiverFilter 过滤器
     */
    public void sendObject(String key, Object value, ICoverManager.OnCoverFilter receiverFilter) {
        if (mProducerEventListener != null) {
            this.mProducerEventListener.sendObject(key, value, receiverFilter);
        }
    }
}

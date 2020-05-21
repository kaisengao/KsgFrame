package com.ksg.ksgplayer.extension;

import android.os.Bundle;

import com.ksg.ksgplayer.receiver.IReceiverGroup;

/**
 * @author 高新
 * @create: 2019/1/28 11:29
 * @describe: 代表事件发送者
 */
public interface DelegateProducerEventSender {

    /**
     * 发送事件
     *
     * @param eventCode      code
     * @param bundle         bundle
     * @param receiverFilter 过滤器
     */
    void sendEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter receiverFilter);


    /**
     * 发送Object事件
     *
     * @param key      key
     * @param value         value
     * @param receiverFilter 过滤器
     */
    void sendObject(String key, Object value, IReceiverGroup.OnReceiverFilter receiverFilter);
}

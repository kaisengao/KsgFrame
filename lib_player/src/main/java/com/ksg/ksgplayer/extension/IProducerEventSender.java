package com.ksg.ksgplayer.extension;

import android.os.Bundle;

import com.ksg.ksgplayer.receiver.IReceiverGroup;

/**
 * @author 高新
 * @create: 2019/1/28 10:41
 * @describe: 事件生产者，发送事件类型
 */
public interface IProducerEventSender {

    /**
     * 发送事件
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    void sendEvent(int eventCode, Bundle bundle);

    /**
     * 发送事件
     *
     * @param eventCode      code
     * @param bundle         bundle
     * @param receiverFilter 过滤器
     */
    void sendEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter receiverFilter);

    /**
     * 发送Boolean类型
     *
     * @param key   key
     * @param value value
     */
    void sendBoolean(String key, boolean value);

    /**
     * 发送Boolean类型
     *
     * @param key            key
     * @param value          value
     * @param receiverFilter 过滤器
     */
    void sendBoolean(String key, boolean value, IReceiverGroup.OnReceiverFilter receiverFilter);

    /**
     * 发送Int类型
     *
     * @param key   key
     * @param value value
     */
    void sendInt(String key, int value);

    /**
     * 发送Int类型
     *
     * @param key            key
     * @param value          value
     * @param receiverFilter 过滤器
     */
    void sendInt(String key, int value, IReceiverGroup.OnReceiverFilter receiverFilter);

    /**
     * 发送String类型
     *
     * @param key   key
     * @param value value
     */
    void sendString(String key, String value);

    /**
     * 发送String类型
     *
     * @param key            key
     * @param value          value
     * @param receiverFilter 过滤器
     */
    void sendString(String key, String value, IReceiverGroup.OnReceiverFilter receiverFilter);

    /**
     * 发送Float类型
     *
     * @param key   key
     * @param value value
     */
    void sendFloat(String key, float value);

    /**
     * 发送Float类型
     *
     * @param key            key
     * @param value          value
     * @param receiverFilter 过滤器
     */
    void sendFloat(String key, float value, IReceiverGroup.OnReceiverFilter receiverFilter);

    /**
     * 发送Long类型
     *
     * @param key   key
     * @param value value
     */
    void sendLong(String key, long value);

    /**
     * 发送Long类型
     *
     * @param key            key
     * @param value          value
     * @param receiverFilter 过滤器
     */
    void sendLong(String key, long value, IReceiverGroup.OnReceiverFilter receiverFilter);

    /**
     * 发送Double类型
     *
     * @param key   key
     * @param value value
     */
    void sendDouble(String key, double value);

    /**
     * 发送Double类型
     *
     * @param key            key
     * @param value          value
     * @param receiverFilter 过滤器
     */
    void sendDouble(String key, double value, IReceiverGroup.OnReceiverFilter receiverFilter);

    /**
     * 发送Object类型
     *
     * @param key   key
     * @param value value
     */
    void sendObject(String key, Object value);

    /**
     * 发送Object类型
     *
     * @param key            key
     * @param value          value
     * @param receiverFilter 过滤器
     */
    void sendObject(String key, Object value, IReceiverGroup.OnReceiverFilter receiverFilter);
}

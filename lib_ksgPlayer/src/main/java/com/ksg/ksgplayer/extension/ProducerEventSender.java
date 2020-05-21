package com.ksg.ksgplayer.extension;

import android.os.Bundle;

import com.ksg.ksgplayer.receiver.IReceiverGroup;

/**
 * @author 高新
 * @create: 2019/1/28 10:40
 * @describe: 生产事件发送者
 */
public final class ProducerEventSender implements IProducerEventSender {

    /**
     * 委托事件发送
     */
    private DelegateProducerEventSender mEventSender;

    public ProducerEventSender(DelegateProducerEventSender eventSender) {
        this.mEventSender = eventSender;
    }

    @Override
    public void sendEvent(int eventCode, Bundle bundle) {
        sendEvent(eventCode, bundle, null);
    }

    @Override
    public void sendEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter receiverFilter) {
        if (mEventSender != null) {
            mEventSender.sendEvent(eventCode, bundle, receiverFilter);
        }
    }

    @Override
    public void sendBoolean(String key, boolean value) {
        sendObject(key, value);
    }

    @Override
    public void sendBoolean(String key, boolean value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendInt(String key, int value) {
        sendObject(key, value);
    }

    @Override
    public void sendInt(String key, int value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendString(String key, String value) {
        sendObject(key, value);
    }

    @Override
    public void sendString(String key, String value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendFloat(String key, float value) {
        sendObject(key, value);
    }

    @Override
    public void sendFloat(String key, float value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendLong(String key, long value) {
        sendObject(key, value);
    }

    @Override
    public void sendLong(String key, long value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendDouble(String key, double value) {
        sendObject(key, value);
    }

    @Override
    public void sendDouble(String key, double value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendObject(String key, Object value) {
        sendObject(key, value, null);
    }

    @Override
    public void sendObject(String key, Object value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        if (mEventSender != null) {
            mEventSender.sendObject(key, value, receiverFilter);
        }
    }
}

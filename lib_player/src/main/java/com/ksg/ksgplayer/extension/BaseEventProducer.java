package com.ksg.ksgplayer.extension;

import java.io.Serializable;

/**
 * @author kaisengao
 * @create: 2019/1/28 11:37
 * @describe: 事件生产者，设计的目的是为了让外部事件引入，服务于视图。
 */
public abstract class BaseEventProducer implements EventProducer, Serializable {

    /**
     * 事件生产者，发送事件类型
     */
    private IProducerEventSender mReceiverEventSender;

    /**
     * 注册事件发送者
     *
     * @param producerEventSender 发送者
     */
    void attachSender(IProducerEventSender producerEventSender) {
        this.mReceiverEventSender = producerEventSender;
    }

    @Override
    public IProducerEventSender getSender() {
        return mReceiverEventSender;
    }
}

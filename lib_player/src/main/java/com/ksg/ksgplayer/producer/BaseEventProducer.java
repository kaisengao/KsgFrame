package com.ksg.ksgplayer.producer;

import java.io.Serializable;

/**
 * @author kaisengao
 * @create: 2019/1/28 11:37
 * @describe: 事件生产者
 */
public abstract class BaseEventProducer implements EventProducer, Serializable {

    private ProducerSender mProducerSender;

    /**
     * 注册事件发送者
     *
     * @param producerSender 发送者
     */
    public void bindSender(ProducerSender producerSender) {
        this.mProducerSender = producerSender;
    }

    /**
     * 获取生产者事件发送者
     *
     * @return {@link ProducerSender}
     */
    @Override
    public ProducerSender getProducerSender() {
        return mProducerSender;
    }

    /**
     * 销毁事件
     */
    @Override
    public void destroy() {
        this.mProducerSender = null;
    }
}

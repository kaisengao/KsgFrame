package com.ksg.ksgplayer.producer;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ProducerManager
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 15:43
 * @Description: 生产者管理器
 */
public final class ProducerManager {

    private ProducerSender mProducerSender;

    private List<BaseEventProducer> mEventProducers;

    public ProducerManager(ProducerSender producerSender) {
        this.mProducerSender = producerSender;
        this.mEventProducers = new ArrayList<>();
    }

    /**
     * 添加 生产者
     *
     * @param eventProducer 生产者
     */
    public void addEventProducer(BaseEventProducer eventProducer) {
        if (eventProducer != null && !mEventProducers.contains(eventProducer)) {
            // 绑定事件发送者
            eventProducer.bindSender(mProducerSender);
            // 添加事件回调
            eventProducer.onAdded();
            // 存入集合
            this.mEventProducers.add(eventProducer);
        }
    }

    /**
     * 移除 生产者
     *
     * @param eventProducer 事件生产者
     */
    public void removeEventProducer(BaseEventProducer eventProducer) {
        if (eventProducer != null && mEventProducers.contains(eventProducer)) {
            // 解绑事件发送者
            eventProducer.bindSender(null);
            // 删除事件回调
            eventProducer.onRemoved();
            // 移除集合
            this.mEventProducers.remove(eventProducer);
        }
    }

    /**
     * 返回 生产者集合
     *
     * @return List
     */
    public List<BaseEventProducer> getEventProducers() {
        return mEventProducers;
    }

    /**
     * 销毁资源
     */
    public void destroy() {
        // 清空集合
        if (mEventProducers != null) {
            for (BaseEventProducer eventProducer : mEventProducers) {
                eventProducer.destroy();
            }
            this.mEventProducers.clear();
            this.mEventProducers = null;
        }
        // 释放
        this.mProducerSender = null;
    }
}

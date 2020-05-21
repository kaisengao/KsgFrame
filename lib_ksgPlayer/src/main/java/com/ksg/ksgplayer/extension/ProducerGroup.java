package com.ksg.ksgplayer.extension;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author kaisengao
 * @create: 2019/1/28 11:39
 * @describe: 管理多个事件生产者
 */
public final class ProducerGroup implements IProducerGroup {

    /**
     * 事件生产者，发送事件类型
     */
    private IProducerEventSender mEventSender;

    /**
     * 事件生产者集合
     */
    private List<BaseEventProducer> mEventProducers;

    public ProducerGroup(IProducerEventSender eventSender) {
        this.mEventSender = eventSender;
        //  线程安全的CopyOnWriteArrayList  (详细了解请指向 https://www.baidu.com)
        mEventProducers = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addEventProducer(BaseEventProducer eventProducer) {
        // 判断是否添加过此事件生产者
        if (!mEventProducers.contains(eventProducer)) {
            // 事件生产者 注册事件发送者
            eventProducer.attachSender(mEventSender);
            // 将事件生产者加入集合中
            mEventProducers.add(eventProducer);
            // 已添加事件回调
            eventProducer.onAdded();
        }
    }

    @Override
    public boolean removeEventProducer(BaseEventProducer eventProducer) {
        // 将事件生产者从集合中删除
        boolean remove = mEventProducers.remove(eventProducer);
        if (remove && eventProducer != null) {
            // 删除事件回调
            eventProducer.onRemoved();
            // 初始化 事件发送者
            eventProducer.attachSender(null);
        }
        return remove;
    }

    @Override
    public List<BaseEventProducer> getEventProducers() {
        return mEventProducers;
    }

    @Override
    public void destroy() {
        // 循环 销毁所有事件生产者
        for (BaseEventProducer eventProducer : mEventProducers) {
            // 删除事件回调
            eventProducer.onRemoved();
            // 销毁事件回调
            eventProducer.destroy();
            // 初始化 事件发送者
            eventProducer.attachSender(null);
        }
        // 清空事件生产者集合
        mEventProducers.clear();
    }
}

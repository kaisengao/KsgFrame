package com.ksg.ksgplayer.producer;

/**
 * @author 高新
 * @create: 2019/1/28 10:35
 * @describe: <p>
 * 事件生产者
 * <p>
 * 例如系统的电源更改事件或网络更改事件
 */
public interface EventProducer {

    /**
     * 添加事件
     */
    void onAdded();

    /**
     * 移除事件
     */
    void onRemoved();

    /**
     * 销毁事件
     */
    void destroy();

    /**
     * 获取生产者事件发送者
     *
     * @return {@link ProducerSender}
     */
    ProducerSender getProducerSender();
}

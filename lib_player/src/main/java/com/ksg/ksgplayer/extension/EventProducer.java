package com.ksg.ksgplayer.extension;

/**
 * @author 高新
 * @create: 2019/1/28 10:35
 * @describe: <p>
 * 事件的制作者。它通常是由用户自己添加的，
 * <p>
 * 例如系统的电源更改事件或网络更改事件。（初始化添加 网络更改事件。）
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
     * 获取事件发送者
     *
     * @return sender 发送者
     */
    IProducerEventSender getSender();
}

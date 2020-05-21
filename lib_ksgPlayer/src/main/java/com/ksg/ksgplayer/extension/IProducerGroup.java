package com.ksg.ksgplayer.extension;

import java.util.List;

/**
 * @author kaisengao
 * @create: 2019/1/28 10:34
 * @describe: 管理多个事件生产者
 */
public interface IProducerGroup {

    /**
     * 添加事件生产者
     *
     * @param eventProducer 事件生产者
     */
    void addEventProducer(BaseEventProducer eventProducer);

    /**
     * 移除事件生产者
     *
     * @param eventProducer 事件生产者
     * @return boolean
     */
    boolean removeEventProducer(BaseEventProducer eventProducer);


    /**
     * 返回事件生产者集合 便于控制
     *
     * @return List
     */
    List<BaseEventProducer> getEventProducers();

    /**
     * 销毁管理者
     */
    void destroy();

}

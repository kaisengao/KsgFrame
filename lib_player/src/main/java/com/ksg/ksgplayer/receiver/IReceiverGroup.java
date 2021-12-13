package com.ksg.ksgplayer.receiver;

import java.util.Comparator;

/**
 * @author 高新
 * @create: 2019/1/28 10:42
 * @describe: 管理多个接收器
 */
public interface IReceiverGroup {

    /**
     * 添加OnReceiverGroupChangeListener侦听接收器
     *
     * @param onReceiverGroupChangeListener onReceiverGroupChangeListener
     */
    void addOnReceiverGroupChangeListener(OnReceiverGroupChangeListener onReceiverGroupChangeListener);

    /**
     * 删除OnReceiverGroupChangeListener侦听接收器
     *
     * @param onReceiverGroupChangeListener onReceiverGroupChangeListener
     */
    void removeOnReceiverGroupChangeListener(OnReceiverGroupChangeListener onReceiverGroupChangeListener);

    /**
     * 添加一个接收器，您需要为此接收器放置一个唯一的密钥
     *
     * @param key      key
     * @param receiver receiver
     */
    void addReceiver(String key, IReceiver receiver);

    /**
     * 根据唯一密钥，移除一个接收器
     *
     * @param key key
     */
    void removeReceiver(String key);

    /**
     * 给接收器排序
     *
     * @param comparator 排序
     */
    void sort(Comparator<IReceiver> comparator);

    /**
     * 通过接收器滤波器循环所有接收器。 循环所有接收器
     *
     * @param onLoopListener onLoopListener
     */
    void forEach(OnLoopListener onLoopListener);

    /**
     * 通过接收器滤波器循环所有接收器
     *
     * @param filter         过滤器
     * @param onLoopListener onLoopListener
     */
    void forEach(OnReceiverFilter filter, OnLoopListener onLoopListener);

    /**
     * 按键获取接收器.
     *
     * @param key key
     * @return T
     */
    <T extends IReceiver> T getReceiver(String key);

    /**
     * 获取ReceiverGroup组值
     *
     * @return groupValue
     */
    GroupValue getGroupValue();

    /**
     * 清除接收器
     */
    void clearReceivers();

    /**
     * 接收器
     */
    interface OnReceiverGroupChangeListener {
        /**
         * 添加一个接收器
         *
         * @param key      key
         * @param receiver 接收器
         */
        void onReceiverAdd(String key, IReceiver receiver);

        /**
         * 移除一个接收器
         *
         * @param key      key
         * @param receiver 接收器
         */
        void onReceiverRemove(String key, IReceiver receiver);
    }

    /**
     * 循环所有接收回调
     */
    interface OnLoopListener {
        /**
         * 寻找每个接收器
         *
         * @param receiver 接收器
         */
        void onEach(IReceiver receiver);
    }

    /**
     * 接收过滤回调
     */
    interface OnReceiverFilter {
        /**
         * 过滤接收器
         *
         * @param receiver 接收器
         * @return true 存在 or  不存在
         */
        boolean filter(IReceiver receiver);
    }

    /**
     * 组件内容更新回调
     */
    interface OnGroupValueUpdateListener {
        /**
         * 过滤的Key
         *
         * @return key数组
         */
        String[] filterKeys();

        /**
         * value值更新
         *
         * @param key   key
         * @param value value
         */
        void onValueUpdate(String key, Object value);
    }
}

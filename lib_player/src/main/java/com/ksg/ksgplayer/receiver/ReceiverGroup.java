package com.ksg.ksgplayer.receiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author kaisengao
 * @create: 2019/1/29 15:34
 * @describe: 管理多个接收器
 * <p>
 * ReceiverGroup的出现目的就是对众多Receiver 接收者进行统一的管理，统一的事件下发，当然还有下面的数据共享问题
 */
public final class ReceiverGroup implements IReceiverGroup {

    /**
     * 接收器 map集合
     */
    private Map<String, IReceiver> mReceivers;
    /**
     * 循环 接收器 集合
     */
    private List<IReceiver> mReceiverArray;
    /**
     * 接收器事件 集合
     */
    private List<OnReceiverGroupChangeListener> mOnReceiverGroupChangeListeners;
    /**
     * 共享数据
     */
    private GroupValue mGroupValue;

    public ReceiverGroup() {
        this(null);
    }

    public ReceiverGroup(GroupValue groupValue) {
        // 并发容器ConcurrentHashMap  (详细了解请指向 https://www.baidu.com)
        mReceivers = new ConcurrentHashMap<>(16);
        // 线程安全的创建集合
        mReceiverArray = Collections.synchronizedList(new ArrayList<IReceiver>());
        // 线程安全的CopyOnWriteArrayList  (详细了解请指向 https://www.baidu.com)
        mOnReceiverGroupChangeListeners = new CopyOnWriteArrayList<>();

        if (groupValue == null) {
            mGroupValue = new GroupValue();
        } else {
            mGroupValue = groupValue;
        }
    }

    @Override
    public void addOnReceiverGroupChangeListener(OnReceiverGroupChangeListener onReceiverGroupChangeListener) {
        if (mOnReceiverGroupChangeListeners.contains(onReceiverGroupChangeListener)) {
            return;
        }
        mOnReceiverGroupChangeListeners.add(onReceiverGroupChangeListener);
    }

    @Override
    public void removeOnReceiverGroupChangeListener(OnReceiverGroupChangeListener onReceiverGroupChangeListener) {
        mOnReceiverGroupChangeListeners.remove(onReceiverGroupChangeListener);
    }

    @Override
    public void addReceiver(String key, IReceiver receiver) {
        // 设置key
        ((BaseReceiver) receiver).setKey(key);
        // 绑定 Receiver 组管理器
        receiver.bindGroup(this);
        // 绑定 Receiver
        receiver.onReceiverBind();
        // 将 receiver 添加入map集合中
        mReceivers.put(key, receiver);
        // 将 receiver 添加入循环集合中
        mReceiverArray.add(receiver);
        // 添加接收器回调通知
        callBackOnReceiverAdd(key, receiver);
    }

    @Override
    public void removeReceiver(String key) {
        // 从map集合中删除
        IReceiver receiver = mReceivers.remove(key);
        // 从循环集合中删除
        mReceiverArray.remove(receiver);
        // 删除接收器回调通知
        onReceiverRemove(key, receiver);
    }

    /**
     * 删除接收器回调通知
     *
     * @param key      key
     * @param receiver 接收器
     */
    private void onReceiverRemove(String key, IReceiver receiver) {
        if (receiver != null) {
            // 删除接收器回调通知
            callBackOnReceiverRemove(key, receiver);
            // 解绑 Receiver
            receiver.onReceiverUnBind();
        }
    }

    /**
     * 回调 接收器添加通知
     *
     * @param key      key
     * @param receiver 接收器
     */
    private void callBackOnReceiverAdd(String key, IReceiver receiver) {
        for (OnReceiverGroupChangeListener listener : mOnReceiverGroupChangeListeners) {
            listener.onReceiverAdd(key, receiver);
        }
    }

    /**
     * 回调 接收器删除通知
     *
     * @param key      key
     * @param receiver 接收器
     */
    private void callBackOnReceiverRemove(String key, IReceiver receiver) {
        for (OnReceiverGroupChangeListener listener : mOnReceiverGroupChangeListeners) {
            listener.onReceiverRemove(key, receiver);
        }
    }

    @Override
    public void sort(Comparator<IReceiver> comparator) {
        Collections.sort(mReceiverArray, comparator);
    }

    @Override
    public void forEach(OnLoopListener onLoopListener) {
        forEach(null, onLoopListener);
    }

    @Override
    public void forEach(OnReceiverFilter filter, OnLoopListener onLoopListener) {
        for (IReceiver receiver : mReceiverArray) {
            if (filter == null || filter.filter(receiver)) {
                onLoopListener.onEach(receiver);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IReceiver> T getReceiver(String key) {
        if (mReceivers != null) {
            return (T) mReceivers.get(key);
        }
        return null;
    }

    @Override
    public GroupValue getGroupValue() {
        return mGroupValue;
    }

    @Override
    public void clearReceivers() {
        for (IReceiver receiver : mReceiverArray) {
            onReceiverRemove(receiver.getKey(), receiver);
        }
        mReceiverArray.clear();
        mReceivers.clear();
    }
}

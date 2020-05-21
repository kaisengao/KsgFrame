package com.ksg.ksgplayer.receiver;

import android.os.Bundle;

import com.ksg.ksgplayer.listener.OnReceiverEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author 高新
 * @create: 2019/1/28 10:44
 * @describe: Receiver 接收
 */
public interface IReceiver {

    /**
     * 绑定Receiver 组管理器
     *
     * @param iReceiverGroup Receiver 组管理器
     */
    void bindGroup(@NonNull IReceiverGroup iReceiverGroup);

    /**
     * 当Receiver被添加到ReceiverGroup时该方法会被调用。建议视图类中View的初始化在此方法中进行。
     */
    void onReceiverBind();

    /**
     * 当Receiver被从ReceiverGroup中移除时调用。可以在此方法中进行一些释放、销毁操作等。
     */
    void onReceiverUnBind();

    /**
     * 所有的播放事件都通过这个方法进行回调.
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onPlayerEvent(int eventCode, Bundle bundle);

    /**
     * 所有的错误事件都通过这个方法进行回调.
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onErrorEvent(int eventCode, Bundle bundle);

    /**
     * 绑定 播放状态获取器.
     *
     * @param stateGetter stateGetter
     */
    void bindStateGetter(StateGetter stateGetter);

    /**
     * 绑定 视图管理器.
     *
     * @param iCoverStrategy iCoverStrategy
     */
    void bindICoverStrategy(ICoverStrategy iCoverStrategy);

    /**
     * 绑定接收器 组件回调
     *
     * @param onReceiverEventListener onReceiverEventListener
     */
    void bindReceiverEventListener(OnReceiverEventListener onReceiverEventListener);

    /**
     * 非特定的Receiver事件都会被回调到此方法中
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onReceiverEvent(int eventCode, Bundle bundle);

    /**
     * 您可以调用此方法分派私有事件.
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     * @return 在接收器的响应之后绑定返回值，可以为空
     */
    @Nullable
    Bundle onPrivateEvent(int eventCode, Bundle bundle);

    /**
     * 生产者事件回调此方法
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onProducerEvent(int eventCode, Bundle bundle);

    /**
     * 生产者数据回调此方法
     *
     * @param key  key
     * @param data data
     */
    void onProducerData(String key, Object data);

    /**
     * 当添加接收器时，所设置的key（ReceiverKey）.
     *
     * @return ReceiverKey
     */
    String getKey();
}

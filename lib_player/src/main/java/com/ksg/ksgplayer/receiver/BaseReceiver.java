package com.ksg.ksgplayer.receiver;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ksg.ksgplayer.listener.OnReceiverEventListener;

/**
 * @author kaisengao
 * @create: 2019/1/29 14:24
 * @describe: Receiver 组管理器
 */
public abstract class BaseReceiver implements IReceiver, StateGetter {

    private final Context mContext;

    private String mKey;

    private IReceiverGroup mIReceiverGroup;

    private StateGetter mStateGetter;

    private ICoverStrategy mICoverStrategy;

    private OnReceiverEventListener mOnReceiverEventListener;

    BaseReceiver(Context context) {
        this.mContext = context;
    }

    /**
     * 绑定ReceiverGroup
     *
     * @param iReceiverGroup receiverGroup
     */
    @Override
    public final void bindGroup(@NonNull IReceiverGroup iReceiverGroup) {
        this.mIReceiverGroup = iReceiverGroup;
    }

    @Override
    public void onReceiverBind() {

    }

    @Override
    public void onReceiverUnBind() {

    }

    protected final GroupValue getGroupValue() {
        return mIReceiverGroup.getGroupValue();
    }

    @Override
    public final void bindStateGetter(StateGetter stateGetter) {
        this.mStateGetter = stateGetter;
    }

    @Override
    public void bindICoverStrategy(ICoverStrategy iCoverStrategy) {
        this.mICoverStrategy = iCoverStrategy;
    }

    @Override
    public final PlayerStateGetter getPlayerStateGetter() {
        if (mStateGetter != null) {
            return mStateGetter.getPlayerStateGetter();
        }
        return null;
    }

    public final ICoverStrategy getICoverStrategy() {
        return mICoverStrategy;
    }

    /**
     * 绑定侦听器。此方法由框架内部调用
     */
    @Override
    public final void bindReceiverEventListener(OnReceiverEventListener onReceiverEventListener) {
        this.mOnReceiverEventListener = onReceiverEventListener;
    }

    /**
     * 一个接收器发送一个事件，同一组中的接收器可以接收该事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    protected final void notifyReceiverEvent(int eventCode, Bundle bundle) {
        if (mOnReceiverEventListener != null) {
            mOnReceiverEventListener.onReceiverEvent(eventCode, bundle);
        }
    }

    /**
     * 发送事件  指定接收器接收
     *
     * @param key       接收器key
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    protected final void notifyReceiverPrivateEvent(@NonNull String key, int eventCode, Bundle bundle) {
        if (mIReceiverGroup != null && !TextUtils.isEmpty(key)) {
            IReceiver receiver = mIReceiverGroup.getReceiver(key);
            if (receiver != null) {
                receiver.onPrivateEvent(eventCode, bundle);
            } else {
                Log.e("BaseReceiver", "not found receiver use you incoming key.");
            }
        }
    }

    @Nullable
    @Override
    public Bundle onPrivateEvent(int eventCode, Bundle bundle) {
        return null;
    }

    @Override
    public void onProducerEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onProducerData(String key, Object data) {

    }

    public final Context getContext() {
        return mContext;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    @Override
    public final String getKey() {
        return mKey;
    }
}

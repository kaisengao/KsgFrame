package com.kaisengao.ksgframe.player.producer;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.kaisengao.ksgframe.receiver.NetworkStateReceiver;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.producer.BaseEventProducer;

/**
 * @ClassName: NetworkEventProducer
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/25 12:36
 * @Description: 网络事件生产者
 */
public class NetworkEventProducer extends BaseEventProducer implements NetworkStateReceiver.NetworkStateListener {

    private Context mContext;

    private NetworkStateReceiver mReceiver;

    public NetworkEventProducer(Context context) {
        this.mContext = context;
    }

    /**
     * 添加事件
     */
    @Override
    public void onAdded() {
        this.registerReceiver();
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        this.unregisterReceiver();
        if (mContext != null && mReceiver == null) {
            this.mReceiver = new NetworkStateReceiver();
            this.mReceiver.setNetStateListener(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            this.mContext.registerReceiver(mReceiver, intentFilter);
        }
    }


    /**
     * 移除事件
     */
    @Override
    public void onRemoved() {
        this.unregisterReceiver();
    }

    /**
     * 取消广播
     */
    private void unregisterReceiver() {
        if (mContext != null && mReceiver != null) {
            this.mContext.unregisterReceiver(mReceiver);
            this.mReceiver = null;
        }
    }

    /**
     * 销毁事件
     */
    @Override
    public void destroy() {
        super.destroy();
        // 释放
        this.mContext = null;
        this.unregisterReceiver();
    }

    /**
     * 网络状态
     *
     * @param netState {@link NetworkUtils}
     */
    @Override
    public void getNetState(int netState) {
        // 发送事件
        if (getProducerSender() != null) {
            this.getProducerSender().sendObject(EventKey.INT_DATA, netState);
        }
    }
}
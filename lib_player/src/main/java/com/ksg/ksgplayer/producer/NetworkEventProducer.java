package com.ksg.ksgplayer.producer;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.ksg.ksgplayer.assist.InterKey;
import com.ksg.ksgplayer.broadcast.NetworkStateReceiver;
import com.ksg.ksgplayer.extension.BaseEventProducer;
import com.ksg.ksgplayer.extension.IProducerEventSender;

/**
 * @author kaisengao
 * @create: 2019/1/28 14:44
 * @describe: 网络更改事件生产者，用于发送监听网络状态更改事件。
 */
public class NetworkEventProducer extends BaseEventProducer implements NetworkStateReceiver.INetStateListener {

    private Context mContext;

    private int mNetworkState = -2;

    private NetworkStateReceiver mReceiver;

    public NetworkEventProducer(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void onAdded() {
        registerNetChangeReceiver();
    }

    /**
     * 注册广播
     */
    private void registerNetChangeReceiver() {
        unregisterNetChangeReceiver();
        if (mContext != null && mReceiver == null) {
            mReceiver = new NetworkStateReceiver();
            mReceiver.setNetStateListener(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(mReceiver, intentFilter);
        }
    }

    /**
     * 取消广播
     */
    private void unregisterNetChangeReceiver() {
        if (mContext != null && mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void getNetState(int netState) {
        if (mNetworkState == netState) {
            return;
        }
        mNetworkState = netState;
        IProducerEventSender sender = getSender();
        if (sender != null) {
            sender.sendInt(InterKey.KEY_NETWORK_STATE, mNetworkState);
        }
    }

    @Override
    public void onRemoved() {
        destroy();
    }

    @Override
    public void destroy() {
        unregisterNetChangeReceiver();
    }

}

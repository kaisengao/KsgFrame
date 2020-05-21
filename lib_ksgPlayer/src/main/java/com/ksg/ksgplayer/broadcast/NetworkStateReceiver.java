package com.ksg.ksgplayer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.ksg.ksgplayer.config.KsgConstant;
import com.ksg.ksgplayer.utils.NetworkUtils;

/**
 * @author kaisengao
 * @create: 2019/1/28 14:09
 * @describe: 网络状态发生改变广播
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    private INetStateListener mINetStateListener;

    public void setNetStateListener(INetStateListener listener) {
        mINetStateListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (mINetStateListener != null) {
                int networkState = NetworkUtils.getNetworkState(context);
                mINetStateListener.getNetState(networkState);
            }
        }

    }

    public interface INetStateListener {
        /**
         * 网络变化状态
         *
         * @param netState {@linkplain KsgConstant}
         */
        void getNetState(int netState);
    }
}

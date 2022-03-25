package com.kasiengao.ksgframe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.kaisengao.base.util.NetworkUtils;


/**
 * @author kaisengao
 * @create: 2019/1/28 14:09
 * @describe: 网络状态发生改变广播
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    private NetworkStateListener mNetworkStateListener;

    public void setNetStateListener(NetworkStateListener networkStateListener) {
        this.mNetworkStateListener = networkStateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (mNetworkStateListener != null) {
                int networkState = NetworkUtils.getNetworkState(context);
                this.mNetworkStateListener.getNetState(networkState);
            }
        }

    }

    public interface NetworkStateListener {

        /**
         * 网络状态
         *
         * @param netState {@link NetworkUtils}
         */
        void getNetState(int netState);
    }
}

package com.ksg.ksgplayer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ksg.ksgplayer.config.KsgConstant;

/**
 * @author kaisengao
 * @create: 2019/1/28 14:10
 * @describe: 网络状态变化
 */
public class NetworkUtils {

    /**
     * 获取当前网络连接类型
     *
     * @param context context
     * @return int
     */
    public static int getNetworkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return KsgConstant.NETWORK_STATE_NONE;
        }

        @SuppressLint("MissingPermission")
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return KsgConstant.NETWORK_STATE_NONE;
        } else {
            NetworkInfo.State networkInfoState = info.getState();
            if (networkInfoState == NetworkInfo.State.CONNECTING) {
                return KsgConstant.NETWORK_STATE_CONNECTING;
            }
            if (!info.isAvailable()) {
                return KsgConstant.NETWORK_STATE_NONE;
            }
        }

        //如果当前的网络连接成功并且网络连接可用
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return KsgConstant.NETWORK_STATE_WIFI;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            return KsgConstant.NETWORK_STATE_MOBILE;
        }

        return KsgConstant.NETWORK_STATE_NONE;
    }

    public static boolean isMobile(int networkState) {
        return networkState > KsgConstant.NETWORK_STATE_WIFI;
    }

    /**
     * 网络是否连接。
     *
     * @param context context
     * @return true/false
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            @SuppressLint("MissingPermission")
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    /**
     * is wifi ?
     *
     * @param context context
     * @return true/false
     */
    public static synchronized boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            @SuppressLint("MissingPermission")
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                int networkInfoType = networkInfo.getType();
                if (networkInfoType == ConnectivityManager.TYPE_WIFI || networkInfoType == ConnectivityManager.TYPE_ETHERNET) {
                    return networkInfo.isConnected();
                }
            }
        }
        return false;
    }

}

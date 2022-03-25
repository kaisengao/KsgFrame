package com.kaisengao.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * @author kaisengao
 * @create: 2019/1/28 14:10
 * @describe: 网络状态变化
 */
public class NetworkUtils {

    /**
     * 正在切换网络
     */
    public static final int NETWORK_STATE_CONNECTING = -2;

    /**
     * 无网络
     */
    public static final int NETWORK_STATE_NONE = -1;

    /**
     * 移动网络
     */
    public static final int NETWORK_STATE_MOBILE = 0;

    /**
     * WIFI
     */
    public static final int NETWORK_STATE_WIFI = 1;

    /**
     * 获取当前网络连接类型
     *
     * @param context context
     * @return int
     */
    public static int getNetworkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return NETWORK_STATE_NONE;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return NETWORK_STATE_NONE;
        } else {
            NetworkInfo.State networkInfoState = info.getState();
            if (networkInfoState == NetworkInfo.State.CONNECTING) {
                return NETWORK_STATE_CONNECTING;
            }
            if (!info.isAvailable()) {
                return NETWORK_STATE_NONE;
            }
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return NETWORK_STATE_WIFI;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            return NETWORK_STATE_MOBILE;
        }

        return NETWORK_STATE_NONE;
    }
}

package com.ksg.ksgplayer.listener;

import android.os.Bundle;

/**
 * @author kaisengao
 * @create: 2019/1/29 13:51
 * @describe: 组件回调
 */
public interface OnReceiverEventListener {

    /**
     * 组件数据回调
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onReceiverEvent(int eventCode, Bundle bundle);
}

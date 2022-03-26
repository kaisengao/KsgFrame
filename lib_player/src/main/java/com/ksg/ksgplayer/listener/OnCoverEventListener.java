package com.ksg.ksgplayer.listener;

import android.os.Bundle;

/**
 * @author kaisengao
 * @create: 2019/1/29 13:51
 * @describe: 组件回调
 */
public interface OnCoverEventListener {

    /**
     * 组件数据回调
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onCoverEvent(int eventCode, Bundle bundle);
}

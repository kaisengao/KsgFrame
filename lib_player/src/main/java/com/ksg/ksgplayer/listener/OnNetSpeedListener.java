package com.ksg.ksgplayer.listener;

/**
 * @author kaisengao
 * @create: 2019/2/27 15:46
 * @describe: 网速
 */
public interface OnNetSpeedListener {

    /**
     * 更新网络速度
     *
     * @param speed 网速
     */
    void onNetSpeed(String speed);
}

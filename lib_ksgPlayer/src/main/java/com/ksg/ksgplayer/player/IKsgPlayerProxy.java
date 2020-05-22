package com.ksg.ksgplayer.player;

import android.os.Bundle;

/**
 * @ClassName: IKsgPlayerProxy
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:26
 * @Description: 播放器的基础信息代理接口
 */
public interface IKsgPlayerProxy {

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    void onDataSourceReady(String dataSource);

    /**
     * 播放 停止状态
     */
    void onIntentStop();

    /**
     * 播放 重置状态
     */
    void onIntentReset();

    /**
     * 播放 销毁状态
     */
    void onIntentDestroy();

    /**
     * 播放器的基础事件
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    void onPlayerEvent(int eventCode, Bundle bundle);

    /**
     * 播放器的错误事件
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    void onErrorEvent(int eventCode, Bundle bundle);

    /**
     * 获取本地缓存
     *
     * @param dataSource 播放地址
     * @return 本地缓存的播放进度
     */
    long getRecord(String dataSource);
}

package com.ksg.ksgplayer.player;

import android.os.Bundle;

import com.ksg.ksgplayer.entity.DataSource;

/**
 * @author kaisengao
 * @create: 2019/1/15 15:11
 * @describe: 记录代理的播放器
 */
public interface IKsgPlayerProxy {

    /**
     * 初始化
     *
     * @param dataSource 数据源
     */
    void onDataSourceReady(DataSource dataSource);

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
     * 事件传递
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    void onPlayerEvent(int eventCode, Bundle bundle);

    /**
     * 错误 事件传递
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    void onErrorEvent(int eventCode, Bundle bundle);

    /**
     * 获取本地缓存
     *
     * @param dataSource 数据源
     * @return 历史播放进度
     */
    int getRecord(DataSource dataSource);
}

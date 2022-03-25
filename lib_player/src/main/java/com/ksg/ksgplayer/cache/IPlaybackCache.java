package com.ksg.ksgplayer.cache;

import android.os.Bundle;

import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.state.PlayerStateGetter;

/**
 * @ClassName: IPlaybackCache
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/25 10:00
 * @Description: 播放缓存
 */
public interface IPlaybackCache {

    /**
     * 绑定 播放状态获取器
     *
     * @param playerStateGetter playerStateGetter
     */
    void bindPlayStateGetter(PlayerStateGetter playerStateGetter);

    /**
     * 设置 播放地址
     */
    void setDataSource(DataSource dataSource);

    /**
     * 暂停状态
     */
    void onIntentPause();

    /**
     * 停止状态
     */
    void onIntentStop();

    /**
     * 销毁状态
     */
    void onIntentDestroy();

    /**
     * 播放事件
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    void onPlayerEvent(int eventCode, Bundle bundle);

    /**
     * 错误事件
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    void onErrorEvent(int eventCode, Bundle bundle);

    /**
     * 添加进度缓存
     */
    void putProgressCache(DataSource dataSource);

    /**
     * 获取进度缓存
     *
     * @return progress
     */
    long getProgressCache(DataSource dataSource);

    /**
     * 重置进度缓存
     */
    void resetProgressCache(DataSource dataSource);
}
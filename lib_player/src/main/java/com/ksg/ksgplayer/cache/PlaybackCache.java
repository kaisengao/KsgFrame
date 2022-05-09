package com.ksg.ksgplayer.cache;

import android.os.Bundle;

import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.state.PlayerInfoGetter;

/**
 * @ClassName: PlaybackCache
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/25 09:58
 * @Description: 播放缓存
 */
public class PlaybackCache implements IPlaybackCache {

    private DataSource mDataSource;

    private PlayerInfoGetter mPlayerInfoGetter;

    /**
     * 绑定 播放状态获取器
     *
     * @param playerInfoGetter playerInfoGetter
     */
    public final void bindPlayInfoGetter(PlayerInfoGetter playerInfoGetter) {
        this.mPlayerInfoGetter = playerInfoGetter;
    }

    /**
     * 设置 播放地址
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        this.putProgressCache(mDataSource);
        this.mDataSource = dataSource;
    }

    /**
     * 暂停状态
     */
    @Override
    public void onIntentPause() {
        this.putProgressCache(mDataSource);
    }

    /**
     * 停止状态
     */
    @Override
    public void onIntentStop() {
        this.putProgressCache(mDataSource);
    }

    /**
     * 销毁状态
     */
    @Override
    public void onIntentDestroy() {
        this.putProgressCache(mDataSource);
        this.mPlayerInfoGetter = null;
    }

    /**
     * 播放事件
     *
     * @param eventCode code
     * @param bundle    bundle
     */

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        // 完成事件,初始进度
        if (eventCode == OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE) {
            this.resetProgressCache(mDataSource);
        }
    }

    /**
     * 错误事件
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    /**
     * 添加进度缓存
     */
    @Override
    public void putProgressCache(DataSource dataSource) {
        if (dataSource != null
                && dataSource.isProgressCache()
                && mPlayerInfoGetter != null) {
            ProgressCache.getInstance().putCache(dataSource.getUrl(), mPlayerInfoGetter.getProgress());
        }
    }

    /**
     * 获取进度缓存
     *
     * @return progress
     */
    public long getProgressCache(DataSource dataSource) {
        if (dataSource != null
                && dataSource.isProgressCache()) {
            return ProgressCache.getInstance().getCache(dataSource.getUrl());
        }
        return 0;
    }

    /**
     * 重置进度缓存
     */
    @Override
    public void resetProgressCache(DataSource dataSource) {
        if (dataSource != null
                && dataSource.isProgressCache()) {
            ProgressCache.getInstance().putCache(dataSource.getUrl(), 0L);
        }
    }
}
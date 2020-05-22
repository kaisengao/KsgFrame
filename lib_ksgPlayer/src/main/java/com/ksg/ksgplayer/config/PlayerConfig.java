package com.ksg.ksgplayer.config;

/**
 * @ClassName: PlayerConfig
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:25
 * @Description: 播放器 全局配置
 */
public class PlayerConfig {

    /**
     * 播放进度本地缓存
     */
    private boolean mPlayProgressCache = true;

    /**
     * 是否默认添加网络状态生产者
     */
    private boolean mNetworkEventProducer = true;

    public static PlayerConfig getInstance() {
        return PlayerConfigHolder.PLAYER_CONFIG;
    }

    public static final class PlayerConfigHolder {
        static final PlayerConfig PLAYER_CONFIG = new PlayerConfig();
    }

    public boolean isPlayProgressCache() {
        return mPlayProgressCache;
    }

    public void setPlayProgressCache(boolean playProgressCache) {
        mPlayProgressCache = playProgressCache;
    }

    /**
     * 是否添加网络状态
     */
    public boolean isNetworkEventProducer() {
        return mNetworkEventProducer;
    }

    public void setNetworkEventProducer(boolean networkEventProducer) {
        mNetworkEventProducer = networkEventProducer;
    }
}

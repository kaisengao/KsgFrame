package com.ksg.ksgplayer.config;

import com.ksg.ksgplayer.render.IRender;

/**
 * @ClassName: PlayerConfig
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:25
 * @Description: 播放器 全局变量
 */
public class KsgPlayerConfig {

    private int mDefaultRenderType;

    private boolean mPlayProgressCache;

    private boolean mNetworkEventProducer;

    public static KsgPlayerConfig getInstance() {
        return PlayerConfigHolder.PLAYER_CONFIG;
    }

    public static final class PlayerConfigHolder {
        static final KsgPlayerConfig PLAYER_CONFIG = new KsgPlayerConfig();
    }

    /**
     * 初始默认值
     */
    private KsgPlayerConfig() {
        this.mDefaultRenderType = IRender.RENDER_TYPE_TEXTURE_VIEW;
        this.mPlayProgressCache = true;
        this.mNetworkEventProducer = true;
    }

    public int getDefaultRenderType() {
        return mDefaultRenderType;
    }

    public void setDefaultRenderType(int defaultRenderType) {
        mDefaultRenderType = defaultRenderType;
    }

    public boolean isPlayProgressCache() {
        return mPlayProgressCache;
    }

    public void setPlayProgressCache(boolean playProgressCache) {
        mPlayProgressCache = playProgressCache;
    }

    public boolean isNetworkEventProducer() {
        return mNetworkEventProducer;
    }

    public void setNetworkEventProducer(boolean networkEventProducer) {
        mNetworkEventProducer = networkEventProducer;
    }
}

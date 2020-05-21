package com.ksg.ksgplayer.config;


/**
 * @author kaisengao
 * @create: 2019/3/11 14:15
 * @describe: 编码器配置
 */
public class PlayerConfig {

    /**
     * 是否开启缓存
     */
    private boolean mPlayRecordState = true;

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

    /**
     * 是否添加网络状态
     */
    public boolean isNetworkEventProducer() {
        return mNetworkEventProducer;
    }

    public void setNetworkEventProducer(boolean networkEventProducer) {
        mNetworkEventProducer = networkEventProducer;
    }

    /**
     * 是否开启缓存
     */
    public boolean isPlayRecordState() {
        return mPlayRecordState;
    }

    public void setPlayRecordState(boolean playRecordState) {
        mPlayRecordState = playRecordState;
    }
}

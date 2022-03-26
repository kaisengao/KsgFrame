package com.ksg.ksgplayer.cover;

import android.os.Bundle;
import android.view.View;

/**
 * @ClassName: ICover
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 16:08
 * @Description: 覆盖组件
 */
public interface ICover {

    /**
     * 等级最大覆盖优先级值  64
     */
    int LEVEL_MAX = 1 << 5;

    /**
     * 最低等级
     */
    int COVER_LEVEL_LOW = 0;

    /**
     * 中等级 32
     */
    int COVER_LEVEL_MEDIUM = 1 << 5;

    /**
     * 高等级 64
     */
    int COVER_LEVEL_HIGH = 1 << 6;

    /**
     * 组件 显示隐藏
     *
     * @param visibility visible/gone
     */
    void setCoverVisibility(int visibility);

    /**
     * 获取组件View
     *
     * @return coverView
     */
    View getCoverView();

    /**
     * 获取组件等级
     *
     * @return level
     */
    int getCoverLevel();

    /**
     * 绑定 覆盖组件管理器
     *
     * @param coverManager 覆盖组件管理器
     */
    void bindManager(ICoverManager coverManager);

    /**
     * 绑定 数据池
     *
     * @param valuePool 数据池
     */
    void bindValuePool(ICoverValue valuePool);

    /**
     * 值 过滤器
     *
     * @return {"Key1","Key2","Key3"}
     */
    String[] getValueFilters();

    /**
     * 值 事件
     *
     * @param key   key
     * @param value value
     */
    void onValueEvent(String key, Object value);

    /**
     * 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onPlayerEvent(int eventCode, Bundle bundle);

    /**
     * 错误事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onErrorEvent(int eventCode, Bundle bundle);

    /**
     * Cover事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onCoverEvent(int eventCode, Bundle bundle);

    /**
     * Cover私有事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onPrivateEvent(int eventCode, Bundle bundle);

    /**
     * 生产者事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onProducerEvent(int eventCode, Bundle bundle);

    /**
     * 生产者事件 数据
     *
     * @param key  key
     * @param data data
     */
    void onProducerData(String key, Object data);

    /**
     * CoverKey
     *
     * @param key 唯一标识
     */
    void setKey(String key);

    /**
     * CoverKey
     *
     * @return 唯一标识
     */
    String getKey();
}

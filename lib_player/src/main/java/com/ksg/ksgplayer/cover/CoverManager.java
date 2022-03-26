package com.ksg.ksgplayer.cover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CoverManager
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 16:39
 * @Description: 覆盖组件管理器
 */
public final class CoverManager implements ICoverManager {

    private final Map<String, ICover> mCoverMap;

    private final List<OnCoverAttachStateChangeListener> mCoverAttachStateChangeListeners;

    private final CoverValuePool mValuePool;

    public CoverManager() {
        this.mCoverMap = new HashMap<>(5);
        this.mCoverAttachStateChangeListeners = new ArrayList<>();
        this.mValuePool = new CoverValuePool(this);
    }

    /**
     * Add 覆盖组件
     *
     * @param key   key
     * @param cover cover
     */
    @Override
    public void addCover(String key, ICover cover) {
        // 设置key
        cover.setKey(key);
        // 绑定 覆盖组件管理器
        cover.bindManager(this);
        // 绑定 数据池
        cover.bindValuePool(mValuePool);
        // 回调 添加事件
        this.callBackCoverAttached(key, cover);
        // 存入集合
        this.mCoverMap.put(key, cover);
        // 恢复数据
        this.mValuePool.restore();
    }

    /**
     * Remove 覆盖组件
     *
     * @param key key
     */
    @Override
    public void removeCover(String key) {
        // 移除集合
        ICover cover = mCoverMap.remove(key);
        // 回调 移除事件
        if (cover != null) {
            this.callBackCoverDetached(key, cover);
        }
    }

    /**
     * 获取覆盖组件
     *
     * @param key key
     */
    @Override
    public ICover getCover(String key) {
        return mCoverMap.get(key);
    }

    /**
     * 获取所有覆盖组件
     */
    @Override
    public Map<String, ICover> getAllCover() {
        return mCoverMap;
    }

    /**
     * 清空覆盖组件
     */
    @Override
    public void clearCover() {
        // 回调 移除事件
        for (Map.Entry<String, ICover> entry : mCoverMap.entrySet()) {
            this.callBackCoverDetached(entry.getKey(), entry.getValue());
        }
        this.mCoverMap.clear();
    }

    /**
     * 获取 数据池
     *
     * @return {@link CoverValuePool}
     */
    @Override
    public CoverValuePool getValuePool() {
        return mValuePool;
    }

    /**
     * 清空 数据池
     */
    @Override
    public void clearValuePool() {
        this.mValuePool.clear();
    }

    /**
     * 循环
     *
     * @param onLoopListener onLoopListener
     */
    @Override
    public void forEach(OnLoopListener onLoopListener) {
        this.forEach(null, onLoopListener);
    }

    /**
     * 带有过滤器的循环
     *
     * @param filter         过滤器
     * @param onLoopListener onLoopListener
     */
    @Override
    public void forEach(OnCoverFilter filter, OnLoopListener onLoopListener) {
        for (Map.Entry<String, ICover> entry : mCoverMap.entrySet()) {
            if (filter == null || filter.filter(entry.getValue())) {
                onLoopListener.onEach(entry.getValue());
            }
        }
    }

    /**
     * 绑定 接收器添加移除事件
     *
     * @param coverAttachStateChangeListener listener
     */
    @Override
    public void addCoverAttachStateChangeListener(OnCoverAttachStateChangeListener coverAttachStateChangeListener) {
        if (mCoverAttachStateChangeListeners.contains(coverAttachStateChangeListener)) {
            return;
        }
        this.mCoverAttachStateChangeListeners.add(coverAttachStateChangeListener);
    }

    /**
     * 解绑 接收器添加移除事件
     *
     * @param coverAttachStateChangeListener listener
     */
    @Override
    public void removeCoverAttachStateChangeListener(OnCoverAttachStateChangeListener coverAttachStateChangeListener) {
        this.mCoverAttachStateChangeListeners.remove(coverAttachStateChangeListener);
    }

    /**
     * 回调 添加事件
     *
     * @param key   key
     * @param cover cover
     */
    private void callBackCoverAttached(String key, ICover cover) {
        for (OnCoverAttachStateChangeListener listener : mCoverAttachStateChangeListeners) {
            listener.onAttached(key, cover);
        }
    }

    /**
     * 回调 移除事件
     *
     * @param key   key
     * @param cover cover
     */
    private void callBackCoverDetached(String key, ICover cover) {
        for (OnCoverAttachStateChangeListener listener : mCoverAttachStateChangeListeners) {
            listener.onDetached(key, cover);
        }
    }
}

package com.ksg.ksgplayer.cache;

import android.text.TextUtils;
import android.util.LruCache;

import com.ksg.ksgplayer.widget.KsgAssistView;

/**
 * @ClassName: AssistCachePool
 * @Author: GaoXin
 * @CreateDate: 2023/1/5 11:08
 * @Description: Assist 缓存池
 */
public class AssistCachePool {

    private static final int MAX_SIZE = 10;

    private final LruCache<String, KsgAssistView> mLruCache;

    private static final class InstanceHolder {
        static final AssistCachePool instance = new AssistCachePool();
    }

    public static AssistCachePool getInstance() {
        return AssistCachePool.InstanceHolder.instance;
    }

    private AssistCachePool() {
        this.mLruCache = new LruCache<String, KsgAssistView>(MAX_SIZE * 4) {
            @Override
            protected int sizeOf(String key, KsgAssistView value) {
                return 4;
            }
        };
    }

    /**
     * 添加缓存
     *
     * @param uuid   唯一标识
     * @param assist 实例
     */
    public void addCache(String uuid, KsgAssistView assist) {
        if (TextUtils.isEmpty(uuid) || assist == null) return;
        this.mLruCache.put(uuid, assist);
    }

    /**
     * 获取缓存
     *
     * @param uuid 唯一标识
     * @return assist 实例
     */
    public KsgAssistView getCache(String uuid) {
        if (TextUtils.isEmpty(uuid)) return null;
        return mLruCache.get(uuid);
    }

    /**
     * 移除缓存
     */
    public void removeCache(String uuid) {
        if (TextUtils.isEmpty(uuid)) return;
        this.mLruCache.remove(uuid);
    }
}
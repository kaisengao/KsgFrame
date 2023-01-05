package com.ksg.ksgplayer.cache;

import android.util.LruCache;

/**
 * @ClassName: ProgressCache
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/25 10:21
 * @Description: 进度缓存
 */
public class ProgressCache {

    private static final int MAX_SIZE = 200;

    private final LruCache<String, Long> mLruCache;

    private static final class InstanceHolder {
        static final ProgressCache instance = new ProgressCache();
    }

    public static ProgressCache getInstance() {
        return InstanceHolder.instance;
    }

    private ProgressCache() {
        this.mLruCache = new LruCache<String, Long>(MAX_SIZE * 4) {
            @Override
            protected int sizeOf(String key, Long value) {
                return 4;
            }
        };
    }

    /**
     * 添加缓存
     */
    public void putCache(String url, long progress) {
        this.mLruCache.put(url, progress);
    }

    /**
     * 获取缓存
     *
     * @return progress
     */
    public long getCache(String url) {
        Long cache = mLruCache.get(url);
        return cache != null ? cache : 0;
    }
}
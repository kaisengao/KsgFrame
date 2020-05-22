package com.ksg.ksgplayer.cache.progress;

import android.text.TextUtils;
import android.util.LruCache;

/**
 * @ClassName: RecordCache
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:08
 * @Description: 播放内存位置记录, 使用LruCache。
 */
class RecordCache {

    private LruCache<String, Long> mLruCache;

    /**
     * 最大缓存数量
     */
    RecordCache(int maxCacheCount) {
        mLruCache = new LruCache<String, Long>(maxCacheCount * 4) {
            @Override
            protected int sizeOf(String key, Long value) {
                return 4;
            }
        };
    }

    /**
     * 添加 缓存
     */
    long putRecord(String key, long record) {
        Long aLong = mLruCache.put(key, record);
        return aLong != null ? aLong : 0;
    }

    /**
     * 删除 缓存
     */
    void removeRecord(String key) {
        mLruCache.remove(key);
    }

    /**
     * 获取 缓存
     */
    long getRecord(String key) {

        if (TextUtils.isEmpty(key)) {
            return 0;
        }

        Long aLong = mLruCache.get(key);
        return aLong != null ? aLong : 0;
    }

    /**
     * 清除 缓存
     */
    void clearRecord() {
        mLruCache.evictAll();
    }

}

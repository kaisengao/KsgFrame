package com.ksg.ksgplayer.record;

import android.text.TextUtils;
import android.util.LruCache;

/**
 * @author kaisengao
 * @create: 2019/1/15 16:39
 * @describe: 播放内存位置记录, 使用LruCache。
 */
class RecordCache {

    private LruCache<String, Integer> mLruCache;

    /**
     * 最大缓存数量
     */
    RecordCache(int maxCacheCount) {
        mLruCache = new LruCache<String, Integer>(maxCacheCount * 4) {
            @Override
            protected int sizeOf(String key, Integer value) {
                return 4;
            }
        };
    }

    /**
     * 添加 缓存
     */
    int putRecord(String key, int record) {
        Integer integer = mLruCache.put(key, record);
        return integer != null ? integer : 0;
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
    int getRecord(String key) {

        if (TextUtils.isEmpty(key)) {
            return 0;
        }

        Integer integer = mLruCache.get(key);
        return integer != null ? integer : 0;
    }

    /**
     * 清除 缓存
     */
    void clearRecord() {
        mLruCache.evictAll();
    }

}

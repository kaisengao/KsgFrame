package com.ksg.ksgplayer.cache.progress;

import android.text.TextUtils;

/**
 * @ClassName: PlayRecord
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:07
 * @Description: 记录播放进度
 */
public class PlayRecord {

    private static final int DEFAULT_MAX_RECORD_COUNT = 200;

    private static PlayRecord sPlayRecord;

    private RecordCache mRecordCache;

    /**
     * 双重检测单例模式
     */
    public static PlayRecord getInstance() {
        if (null == sPlayRecord) {
            synchronized (PlayRecord.class) {
                if (null == sPlayRecord) {
                    sPlayRecord = new PlayRecord();
                }
            }
        }
        return sPlayRecord;
    }

    private PlayRecord() {
        this.mRecordCache = new RecordCache(DEFAULT_MAX_RECORD_COUNT);
    }

    /**
     * 记录 播放进度
     *
     * @param dataSource      数据源
     * @param currentPosition 进度
     */
    void record(String dataSource, long currentPosition) {
        if (!TextUtils.isEmpty(dataSource)) {
            this.mRecordCache.putRecord(dataSource, currentPosition);
        }
    }

    /**
     * 重置 播放进度
     *
     * @param dataSource 数据源
     */
    void reset(String dataSource) {
        if (!TextUtils.isEmpty(dataSource)) {
            this.mRecordCache.putRecord(dataSource, 0L);
        }
    }

    /**
     * 获取播放记录
     *
     * @param dataSource 数据源
     */
    long getRecord(String dataSource) {
        if (!TextUtils.isEmpty(dataSource)) {
            return this.mRecordCache.getRecord(dataSource);
        }
        return 0;
    }

    /**
     * 清空播放记录
     */
    void clearRecord() {
        this.mRecordCache.clearRecord();
    }

    /**
     * 销毁
     */
    void destroy() {
        sPlayRecord = null;
    }
}

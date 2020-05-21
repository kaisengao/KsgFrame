package com.ksg.ksgplayer.record;

import com.ksg.ksgplayer.entity.DataSource;

/**
 * @author kaisengao
 * @create: 2019/1/15 15:55
 * @describe: 调用缓存程序
 */
class RecordInvoker {

    private RecordCache mRecordCache;

    RecordInvoker(RecordConfig config) {
        mRecordCache = new RecordCache(config.getMaxRecordCount());
    }

    /**
     * 存储播放缓存
     *
     * @param dataSource 数据源
     * @param record     播放节点
     */
    int saveRecord(DataSource dataSource, int record) {
        return mRecordCache.putRecord(getKey(dataSource), record);
    }

    /**
     * 获取播放缓存
     *
     * @param dataSource 数据源
     * @return 播放节点
     */
    int getRecord(DataSource dataSource) {
        return mRecordCache.getRecord(getKey(dataSource));
    }

    void resetRecord(DataSource dataSource) {
        int i = mRecordCache.putRecord(getKey(dataSource), 0);
    }

    void removeRecord(DataSource dataSource) {
        mRecordCache.removeRecord(getKey(dataSource));
    }

    void clearRecord() {
        mRecordCache.clearRecord();
    }

    private String getKey(DataSource dataSource) {
        return PlayRecordManager.getKey(dataSource);
    }
}

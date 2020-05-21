package com.ksg.ksgplayer.record;

/**
 * @author kaisengao
 * @create: 2019/1/15 17:08
 * @describe: 记录（缓存）配置
 */
class RecordConfig {

    private static final int DEFAULT_MAX_RECORD_COUNT = 200;

    /**
     * 最大缓存数
     */
    private int maxRecordCount = DEFAULT_MAX_RECORD_COUNT;
    /**
     * 生成唯一Key
     */
    private RecordKeyProvider recordKeyProvider;

    public RecordConfig setMaxRecordCount() {
        return this;
    }

    public RecordConfig setMaxRecordCount(int maxRecordCount) {
        this.maxRecordCount = maxRecordCount;
        return this;
    }

    public int getMaxRecordCount() {
        return maxRecordCount;
    }

    public RecordKeyProvider getRecordKeyProvider() {
        return recordKeyProvider;
    }

    public RecordConfig setRecordKeyProvider(RecordKeyProvider recordKeyProvider) {
        this.recordKeyProvider = recordKeyProvider;
        return this;
    }
}

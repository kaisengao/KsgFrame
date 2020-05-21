package com.ksg.ksgplayer.record;

import com.ksg.ksgplayer.entity.DataSource;

/**
 * @author kaisengao
 * @create: 2019/1/15 15:43
 * @describe: 播放记录
 */
public class PlayRecord {

    private static PlayRecord sPlayRecord;

    private RecordInvoker mRecordInvoker;

    private PlayRecord() {
        mRecordInvoker = new RecordInvoker(PlayRecordManager.getConfig());
    }

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

    /**
     * 记录
     *
     * @param data   数据源
     * @param record 记录
     */
    void record(DataSource data, int record) {
        if (data == null) {
            return;
        }
        int i = mRecordInvoker.saveRecord(data, record);
    }

    /**
     * 重置
     *
     * @param data 数据源
     */
    void reset(DataSource data) {
        if (data == null) {
            return;
        }
        mRecordInvoker.resetRecord(data);
    }

    /**
     * 删除记录
     *
     * @param data 数据源
     */
    void removeRecord(DataSource data) {
        if (data == null) {
            return;
        }
        mRecordInvoker.removeRecord(data);
    }

    /**
     * 获取播放记录
     *
     * @param data 数据源
     */
    int getRecord(DataSource data) {
        if (data == null) {
            return 0;
        }
        return mRecordInvoker.getRecord(data);
    }

    /**
     * 清空播放记录
     */
    void clearRecord() {
        mRecordInvoker.clearRecord();
    }

    /**
     * 销毁
     */
    void destroy() {
        sPlayRecord = null;
    }
}

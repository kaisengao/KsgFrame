package com.ksg.ksgplayer.record;

import com.ksg.ksgplayer.entity.DataSource;

/**
 * @author kaisengao
 * @create: 2019/1/15 17:00
 * @describe: 用户缓存配置
 */
public class PlayRecordManager {

    private static RecordKeyProvider sRecordKeyProvider;

    private static RecordConfig sRecordConfig;

    public static void setRecordConfig(RecordConfig recordConfig) {
        sRecordConfig = recordConfig;
        checkDefaultConfig();
        sRecordKeyProvider = sRecordConfig.getRecordKeyProvider();
    }

    private static void checkDefaultConfig() {
        if (sRecordConfig == null) {
            sRecordConfig = new RecordConfig()
                    .setMaxRecordCount()
                    .setRecordKeyProvider(new DefaultRecordKeyProvider());
        }
    }

    static RecordConfig getConfig() {
        checkDefaultConfig();
        return sRecordConfig;
    }

    /**
     * 生成key
     */
    private static RecordKeyProvider getRecordKeyProvider() {
        if (sRecordKeyProvider == null) {
            return new DefaultRecordKeyProvider();
        }
        return sRecordKeyProvider;
    }

    /**
     * 获取Key
     */
    static String getKey(DataSource dataSource) {
        return getRecordKeyProvider().generatorKey(dataSource);
    }

    /**
     * 删除记录
     *
     * @param dataSource 数据源
     */
    public static void removeRecord(DataSource dataSource) {
        PlayRecord.getInstance().removeRecord(dataSource);
    }

    /**
     * 清空缓存记录
     */
    public static void clearRecord() {
        PlayRecord.getInstance().clearRecord();
    }

    /**
     * 摧毁内存缓存
     */
    public static void destroyCache() {
        PlayRecord.getInstance().destroy();
    }
}

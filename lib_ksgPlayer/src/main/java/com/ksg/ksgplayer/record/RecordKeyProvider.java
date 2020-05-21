package com.ksg.ksgplayer.record;

import com.ksg.ksgplayer.entity.DataSource;

/**
 * @author kaisengao
 * @create: 2019/1/15 17:06
 * @describe: 生成缓存Key
 */
public interface RecordKeyProvider {

    /**
     * 缓存唯一key
     *
     * @param dataSource 数据源
     * @return key
     */
    String generatorKey(DataSource dataSource);
}

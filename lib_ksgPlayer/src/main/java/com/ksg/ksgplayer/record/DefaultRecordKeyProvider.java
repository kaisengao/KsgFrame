package com.ksg.ksgplayer.record;

import android.text.TextUtils;

import com.ksg.ksgplayer.entity.DataSource;

/**
 * @author kaisengao
 * @create: 2019/1/15 17:16
 * @describe: 生成key
 */
public class DefaultRecordKeyProvider implements RecordKeyProvider {

    @Override
    public String generatorKey(DataSource dataSource) {
        String data = dataSource.getUrl();
        if (!TextUtils.isEmpty(data)) {
            return data;
        }
        return null;
    }
}

package com.ksg.ksgplayer.cover;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: CoverValue
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/24 16:12
 * @Description: 共享数据池
 */
public class CoverValuePool implements ICoverValue {

    private final Map<String, Object> mValuePool;

    private final CoverManager mCoverManager;

    public CoverValuePool(CoverManager coverManager) {
        this.mCoverManager = coverManager;
        this.mValuePool = new HashMap<>();
    }

    public void restore() {
        Map<String, ICover> allCover = mCoverManager.getAllCover();
        if (allCover != null && !allCover.isEmpty()) {
            for (Map.Entry<String, ICover> coverEntry : allCover.entrySet()) {
                ICover cover = coverEntry.getValue();
                String[] filters = cover.getValueFilters();
                if (filters != null && filters.length > 0) {
                    if (!mValuePool.isEmpty()) {
                        for (Map.Entry<String, Object> valueEntry : mValuePool.entrySet()) {
                            for (String filter : filters) {
                                if (filter.equals(valueEntry.getKey())) {
                                    cover.onValueEvent(valueEntry.getKey(), valueEntry.getValue());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void putObject(String key, Object value) {
        put(key, value, true, true);
    }

    @Override
    public void putObject(String key, Object value, boolean cache) {
        put(key, value, cache);
    }

    @Override
    public void putObject(String key, Object value, boolean cache, boolean notify) {
        put(key, value, cache, notify);
    }

    private void put(String key, Object value, boolean cache) {
        put(key, value, cache, true);
    }

    private void put(String key, Object value, boolean cache, boolean notify) {
        // 缓存
        if (cache) {
            this.mValuePool.put(key, value);
        }
        // 通知
        if (notify) {
            Map<String, ICover> allCover = mCoverManager.getAllCover();
            if (allCover != null && !allCover.isEmpty()) {
                for (Map.Entry<String, ICover> coverEntry : allCover.entrySet()) {
                    ICover cover = coverEntry.getValue();
                    String[] filters = cover.getValueFilters();
                    if (filters != null && filters.length > 0) {
                        for (String filter : filters) {
                            if (filter.equals(key)) {
                                cover.onValueEvent(key, value);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    @Override
    public Boolean getBoolean(String key, boolean defaultValue) {
        Boolean aBoolean = get(key);
        if (aBoolean == null) {
            return defaultValue;
        }
        return aBoolean;
    }

    @Override
    public Integer getInt(String key) {
        return getInt(key, 0);
    }

    @Override
    public Integer getInt(String key, int defaultValue) {
        Integer integer = get(key);
        if (integer == null) {
            return defaultValue;
        }
        return integer;
    }

    @Override
    public String getString(String key) {
        return get(key);
    }

    @Override
    public Float getFloat(String key) {
        return getFloat(key, 0);
    }

    @Override
    public Float getFloat(String key, float defaultValue) {
        Float aFloat = get(key);
        if (aFloat == null) {
            return defaultValue;
        }
        return aFloat;
    }

    @Override
    public Long getLong(String key) {
        return getLong(key, 0);
    }

    @Override
    public Long getLong(String key, long defaultValue) {
        Long aLong = get(key);
        if (aLong == null) {
            return defaultValue;
        }
        return aLong;
    }

    @Override
    public Double getDouble(String key) {
        return getDouble(key, 0);
    }

    @Override
    public Double getDouble(String key, double defaultValue) {
        Double aDouble = get(key);
        if (aDouble == null) {
            return defaultValue;
        }
        return aDouble;
    }

    /**
     * 根据key获取value
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object o = mValuePool.get(key);
        try {
            if (o != null) {
                return (T) o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 清空
     */
    public void clear() {
        this.mValuePool.clear();
    }
}
package com.ksg.ksgplayer.cover;

/**
 * @ClassName: CoverValue
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/24 16:10
 * @Description: 共享数据池
 */
public interface ICoverValue {


    /**
     * 发送Object类型
     *
     * @param key   key
     * @param value value
     * @param cache 缓存
     */
    void putObject(String key, Object value);

    /**
     * 发送Object类型
     *
     * @param key   key
     * @param value value
     * @param cache 缓存
     */
    void putObject(String key, Object value, boolean cache);

    /**
     * 发送Object类型
     *
     * @param key    key
     * @param value  value
     * @param cache  缓存
     * @param notify 通知
     */
    void putObject(String key, Object value, boolean cache, boolean notify);

    /**
     * 根据 key  获取  boolean类型
     *
     * @param key key
     * @return true/false
     */
    Boolean getBoolean(String key);

    /**
     * 根据 key  获取  boolean类型
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return true/false
     */
    Boolean getBoolean(String key, boolean defaultValue);

    /**
     * 根据 key  获取  Int类型
     *
     * @param key key
     * @return Int
     */
    Integer getInt(String key);

    /**
     * 根据 key  获取  Int类型
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return Int
     */
    Integer getInt(String key, int defaultValue);

    /**
     * 根据 key  获取 String类型
     *
     * @param key key
     * @return String
     */
    String getString(String key);

    /**
     * 根据 key  获取  Float类型
     *
     * @param key key
     * @return Float
     */
    Float getFloat(String key);

    /**
     * 根据 key  获取  Float类型
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return Float
     */
    Float getFloat(String key, float defaultValue);

    /**
     * 根据 key  获取  Long类型
     *
     * @param key key
     * @return Long
     */
    Long getLong(String key);

    /**
     * 根据 key  获取  Long类型
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return Long
     */
    Long getLong(String key, long defaultValue);

    /**
     * 根据 key  获取  Double类型
     *
     * @param key key
     * @return Double
     */
    Double getDouble(String key);

    /**
     * 根据 key  获取  Double类型
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return Double
     */
    Double getDouble(String key, double defaultValue);

    /**
     * 根据Key  获取 T
     *
     * @param key key
     * @return T
     */
    <T> T get(String key);

}
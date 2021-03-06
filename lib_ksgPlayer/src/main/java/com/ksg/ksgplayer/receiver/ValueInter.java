package com.ksg.ksgplayer.receiver;

/**
 * @author kaisengao
 * @create: 2019/1/29 15:09
 * @describe: 组共享数据池 发送数据 or 获取数据
 */
public interface ValueInter {

    /**
     * 发送Boolean类型
     *
     * @param key   key
     * @param value value
     */
    void putBoolean(String key, boolean value);

    /**
     * 发送Boolean类型
     *
     * @param key          key
     * @param value        value
     * @param notifyUpdate 通知更新
     */
    void putBoolean(String key, boolean value, boolean notifyUpdate);

    /**
     * 发送Int类型
     *
     * @param key   key
     * @param value value
     */
    void putInt(String key, int value);

    /**
     * 发送Int类型
     *
     * @param key          key
     * @param value        value
     * @param notifyUpdate 通知更新
     */
    void putInt(String key, int value, boolean notifyUpdate);

    /**
     * 发送String类型
     *
     * @param key   key
     * @param value value
     */
    void putString(String key, String value);

    /**
     * 发送String类型
     *
     * @param key          key
     * @param value        value
     * @param notifyUpdate 通知更新
     */
    void putString(String key, String value, boolean notifyUpdate);

    /**
     * 发送Float类型
     *
     * @param key   key
     * @param value value
     */
    void putFloat(String key, float value);

    /**
     * 发送Float类型
     *
     * @param key          key
     * @param value        value
     * @param notifyUpdate 通知更新
     */
    void putFloat(String key, float value, boolean notifyUpdate);

    /**
     * 发送Long类型
     *
     * @param key   key
     * @param value value
     */
    void putLong(String key, long value);

    /**
     * 发送Long类型
     *
     * @param key          key
     * @param value        value
     * @param notifyUpdate 通知更新
     */
    void putLong(String key, long value, boolean notifyUpdate);

    /**
     * 发送Double类型
     *
     * @param key   key
     * @param value value
     */
    void putDouble(String key, double value);

    /**
     * 发送Double类型
     *
     * @param key          key
     * @param value        value
     * @param notifyUpdate 通知更新
     */
    void putDouble(String key, double value, boolean notifyUpdate);

    /**
     * 发送Object类型
     *
     * @param key   key
     * @param value value
     */
    void putObject(String key, Object value);

    /**
     * 发送Object类型
     *
     * @param key          key
     * @param value        value
     * @param notifyUpdate 通知更新
     */
    void putObject(String key, Object value, boolean notifyUpdate);


    /**
     * 根据Key  获取 T
     *
     * @param key key
     * @return T
     */
    <T> T get(String key);

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

}

package com.ksg.ksgplayer.receiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author kaisengao
 * @create: 2019/1/29 15:18
 * @describe: 组共享数据池，组内的所有Receiver共享。通过GroupValue您可以写入数据也可以获取数据，您还可以设置监听器去监听某些值的更新
 * <p>
 * 默认的Put数据将回调ValueUpdateListeners
 */
public final class GroupValue implements ValueInter {

    /**
     * 组件内容value值 存储map集合
     */
    private Map<String, Object> mValueMap;

    /**
     * 组件内容更新事件 组件过滤key 存储map集合
     */
    private Map<IReceiverGroup.OnGroupValueUpdateListener, String[]> mListenerKeys;

    /**
     * 组件内容更新事件集合
     */
    private List<IReceiverGroup.OnGroupValueUpdateListener> mOnGroupValueUpdateListeners;

    GroupValue() {
        // 并发容器ConcurrentHashMap  (详细了解请指向 https://www.baidu.com)
        mValueMap = new ConcurrentHashMap<>();
        mListenerKeys = new ConcurrentHashMap<>();
        // 线程安全的CopyOnWriteArrayList  (详细了解请指向 https://www.baidu.com)
        mOnGroupValueUpdateListeners = new CopyOnWriteArrayList<>();
    }

    /**
     * 如果你想监听一些数据的变化，你可以注册一个侦听器来实现它
     *
     * @param onGroupValueUpdateListener key
     */
    public void registerOnGroupValueUpdateListener(IReceiverGroup.OnGroupValueUpdateListener onGroupValueUpdateListener) {
        // 判断是否添加过此事件
        if (mOnGroupValueUpdateListeners.contains(onGroupValueUpdateListener)) {
            return;
        }
        // 将事件添加入集合
        mOnGroupValueUpdateListeners.add(onGroupValueUpdateListener);
        // 获取事件的 过滤key
        String[] keyArrays = onGroupValueUpdateListener.filterKeys();
        // 排序
        Arrays.sort(keyArrays);
        // 将key 和 事件存入Map集合中
        mListenerKeys.put(onGroupValueUpdateListener, keyArrays);
        // 当监听器添加时，如果用户观察到当前密钥集中的密钥，请将其调用
        checkCurrentKeySet(onGroupValueUpdateListener);
    }

    private void checkCurrentKeySet(IReceiverGroup.OnGroupValueUpdateListener onGroupValueUpdateListener) {
        // 获取组件内容集合 key
        Set<String> keys = mValueMap.keySet();
        // 循环查找对应value值
        for (String key : keys) {
            if (containsKey(mListenerKeys.get(onGroupValueUpdateListener), key)) {
                onGroupValueUpdateListener.onValueUpdate(key, mValueMap.get(key));
            }
        }
    }

    /**
     * 移除监听器
     */
    public void unregisterOnGroupValueUpdateListener(IReceiverGroup.OnGroupValueUpdateListener onGroupValueUpdateListener) {
        mListenerKeys.remove(onGroupValueUpdateListener);
        mOnGroupValueUpdateListeners.remove(onGroupValueUpdateListener);
    }

    /**
     * 清空监听器集合
     */
    public void clearOnGroupValueUpdateListeners() {
        mOnGroupValueUpdateListeners.clear();
    }

    /**
     * 清空value集合
     */
    public void clearValues() {
        mValueMap.clear();
    }

    @Override
    public void putBoolean(String key, boolean value) {
        put(key, value);
    }

    @Override
    public void putBoolean(String key, boolean value, boolean notifyUpdate) {
        put(key, value, notifyUpdate);
    }

    @Override
    public void putInt(String key, int value) {
        put(key, value);
    }

    @Override
    public void putInt(String key, int value, boolean notifyUpdate) {
        put(key, value, notifyUpdate);
    }

    @Override
    public void putString(String key, String value) {
        put(key, value);
    }

    @Override
    public void putString(String key, String value, boolean notifyUpdate) {
        put(key, value, notifyUpdate);
    }

    @Override
    public void putFloat(String key, float value) {
        put(key, value);
    }

    @Override
    public void putFloat(String key, float value, boolean notifyUpdate) {
        put(key, value, notifyUpdate);
    }

    @Override
    public void putLong(String key, long value) {
        put(key, value);
    }

    @Override
    public void putLong(String key, long value, boolean notifyUpdate) {
        put(key, value, notifyUpdate);
    }

    @Override
    public void putDouble(String key, double value) {
        put(key, value);
    }

    @Override
    public void putDouble(String key, double value, boolean notifyUpdate) {
        put(key, value, notifyUpdate);
    }

    @Override
    public void putObject(String key, Object value) {
        put(key, value);
    }

    @Override
    public void putObject(String key, Object value, boolean notifyUpdate) {
        put(key, value, notifyUpdate);
    }

    private void put(String key, Object value) {
        put(key, value, true);
    }

    private void put(String key, Object value, boolean notifyUpdate) {
        mValueMap.put(key, value);
        if (notifyUpdate) {
            callBackValueUpdate(key, value);
        }
    }

    private void callBackValueUpdate(String key, Object value) {
        List<IReceiverGroup.OnGroupValueUpdateListener> mCallbacks = new ArrayList<>();
        // 筛选回调
        for (IReceiverGroup.OnGroupValueUpdateListener listener : mOnGroupValueUpdateListeners) {
            if (containsKey(mListenerKeys.get(listener), key)) {
                mCallbacks.add(listener);
            }
        }
        // 回调
        for (IReceiverGroup.OnGroupValueUpdateListener callback : mCallbacks) {
            callback.onValueUpdate(key, value);
        }
    }

    /**
     * 利用Arrays.binarySearch二分法来查找数据
     *
     * @param keys   key
     * @param nowKey nowKey
     */
    private boolean containsKey(String[] keys, String nowKey) {
        if (keys != null && keys.length > 0) {
            return Arrays.binarySearch(keys, nowKey) >= 0;
        }
        return false;
    }

    /**
     * 根据key获取value
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object o = mValueMap.get(key);
        try {
            if (o != null) {
                return (T) o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
}

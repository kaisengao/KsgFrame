package com.ksg.ksgplayer.receiver;

import android.os.Bundle;

/**
 * @author kaisengao
 * @create: 2019/1/29 16:53
 * @describe: 请求控制
 */
public interface ICoverHandle {

    /**
     * 请求 发送其他事件
     *
     * @param bundle bundle
     */
    void requestOption(Bundle bundle);

    /**
     * 请求 播放
     *
     * @param bundle bundle
     */
    void requestStart(Bundle bundle);

    /**
     * 请求 暂停
     *
     * @param bundle bundle
     */
    void requestPause(Bundle bundle);

    /**
     * 请求 恢复播放
     *
     * @param bundle bundle
     */
    void requestResume(Bundle bundle);

    /**
     * 请求 指定位置播放
     *
     * @param bundle bundle
     */
    void requestSeek(Bundle bundle);

    /**
     * 请求 停止播放
     *
     * @param bundle bundle
     */
    void requestStop(Bundle bundle);

    /**
     * 请求 重置
     *
     * @param bundle bundle
     */
    void requestReset(Bundle bundle);

    /**
     * 请求 重试
     *
     * @param bundle bundle
     */
    void requestRetry(Bundle bundle);

    /**
     * 请求 重播
     *
     * @param bundle bundle
     */
    void requestReplay(Bundle bundle);

    /**
     * 请求 设置数据源
     *
     * @param bundle bundle
     */
    void requestPlayDataSource(Bundle bundle);

    /**
     * 请求 添加事件生产者
     *
     * @param bundle bundle
     */
    void requestAddEventProducer(Bundle bundle);

    /**
     * 请求 删除事件生产者
     *
     * @param bundle bundle
     */
    void requestRemoveEventProducer(Bundle bundle);

    void requestNotifyTimer();

    void requestStopTimer();
}

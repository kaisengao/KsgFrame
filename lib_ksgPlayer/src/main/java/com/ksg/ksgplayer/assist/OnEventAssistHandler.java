package com.ksg.ksgplayer.assist;

import android.os.Bundle;

/**
 * @author kaisengao
 * @create: 2019/3/22 15:20
 * @describe: 此接口用于处理基本播放
 * <p>
 * *调用方发出的操作事件。比如暂停，
 * <p>
 * *快速前进和其他操作。
 * <p>
 * *
 * <p>
 * *@param<t> 播放主控制器，可能是ksgPlayer其他继承类
 */
public interface OnEventAssistHandler<T> {

    /**
     * 事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onAssistHandle(int eventCode, Bundle bundle);

    /**
     * 自定义 通知
     *
     * @param bundle bundle
     */
    void requestOption(Bundle bundle);

    /**
     * 播放
     *
     * @param bundle bundle
     */
    void requestStart(Bundle bundle);

    /**
     * 暂停
     *
     * @param bundle bundle
     */
    void requestPause(Bundle bundle);

    /**
     * 继续播放
     *
     * @param bundle bundle
     */
    void requestResume(Bundle bundle);

    /**
     * Seek
     *
     * @param bundle bundle
     */
    void requestSeek(Bundle bundle);

    /**
     * 停止
     *
     * @param bundle bundle
     */
    void requestStop(Bundle bundle);

    /**
     * 重置播放器
     *
     * @param bundle bundle
     */
    void requestReset(Bundle bundle);

    /**
     * 重新播放
     *
     * @param bundle bundle
     */
    void requestReplay(Bundle bundle);

    /**
     * 重新播放 新的数据源
     *
     * @param bundle bundle
     */
    void requestPlayDataSource(Bundle bundle);

}

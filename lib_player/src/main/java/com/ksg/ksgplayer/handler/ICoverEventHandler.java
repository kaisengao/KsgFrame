package com.ksg.ksgplayer.handler;

import android.os.Bundle;

/**
 * @ClassName: OnCoverEventHandler
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/1 17:20
 * @Description: Cover事件处理程序
 */
public interface ICoverEventHandler {

    /**
     * 事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    void onHandle(int eventCode, Bundle bundle);

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
     * 跳转进度
     *
     * @param bundle bundle
     */
    void requestSeekTo(Bundle bundle);

    /**
     * 停止播放
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

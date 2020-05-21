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

    void onAssistHandle(T assist, int eventCode, Bundle bundle);

    void requestOption(T assist, Bundle bundle);

    void requestStart(T assist, Bundle bundle);

    void requestPause(T assist, Bundle bundle);

    void requestResume(T assist, Bundle bundle);

    void requestSeek(T assist, Bundle bundle);

    void requestStop(T assist, Bundle bundle);

    void requestReset(T assist, Bundle bundle);

    void requestRetry(T assist, Bundle bundle);

    void requestReplay(T assist, Bundle bundle);

    void requestPlayDataSource(T assist, Bundle bundle);

}

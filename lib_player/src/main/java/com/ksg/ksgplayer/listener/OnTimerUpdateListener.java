package com.ksg.ksgplayer.listener;

/**
 * @ClassName: OnTimerUpdateListener
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 14:55
 * @Description:
 */
public interface OnTimerUpdateListener {

    /**
     * @param curr             当前进度
     * @param duration         总进度
     * @param bufferPercentage 缓冲进度
     */
    void onTimerUpdate(long curr, long duration, long bufferPercentage);
}

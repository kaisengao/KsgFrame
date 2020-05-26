package com.ksg.ksgplayer.listener;

public interface OnTimerUpdateListener {

    /**
     * @param curr             当前进度
     * @param duration         总进度
     * @param bufferPercentage 缓冲进度
     */
    void onTimerUpdate(long curr, long duration, long bufferPercentage);
}

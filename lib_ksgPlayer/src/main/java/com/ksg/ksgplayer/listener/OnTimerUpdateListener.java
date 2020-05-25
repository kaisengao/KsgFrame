package com.ksg.ksgplayer.listener;

public interface OnTimerUpdateListener {

    void onTimerUpdate(long curr, long duration, long bufferPercentage);
}

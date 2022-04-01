package com.ksg.ksgplayer.proxy;

import android.os.Bundle;

import com.ksg.ksgplayer.config.WeakHandler;
import com.ksg.ksgplayer.listener.OnPlayerListener;

/**
 * @ClassName: TimerCounterProxy
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/25 10:42
 * @Description: 进度更新计时代理
 */
public class TimerCounterProxy {

    private static final int DEFAULT_TIMER = 1000;

    private int mCounterInterval;

    private WeakHandler mWeakHandler = new WeakHandler();

    private OnTimerCounterListener mTimerCounterListener;

    /**
     * @param counterIntervalMs 计时单位 毫秒
     */
    public TimerCounterProxy(int counterIntervalMs) {
        this.mCounterInterval = counterIntervalMs;
    }

    /**
     * 倍速
     */
    public void setSpeed(float speed) {
        if (speed < 1) {
            this.mCounterInterval = (int) (DEFAULT_TIMER + (speed * 1000f));
        } else if (speed > 1) {
            this.mCounterInterval = (int) (DEFAULT_TIMER - ((speed * 1000f) - DEFAULT_TIMER));
        } else {
            this.mCounterInterval = DEFAULT_TIMER;
        }
    }

    /**
     * 计时更新通知
     *
     * @param timerCounterListener onCounterUpdateListener
     */
    public void setTimerCounterListener(OnTimerCounterListener timerCounterListener) {
        this.mTimerCounterListener = timerCounterListener;
    }

    /**
     * 代理 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    public void proxyPlayEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
            case OnPlayerListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
            case OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_START:
            case OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_END:
            case OnPlayerListener.PLAYER_EVENT_ON_PAUSE:
            case OnPlayerListener.PLAYER_EVENT_ON_RESUME:
            case OnPlayerListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                // 开启
                this.start();
                break;
            case OnPlayerListener.PLAYER_EVENT_ON_STOP:
            case OnPlayerListener.PLAYER_EVENT_ON_RESET:
            case OnPlayerListener.PLAYER_EVENT_ON_DESTROY:
            case OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                // 取消
                this.stop();
                break;
        }
    }

    /**
     * 代理 失败事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    public void proxyErrorEvent(int eventCode, Bundle bundle) {
        // 取消计时
        this.stop();
    }

    /**
     * 开启
     */
    public void start() {
        this.stop();
        this.mWeakHandler.post(loop);
    }

    /**
     * 循环计时
     */
    private void loopNext() {
        this.mWeakHandler.postDelayed(loop, mCounterInterval);
    }

    /**
     * 取消
     */
    public void stop() {
        this.mWeakHandler.removeCallbacks(loop);
    }

    /**
     * Loop
     */
    private final Runnable loop = new Runnable() {
        @Override
        public void run() {
            // 计时更新通知
            if (mTimerCounterListener != null) {
                mTimerCounterListener.onCounter();
            }
            // 循环计时
            loopNext();
        }
    };

    /**
     * 销毁资源
     */
    public void destroy() {
        this.stop();
        this.mWeakHandler = null;
    }

    /**
     * 计时事件
     */
    public interface OnTimerCounterListener {

        void onCounter();
    }
}
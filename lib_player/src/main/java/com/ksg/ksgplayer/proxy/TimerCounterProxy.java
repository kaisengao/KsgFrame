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

    /**
     * 计时单位 毫秒
     */
    private final int mCounterInterval;

    /**
     * 代理状态 默认开启
     */
    private boolean mUseProxy = true;

    private WeakHandler mWeakHandler = new WeakHandler();

    private OnTimerCounterListener mTimerCounterListener;

    /**
     * @param counterIntervalMs 计时单位 毫秒
     */
    public TimerCounterProxy(int counterIntervalMs) {
        this.mCounterInterval = counterIntervalMs;
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
     * 代理状态
     *
     * @param useProxy 开/关
     */
    public void setUseProxy(boolean useProxy) {
        this.mUseProxy = useProxy;
        if (!useProxy) {
            this.stop();
        } else {
            this.start();
        }
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
                if (!mUseProxy)
                    return;
                // 开启计时
                this.start();
                break;
            case OnPlayerListener.PLAYER_EVENT_ON_STOP:
            case OnPlayerListener.PLAYER_EVENT_ON_RESET:
            case OnPlayerListener.PLAYER_EVENT_ON_DESTROY:
            case OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                // 取消计时
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
     * 开启计时
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
     * 取消计时
     */
    public void stop() {
        this.mWeakHandler.removeCallbacks(loop);
    }

    /**
     * Runnable
     */
    private final Runnable loop = new Runnable() {
        @Override
        public void run() {
            // 验证是否开放了计时器
            if (!mUseProxy) {
                return;
            }
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
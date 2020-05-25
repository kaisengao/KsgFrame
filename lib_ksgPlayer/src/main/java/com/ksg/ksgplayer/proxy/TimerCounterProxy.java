package com.ksg.ksgplayer.proxy;

import android.os.Bundle;

import com.kasiengao.base.configure.WeakHandler;
import com.kasiengao.base.util.KLog;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;

/**
 * @ClassName: TimerCounterProxy
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 13:11
 * @Description: 进度更新计时代理
 */
public class TimerCounterProxy {

    /**
     * 计时单位 毫秒
     */
    private int mCounterInterval;

    /**
     * 代理状态 默认开启
     */
    private boolean mUseProxy = true;

    /**
     * Handler
     */
    private WeakHandler mWeakHandler = new WeakHandler();

    private OnCounterUpdateListener mOnCounterUpdateListener;

    /**
     * @param counterIntervalMs 计时单位 毫秒
     */
    public TimerCounterProxy(int counterIntervalMs) {
        this.mCounterInterval = counterIntervalMs;
    }

    /**
     * 代理状态
     *
     * @param useProxy 开/关
     */
    public void setUseProxy(boolean useProxy) {
        this.mUseProxy = useProxy;
        if (!useProxy) {
            cancel();
            KLog.e("TimerCounterProxy", "Timer Stopped");
        } else {
            start();
            KLog.e("TimerCounterPoxy", "Timer Started");
        }
    }

    /**
     * 计时更新通知
     *
     * @param onCounterUpdateListener onCounterUpdateListener
     */
    public void setOnCounterUpdateListener(OnCounterUpdateListener onCounterUpdateListener) {
        this.mOnCounterUpdateListener = onCounterUpdateListener;
    }

    /**
     * 代理 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    public void proxyPlayEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END:
            case OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE:
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESUME:
            case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                if (!mUseProxy)
                    return;
                // 开启计时
                this.start();
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_STOP:
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESET:
            case OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY:
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                // 取消计时
                this.cancel();
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
        this.cancel();
    }

    /**
     * 开启计时
     */
    public void start() {
        this.removeMessage();
        this.mWeakHandler.post(mRunnable);
    }

    /**
     * 循环计时
     */
    private void loopNext() {
        this.removeMessage();
        this.mWeakHandler.postDelayed(mRunnable, mCounterInterval);
    }

    /**
     * 取消计时
     */
    public void cancel() {
        this.removeMessage();
    }

    /**
     * 移除计时消息
     */
    private void removeMessage() {
        this.mWeakHandler.removeCallbacks(mRunnable);
    }

    /**
     * Runnable
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // 验证是否开放了计时器
            if (!mUseProxy) {
                return;
            }
            // 计时更新通知
            if (mOnCounterUpdateListener != null) {
                mOnCounterUpdateListener.onCounter();
            }
            // 循环计时
            loopNext();
        }
    };

    public interface OnCounterUpdateListener {

        void onCounter();
    }
}

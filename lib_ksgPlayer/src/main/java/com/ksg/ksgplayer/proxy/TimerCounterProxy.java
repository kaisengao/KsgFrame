package com.ksg.ksgplayer.proxy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.ksg.ksgplayer.listener.OnCounterUpdateListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;

/**
 * @author kaisengao
 * @create: 2019/1/7 15:17
 * @describe: 定时计数代理
 */
public class TimerCounterProxy {

    private final int MSG_CODE_COUNTER = 1;

    /**
     * 计数器间隔时间
     */
    private int counterInterval;

    /**
     * 代理的状态,默认使用
     */
    private boolean useProxy = true;

    /**
     * 实例弱引用Handler
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CODE_COUNTER:
                    if (!useProxy) {
                        return;
                    }
                    if (onCounterUpdateListener != null) {
                        onCounterUpdateListener.onCounter();
                    }
                    loopNext();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 计时器通知回调
     */
    private OnCounterUpdateListener onCounterUpdateListener;

    /**
     * 设置间隔时长
     */
    public TimerCounterProxy(int counterInterval) {
        this.counterInterval = counterInterval;
    }

    /**
     * 设置代理状态
     */
    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
        if (!useProxy) {
            cancel();
            Log.e("TimerCounterProxy", "Timer Stopped");
        } else {
            start();
            Log.e("TimerCounterProxy", "Timer Started");
        }
    }

    /**
     * 设置回调
     */
    public void setOnCounterUpdateListener(OnCounterUpdateListener onCounterUpdateListener) {
        this.onCounterUpdateListener = onCounterUpdateListener;
    }

    /**
     * 计时器播放状态 开始计时
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
                if (!useProxy) {
                    return;
                }
                start();
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_STOP:
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESET:
            case OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY:
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                cancel();
                break;
            default:
                break;
        }
    }

    /**
     * 开始计时
     */
    public void start() {
        removeMessage();
        mHandler.sendEmptyMessage(MSG_CODE_COUNTER);
    }

    /**
     * 轮询计时
     */
    private void loopNext() {
        removeMessage();
        mHandler.sendEmptyMessageDelayed(MSG_CODE_COUNTER, counterInterval);
    }

    /**
     * 取消计时
     */
    public void cancel() {
        removeMessage();
    }

    /**
     * 取消发送handler
     */
    private void removeMessage() {
        mHandler.removeMessages(MSG_CODE_COUNTER);
    }

    /**
     * 开启线程 计时
     */
    private final Runnable mTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (!useProxy) {
                return;
            }
            if (onCounterUpdateListener != null) {
                onCounterUpdateListener.onCounter();
            }
            loopNext();
        }
    };
}

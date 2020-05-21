package com.ksg.ksgplayer.player;

import android.os.Bundle;

import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;

/**
 * @author kaisengao
 * @create: 2019/1/7 15:43
 * @describe: 基类的解码方案 解码器需要访问这个类继承
 */
public abstract class BaseInternalPlayer implements IKsgPlayer {

    /**
     * 当前播放器状态
     */
    private int mCurrentState = STATE_IDLE;

    /**
     * 缓冲进度
     */
    private int mBuffer;

    /**
     * 播放器状态事件
     */
    private OnPlayerEventListener mOnPlayerEventListener;

    /**
     * 播放器错误事件
     */
    private OnErrorEventListener mOnErrorEventListener;

    @Override
    public void option(int code, Bundle bundle) {
        // 不处理
    }

    @Override
    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    @Override
    public void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        this.mOnErrorEventListener = onErrorEventListener;
    }

    /**
     * 发送播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    protected final void submitPlayerEvent(int eventCode, Bundle bundle) {
        if (mOnPlayerEventListener != null) {
            mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
        }
    }

    /**
     * 发送错误事件
     */
    protected final void submitErrorEvent(int eventCode, String error) {
        if (mOnErrorEventListener != null) {
            Bundle bundle = BundlePool.obtain();
            bundle.putString(EventKey.STRING_DATA, error);
            mOnErrorEventListener.onErrorEvent(eventCode, bundle);
        }
    }

    /**
     * 更新播放器状态
     *
     * @param status 状态
     */
    protected final void updateStatus(int status) {
        this.mCurrentState = status;
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_DATA, status);
        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_STATUS_CHANGE, bundle);
    }

    protected void setBuffer(int buffer) {
        mBuffer = buffer;
    }

    @Override
    public int getBuffer() {
        return mBuffer;
    }

    @Override
    public int getState() {
        return mCurrentState;
    }
}

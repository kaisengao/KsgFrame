package com.ksg.ksgplayer.player;

import android.content.Context;
import android.os.Bundle;

import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorListener;
import com.ksg.ksgplayer.listener.OnPlayerListener;

/**
 * @ClassName: BasePlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:22
 * @Description: 播放器抽象接口实现类（如果有自定义的播放器需要继承该类）
 */
public abstract class BasePlayer implements IPlayer {

    protected final Context mContext;

    protected int mBufferPercentage;

    protected int mCurrentState = STATE_IDLE;

    private OnPlayerListener mPlayerListener;

    private OnErrorListener mErrorListener;

    public BasePlayer(Context context) {
        this.mContext = context;
        // Init Player
        this.initPlayer();
    }

    /**
     * @param bufferPercentage 缓冲进度
     */
    protected final void setBufferPercentage(int bufferPercentage) {
        this.mBufferPercentage = bufferPercentage;
    }

    /**
     * 是否处于播放
     *
     * @return boolean
     */
    @Override
    public boolean isItPlaying() {
        int state = getState();
        return state != IPlayer.STATE_DESTROY
                && state != IPlayer.STATE_ERROR
                && state != IPlayer.STATE_IDLE
                && state != IPlayer.STATE_INIT
                && state != IPlayer.STATE_STOP;
    }

    /**
     * 获取当前state
     *
     * @return 返回状态
     */
    @Override
    public int getState() {
        return mCurrentState;
    }

    /**
     * 自定义事件
     *
     * @param code   code
     * @param bundle bundle
     */
    @Override
    public void option(int code, Bundle bundle) {

    }

    /**
     * 设置 播放器状态事件
     *
     * @param playerListener onPlayerEventListener
     */
    @Override
    public void setPlayerListener(OnPlayerListener playerListener) {
        this.mPlayerListener = playerListener;
    }

    /**
     * 设置 播放器错误事件
     *
     * @param errorListener onErrorEventListener
     */
    @Override
    public void setErrorListener(OnErrorListener errorListener) {
        this.mErrorListener = errorListener;
    }

    /**
     * 发送 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    protected final void sendPlayerEvent(int eventCode, Bundle bundle) {
        if (mPlayerListener != null) {
            this.mPlayerListener.onPlayerEvent(eventCode, bundle);
        }
    }

    /**
     * 发送 错误事件
     */
    protected final void sendErrorEvent(int eventCode, String error) {
        if (mErrorListener != null) {
            Bundle bundle = BundlePool.obtain();
            bundle.putString(EventKey.STRING_DATA, error);
            this.mErrorListener.onErrorEvent(eventCode, bundle);
        }
    }

    /**
     * 更新状态
     *
     * @param status 状态
     */
    protected final void updateStatus(int status) {
        this.mCurrentState = status;
        // 发送事件
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_DATA, status);
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_STATUS_CHANGE, bundle);
    }
}

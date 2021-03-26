package com.ksg.ksgplayer.player;

import android.content.Context;
import android.os.Bundle;

import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;

/**
 * @ClassName: BaseInternalPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:22
 * @Description: 播放器抽象接口实现类（如果有自定义的播放器需要继承该类）
 */
public abstract class BaseInternalPlayer implements IKsgPlayer {

    protected final Context mContext;

    protected int mBufferPercentage;

    /**
     * 当前播放器状态
     */
    protected int mCurrentState = STATE_IDLE;

    /**
     * 播放器状态事件
     */
    private OnPlayerEventListener mOnPlayerEventListener;

    /**
     * 播放器错误事件
     */
    private OnErrorEventListener mOnErrorEventListener;

    public BaseInternalPlayer(Context context) {
        this.mContext = context;
        // 初始化 播放器
        this.initPlayer();
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
     * @param onPlayerEventListener onPlayerEventListener
     */
    @Override
    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    /**
     * 设置 播放器错误事件
     *
     * @param onErrorEventListener onErrorEventListener
     */
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
        if (this.mOnPlayerEventListener != null) {
            this.mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
        }
    }

    /**
     * 发送错误事件
     */
    protected final void submitErrorEvent(int eventCode, String error) {
        if (this.mOnErrorEventListener != null) {
            Bundle bundle = BundlePool.obtain();
            bundle.putString(EventKey.STRING_DATA, error);
            this.mOnErrorEventListener.onErrorEvent(eventCode, bundle);
        }
    }

    /**
     * @param bufferPercentage 缓冲进度
     */
    protected final void setBufferPercentage(int bufferPercentage) {
        this.mBufferPercentage = bufferPercentage;
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
}

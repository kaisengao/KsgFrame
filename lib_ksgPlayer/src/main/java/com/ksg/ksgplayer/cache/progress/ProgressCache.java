package com.ksg.ksgplayer.cache.progress;

import android.os.Bundle;

import com.ksg.ksgplayer.cache.PlayValueGetter;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.player.IKsgPlayerProxy;

/**
 * @ClassName: ProgressCache
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:31
 * @Description: 记录播放进度缓存
 */
public class ProgressCache implements IKsgPlayerProxy {

    private PlayValueGetter mPlayValueGetter;

    private String mDataSource;

    public ProgressCache(PlayValueGetter valueGetter) {
        this.mPlayValueGetter = valueGetter;
    }

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    @Override
    public void onDataSourceReady(String dataSource) {
        // 数据源改变 记录上一个视频的进度
        this.record();
        this.mDataSource = dataSource;
    }

    /**
     * 播放 停止状态
     */
    @Override
    public void onIntentStop() {
        this.record();
    }

    /**
     * 播放 重置状态
     */
    @Override
    public void onIntentReset() {
        this.record();
    }

    /**
     * 播放 销毁状态
     */
    @Override
    public void onIntentDestroy() {
        this.record();
    }

    /**
     * 播放器的基础事件
     */
    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE:
                // 播放暂停事件,记录进度.
                this.record();
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                // 播放完成重置播放位置.
                PlayRecord.getInstance().reset(mDataSource);
                break;
            default:
                break;
        }
    }

    /**
     * 记录
     */
    private void record() {
        if (isInPlaybackState() && getProgress() > 0) {
            PlayRecord.getInstance().record(mDataSource, getProgress());
        }
    }

    /**
     * 播放进度
     */
    private long getProgress() {
        if (mPlayValueGetter != null) {
            return mPlayValueGetter.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 是否处于播放状态
     */
    private boolean isInPlaybackState() {
        int state = mPlayValueGetter != null ? mPlayValueGetter.getState() : IKsgPlayer.STATE_IDLE;
        return state != IKsgPlayer.STATE_END
                && state != IKsgPlayer.STATE_ERROR
                && state != IKsgPlayer.STATE_IDLE
                && state != IKsgPlayer.STATE_INITIALIZED
                && state != IKsgPlayer.STATE_STOPPED
                && state != IKsgPlayer.STATE_PLAYBACK_COMPLETE;
    }

    /**
     * 获取本地缓存
     *
     * @param dataSource 播放地址
     * @return 本地缓存的播放进度
     */
    @Override
    public long getRecord(String dataSource) {
        return PlayRecord.getInstance().getRecord(dataSource);
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }
}

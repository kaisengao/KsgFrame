package com.ksg.ksgplayer.record;

import android.os.Bundle;

import com.ksg.ksgplayer.entity.DataSource;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.player.IKsgPlayerProxy;

/**
 * @author kaisengao
 * @create: 2019/1/15 15:24
 * @describe: 记录代理的播放器
 */
public class RecordProxyPlayer implements IKsgPlayerProxy {

    private PlayValueGetter mPlayValueGetter;

    private DataSource mDataSource;

    public RecordProxyPlayer(PlayValueGetter valueGetter) {
        this.mPlayValueGetter = valueGetter;
    }

    @Override
    public void onDataSourceReady(DataSource dataSource) {
        // 更改数据源,记录它们
        record();
        this.mDataSource = dataSource;
    }

    @Override
    public void onIntentStop() {
        record();
    }

    @Override
    public void onIntentReset() {
        record();
    }

    @Override
    public void onIntentDestroy() {
        record();
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE:
                // 暂停,记录位置.
                record();
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
    private int getProgress() {
        if (mPlayValueGetter != null) {
            return mPlayValueGetter.getProgress();
        }
        return 0;
    }

    /**
     * 播放进度
     */
    private int getDuration() {
        if (mPlayValueGetter != null) {
            return mPlayValueGetter.getDuration();
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

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public int getRecord(DataSource dataSource) {
        return PlayRecord.getInstance().getRecord(dataSource);
    }
}

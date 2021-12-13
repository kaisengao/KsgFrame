package com.ksg.ksgplayer.assist;

import android.os.Bundle;
import android.util.Log;

import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.widget.KsgVideoView;

/**
 * @ClassName: OnVideoViewEventHandler
 * @Author: KaiSenGao
 * @CreateDate: 2019/3/22 15:20
 * @Description:
 */
public class OnVideoViewEventHandler extends BaseEventAssistHandler<KsgVideoView> {

    protected OnVideoViewEventHandler(KsgVideoView assist) {
        super(assist);
    }

    /**
     * 自定义 通知
     *
     * @param bundle bundle
     */
    @Override
    public void requestOption(Bundle bundle) {
        this.getAssist().getVideoPlayer().option(bundle.getInt(EventKey.INT_DATA), bundle);
    }

    /**
     * 播放
     *
     * @param bundle bundle
     */
    @Override
    public void requestStart(Bundle bundle) {
        this.getAssist().start();
    }

    /**
     * 暂停
     *
     * @param bundle bundle
     */
    @Override
    public void requestPause(Bundle bundle) {
        if (isInPlaybackState()) {
            this.getAssist().pause();
        } else {
            this.getAssist().stop();
        }
    }

    /**
     * 继续播放
     *
     * @param bundle bundle
     */
    @Override
    public void requestResume(Bundle bundle) {
        if (isInPlaybackState()) {
            this.getAssist().resume();
        } else {
            this.requestReplay(bundle);
        }
    }

    /**
     * Seek
     *
     * @param bundle bundle
     */
    @Override
    public void requestSeek(Bundle bundle) {
        this.getAssist().seekTo(bundle.getLong(EventKey.LONG_DATA));
    }

    /**
     * 停止
     *
     * @param bundle bundle
     */
    @Override
    public void requestStop(Bundle bundle) {
        this.getAssist().stop();
    }

    /**
     * 重置播放器
     *
     * @param bundle bundle
     */
    @Override
    public void requestReset(Bundle bundle) {
        this.getAssist().stop();
        this.getAssist().reset();
    }

    /**
     * 重新播放
     *
     * @param bundle bundle
     */
    @Override
    public void requestReplay(Bundle bundle) {
        long msc = 0;
        if (bundle != null) {
            msc = bundle.getLong(EventKey.LONG_DATA);
        }
        this.getAssist().replay(msc);
    }

    /**
     * 重新播放 新的数据源
     *
     * @param bundle bundle
     */
    @Override
    public void requestPlayDataSource(Bundle bundle) {
        if (bundle != null) {
            DataSource dataSource = (DataSource) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
            if (dataSource == null) {
                Log.e("OnVideoViewEventHandler", "requestPlayDataSource need legal data source");
                return;
            }
            this.getAssist().stop();
            this.getAssist().setDataSource(dataSource);
            this.getAssist().start();
        }
    }

    private boolean isInPlaybackState() {
        int state = getAssist().getVideoPlayer().getState();
        return state != IKsgPlayer.STATE_END
                && state != IKsgPlayer.STATE_ERROR
                && state != IKsgPlayer.STATE_IDLE
                && state != IKsgPlayer.STATE_INITIALIZED
                && state != IKsgPlayer.STATE_STOPPED;
    }
}

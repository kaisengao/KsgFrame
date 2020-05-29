package com.ksg.ksgplayer.assist;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.player.KsgVideoPlayer;

/**
 * @author kaisengao
 * @create: 2019/3/22 15:20
 * @describe:
 */
public class OnVideoViewEventHandler extends BaseEventAssistHandler<KsgVideoPlayer> {

    @Override
    public void requestOption(KsgVideoPlayer assist, Bundle bundle) {
        int code = bundle.getInt(EventKey.INT_ARG1);
        assist.option(code, bundle);
    }

    @Override
    public void requestStart(KsgVideoPlayer assist, Bundle bundle) {
        assist.start();
    }

    @Override
    public void requestPause(KsgVideoPlayer videoView, Bundle bundle) {
        if (isInPlaybackState(videoView)) {
            videoView.pause();
        } else {
            videoView.stop();
        }
    }

    @Override
    public void requestResume(KsgVideoPlayer videoView, Bundle bundle) {
        if (isInPlaybackState(videoView)) {
            videoView.resume();
        } else {
            requestRetry(videoView, bundle);
        }
    }

    @Override
    public void requestSeek(KsgVideoPlayer videoView, Bundle bundle) {
        long pos = 0;
        if (bundle != null) {
            pos = bundle.getLong(EventKey.LONG_DATA);
        }
        videoView.seekTo(pos);
    }

    @Override
    public void requestStop(KsgVideoPlayer videoView, Bundle bundle) {
        videoView.stop();
    }

    @Override
    public void requestReset(KsgVideoPlayer videoView, Bundle bundle) {
        videoView.stop();
    }

    @Override
    public void requestRetry(KsgVideoPlayer videoView, Bundle bundle) {
        int pos = 0;
        if (bundle != null) {
            pos = bundle.getInt(EventKey.INT_DATA);
        }
        videoView.rePlay(pos);
    }

    @Override
    public void requestReplay(KsgVideoPlayer videoView, Bundle bundle) {
        videoView.rePlay(0);
    }

    @Override
    public void requestPlayDataSource(KsgVideoPlayer assist, Bundle bundle) {
        if (bundle != null) {
            String dataSource = bundle.getString(EventKey.STRING_DATA, "");
            if (TextUtils.isEmpty(dataSource)) {
                Log.e("OnVideoViewEventHandler", "requestPlayDataSource need legal data source");
                return;
            }
            assist.stop();
            assist.setDataSource(dataSource);
            assist.start();
        }
    }

    private boolean isInPlaybackState(KsgVideoPlayer videoView) {
        int state = videoView.getState();
        return state != IKsgPlayer.STATE_END
                && state != IKsgPlayer.STATE_ERROR
                && state != IKsgPlayer.STATE_IDLE
                && state != IKsgPlayer.STATE_INITIALIZED
                && state != IKsgPlayer.STATE_STOPPED;
    }

}

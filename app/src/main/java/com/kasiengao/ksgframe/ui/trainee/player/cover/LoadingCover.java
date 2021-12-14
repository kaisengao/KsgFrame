package com.kasiengao.ksgframe.ui.trainee.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.kasiengao.ksgframe.R;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.receiver.BaseCover;
import com.ksg.ksgplayer.receiver.PlayerStateGetter;

/**
 * @ClassName: LoadingCover
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/27 9:33 PM
 * @Description: Loading Cover
 */
public class LoadingCover extends BaseCover {

    public LoadingCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_loading, null);
    }

    @Override
    protected void onCoverAttachedToWindow() {
        super.onCoverAttachedToWindow();
        PlayerStateGetter playerStateGetter = getPlayerStateGetter();
        if (playerStateGetter != null && isInPlaybackState(playerStateGetter)) {
            this.setLoadingState(playerStateGetter.isBuffering());
        }
    }

    /**
     * 当前播放状态
     *
     * @param playerStateGetter {@link PlayerStateGetter}
     * @return boolean
     */
    private boolean isInPlaybackState(PlayerStateGetter playerStateGetter) {
        int state = playerStateGetter.getState();
        return state != IKsgPlayer.STATE_END
                && state != IKsgPlayer.STATE_ERROR
                && state != IKsgPlayer.STATE_IDLE
                && state != IKsgPlayer.STATE_INITIALIZED
                && state != IKsgPlayer.STATE_STOPPED;
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
            case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_TO:
                this.setLoadingState(true);
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END:
            case OnPlayerEventListener.PLAYER_EVENT_ON_STOP:
            case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_ERROR:
            case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                this.setLoadingState(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {
        this.setLoadingState(false);
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    /**
     * 显示 隐藏 loading
     *
     * @param visibility visibility
     */
    private void setLoadingState(boolean visibility) {
        this.setCoverVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getCoverLevel() {
        return levelHigh(5);
    }

}

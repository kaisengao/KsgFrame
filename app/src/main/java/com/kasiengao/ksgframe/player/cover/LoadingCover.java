package com.kasiengao.ksgframe.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.kasiengao.ksgframe.R;
import com.ksg.ksgplayer.cover.BaseCover;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.state.PlayerStateGetter;

/**
 * @ClassName: LoadingCover
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/27 9:33 PM
 * @Description: Loading
 */
public class LoadingCover extends BaseCover {

    public LoadingCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_loading, null);
    }
    /**
     * InitViews
     */
    @Override
    public void initViews() {}
    /**
     * loading
     *
     * @param visibility visibility
     */
    private void setLoadingState(boolean visibility) {
        this.setCoverVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    /**
     * View 与页面视图绑定
     */
    @Override
    public void onCoverViewBind() {
        PlayerStateGetter playerStateGetter = getPlayerStateGetter();
        if (playerStateGetter != null && isItPlaying(playerStateGetter)) {
            this.setLoadingState(playerStateGetter.isBuffering());
        }
    }

    /**
     * View 与页面视图解绑
     */
    @Override
    public void onCoverViewUnBind() {

    }

    /**
     * 当前播放状态
     *
     * @param playerStateGetter {@link PlayerStateGetter}
     * @return boolean
     */
    private boolean isItPlaying(PlayerStateGetter playerStateGetter) {
        int state = playerStateGetter.getState();
        return state != IPlayer.STATE_STOP
                && state != IPlayer.STATE_ERROR
                && state != IPlayer.STATE_IDLE
                && state != IPlayer.STATE_INIT
                && state != IPlayer.STATE_COMPLETE;
    }

    /**
     * 值 过滤器
     *
     * @return {"Key1","Key2","Key3"}
     */
    @Override
    public String[] getValueFilters() {
        return new String[]{};
    }

    /**
     * 值 事件
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void onValueEvent(String key, Object value) {
    }

    /**
     * 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_START:
            case OnPlayerListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
            case OnPlayerListener.PLAYER_EVENT_ON_PROVIDER_DATA_START:
            case OnPlayerListener.PLAYER_EVENT_ON_SEEK_TO:
                this.setLoadingState(true);
                break;
            case OnPlayerListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
            case OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_END:
            case OnPlayerListener.PLAYER_EVENT_ON_STOP:
            case OnPlayerListener.PLAYER_EVENT_ON_PROVIDER_DATA_ERROR:
            case OnPlayerListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                this.setLoadingState(false);
                break;
            default:
                break;
        }
    }

    /**
     * 错误事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {
        this.setLoadingState(false);
    }

    /**
     * Cover事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {

    }

    /**
     * Cover私有事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onPrivateEvent(int eventCode, Bundle bundle) {

    }

    /**
     * 生产者事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onProducerEvent(int eventCode, Bundle bundle) {

    }

    /**
     * 生产者事件 数据
     *
     * @param key  key
     * @param data data
     */
    @Override
    public void onProducerData(String key, Object data) {

    }

    /**
     * 获取组件等级
     *
     * @return level
     */
    @Override
    public int getCoverLevel() {
        return levelLow(10);
    }
}

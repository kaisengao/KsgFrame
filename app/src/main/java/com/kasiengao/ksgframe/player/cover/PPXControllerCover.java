package com.kasiengao.ksgframe.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.util.BitmapUtils;
import com.kasiengao.ksgframe.common.widget.PlayStateView;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.IPlayer;

/**
 * @ClassName: PPXControllerCover
 * @Author: KaiSenGao
 * @CreateDate: 2022/5/7 20:45
 * @Description: PPX 控制器
 */
public class PPXControllerCover extends BaseControllerCover {

    private PlayStateView mPlayState;

    public PPXControllerCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_ppx_controller, null);
    }

    /**
     * InitViews
     */
    @Override
    public void initViews() {
        super.initViews();
        // Init
        this.mControllerShow = false;
        // Views
        this.mPlayState = findViewById(R.id.cover_controller_play);
        this.mPlayState.setMBitStart(BitmapUtils.getVectorBitmap(mContext, R.drawable.ic_start_ppx));
        this.mPlayState.setMBitPause(null);
    }

    /**
     * View 与页面视图绑定
     */
    @Override
    public void onCoverViewBind() {
        super.onCoverViewBind();
        // 开启手势
        this.setGestureEnabled(true, false);
    }

    /**
     * View 与页面视图解绑
     */
    @Override
    public void onCoverViewUnBind() {
        super.onCoverViewUnBind();
        // 关闭手势
        this.setGestureEnabled(false, false);
    }

    /**
     * 值 过滤器
     *
     * @return {"Key1","Key2","Key3"}
     */
    @Override
    public String[] getValueFilters() {
        return super.getValueFilters();
    }

    /**
     * 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        super.onPlayerEvent(eventCode, bundle);
        if (eventCode == OnPlayerListener.PLAYER_EVENT_ON_STATE_CHANGE) {
            // 播放状态改变
            this.setPlayState(bundle.getInt(EventKey.INT_DATA));
        }
    }

    /**
     * 单击
     */
    @Override
    protected void onSingleTap(Object value) {
        // 播放状态 播放/暂停
        this.onSwitchPlayState();
    }

    /**
     * 双击
     */
    @Override
    protected void onDoubleTap() {
    }

    /**
     * 设置状态
     */
    private void setPlayState(int state) {
        if (state == IPlayer.STATE_PAUSE) {
            this.mPlayState.switchToStart();
            this.setCoverVisibility(View.VISIBLE);
        } else if (state == IPlayer.STATE_START
                || state == IPlayer.STATE_COMPLETE) {
            this.mPlayState.switchToPause();
            this.setCoverVisibility(View.GONE);
        }
    }

    /**
     * 播放状态
     */
    protected void onSwitchPlayState() {
        super.onSwitchPlayState();
        switch (mPlayState.getMCurrState()) {
            case PlayStateView.STATE_START:
                this.requestResume(null);
                break;
            case PlayStateView.STATE_PAUSE:
                this.requestPause(null);
                break;
            default:
                break;
        }
    }

    /**
     * onClick
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * 获取组件等级
     *
     * @return level
     */
    @Override
    public int getCoverLevel() {
        return levelMedium(20);
    }

}
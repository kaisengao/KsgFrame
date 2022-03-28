package com.kasiengao.ksgframe.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.kasiengao.ksgframe.R;
import com.ksg.ksgplayer.cover.BaseCover;
import com.ksg.ksgplayer.listener.OnPlayerListener;

/**
 * @ClassName: UploaderCover
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/28 19:07
 * @Description: Up主信息（视频结束出现）
 */
public class UploaderCover extends BaseCover {

    public UploaderCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_uploader, null);
    }

    /**
     * View 与页面视图绑定
     */
    @Override
    public void onCoverViewBind() {
        findViewById(R.id.cover_uploader_replay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReplay(null);
            }
        });

        getCoverView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /**
     * View 与页面视图解绑
     */
    @Override
    public void onCoverViewUnBind() {

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
            case OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                // 播放结束 Show
                this.setCoverVisibility(View.VISIBLE);
                break;
            default:
                this.setCoverVisibility(View.GONE);
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
        return levelHigh(10);
    }
}
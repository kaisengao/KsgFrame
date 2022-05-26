package com.kaisengao.ksgframe.player.cover;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.kaisengao.base.util.GlideUtil;
import com.kaisengao.ksgframe.constant.CoverConstant;
import com.kaisengao.ksgframe.ui.main.bean.VideoBean;
import com.kaisengao.ksgframe.R;
import com.ksg.ksgplayer.cover.BaseCover;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.player.IPlayer;

/**
 * @ClassName: UploaderCover
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/28 19:07
 * @Description: UP主信息（视频结束出现）
 */
public class UploaderCover extends BaseCover implements View.OnClickListener {

    private ShapeableImageView mUploaderAvatar;

    private AppCompatTextView mUploaderNickname;

    private AppCompatTextView mUploaderFans;

    public UploaderCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_cover_uploader, null);
    }

    /**
     * InitViews
     */
    @Override
    public void initViews() {
        this.mUploaderAvatar = findViewById(R.id.cover_uploader_avatar);
        this.mUploaderNickname = findViewById(R.id.cover_uploader_nickname);
        this.mUploaderFans = findViewById(R.id.cover_uploader_fans);
        // OnClick
        this.findViewById(R.id.cover_uploader_replay).setOnClickListener(this);
        // 拦截所有底层事件
        this.getCoverView().setOnClickListener(this);
    }

    /**
     * View 与页面视图绑定
     */
    @Override
    public void onCoverViewBind() {

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
        return new String[]{
                CoverConstant.ValueKey.KEY_UPLOADER_DATA
        };
    }

    /**
     * 值 事件
     *
     * @param key   key
     * @param value value
     */
    @Override
    public void onValueEvent(String key, Object value) {
        // UP主信息
        if (key.equals(CoverConstant.ValueKey.KEY_UPLOADER_DATA)) {
            if (value instanceof VideoBean) {
                VideoBean info = (VideoBean) value;
                // 头像
                GlideUtil.loadImage(mContext, info.getAvatar(), mUploaderAvatar);
                // 昵称
                this.mUploaderNickname.setText(info.getNickname());
                // 粉丝
                String fans = info.getFans();
                if (!TextUtils.isEmpty(fans)) {
                    this.mUploaderFans.setText(fans);
                    this.mUploaderFans.setVisibility(View.VISIBLE);
                } else {
                    this.mUploaderFans.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        if (eventCode == OnPlayerListener.PLAYER_EVENT_ON_STATE_CHANGE) {
            // 播放状态改变
            if (bundle.getInt(EventKey.INT_DATA) == IPlayer.STATE_COMPLETE) {
                this.setCoverVisibility(View.VISIBLE);
            } else {
                this.setCoverVisibility(View.GONE);
            }
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
     * onClick
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cover_uploader_replay) {
            // 重播
            this.requestReplay(null);
        }
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
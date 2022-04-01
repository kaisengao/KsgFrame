package com.ksg.ksgplayer.cover;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.ksg.ksgplayer.listener.OnCoverEventListener;
import com.ksg.ksgplayer.state.PlayerStateGetter;
import com.ksg.ksgplayer.state.StateGetter;

/**
 * @ClassName: BaseCover
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 16:09
 * @Description: 覆盖组件
 */
public abstract class BaseCover implements ICover, ICoverHandle, StateGetter, View.OnAttachStateChangeListener {

    protected final Context mContext;

    private final View mCoverView;

    private String mKey;

    private ICoverManager mCoverManager;

    private ICoverValue mValuePool;

    private StateGetter mStateGetter;

    private OnCoverEventListener mCoverEventListener;

    public BaseCover(Context context) {
        this.mContext = context;
        this.mCoverView = onCreateCoverView(context);
        this.mCoverView.addOnAttachStateChangeListener(this);
        // InitViews
        this.initViews();
    }

    /**
     * 创建View视图
     *
     * @param context 上下文
     * @return RootView
     */
    protected abstract View onCreateCoverView(Context context);

    /**
     * FindViewById
     *
     * @param id  id
     * @param <T> View
     * @return View
     */
    protected final <T extends View> T findViewById(int id) {
        return mCoverView.findViewById(id);
    }

    /**
     * 组件 显示隐藏
     *
     * @param visibility visible/gone
     */
    @Override
    public final void setCoverVisibility(int visibility) {
        this.mCoverView.setVisibility(visibility);
    }

    /**
     * 获取组件View
     *
     * @return coverView
     */
    @Override
    public View getCoverView() {
        return mCoverView;
    }

    /**
     * 获取组件等级
     *
     * @return level
     */
    @Override
    public int getCoverLevel() {
        return ICover.COVER_LEVEL_LOW;
    }

    /**
     * 绑定 覆盖组件管理器
     *
     * @param coverManager 覆盖组件管理器
     */
    @Override
    public void bindManager(ICoverManager coverManager) {
        this.mCoverManager = coverManager;
    }

    /**
     * 绑定 数据池
     *
     * @param valuePool 数据池
     */
    @Override
    public void bindValuePool(ICoverValue valuePool) {
        this.mValuePool = valuePool;
    }

    /**
     * 获取 数据池
     */
    public ICoverValue getValuePool() {
        return mValuePool;
    }

    /**
     * 低覆盖级别
     *
     * @param priority range from 0-31
     * @return level
     */
    protected final int levelLow(@IntRange(from = 0, to = 31) int priority) {
        return levelPriority(ICover.COVER_LEVEL_LOW, priority);
    }

    /**
     * 中覆盖级别
     *
     * @param priority range from 0-31
     * @return level
     */
    protected final int levelMedium(@IntRange(from = 0, to = 31) int priority) {
        return levelPriority(ICover.COVER_LEVEL_MEDIUM, priority);
    }

    /**
     * 高覆盖级别
     *
     * @param priority range from 0-31
     * @return level
     */
    protected final int levelHigh(@IntRange(from = 0, to = 31) int priority) {
        return levelPriority(ICover.COVER_LEVEL_HIGH, priority);
    }

    /**
     * 返回等级优先权
     *
     * @return level
     */
    private int levelPriority(int level, int priority) {
        return level + (priority % LEVEL_MAX);
    }

    /**
     * 可视界面的时候调用
     *
     * @param v View
     */
    @Override
    public final void onViewAttachedToWindow(View v) {
        // View 与页面视图绑定
        this.onCoverViewBind();

    }

    /**
     * 移除界面的时候调用
     *
     * @param v View
     */
    @Override
    public final void onViewDetachedFromWindow(View v) {
        this.onCoverViewUnBind();
    }

    /**
     * InitViews
     */
    public abstract void initViews();

    /**
     * View 与页面视图绑定
     */
    public abstract void onCoverViewBind();

    /**
     * View 与页面视图解绑
     */
    public abstract void onCoverViewUnBind();

    /**
     * CoverKey
     *
     * @param key 唯一标识
     */
    @Override
    public void setKey(String key) {
        this.mKey = key;
    }

    /**
     * CoverKey
     *
     * @return 唯一标识
     */
    @Override
    public String getKey() {
        return mKey;
    }

    /**
     * 释放
     */
    @Override
    public void release() {
    }

    /**
     * 绑定 一些状态获取器
     *
     * @param stateGetter stateGetter
     */
    public final void bindStateGetter(StateGetter stateGetter) {
        this.mStateGetter = stateGetter;
    }

    /**
     * 绑定 组件回调事件监听器
     */
    public final void bindCoverEventListener(OnCoverEventListener onCoverEventListener) {
        this.mCoverEventListener = onCoverEventListener;
    }

    /**
     * 获取 播放状态
     *
     * @return {@link PlayerStateGetter}
     */
    @Override
    public final PlayerStateGetter getPlayerStateGetter() {
        if (mStateGetter != null) {
            return mStateGetter.getPlayerStateGetter();
        }
        return null;
    }

    @Override
    public final void requestOption(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_OPTION, bundle);
    }

    @Override
    public final void requestStart(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_START, bundle);
    }

    @Override
    public final void requestPause(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_PAUSE, bundle);
    }

    @Override
    public final void requestResume(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_RESUME, bundle);
    }

    @Override
    public final void requestSeek(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_SEEK, bundle);
    }

    @Override
    public final void requestStop(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_STOP, bundle);
    }

    @Override
    public final void requestReset(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_RESET, bundle);
    }

    @Override
    public final void requestReplay(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_REPLAY, bundle);
    }

    @Override
    public final void requestSpeed(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_SPEED, bundle);
    }

    @Override
    public void requestAddProducer(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_ADD_PRODUCER, bundle);
    }

    @Override
    public void requestRemoveProducer(Bundle bundle) {
        this.notifyCoverEvent(ICoverEvent.CODE_REQUEST_REMOVE_PRODUCER, bundle);
    }

    /**
     * 通知 Cover事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    protected final void notifyCoverEvent(int eventCode, Bundle bundle) {
        if (mCoverEventListener != null) {
            this.mCoverEventListener.onCoverEvent(eventCode, bundle);
        }
    }

    /**
     * 通知 Cover私有事件
     *
     * @param key       key
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    protected final void notifyPrivateEvent(@NonNull String key, int eventCode, Bundle bundle) {
        if (mCoverManager != null && !TextUtils.isEmpty(key)) {
            ICover cover = mCoverManager.getCover(key);
            if (cover != null) {
                cover.onPrivateEvent(eventCode, bundle);
            }
        }
    }
}

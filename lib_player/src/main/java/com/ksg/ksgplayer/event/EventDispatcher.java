package com.ksg.ksgplayer.event;

import android.os.Bundle;

import com.ksg.ksgplayer.cover.ICover;
import com.ksg.ksgplayer.cover.ICoverManager;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.listener.OnTimerUpdateListener;

/**
 * @author kaisengao
 * @create: 2019/2/26 15:22
 * @describe: 事件发送者
 */
public final class EventDispatcher implements IEventDispatcher {

    private ICoverManager mCoverManager;

    /**
     * 绑定 覆盖组件管理器
     */
    @Override
    public void bindCoverManager(ICoverManager coverManager) {
        this.mCoverManager = coverManager;
    }

    /**
     * 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void dispatchPlayEvent(final int eventCode, final Bundle bundle) {
        if (mCoverManager == null) {
            return;
        }
        switch (eventCode) {
            case OnPlayerListener.PLAYER_EVENT_ON_TIMER_UPDATE:
                mCoverManager.forEach(new ICoverManager.OnLoopListener() {
                    @Override
                    public void onEach(ICover cover) {
                        if (cover instanceof OnTimerUpdateListener && bundle != null) {
                            ((OnTimerUpdateListener) cover).onTimerUpdate(
                                    bundle.getLong(EventKey.LONG_ARG1),
                                    bundle.getLong(EventKey.LONG_ARG2),
                                    bundle.getLong(EventKey.LONG_ARG3));
                        }
                        cover.onPlayerEvent(eventCode, bundle);
                    }
                });
            default:
                mCoverManager.forEach(new ICoverManager.OnLoopListener() {
                    @Override
                    public void onEach(ICover cover) {
                        cover.onPlayerEvent(eventCode, bundle);
                    }
                });
                break;
        }
        recycleBundle(bundle);
    }

    /**
     * 错误事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void dispatchErrorEvent(final int eventCode, final Bundle bundle) {
        mCoverManager.forEach(new ICoverManager.OnLoopListener() {
            @Override
            public void onEach(ICover cover) {
                cover.onErrorEvent(eventCode, bundle);
            }
        });
        recycleBundle(bundle);
    }

    /**
     * Cover事件
     *
     * @param eventCode   eventCode
     * @param bundle      bundle
     */
    @Override
    public void dispatchCoverEvent(final int eventCode, final Bundle bundle) {
        dispatchCoverEvent(eventCode, bundle, null);
    }

    /**
     * Cover事件
     *
     * @param eventCode   eventCode
     * @param bundle      bundle
     * @param coverFilter coverFilter
     */
    @Override
    public void dispatchCoverEvent(final int eventCode, final Bundle bundle, ICoverManager.OnCoverFilter coverFilter) {
        mCoverManager.forEach(coverFilter, new ICoverManager.OnLoopListener() {
            @Override
            public void onEach(ICover cover) {
                cover.onCoverEvent(eventCode, bundle);
            }
        });
        recycleBundle(bundle);
    }

    /**
     * 生产者事件
     *
     * @param eventCode   eventCode
     * @param bundle      bundle
     * @param coverFilter coverFilter
     */
    @Override
    public void dispatchProducerEvent(final int eventCode, final Bundle bundle, ICoverManager.OnCoverFilter coverFilter) {
        mCoverManager.forEach(coverFilter, new ICoverManager.OnLoopListener() {
            @Override
            public void onEach(ICover cover) {
                cover.onProducerEvent(eventCode, bundle);
            }
        });
        recycleBundle(bundle);
    }

    /**
     * 生产者事件 数据
     *
     * @param key         key
     * @param data        data
     * @param coverFilter coverFilter
     */
    @Override
    public void dispatchProducerData(final String key, final Object data, ICoverManager.OnCoverFilter coverFilter) {
        mCoverManager.forEach(coverFilter, new ICoverManager.OnLoopListener() {
            @Override
            public void onEach(ICover cover) {
                cover.onProducerData(key, data);
            }
        });
    }

    private void recycleBundle(Bundle bundle) {
        if (bundle != null) {
            bundle.clear();
        }
    }
}

package com.ksg.ksgplayer.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.ksg.ksgplayer.cover.BaseCover;
import com.ksg.ksgplayer.cover.DefaultLevelCoverContainer;
import com.ksg.ksgplayer.cover.ICover;
import com.ksg.ksgplayer.cover.ICoverContainer;
import com.ksg.ksgplayer.cover.ICoverEvent;
import com.ksg.ksgplayer.cover.ICoverManager;
import com.ksg.ksgplayer.event.EventDispatcher;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.event.IEventDispatcher;
import com.ksg.ksgplayer.listener.OnCoverEventListener;
import com.ksg.ksgplayer.listener.OnProducerSenderListener;
import com.ksg.ksgplayer.producer.BaseEventProducer;
import com.ksg.ksgplayer.producer.ProducerManager;
import com.ksg.ksgplayer.producer.ProducerSender;
import com.ksg.ksgplayer.state.StateGetter;

import java.util.List;

/**
 * @ClassName: VideoContainer
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 15:27
 * @Description: 视图容器
 */
public class VideoContainer extends FrameLayout {

    private FrameLayout mRendererLayout;

    private StateGetter mStateGetter;

    private ProducerManager mProducerManager;

    private ICoverManager mCoverManager;

    private ICoverContainer mCoverContainer;

    private IEventDispatcher mEventDispatcher;

    private OnCoverEventListener mCoverEventListener;

    public VideoContainer(@NonNull Context context) {
        super(context);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        // Init 渲染器容器
        this.initRendererLayout();
        // Init 生产者管理器
        this.initProducerGroup();
        // Init 覆盖组件视图类
        this.initCoverContainer();
        // Init 事件发送者
        this.initEventDispatcher();
    }

    /**
     * Init 渲染器容器
     */
    private void initRendererLayout() {
        this.mRendererLayout = new FrameLayout(getContext());
        this.addView(mRendererLayout, 0, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * Init 生产者管理器
     */
    private void initProducerGroup() {
        this.mProducerManager = new ProducerManager(new ProducerSender(mProducerSenderListener));
    }

    /**
     * Init 覆盖组件视图类
     */
    private void initCoverContainer() {
        this.mCoverContainer = getCoverContainer(getContext());
        this.addView(mCoverContainer.getRootView(), new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * Init 事件发送者
     */
    private void initEventDispatcher() {
        this.mEventDispatcher = new EventDispatcher();
    }

    /**
     * 获取 覆盖组件视图类
     */
    protected ICoverContainer getCoverContainer(Context context) {
        return new DefaultLevelCoverContainer(context);
    }

    /**
     * 设置渲染器
     *
     * @param view view
     */
    public final void setRenderer(View view) {
        // 非空验证
        if (mRendererLayout.getChildCount() > 0) {
            View childAt = mRendererLayout.getChildAt(0);
            // 1、过滤同一个渲染器避免重复添加
            if (childAt == view) {
                return;
            }
            // 2、不是同一个则清空容器
            else {
                this.mRendererLayout.removeAllViews();
            }
        }
        // AddView
        this.mRendererLayout.addView(view, new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER));
    }

    /**
     * 设置 一些状态获取器
     *
     * @param stateGetter {@link StateGetter}
     */
    public final void setStateGetter(StateGetter stateGetter) {
        this.mStateGetter = stateGetter;
    }

    /**
     * 设置 覆盖组件管理器
     *
     * @param coverManager {@link ICoverManager}
     */
    public final void setCoverManager(ICoverManager coverManager) {
        this.mCoverManager = coverManager;
        this.mEventDispatcher.bindCoverManager(coverManager);
        // 移除所有Cover组件
        this.removeAllCovers();
        // 解绑 Cover添加移除事件
        this.mCoverManager.removeCoverAttachStateChangeListener(mCoverAttachStateChangeListener);
        // 循环AttachCover
        this.mCoverManager.forEach(new ICoverManager.OnLoopListener() {
            @Override
            public void onEach(ICover cover) {
                attachCover(cover);
            }
        });
        // 添加接收器组更改侦听器，动态附加接收器， 当用户添加或移除接收器时分离接收器
        this.mCoverManager.addCoverAttachStateChangeListener(mCoverAttachStateChangeListener);
    }

    /**
     * AttachCover
     */
    private void attachCover(ICover cover) {
        BaseCover baseCover = (BaseCover) cover;
        // 事件
        baseCover.bindStateGetter(mStateGetter);
        baseCover.bindCoverEventListener(mBaseCoverEventListener);
        // Attach
        this.mCoverContainer.addCover(baseCover);
    }

    /**
     * DetachCover
     */
    private void detachCover(ICover cover) {
        BaseCover baseCover = (BaseCover) cover;
        // 事件
        baseCover.bindStateGetter(null);
        baseCover.bindCoverEventListener(null);
        // Detach
        this.mCoverContainer.removeCover(baseCover);
    }

    /**
     * 播放事件发送
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    public final void dispatchPlayEvent(int eventCode, Bundle bundle) {
        if (mEventDispatcher != null) {
            this.mEventDispatcher.dispatchPlayEvent(eventCode, bundle);
        }
    }

    /**
     * 错误事件发送
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    public final void dispatchErrorEvent(int eventCode, Bundle bundle) {
        if (mEventDispatcher != null) {
            this.mEventDispatcher.dispatchErrorEvent(eventCode, bundle);
        }
    }

    /**
     * 设置组件回调
     */
    public void setCoverEventListener(OnCoverEventListener coverEventListener) {
        this.mCoverEventListener = coverEventListener;
    }

    /**
     * 添加自定义事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    public void addEventProducer(BaseEventProducer eventProducer) {
        this.mProducerManager.addEventProducer(eventProducer);
    }

    /**
     * 移除一个事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    public void removeEventProducer(BaseEventProducer eventProducer) {
        this.mProducerManager.removeEventProducer(eventProducer);
    }

    /**
     * 返回事件生产者集合 便于控制
     *
     * @return List
     */
    public List<BaseEventProducer> getEventProducers() {
        return mProducerManager.getEventProducers();
    }

    /**
     * 移除所有Cover组件
     */
    protected void removeAllCovers() {
        this.mCoverContainer.removeAllCovers();
    }

    /**
     * Cover 添加移除事件
     */
    private final ICoverManager.OnCoverAttachStateChangeListener mCoverAttachStateChangeListener = new ICoverManager.OnCoverAttachStateChangeListener() {

        /**
         * 添加事件
         *
         * @param key   key
         * @param cover cover
         */
        @Override
        public void onAttached(String key, ICover cover) {
            attachCover(cover);
        }

        /**
         * 移除事件
         *
         * @param key   key
         * @param cover cover
         */
        @Override
        public void onDetached(String key, ICover cover) {
            detachCover(cover);
        }
    };

    /**
     * 生产者事件
     */
    private final OnProducerSenderListener mProducerSenderListener = new OnProducerSenderListener() {
        @Override
        public void sendEvent(int eventCode, Bundle bundle, ICoverManager.OnCoverFilter coverFilter) {
            if (mEventDispatcher != null) {
                mEventDispatcher.dispatchProducerEvent(eventCode, bundle, coverFilter);
            }
        }

        @Override
        public void sendObject(String key, Object value, ICoverManager.OnCoverFilter coverFilter) {
            if (mEventDispatcher != null) {
                mEventDispatcher.dispatchProducerData(key, value, coverFilter);
            }
        }
    };

    /**
     * 组件回调事件
     */
    private final OnCoverEventListener mBaseCoverEventListener = new OnCoverEventListener() {
        @Override
        public void onCoverEvent(int eventCode, Bundle bundle) {
            BaseEventProducer producer;
            switch (eventCode) {
                case ICoverEvent.CODE_REQUEST_ADD_PRODUCER:
                    producer = (BaseEventProducer) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
                    // 添加事件生产者
                    addEventProducer(producer);
                    break;
                case ICoverEvent.CODE_REQUEST_REMOVE_PRODUCER:
                    // 移除事件生产者
                    producer = (BaseEventProducer) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
                    removeEventProducer(producer);
                    break;
                default:
                    break;
            }
            // 回调上一级
            if (mCoverEventListener != null) {
                mCoverEventListener.onCoverEvent(eventCode, bundle);
            }
            // 回调给组件（内部含有清空Bundle操作 所以必须放在最后一行执行）
            if (mEventDispatcher != null) {
                mEventDispatcher.dispatchCoverEvent(eventCode, bundle);
            }
        }
    };

    /**
     * destroy
     */
    public void destroy() {
        // 释放 生产者管理器
        if (mProducerManager != null) {
            this.mProducerManager.destroy();
            this.mProducerManager = null;
        }
        // 释放 覆盖组件管理器
        if (mCoverManager != null) {
            this.mCoverManager.removeCoverAttachStateChangeListener(mCoverAttachStateChangeListener);
            this.mCoverManager.clearCover();
            this.mCoverManager.clearValuePool();
            this.mCoverManager = null;
        }
        // 释放 事件发送者
        this.mEventDispatcher = null;
        // 移除所有Cover组件
        this.removeAllCovers();
        // 移除所有视图
        this.removeAllViews();
    }
}

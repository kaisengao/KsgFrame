package com.ksg.ksgplayer.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ksg.ksgplayer.assist.InterEvent;
import com.ksg.ksgplayer.event.EventDispatcher;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.event.IEventDispatcher;
import com.ksg.ksgplayer.extension.BaseEventProducer;
import com.ksg.ksgplayer.extension.DelegateProducerEventSender;
import com.ksg.ksgplayer.extension.IProducerGroup;
import com.ksg.ksgplayer.extension.ProducerEventSender;
import com.ksg.ksgplayer.extension.ProducerGroup;
import com.ksg.ksgplayer.listener.OnReceiverEventListener;
import com.ksg.ksgplayer.receiver.BaseCover;
import com.ksg.ksgplayer.receiver.CoverComparator;
import com.ksg.ksgplayer.receiver.DefaultLevelCoverContainer;
import com.ksg.ksgplayer.receiver.ICoverStrategy;
import com.ksg.ksgplayer.receiver.IReceiver;
import com.ksg.ksgplayer.receiver.IReceiverGroup;
import com.ksg.ksgplayer.receiver.StateGetter;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * @author kaisengao
 * @create: 2019/1/11 13:36
 * @describe: 容器
 */
public class KsgContainer extends FrameLayout {

    private FrameLayout mRenderContainer;

    private StateGetter mStateGetter;

    private IProducerGroup mProducerGroup;

    private ICoverStrategy mCoverStrategy;

    private IReceiverGroup mReceiverGroup;

    private IEventDispatcher mEventDispatcher;

    private OnReceiverEventListener mOnReceiverEventListener;

    public KsgContainer(@NonNull Context context) {
        super(context);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        initProducerGroup();
        initRenderContainer(context);
        initReceiverContainer(context);
    }

    /**
     * 初始化 事件生产者管理器
     */
    private void initProducerGroup() {
        mProducerGroup = new ProducerGroup(new ProducerEventSender(mDelegateProducerEventSender));
    }

    /**
     * 初始化 视图容器
     *
     * @param context 上下文
     */
    private void initRenderContainer(Context context) {
        mRenderContainer = new FrameLayout(context);
        addView(mRenderContainer,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 初始 视图管理器
     */
    private void initReceiverContainer(Context context) {
        mCoverStrategy = getCoverStrategy(context);
        addView(mCoverStrategy.getContainerView(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 创建默认视图容器管理器
     */
    protected ICoverStrategy getCoverStrategy(Context context) {
        return new DefaultLevelCoverContainer(context);
    }

    /**
     * 在容器中添加view
     *
     * @param view view
     */
    public final void setRenderView(View view) {

        removeRender();

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        mRenderContainer.addView(view, 0, params);
    }

    /**
     * 移除View视图 初始化容器
     */
    public void removeRender() {
        if (mRenderContainer != null) {
            mRenderContainer.removeAllViews();
        }
    }


    public final void setStateGetter(StateGetter stateGetter) {
        mStateGetter = stateGetter;
    }

    /**
     * 自定义设置接收器组,自己处理事件接收
     *
     * @param receiverGroup receiverGroup
     */
    public final void setReceiverGroup(IReceiverGroup receiverGroup) {
        if (receiverGroup == null || receiverGroup.equals(mReceiverGroup)) {
            return;
        }
        // 初始化 移除所有视图
        removeAllCovers();

        // 清除回调
        if (mReceiverGroup != null) {
            mReceiverGroup.removeOnReceiverGroupChangeListener(mInternalReceiverGroupChangeListener);
        }

        this.mReceiverGroup = receiverGroup;

        // 初始化事件发送者
        mEventDispatcher = new EventDispatcher(receiverGroup);

        // 按级别排序
        mReceiverGroup.sort(new CoverComparator());

        // 循环添加接收器
        mReceiverGroup.forEach(new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
                attachReceiver(receiver);
            }
        });
        // 添加接收器组更改侦听器，动态附加接收器， 当用户添加或移除接收器时分离接收器
        mReceiverGroup.addOnReceiverGroupChangeListener(mInternalReceiverGroupChangeListener);
    }

    /**
     * 事件发送
     *
     * @param eventCode code
     * @param bundle    bundle
     */
    public final void dispatchPlayEvent(int eventCode, Bundle bundle) {
        if (mEventDispatcher != null) {
            mEventDispatcher.dispatchPlayEvent(eventCode, bundle);
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
            mEventDispatcher.dispatchErrorEvent(eventCode, bundle);
        }
    }

    /**
     * 设置组件回调
     */
    public void setOnReceiverEventListener(OnReceiverEventListener onReceiverEventListener) {
        this.mOnReceiverEventListener = onReceiverEventListener;
    }

    /**
     * 用户添加时动态附加接收器，当用户移除接收器时，将其分离。
     */
    private IReceiverGroup.OnReceiverGroupChangeListener mInternalReceiverGroupChangeListener = new IReceiverGroup.OnReceiverGroupChangeListener() {
        @Override
        public void onReceiverAdd(String key, IReceiver receiver) {
            attachReceiver(receiver);
        }

        @Override
        public void onReceiverRemove(String key, IReceiver receiver) {
            detachReceiver(receiver);
        }
    };

    private void attachReceiver(IReceiver receiver) {
        // 绑定接收器事件
        receiver.bindReceiverEventListener(mInternalReceiverEventListener);
        receiver.bindStateGetter(mStateGetter);
        receiver.bindICoverStrategy(mCoverStrategy);
        if (receiver instanceof BaseCover) {
            BaseCover cover = (BaseCover) receiver;
            // 将视图添加到 视图管理器中
            mCoverStrategy.addCover(cover);
        }
    }


    private void detachReceiver(IReceiver receiver) {
        if (receiver instanceof BaseCover) {
            BaseCover cover = (BaseCover) receiver;
            // 将视图从视图管理器中删除
            mCoverStrategy.removeCover(cover);
        }
        // 初始化接收器事件
        receiver.bindReceiverEventListener(null);
        receiver.bindStateGetter(null);
        receiver.bindICoverStrategy(null);
    }

    /**
     * 添加自定义事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    public void addEventProducer(BaseEventProducer eventProducer) {
        mProducerGroup.addEventProducer(eventProducer);
    }

    /**
     * 移除一个事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    public void removeEventProducer(BaseEventProducer eventProducer) {
        mProducerGroup.removeEventProducer(eventProducer);
    }

    /**
     * 返回事件生产者集合 便于控制
     *
     * @return List
     */
    public List<BaseEventProducer> getEventProducers() {
        return mProducerGroup.getEventProducers();
    }

    /**
     * 事件发送者的消息
     */
    private DelegateProducerEventSender mDelegateProducerEventSender = new DelegateProducerEventSender() {
        @Override
        public void sendEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter receiverFilter) {
            if (mEventDispatcher != null) {
                mEventDispatcher.dispatchProducerEvent(eventCode, bundle, receiverFilter);
            }
        }

        @Override
        public void sendObject(String key, Object value, IReceiverGroup.OnReceiverFilter receiverFilter) {
            if (mEventDispatcher != null) {
                mEventDispatcher.dispatchProducerData(key, value, receiverFilter);
            }
        }
    };
    /**
     * 接收器事件侦听器，用于某些接收器通信的桥接器
     */
    private OnReceiverEventListener mInternalReceiverEventListener = new OnReceiverEventListener() {
        @Override
        public void onReceiverEvent(int eventCode, Bundle bundle) {
            BaseEventProducer serializable;
            switch (eventCode) {
                case InterEvent.CODE_REQUEST_EVENT_ADD_PRODUCER:
                    serializable = (BaseEventProducer) bundle.getSerializable(EventKey.PARCELABLE_DATA);
                    // 添加事件生产者
                    addEventProducer(serializable);
                    break;
                case InterEvent.CODE_REQUEST_EVENT_REMOVE_PRODUCER:
                    // 移除事件生产者
                    serializable = (BaseEventProducer) bundle.getSerializable(EventKey.PARCELABLE_DATA);
                    removeEventProducer(serializable);
                    break;
                default:
                    break;
            }

            if (mOnReceiverEventListener != null) {
                mOnReceiverEventListener.onReceiverEvent(eventCode, bundle);
            }

            if (mEventDispatcher != null) {
                mEventDispatcher.dispatchReceiverEvent(eventCode, bundle);
            }
        }
    };

    /**
     * 移除所有视图
     */
    protected void removeAllCovers() {
        mCoverStrategy.removeAllCovers();
    }

    public void destroy() {
        // 清除 ReceiverGroupChangeListener
        if (mReceiverGroup != null) {
            mReceiverGroup.removeOnReceiverGroupChangeListener(mInternalReceiverGroupChangeListener);
            mReceiverGroup.clearReceivers();
        }
        // 摧毁生产者
        mProducerGroup.destroy();
        // 移除View视图
        removeRender();
        // 移除所有视图
        removeAllCovers();
    }
}

package com.ksg.ksgplayer.event;

import android.os.Bundle;
import android.view.MotionEvent;

import com.kasiengao.base.util.KLog;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.listener.OnTimerUpdateListener;
import com.ksg.ksgplayer.receiver.IReceiver;
import com.ksg.ksgplayer.receiver.IReceiverGroup;

/**
 * @author kaisengao
 * @create: 2019/2/26 15:22
 * @describe: 事件调度员  事件收发
 */
public final class EventDispatcher implements IEventDispatcher {

    private IReceiverGroup mReceiverGroup;

    public EventDispatcher(IReceiverGroup receiverGroup) {
        this.mReceiverGroup = receiverGroup;
    }

    /**
     * dispatch play event
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void dispatchPlayEvent(final int eventCode, final Bundle bundle) {
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_TIMER_UPDATE:
                mReceiverGroup.forEach(new IReceiverGroup.OnLoopListener() {
                    @Override
                    public void onEach(IReceiver receiver) {
                        if (receiver instanceof OnTimerUpdateListener && bundle != null)
                            ((OnTimerUpdateListener) receiver).onTimerUpdate(
                                    bundle.getLong(EventKey.LONG_ARG1),
                                    bundle.getLong(EventKey.LONG_ARG2),
                                    bundle.getLong(EventKey.LONG_ARG3));
                        receiver.onPlayerEvent(eventCode, bundle);
                    }
                });
            default:
                mReceiverGroup.forEach(new IReceiverGroup.OnLoopListener() {
                    @Override
                    public void onEach(IReceiver receiver) {
                        receiver.onPlayerEvent(eventCode, bundle);
                    }
                });
                break;
        }
        recycleBundle(bundle);
    }

    /**
     * dispatch error event
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void dispatchErrorEvent(final int eventCode, final Bundle bundle) {
        mReceiverGroup.forEach(new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
                receiver.onErrorEvent(eventCode, bundle);
            }
        });
        recycleBundle(bundle);
    }

    @Override
    public void dispatchReceiverEvent(final int eventCode, final Bundle bundle) {
        dispatchReceiverEvent(eventCode, bundle, null);
    }

    /**
     * dispatch receivers event
     *
     * @param eventCode        eventCode
     * @param bundle           bundle
     * @param onReceiverFilter onReceiverFilter
     */
    @Override
    public void dispatchReceiverEvent(final int eventCode, final Bundle bundle, IReceiverGroup.OnReceiverFilter onReceiverFilter) {
        mReceiverGroup.forEach(onReceiverFilter, new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
                receiver.onReceiverEvent(eventCode, bundle);
            }
        });
        recycleBundle(bundle);
    }

    /**
     * dispatch producer event
     *
     * @param eventCode        eventCode
     * @param bundle           bundle
     * @param onReceiverFilter onReceiverFilter
     */
    @Override
    public void dispatchProducerEvent(final int eventCode, final Bundle bundle, IReceiverGroup.OnReceiverFilter onReceiverFilter) {
        mReceiverGroup.forEach(onReceiverFilter, new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
                receiver.onProducerEvent(eventCode, bundle);
            }
        });
        recycleBundle(bundle);
    }

    /**
     * dispatch producer data
     *
     * @param key              key
     * @param data             data
     * @param onReceiverFilter onReceiverFilter
     */
    @Override
    public void dispatchProducerData(final String key, final Object data, IReceiverGroup.OnReceiverFilter onReceiverFilter) {
        mReceiverGroup.forEach(onReceiverFilter, new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
                receiver.onProducerData(key, data);
            }
        });
    }

    //-----------------------------------dispatch gesture touch event-----------------------------------

    @Override
    public void dispatchTouchEventOnSingleTabUp(final MotionEvent event) {
        filterImplOnTouchEventListener(new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
//                ((OnTouchGestureListener)receiver).onSingleTapUp(event);
            }
        });
    }

    @Override
    public void dispatchTouchEventOnDoubleTabUp(final MotionEvent event) {
        filterImplOnTouchEventListener(new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
//                ((OnTouchGestureListener)receiver).onDoubleTap(event);
            }
        });
    }

    @Override
    public void dispatchTouchEventOnDown(final MotionEvent event) {
        filterImplOnTouchEventListener(new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
//                ((OnTouchGestureListener)receiver).onDown(event);
            }
        });
    }

    @Override
    public void dispatchTouchEventOnScroll(final MotionEvent e1, final MotionEvent e2,
                                           final float distanceX, final float distanceY) {
        filterImplOnTouchEventListener(new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
//                ((OnTouchGestureListener)receiver).onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    @Override
    public void dispatchTouchEventOnEndGesture() {
        filterImplOnTouchEventListener(new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
//                ((OnTouchGestureListener)receiver).onEndGesture();
            }
        });
    }

    private void filterImplOnTouchEventListener(final IReceiverGroup.OnLoopListener onLoopListener) {
        mReceiverGroup.forEach(new IReceiverGroup.OnReceiverFilter() {
            @Override
            public boolean filter(IReceiver receiver) {
//                return receiver instanceof OnTouchGestureListener;
                return false;
            }
        }, new IReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(IReceiver receiver) {
                onLoopListener.onEach(receiver);
            }
        });
    }

    private void recycleBundle(Bundle bundle) {
        if (bundle != null) {
            bundle.clear();
        }
    }

}

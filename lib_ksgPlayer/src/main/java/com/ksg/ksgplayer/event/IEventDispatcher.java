package com.ksg.ksgplayer.event;

import android.os.Bundle;
import android.view.MotionEvent;

import com.ksg.ksgplayer.receiver.IReceiverGroup;

/**
 * @author kaisengao
 * @create: 2019/1/30 10:20
 * @describe: 事件调度员  事件收发
 */
public interface IEventDispatcher {

    void dispatchPlayEvent(int eventCode, Bundle bundle);

    void dispatchErrorEvent(int eventCode, Bundle bundle);

    void dispatchReceiverEvent(int eventCode, Bundle bundle);

    void dispatchReceiverEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter onReceiverFilter);

    void dispatchProducerEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter onReceiverFilter);

    void dispatchProducerData(String key, Object data, IReceiverGroup.OnReceiverFilter onReceiverFilter);

    void dispatchTouchEventOnSingleTabUp(MotionEvent event);

    void dispatchTouchEventOnDoubleTabUp(MotionEvent event);

    void dispatchTouchEventOnDown(MotionEvent event);

    void dispatchTouchEventOnScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

    void dispatchTouchEventOnEndGesture();
}

package com.ksg.ksgplayer.event;

import android.os.Bundle;

import com.ksg.ksgplayer.cover.ICoverManager;

/**
 * @author kaisengao
 * @create: 2019/1/30 10:20
 * @describe: 事件发送者
 */
public interface IEventDispatcher {

    void bindCoverManager(ICoverManager coverManager);

    void dispatchPlayEvent(int eventCode, Bundle bundle);

    void dispatchErrorEvent(int eventCode, Bundle bundle);

    void dispatchCoverEvent(int eventCode, Bundle bundle);

    void dispatchCoverEvent(int eventCode, Bundle bundle, ICoverManager.OnCoverFilter coverFilter);

    void dispatchProducerEvent(int eventCode, Bundle bundle, ICoverManager.OnCoverFilter coverFilter);

    void dispatchProducerData(String key, Object data, ICoverManager.OnCoverFilter coverFilter);
}

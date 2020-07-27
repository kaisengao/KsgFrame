package com.ksg.ksgplayer.assist;

import android.os.Bundle;

import com.kasiengao.base.util.KLog;

/**
 * @author kaisengao
 * @create: 2019/3/22 15:20
 * @describe:
 */
public abstract class BaseEventAssistHandler<T> implements OnEventAssistHandler<T> {

    private final T mAssist;

    BaseEventAssistHandler(T assist) {
        this.mAssist = assist;
    }

    /**
     * 事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onAssistHandle(int eventCode, Bundle bundle) {
        if (getAssist() == null) {
            KLog.e("BaseEventAssistHandler", "getAssist == null");
            return;
        }
        switch (eventCode) {
            case InterEvent.CODE_REQUEST_OPTION:
                this.requestOption(bundle);
                break;
            case InterEvent.CODE_REQUEST_START:
                this.requestStart(bundle);
                break;
            case InterEvent.CODE_REQUEST_PAUSE:
                this.requestPause(bundle);
                break;
            case InterEvent.CODE_REQUEST_RESUME:
                this.requestResume(bundle);
                break;
            case InterEvent.CODE_REQUEST_SEEK:
                this.requestSeek(bundle);
                break;
            case InterEvent.CODE_REQUEST_STOP:
                this.requestStop(bundle);
                break;
            case InterEvent.CODE_REQUEST_RESET:
                this.requestReset(bundle);
                break;
            case InterEvent.CODE_REQUEST_REPLAY:
                this.requestReplay(bundle);
                break;
            case InterEvent.CODE_REQUEST_PLAY_DATA_SOURCE:
                this.requestPlayDataSource(bundle);
                break;
            default:
                break;
        }
    }

    public T getAssist() {
        return mAssist;
    }
}

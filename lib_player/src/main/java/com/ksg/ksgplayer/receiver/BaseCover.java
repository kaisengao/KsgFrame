package com.ksg.ksgplayer.receiver;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IntRange;

import com.ksg.ksgplayer.assist.InterEvent;

/**
 * @author kaisengao
 * @create: 2019/1/29 16:55
 * @describe: 视图基类 （分级填充）
 */
public abstract class BaseCover extends BaseReceiver implements ICover, ICoverHandle, View.OnAttachStateChangeListener {

    private final View mCoverView;

    public BaseCover(Context context) {
        super(context);
        this.mCoverView = onCreateCoverView(context);
        if (mCoverView != null) {
            this.mCoverView.addOnAttachStateChangeListener(this);
        } else {
            throw new NullPointerException("Please add the layout file first!");
        }
    }

    /**
     * 创建View视图
     *
     * @param context 上下文
     * @return RootView
     */
    protected abstract View onCreateCoverView(Context context);

    protected final <T extends View> T findViewById(int id) {
        return mCoverView.findViewById(id);
    }

    @Override
    public final void setCoverVisibility(int visibility) {
        mCoverView.setVisibility(visibility);
    }

    @Override
    public View getView() {
        return mCoverView;
    }

    @Override
    public int getCoverLevel() {
        return ICover.COVER_LEVEL_LOW;
    }

    /**
     * 将优先级设置为“低覆盖级别”。
     * 高优先级保险将放在上面，
     * 否则，较低优先级将放在下面。
     *
     * @param priority range from 0-31
     * @return level
     */
    protected final int levelLow(@IntRange(from = 0, to = 31) int priority) {
        return levelPriority(ICover.COVER_LEVEL_LOW, priority);
    }

    /**
     * 将优先级设置为“中覆盖级别”。
     * 高优先级保险将放在上面，
     * 否则，较低优先级将放在下面。
     *
     * @param priority range from 0-31
     * @return level
     */
    protected final int levelMedium(@IntRange(from = 0, to = 31) int priority) {
        return levelPriority(ICover.COVER_LEVEL_MEDIUM, priority);
    }

    /**
     * 将优先级设置为“高覆盖级别”。
     * 高优先级保险将放在上面，
     * 否则，较低优先级将放在下面。
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

    @Override
    public final void requestOption(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_OPTION, bundle);
    }

    @Override
    public final void requestStart(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_START, bundle);
    }

    @Override
    public final void requestPause(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_PAUSE, bundle);
    }

    @Override
    public final void requestResume(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_RESUME, bundle);
    }

    @Override
    public final void requestSeek(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_SEEK, bundle);
    }

    @Override
    public final void requestStop(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_STOP, bundle);
    }

    @Override
    public final void requestReset(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_RESET, bundle);
    }

    @Override
    public final void requestReplay(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_REPLAY, bundle);
    }

    @Override
    public final void requestPlayDataSource(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_PLAY_DATA_SOURCE, bundle);
    }

    @Override
    public void requestAddEventProducer(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_EVENT_ADD_PRODUCER, bundle);
    }

    @Override
    public void requestRemoveEventProducer(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_EVENT_REMOVE_PRODUCER, bundle);
    }

    @Override
    public final void requestNotifyTimer() {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_NOTIFY_TIMER, null);
    }

    @Override
    public final void requestStopTimer() {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_STOP_TIMER, null);
    }

    @Override
    public final void onViewAttachedToWindow(View v) {
        onCoverAttachedToWindow();
    }

    @Override
    public final void onViewDetachedFromWindow(View v) {
        onCoverDetachedToWindow();
    }

    /**
     * 当视图依附到window时
     */
    protected void onCoverAttachedToWindow() {

    }

    /**
     * 当视图从window移除时
     */
    protected void onCoverDetachedToWindow() {

    }
}

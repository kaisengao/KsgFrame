package com.kasiengao.ksgframe.java.element;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.kasiengao.base.util.CommonUtil;
import com.kasiengao.base.util.KLog;

/**
 * @ClassName: MyLifecycleObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/26 14:13
 * @Description: 生命周期
 */
public abstract class MyLifecycleObserver implements LifecycleObserver {

    private final Lifecycle mLifecycle;

    public MyLifecycleObserver(Context context) {
        this.mLifecycle = CommonUtil.scanForActivity(context).getLifecycle();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onActivityResume() {
        KLog.d("onActivityResume");
        this.onAcResume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onActivityPause() {
        KLog.d("onActivityPause");
        this.onAcPause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onActivityDestroy() {
        KLog.d("onActivityDestroy");
        this.onAcDestroy();
    }

    public void addObserver() {
        this.mLifecycle.addObserver(this);
    }

    public void removeLifecycle() {
        this.mLifecycle.removeObserver(this);
    }

    protected abstract void onAcResume();

    protected abstract void onAcPause();

    protected abstract void onAcDestroy();

}

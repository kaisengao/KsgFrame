package com.kasiengao.ksgframe.java.observer;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.kasiengao.base.util.CommonUtil;

/**
 * @ClassName: MyLifecycleObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/26 14:13
 * @Description: 生命周期
 */
public abstract class MyLifecycleObserver implements LifecycleObserver {

    private final Lifecycle mLifecycle;

    protected MyLifecycleObserver(Lifecycle lifecycle) {
        this.mLifecycle = lifecycle;
    }

    protected MyLifecycleObserver(Fragment context) {
        this.mLifecycle = context.getLifecycle();
    }

    protected MyLifecycleObserver(AppCompatActivity context) {
        this.mLifecycle = context.getLifecycle();
    }

    protected MyLifecycleObserver(Context context) {
        this.mLifecycle = CommonUtil.scanForActivity(context).getLifecycle();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onActivityResume() {
        this.onAcResume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onActivityPause() {
        this.onAcPause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onActivityDestroy() {
        this.onAcDestroy();
    }

    public void addObserver() {
        this.mLifecycle.addObserver(this);
    }

    public void removeObserver() {
        this.mLifecycle.removeObserver(this);
    }

    protected abstract void onAcResume();

    protected abstract void onAcPause();

    protected abstract void onAcDestroy();
}

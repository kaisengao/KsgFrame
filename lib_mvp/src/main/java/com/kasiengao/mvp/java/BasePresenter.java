package com.kasiengao.mvp.java;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;

/**
 * @ClassName: BasePresenter
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 16:01
 * @Description:
 */
public class BasePresenter<V extends BaseContract.BaseView> implements BaseContract.BasePresenter<V> {

    private V mView;

    private WeakReference<V> mWeakReference;

    /**
     * 给子类使用的获取View的操作
     * 不允许复写
     *
     * @return View
     */
    public final V getView() {
        return this.mView;
    }

    @Override
    public void attachView(V rootView) {
        this.mView = rootView;
        this.mWeakReference = new WeakReference<>(rootView);
    }

    @Override
    public void detachView() {
        this.mView = null;
        this.mWeakReference.clear();
    }

    @NonNull
    protected Context getContext() {
        return this.mView.getContext();
    }

    @NonNull
    protected LifecycleOwner getLifecycleOwner() {
        return this.mView.getLifecycleOwner();
    }
}

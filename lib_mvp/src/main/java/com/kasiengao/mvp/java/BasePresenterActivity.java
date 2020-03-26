package com.kasiengao.mvp.java;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

/**
 * @ClassName: BasePresenterActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 16:00
 * @Description: 带有Presenter 的 Activity基类
 */
public abstract class BasePresenterActivity<Presenter extends BaseContract.BasePresenter> extends BaseToolbarActivity implements BaseContract.BaseView {

    protected Presenter mPresenter;

    @Override
    @SuppressWarnings("unchecked")
    protected void initBefore() {
        super.initBefore();

        this.mPresenter = initPresenter();
        if (this.mPresenter != null) {
            this.mPresenter.attachView(this);
        }
    }

    /**
     * 初始化Presenter
     *
     * @return Presenter
     */
    public abstract Presenter initPresenter();

    /**
     * 显示一个Toast
     *
     * @param message Toast信息
     */
    @Override
    public void showToast(String message) {
        super.showLongSafe(message);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.mPresenter != null) {
            this.mPresenter.detachView();
        }
    }
}

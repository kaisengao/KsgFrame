package com.kasiengao.mvp.java;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.kasiengao.base.util.ToastUtil;

/**
 * @ClassName: BasePresenterFragment
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 16:08
 * @Description: 带有 Presenter 的 Fragment基类
 */
public abstract class BasePresenterFragment<Presenter extends BaseContract.BasePresenter> extends BaseFragment implements BaseContract.BaseView {

    /**
     * 是否是第一次加载数据
     */
    private boolean isFirstLoad = true;

    protected Presenter mPresenter;

    @Override
    @SuppressWarnings("unchecked")
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.mPresenter = initPresenter();
        if (this.mPresenter != null) {
            this.mPresenter.attachView(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.isFirstLoad) {
            this.onFirstInit();
            this.isFirstLoad = false;
        }
    }

    /**
     * 当首次初始化数据的时候会调用的方法
     */
    protected abstract void onFirstInit();

    /**
     * 初始化Presenter
     *
     * @return Presenter
     */
    public abstract Presenter initPresenter();

    @Override
    public void showToast(String message) {
        ToastUtil.showLongSafe(message);
    }

    @Nullable
    @Override
    public Context getContext() {
        return super.getActivity();
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return super.getViewLifecycleOwner();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 销毁视图后 初始化懒加载
        this.isFirstLoad = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.mPresenter != null) {
            this.mPresenter.detachView();
        }
    }
}

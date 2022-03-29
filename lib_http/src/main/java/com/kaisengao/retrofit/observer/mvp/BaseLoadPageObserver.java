package com.kaisengao.retrofit.observer.mvp;

import android.content.Context;

import androidx.annotation.NonNull;

import com.kaisengao.base.factory.LoadPageFactory;
import com.kaisengao.base.listener.OnLoadReloadListener;
import com.kaisengao.base.loadpage.load.LoadingViewLoad;
import com.kaisengao.base.loadpage.load.base.ILoad;
import com.kaisengao.retrofit.observer.BaseRxObserver;

import io.reactivex.disposables.Disposable;

/**
 * @ClassName: BaseLoadPageObserver
 * @Author: KaiSenGao
 * @CreateDate: 2019-10-12 14:26
 * @Description: 页面Load
 */
public abstract class BaseLoadPageObserver<T> extends BaseRxObserver<T> implements OnLoadReloadListener {

    private final Object mRegister;

    private final LoadPageFactory mLoadPageFactory;

    private Class<? extends ILoad> mLoadingView = LoadingViewLoad.class;

    protected BaseLoadPageObserver(Context context, Object register) {
        super(context);
        this.mRegister = register;
        this.mLoadPageFactory = LoadPageFactory.getInstance();
        this.mLoadPageFactory.register(mContext, mRegister,this);
    }

    /**
     * 设置 Loading视图
     *
     * @param loadView LoadingView
     */
    public BaseRxObserver<T> setLoadView(final Class<? extends ILoad> loadView) {
        this.mLoadingView = loadView;
        return this;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        super.onSubscribe(d);
        this.mLoadPageFactory
                .showLoading(
                        mContext,
                        mRegister,
                        mLoadBgColor,
                        mLoadColor,
                        mContext.getString(mLoadMessage),
                        mLoadingView);
    }

    @Override
    public void onNext(@NonNull T t) {
        this.mLoadPageFactory.showSuccess(mContext, mRegister);
        super.onNext(t);
    }

    @Override
    protected void onError(String message) {
        super.onError(message);
        this.mLoadPageFactory.showError(mContext, mRegister, mLoadErrorIcon, mLoadBgColor, mLoadColor, message);
    }

    @Override
    public void onLoadReload(Object target) {
        this.onReload(target);
    }

    protected abstract void onReload(Object register);
}

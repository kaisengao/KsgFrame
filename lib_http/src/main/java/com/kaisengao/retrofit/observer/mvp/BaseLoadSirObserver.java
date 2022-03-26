package com.kaisengao.retrofit.observer.mvp;

import android.content.Context;

import com.kaisengao.retrofit.factory.LoadSirFactory;
import com.kaisengao.retrofit.listener.OnLoadSirReloadListener;
import com.kaisengao.retrofit.observer.BaseRxObserver;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.Disposable;

/**
 * @ClassName: BaseLoadSirObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:22
 * @Description: 绑定LoadSir的Observer
 */
public abstract class BaseLoadSirObserver<T> extends BaseRxObserver<T> implements OnLoadSirReloadListener {

    private final Object mTarget;

    private final LoadSirFactory mLoadSirFactory;

    protected BaseLoadSirObserver(Context context, Object target) {
        super(context);

        this.mTarget = target;

        this.mLoadSirFactory = LoadSirFactory.getInstance();

        this.mLoadSirFactory.register(this.mContext, this.mTarget);

        this.mLoadSirFactory.listener(context, this);
    }

    @Override
    public void onSubscribe(@NotNull Disposable d) {
        super.onSubscribe(d);
        this.mLoadSirFactory.showLoading(mContext, mTarget, mContext.getString(mLoadMessage), mLoadColor, mLoadBgColor);
    }

    @Override
    public void onNext(@NotNull T t) {
        // Success 恢复页面
        this.mLoadSirFactory.showSuccess(this.mContext, this.mTarget);
        // 回调
        super.onNext(t);
    }

    @Override
    protected void onError(String message) {
        this.mLoadSirFactory.showError(mContext, mTarget, message, mLoadColor, mLoadBgColor,mLoadErrorIcon);
    }

    @Override
    public void onLoadSirReload(Object target) {

        this.onReload(target);
    }

    protected abstract void onReload(Object target);
}

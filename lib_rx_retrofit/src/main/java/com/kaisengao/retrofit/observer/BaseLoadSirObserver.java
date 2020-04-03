package com.kaisengao.retrofit.observer;

import android.content.Context;

import com.kaisengao.retrofit.factory.LoadSirFactory;
import com.kaisengao.retrofit.listener.OnLoadSirReloadListener;

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
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);

        this.mLoadSirFactory
                .showLoading(
                        this.mContext,
                        this.mTarget,
                        this.mLoadingColor,
                        this.mBackgroundColor,
                        this.mContext.getString(this.mLoadingText));
    }

    @Override
    public void onNext(T t) {

        this.mLoadSirFactory.showSuccess(this.mContext, this.mTarget);

        super.onNext(t);
    }

    @Override
    protected void onError(int icon, int backgroundColor, int color, String message) {
        super.onError(icon, backgroundColor, color, message);

        this.mLoadSirFactory.showError(this.mContext, this.mTarget, icon, backgroundColor, color, message);
    }

    @Override
    public void onLoadSirReload(Object target) {

        this.onReload(target);
    }

    protected abstract void onReload(Object target);
}

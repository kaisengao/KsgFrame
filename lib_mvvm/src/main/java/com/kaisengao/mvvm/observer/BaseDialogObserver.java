package com.kaisengao.mvvm.observer;

import android.content.Context;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @ClassName: BaseDialogObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/18 14:09
 * @Description: MVVM Dialog Observer
 */
public abstract class BaseDialogObserver<T> implements Observer<T> {

    Context mContext;

    protected BaseDialogObserver(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T result) {
        this.onResult(result);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    /**
     * Result
     *
     * @param t Result
     */
    protected abstract void onResult(T t);

}

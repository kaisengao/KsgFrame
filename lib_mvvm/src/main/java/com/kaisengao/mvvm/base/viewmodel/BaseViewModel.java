package com.kaisengao.mvvm.base.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.kaisengao.mvvm.base.model.BaseModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @ClassName: ViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 10:33
 * @Description: MVVM BaseViewModel
 */
public abstract class BaseViewModel<M extends BaseModel> extends AndroidViewModel implements Consumer<Disposable> {

    private M mModel;

    private CompositeDisposable mDisposables;

    public BaseViewModel(@NonNull Application application) {
        this(application, null);
    }

    public BaseViewModel(@NonNull Application application, M model) {
        super(application);
        this.mModel = model;
    }

    public final M getModel() {
        return mModel;
    }

    /**
     * 添加 订阅
     */
    private void addSubscribe(Disposable disposable) {
        if (mDisposables == null) {
            this.mDisposables = new CompositeDisposable();
        }
        this.mDisposables.add(disposable);
    }

    /**
     * 绑定 订阅
     *
     * @param disposable disposable
     */
    @Override
    public void accept(Disposable disposable) {
        this.addSubscribe(disposable);
    }

    /**
     * 销毁所有资源
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        if (mModel != null) {
            this.mModel.onCleared();
            this.mModel = null;
        }
        // 取消所有订阅
        if (mDisposables != null) {
            this.mDisposables.clear();
            this.mDisposables = null;
        }
    }
}

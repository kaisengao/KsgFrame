package com.kaisengao.mvvm.base.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kaisengao.mvvm.event.SingleLiveEvent;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @ClassName: ViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 10:33
 * @Description: MVVM BaseViewModel
 */
public abstract class BaseViewModel extends AndroidViewModel implements Consumer<Disposable> {

    private CompositeDisposable mDisposables;

    public BaseViewModel(@NonNull Application application) {
        super(application);
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
     * 创建 MutableLiveData
     *
     * @param liveData liveData
     * @param <T>      泛型
     * @return MutableLiveData
     */
    protected <T> MutableLiveData<T> createLiveData(MutableLiveData<T> liveData) {
        return this.createLiveData(liveData, null);
    }

    /**
     * 创建 MutableLiveData
     *
     * @param liveData liveData
     * @param <T>      泛型
     * @return MutableLiveData
     */
    protected <T> MutableLiveData<T> createLiveData(MutableLiveData<T> liveData, T defaultT) {
        if (liveData == null) {
            liveData = new MutableLiveData<>(defaultT);
        }
        return liveData;
    }

    /**
     * 创建 SingleLiveEvent
     *
     * @param liveData liveData
     * @param <T>      泛型
     * @return SingleLiveEvent
     */
    protected <T> SingleLiveEvent<T> createSingleLiveData(SingleLiveEvent<T> liveData) {
        return this.createSingleLiveData(liveData, null);
    }

    /**
     * 创建 SingleLiveEvent
     *
     * @param liveData liveData
     * @param <T>      泛型
     * @return SingleLiveEvent
     */
    protected <T> SingleLiveEvent<T> createSingleLiveData(SingleLiveEvent<T> liveData, T defaultT) {
        if (liveData == null) {
            liveData = new SingleLiveEvent<>(defaultT);
        }
        return liveData;
    }

    /**
     * 销毁所有资源
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        // 取消所有订阅
        if (mDisposables != null) {
            this.mDisposables.clear();
            this.mDisposables = null;
        }
    }
}

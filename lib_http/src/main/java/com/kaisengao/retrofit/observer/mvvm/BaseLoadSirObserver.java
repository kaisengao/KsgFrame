package com.kaisengao.retrofit.observer.mvvm;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.kaisengao.retrofit.observer.BaseRxObserver;
import com.kaisengao.base.state.LoadState;
import com.kaisengao.base.state.LoadingState;

import io.reactivex.disposables.Disposable;

/**
 * @ClassName: BaseLoadSirObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/19 13:16
 * @Description: 绑定LoadSir的Observer
 */
public abstract class BaseLoadSirObserver<T> extends BaseRxObserver<T> {

    private final LoadingState mLoadState;

    private final MutableLiveData<LoadingState> mLoadingState;

    protected BaseLoadSirObserver(Context context, MutableLiveData<LoadingState> loadingState) {
        super(context);
        this.mLoadingState = loadingState;
        this.mLoadState = new LoadingState();
        // 初始化
        this.notify(LoadState.INITIAL, mLoadColor, mLoadBgColor, mContext.getString(mLoadMessage));
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        this.notify(LoadState.LOADING, mLoadColor, mLoadBgColor, mContext.getString(mLoadMessage));
    }

    @Override
    public void onNext(T t) {
        // Success 恢复页面
        this.notify(LoadState.SUCCESS, mLoadColor, mLoadBgColor, "Success");
        // 回调
        super.onNext(t);
    }

    @Override
    protected void onError(String message) {
        this.notify(LoadState.ERROR, mLoadColor, mLoadBgColor, mLoadErrorIcon, message);
    }

    /**
     * 通知
     */
    private void notify(@LoadState int loadState, int loadColor, int loadBgColor, String loadMessage) {
        this.notify(loadState, loadColor, loadBgColor, 0, loadMessage);
    }

    /**
     * 通知
     */
    private void notify(@LoadState int loadState, int loadColor, int loadBgColor, int loadErrorIcon, String loadMessage) {
        this.mLoadState.setLoadState(loadState);
        this.mLoadState.setLoadColor(loadColor);
        this.mLoadState.setLoadBgColor(loadBgColor);
        this.mLoadState.setLoadErrorIcon(loadErrorIcon);
        this.mLoadState.setLoadMessage(loadMessage);
        this.mLoadingState.setValue(mLoadState);
    }
}

package com.kaisengao.retrofit.observer.mvvm;

import androidx.lifecycle.MutableLiveData;

import com.kaisengao.base.factory.AppFactory;
import com.kaisengao.base.loadpage.load.LoadingViewLoad;
import com.kaisengao.base.loadpage.load.base.ILoad;
import com.kaisengao.base.state.LoadState;
import com.kaisengao.base.state.LoadingState;
import com.kaisengao.base.util.TextUtil;
import com.kaisengao.retrofit.observer.BaseRxObserver;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @ClassName: BaseLoadPageObserver
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/19 13:16
 * @Description: 绑定LoadPage的Observer
 */
public abstract class BaseLoadPageObserver<T> extends BaseRxObserver<T> {

    private final LoadingState mLoadState;

    private final MutableLiveData<LoadingState> mLoadingState;

    private Class<? extends ILoad> mLoadingView = LoadingViewLoad.class;

    protected BaseLoadPageObserver(MutableLiveData<LoadingState> loadingState) {
        this.mLoadingState = loadingState;
        this.mLoadState = new LoadingState();
        // 初始化
        this.notify(LoadState.INITIAL, mLoadColor, mLoadBgColor, TextUtil.getString(mLoadMessage));
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
        this.notify(LoadState.LOADING, mLoadColor, mLoadBgColor, AppFactory.application().getString(mLoadMessage));
    }

    @Override
    public void onNext(@NonNull T t) {
        // Success 恢复页面
        this.onSuccess();
        // 回调
        super.onNext(t);
    }

    @Override
    protected void onError(String message) {
        this.notify(LoadState.ERROR, mLoadColor, mLoadBgColor, mLoadErrorIcon, message);
    }

    /**
     * Success 恢复页面
     */
    public void onSuccess() {
        this.notify(LoadState.SUCCESS, mLoadColor, mLoadBgColor, "Success");
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
        this.mLoadState.setLoadingView(mLoadingView);
        this.mLoadingState.setValue(mLoadState);
    }
}

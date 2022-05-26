package com.kaisengao.ksgframe.ui.trainee.retrofit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.kaisengao.base.state.LoadingState;
import com.kaisengao.ksgframe.R;
import com.kaisengao.mvvm.binding.command.BindingParamImp;
import com.kaisengao.mvvm.viewmodel.ToolbarViewModel;
import com.kaisengao.retrofit.observer.BaseRxObserver;
import com.kaisengao.retrofit.observer.dialog.BaseDialogObserver;
import com.kaisengao.retrofit.observer.mvvm.BaseLoadPageObserver;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;

/**
 * @ClassName: RxRetrofitViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/2 21:45
 * @Description: Rx+Retrofit ViewModel
 */
public class RxRetrofitViewModel extends ToolbarViewModel {

    private MutableLiveData<LoadingState> mLoadState;

    private MutableLiveData<String> mData;

    private final RxRetrofitModel mModel;

    public RxRetrofitViewModel(@NonNull Application application) {
        super(application);
        this.mModel = new RxRetrofitModel();
    }

    /**
     * Init Toolbar
     */
    @Override
    protected void initToolbar() {
        // Toolbar Title
        this.setToolbarTitle(R.string.rx_retrofit_title);
    }

    /**
     * 请求（无提示）
     */
    public void requestNoHint() {
        this.requestNewsTop()
                .subscribe(new BaseRxObserver<NewsTopBean>() {
                    @Override
                    protected void onResult(@NotNull NewsTopBean newsTopBean) {
                        getData().setValue(newsTopBean.getReason());
                    }
                });
    }

    /**
     * 请求（Dialog）
     */
    public void requestDialog() {
        this.requestNewsTop()
                .subscribe(new BaseDialogObserver<NewsTopBean>() {
                    @Override
                    protected void onResult(@NotNull NewsTopBean newsTopBean) {
                        getData().setValue(newsTopBean.getReason());
                    }
                });
    }


    /**
     * 请求（LoadPage）
     */
    public void requestLoadPage() {
        this.requestNewsTop()
                .subscribe(new BaseLoadPageObserver<NewsTopBean>(getLoadState()) {
                    @Override
                    protected void onResult(@NotNull NewsTopBean newsTopBean) {
                        getData().setValue(newsTopBean.getReason());
                    }
                });
    }

    /**
     * 聚合数据 新闻
     */
    private Observable<NewsTopBean> requestNewsTop() {
        this.getData().setValue("请求中...");
        return mModel
                .requestNewsTop()
                .doOnSubscribe(this);
    }

    /**
     * Reload（LoadPage）
     */
    public final BindingParamImp<Object> mReloadImp = new BindingParamImp<>(param -> requestLoadPage());

    public MutableLiveData<LoadingState> getLoadState() {
        return mLoadState = createLiveData(mLoadState);
    }

    public MutableLiveData<String> getData() {
        return mData = createLiveData(mData);
    }
}
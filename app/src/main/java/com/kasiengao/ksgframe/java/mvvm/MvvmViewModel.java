package com.kasiengao.ksgframe.java.mvvm;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.kaisengao.mvvm.base.viewmodel.BaseViewModel;
import com.kaisengao.mvvm.emun.LoadState;
import com.kaisengao.mvvm.observer.BaseDialogObserver;
import com.kasiengao.ksgframe.java.retrofit.NewsTopBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @ClassName: MvvmViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/16 14:09
 * @Description:
 */
public class MvvmViewModel extends BaseViewModel {

    private final MvvmRepository mMvvmRepository;

    private MutableLiveData<String> mLiveData;

    private MutableLiveData<String> mLiveData2;

    private MutableLiveData<Integer> mLoadData;

    private MutableLiveData<Integer> mLoadData2;

    public MvvmViewModel(@NonNull Application application) {
        super(application);
        this.mMvvmRepository = new MvvmRepository();

    }

    public void onClick(View view) {
        getLoadData().setValue(LoadState.LOADING);

        Observable
                // 延时3秒
                .timer(3, TimeUnit.SECONDS)
                // 聚合数据
                .flatMap((Function<Long, ObservableSource<NewsTopBean>>) aLong -> mMvvmRepository.requestNewsTop())
                .doOnSubscribe(this)
                .subscribe(new BaseDialogObserver<NewsTopBean>(getApplication()) {
                    @Override
                    protected void onResult(NewsTopBean newsTopBean) {
                        getLoadData().setValue(LoadState.SUCCESS);
                        StringBuilder builder = new StringBuilder();
                        for (NewsTopBean.ResultBean.DataBean topBean : newsTopBean.getResult().getData()) {
                            builder.append(topBean.getAuthorName());
                            builder.append("\n");
                        }
                        getLiveData().setValue(builder.toString());
                    }
                });
    }

    public void onClick2(View view) {
        getLoadData2().setValue(LoadState.LOADING);

        Observable
                // 延时3秒
                .timer(5, TimeUnit.SECONDS)
                // 聚合数据
                .flatMap((Function<Long, ObservableSource<NewsTopBean>>) aLong -> mMvvmRepository.requestNewsTop())
                .doOnSubscribe(this)
                .subscribe(new BaseDialogObserver<NewsTopBean>(getApplication()) {
                    @Override
                    protected void onResult(NewsTopBean newsTopBean) {
                        getLoadData2().setValue(LoadState.SUCCESS);
                        StringBuilder builder = new StringBuilder();
                        for (NewsTopBean.ResultBean.DataBean topBean : newsTopBean.getResult().getData()) {
                            builder.append(topBean.getAuthorName());
                            builder.append("\n");
                        }
                        getLiveData2().setValue(builder.toString());
                    }
                });
    }

    public MutableLiveData<String> getLiveData() {
        return mLiveData = createLiveData(mLiveData);
    }

    public MutableLiveData<String> getLiveData2() {
        return mLiveData2 = createLiveData(mLiveData2);
    }

    public MutableLiveData<Integer> getLoadData() {
        return mLoadData = createLiveData(mLoadData);
    }

    public MutableLiveData<Integer> getLoadData2() {
        return mLoadData2 = createLiveData(mLoadData2);
    }
}

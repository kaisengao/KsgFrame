package com.kasiengao.ksgframe.java.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.kaisengao.mvvm.base.viewmodel.BaseViewModel;
import com.kaisengao.retrofit.observer.mvvm.BaseLoadSirObserver;
import com.kasiengao.base.loading.LoadingState;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.retrofit.NewsTopBean;

/**
 * @ClassName: MvvmViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/16 14:09
 * @Description:
 */
public class MvvmViewModel extends BaseViewModel {

    private final MvvmRepository mMvvmRepository;

    private MutableLiveData<String> mLiveData;

    private MutableLiveData<LoadingState> mLoadState1;

    private MutableLiveData<LoadingState> mLoadState2;

    public MvvmViewModel(@NonNull Application application) {
        super(application);
        this.mMvvmRepository = new MvvmRepository();
    }

    public void requestTest1() {

        this.mMvvmRepository
                .requestTest()
                .doOnSubscribe(this)
                .subscribe(new BaseLoadSirObserver<NewsTopBean>(getApplication(), getLoadState1()) {
                    @Override
                    protected void onResult(NewsTopBean newsTopBean) {
                        if (newsTopBean.getResultCode().equals("200")) {
                            StringBuilder builder = new StringBuilder();
                            for (NewsTopBean.ResultBean.DataBean topBean : newsTopBean.getResult().getData()) {
                                builder.append(topBean.getAuthorName());
                                builder.append("\n");
                            }
                            getLiveData().setValue(builder.toString());
                        } else {
                            onError(new Exception(newsTopBean.getReason()));
                        }
                    }
                }.setLoadMessage(R.string.loading_mvvm)
                        .setLoadColor(R.color.white)
                        .setLoadBgColor(R.color.color_F49B3C)
                        .setLoadErrorIcon(R.drawable.icon_empty));
    }

    public void requestTest2() {

        this.mMvvmRepository
                .requestTest()
                .doOnSubscribe(this)
                .subscribe(new BaseLoadSirObserver<NewsTopBean>(getApplication(), getLoadState2()) {
                    @Override
                    protected void onResult(NewsTopBean newsTopBean) {
                        if (newsTopBean.getResultCode().equals("200")) {
                            StringBuilder builder = new StringBuilder();
                            for (NewsTopBean.ResultBean.DataBean topBean : newsTopBean.getResult().getData()) {
                                builder.append(topBean.getAuthorName());
                                builder.append("\n");
                            }
                            getLiveData().setValue(builder.toString());
                        } else {
                            onError(new Exception(newsTopBean.getReason()));
                        }
                    }
                }.setLoadColor(R.color.black)
                        .setLoadBgColor(R.color.color_FF5252));
    }

    public MutableLiveData<String> getLiveData() {
        return mLiveData = createLiveData(mLiveData);
    }

    public MutableLiveData<LoadingState> getLoadState1() {
        return mLoadState1 = createLiveData(mLoadState1);
    }

    public MutableLiveData<LoadingState> getLoadState2() {
        return mLoadState2 = createLiveData(mLoadState2);
    }
}

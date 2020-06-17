package com.kasiengao.ksgframe.java.mvvm;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.kaisengao.mvvm.base.viewmodel.BaseViewModel;
import com.kaisengao.retrofit.api.ApiService;
import com.kaisengao.retrofit.observer.BaseRxObserver;
import com.kasiengao.base.util.KLog;
import com.kasiengao.ksgframe.java.mvvm.data.source.DataRepository;
import com.kasiengao.ksgframe.java.retrofit.NewsTopBean;

/**
 * @ClassName: MvvmViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/16 14:09
 * @Description:
 */
public class MvvmViewModel extends BaseViewModel<DataRepository> {

    private final MvvmRepository mMvvmRepository;

    private MutableLiveData<String> mLiveData = new MutableLiveData<>();

    public MvvmViewModel(@NonNull Application application) {
        super(application, Injection.provideDataRepository());
        // ApiService
        this.mMvvmRepository = new MvvmRepository(getModel().create(ApiService.class));
    }

    public MutableLiveData<String> getLiveData() {
        return mLiveData;
    }

    public void onClick(View view) {

        this.mMvvmRepository
                .requestNewsTop()
                .doOnSubscribe(this)
                .subscribe(new BaseRxObserver<NewsTopBean>(getApplication()) {
                    @Override
                    protected void onResult(NewsTopBean newsTopBean) {
                        KLog.d("zzz", "onResult");
                        StringBuilder builder = new StringBuilder();
                        for (NewsTopBean.ResultBean.DataBean topBean : newsTopBean.getResult().getData()) {
                            builder.append(topBean.getAuthorName());
                            builder.append("\n");
                        }
                        mLiveData.setValue(builder.toString());
                    }
                });

    }
}

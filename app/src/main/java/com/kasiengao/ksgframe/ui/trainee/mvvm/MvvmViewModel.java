package com.kasiengao.ksgframe.ui.trainee.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.kaisengao.mvvm.base.viewmodel.BaseViewModel;
import com.kaisengao.retrofit.observer.BaseRxObserver;
import com.kaisengao.retrofit.observer.dialog.BaseDialogObserver;
import com.kaisengao.retrofit.observer.mvvm.BaseLoadSirObserver;
import com.kaisengao.base.state.LoadingState;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.ui.trainee.retrofit.NewsTopBean;

/**
 * @ClassName: MvvmViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/16 14:09
 * @Description:
 */
public class MvvmViewModel extends BaseViewModel {

    private final MvvmModel mModel;

    private MutableLiveData<String> mLiveData;

    private MutableLiveData<LoadingState> mLoadState1;

    private MutableLiveData<LoadingState> mLoadState2;

    public MvvmViewModel(@NonNull Application application) {
        super(application);
        this.mModel = new MvvmModel();
    }

    public void requestTest1() {

        BaseRxObserver<NewsTopBean> loadSirObserver = new BaseLoadSirObserver<NewsTopBean>(getApplication(), getLoadState1()) {
            @Override
            protected void onResult(NewsTopBean newsTopBean) {
                resultData(this, newsTopBean);
            }
        }.setLoadMessage(R.string.loading_mvvm)
                .setLoadColor(R.color.white)
                .setLoadBgColor(R.color.color_F49B3C)
                .setLoadErrorIcon(R.drawable.icon_empty);

        this.requestTest(loadSirObserver);
    }

    public void requestTest2() {

        BaseRxObserver<NewsTopBean> loadSirObserver = new BaseLoadSirObserver<NewsTopBean>(getApplication(), getLoadState2()) {
            @Override
            protected void onResult(NewsTopBean newsTopBean) {
                resultData(this, newsTopBean);
            }
        }.setLoadColor(R.color.black)
                .setLoadBgColor(R.color.color_FF5252);

        this.requestTest(loadSirObserver);
    }

    public void requestTest3() {

        BaseRxObserver<NewsTopBean> loadSirObserver = new BaseDialogObserver<NewsTopBean>(getApplication()) {
            @Override
            protected void onResult(NewsTopBean newsTopBean) {
                resultData(this, newsTopBean);
            }
        }.setLoadMessage(R.string.loading_mvvm).setLoadColor(R.color.black);

        this.requestTest(loadSirObserver);
    }

    private void requestTest(BaseRxObserver<NewsTopBean> observer) {
        this.mModel
                .requestTest()
                .doOnSubscribe(this)
                .subscribe(observer);
    }

    private void resultData(BaseRxObserver<NewsTopBean> observer, NewsTopBean newsTopBean) {
        if (newsTopBean.getResult() != null) {
            StringBuilder builder = new StringBuilder();
            int count = 0;
            for (NewsTopBean.ResultBean.DataBean topBean : newsTopBean.getResult().getData()) {
                if (count > 5) {
                    break;
                }
                count++;
                builder.append(topBean.getAuthorName());
                builder.append("\n");
            }
            getLiveData().setValue(builder.toString());
        } else {
            observer.onError(new Exception(newsTopBean.getReason()));
        }
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

package com.kasiengao.ksgframe.ui.trainee.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.kaisengao.base.state.LoadingState;
import com.kaisengao.mvvm.viewmodel.ToolbarViewModel;
import com.kaisengao.retrofit.observer.mvvm.BaseLoadPageObserver;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.load.MVVMVideoLoad;
import com.kasiengao.ksgframe.ui.trainee.bean.VideoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @ClassName: MvvmViewModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/16 14:09
 * @Description: MVVM
 */
public class MvvmViewModel extends ToolbarViewModel {

    private final MvvmModel mModel;

    private MutableLiveData<LoadingState> mLoadState;

    private MutableLiveData<List<VideoBean>> mVideos;

    public MvvmViewModel(@NonNull Application application) {
        super(application);
        this.mModel = new MvvmModel();
    }

    /**
     * Init Toolbar
     */
    @Override
    protected void initToolbar() {
        this.setToolbarTitle(R.string.trainee_mvvm);
    }

    /**
     * 请求 视频列表
     */
    public void requestVideos() {
        this.mModel
                .requestVideos()
                .doOnSubscribe(this)
                .subscribe(new BaseLoadPageObserver<List<VideoBean>>(getApplication(), getLoadState()) {
                    @Override
                    protected void onResult(@NotNull List<VideoBean> videos) {
                        getVideos().setValue(videos);
                    }
                }.setLoadView(MVVMVideoLoad.class));
    }

    public MutableLiveData<LoadingState> getLoadState() {
        return mLoadState = createLiveData(mLoadState);
    }

    public MutableLiveData<List<VideoBean>> getVideos() {
        return mVideos = createLiveData(mVideos);
    }
}

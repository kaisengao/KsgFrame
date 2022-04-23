package com.kasiengao.ksgframe.ui.trainee.mvvm;

import com.google.gson.reflect.TypeToken;
import com.kaisengao.base.factory.AppFactory;
import com.kaisengao.base.util.CommonUtil;
import com.kaisengao.mvvm.base.model.BaseModel;
import com.kaisengao.retrofit.compose.RxCompose;
import com.kaisengao.retrofit.factory.GsonBuilderFactory;
import com.kasiengao.ksgframe.data.Injection;
import com.kasiengao.ksgframe.data.source.DataRepository;
import com.kasiengao.ksgframe.ui.trainee.bean.VideoBean;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * @ClassName: MvvmModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 14:10
 * @Description: MVVM
 */
public class MvvmModel extends BaseModel<DataRepository> {

    public MvvmModel() {
        super(Injection.provideDataRepository());
    }

    /**
     * 请求 预告视频列表
     *
     * @return 预告视频列表集合
     */
    public Observable<List<VideoBean>> requestVideos() {
        return Observable
                .create((ObservableOnSubscribe<List<VideoBean>>) emitter -> {
                    // 数据
                    String json = CommonUtil.getAssetsJson(AppFactory.application(), "VideosData.json");
                    // 解析
                    Type type = new TypeToken<List<VideoBean>>() {
                    }.getType();
                    List<VideoBean> videos = GsonBuilderFactory.getInstance().fromJson(json, type);
                    // Next
                    emitter.onNext(videos);
                    emitter.onComplete();
                })
                .delay(3000, TimeUnit.MILLISECONDS)
                .compose(RxCompose.applySchedulers());

    }
}

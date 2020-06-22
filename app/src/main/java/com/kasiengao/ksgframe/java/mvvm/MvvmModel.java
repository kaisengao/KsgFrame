package com.kasiengao.ksgframe.java.mvvm;

import com.kaisengao.mvvm.base.model.BaseModel;
import com.kaisengao.retrofit.RxCompose;
import com.kaisengao.retrofit.api.ApiService;
import com.kaisengao.retrofit.util.ParamsUtil;
import com.kasiengao.ksgframe.java.mvvm.data.source.DataRepository;
import com.kasiengao.ksgframe.java.retrofit.NewsTopBean;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @ClassName: MvvmModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 14:10
 * @Description: MVVM 数据
 */
public class MvvmModel extends BaseModel<DataRepository> {

    private final ApiService mApiService;

    public MvvmModel() {
        super(Injection.provideDataRepository());
        this.mApiService = mRepository.create(ApiService.class);
    }

    public Observable<NewsTopBean> requestTest() {

        return Observable
                // 延时
                .timer(2, TimeUnit.SECONDS)
                // 聚合数据
                .flatMap((Function<Long, ObservableSource<NewsTopBean>>) aLong -> requestNewsTop())
                .map(newsTopBean -> {
                    // 模拟错误
                    int random = (int) (2 * Math.random());
                    if (random == 1) {
                        Integer.parseInt("高晓松");
                    }
                    return newsTopBean;
                });
    }

    /**
     * 聚合数据 新闻
     */
    public Observable<NewsTopBean> requestNewsTop() {

        String url = "http://v.juhe.cn/toutiao/index";

        String appKey = "1ce0db9e8525d4818462ebe16fa1b810";

        HashMap<String, String> params = new ParamsUtil.HashBuilder()
                .put("key", appKey)
                .build();

        return this.mApiService.post(url, params).compose(RxCompose.fromJsonObj(NewsTopBean.class));
    }
}

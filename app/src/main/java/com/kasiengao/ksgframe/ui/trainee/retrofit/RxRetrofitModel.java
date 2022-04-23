package com.kasiengao.ksgframe.ui.trainee.retrofit;

import com.kaisengao.retrofit.RetrofitClient;
import com.kaisengao.retrofit.compose.RxCompose;
import com.kaisengao.retrofit.api.ApiService;
import com.kaisengao.retrofit.util.ParamsUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * @ClassName: RxRetrofitModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:56
 * @Description: Rx+Retrofit Model
 */
public class RxRetrofitModel {

    private static final String API = "http://v.juhe.cn/toutiao/index";

    private final ApiService mApiService;

    public RxRetrofitModel() {
        this.mApiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    /**
     * 聚合数据 新闻
     */
    public Observable<NewsTopBean> requestNewsTop() {
        HashMap<String, String> params = new ParamsUtil.HashBuilder()
                .put("key", "1ce0db9e8525d4818462ebe16fa1b810")
                .build();

        return mApiService
                .post(API, params)
                .delay(2000, TimeUnit.MILLISECONDS)
                .compose(RxCompose.handleJsonObj(NewsTopBean.class));
    }
}

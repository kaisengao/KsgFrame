package com.kasiengao.ksgframe.ui.trainee.retrofit;

import com.kaisengao.retrofit.RetrofitClient;
import com.kaisengao.retrofit.RxCompose;
import com.kaisengao.retrofit.api.ApiService;
import com.kaisengao.retrofit.util.ParamsUtil;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * @ClassName: RxRetrofitModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:56
 * @Description: Rx+Retrofit Model
 */
public class RxRetrofitModel {

    private final ApiService mApiService;

    public RxRetrofitModel() {

        this.mApiService = RetrofitClient.getInstance().create(ApiService.class);
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

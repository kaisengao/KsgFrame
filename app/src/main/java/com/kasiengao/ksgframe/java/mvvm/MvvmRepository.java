package com.kasiengao.ksgframe.java.mvvm;

import com.kaisengao.mvvm.base.model.BaseModel;
import com.kaisengao.retrofit.RxCompose;
import com.kaisengao.retrofit.api.ApiService;
import com.kaisengao.retrofit.util.ParamsUtil;
import com.kasiengao.ksgframe.java.mvvm.data.source.DataRepository;
import com.kasiengao.ksgframe.java.retrofit.NewsTopBean;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * @ClassName: MvvmRepository
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 14:10
 * @Description: MVVM 数据仓库
 */
public class MvvmRepository extends BaseModel<DataRepository> {

    private final ApiService mApiService;

    public MvvmRepository() {
        super(Injection.provideDataRepository());
        this.mApiService = mRepository.create(ApiService.class);
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

package com.kaisengao.retrofit.api;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @ClassName: ApiService
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:35
 * @Description: Api
 */
public interface ApiService {

    /**
     * 通用 post请求
     *
     * @param url    接口地址
     * @param params 请求参数
     * @return Observable
     */
    @POST
    @FormUrlEncoded
    Observable<String> post(@Url String url, @FieldMap Map<String, String> params);
}

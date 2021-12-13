package com.kaisengao.retrofit;

import android.annotation.SuppressLint;

import com.kaisengao.retrofit.factory.GsonBuilderFactory;
import com.kaisengao.retrofit.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @ClassName: RetrofitClient
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 17:55
 * @Description: Retrofit网络请求
 */
public class RetrofitClient {

    /**
     * 请求 超时时间 (默认30秒)
     */
    private final static int TIMEOUT = 30;

    private final static int WRITE_TIMEOUT = 60;

    private Retrofit mRetrofit;

    private HashMap<Class, Object> mApiServices;

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 静态单例（只是比较喜欢这种方式而已）
     */
    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    /**
     * 静态构造
     */
    private RetrofitClient() {

        this.mRetrofit = createRetrofit();

        this.mApiServices = new HashMap<>();
    }

    /**
     * 创建  Retrofit
     *
     * @return Retrofit
     */
    private Retrofit createRetrofit() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                // 连接超时时长
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                // 读取超时时长
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                // 写入超时时长
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("http://vfx.mtime.cn/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilderFactory.getInstance().getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    /**
     * 返回一个请求代理
     *
     * @return service
     */
    @SuppressWarnings("unchecked")
    public <service> service create(final Class<service> apiService) {

        if (apiService == null) {
            throw new RuntimeException("Api service is null!");
        }

        service service = (service) mApiServices.get(apiService);

        if (service == null) {

            service = mRetrofit.create(apiService);

            // 存储集合中
            this.mApiServices.put(apiService, service);
        }

        return service;
    }
}

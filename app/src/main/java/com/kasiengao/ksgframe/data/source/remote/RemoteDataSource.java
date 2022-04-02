package com.kasiengao.ksgframe.data.source.remote;

import com.kaisengao.retrofit.RetrofitClient;
import com.kasiengao.ksgframe.data.source.DataSource;

/**
 * @ClassName: RemoteDataSource
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 13:37
 * @Description: 远程 数据仓库
 */
public class RemoteDataSource implements DataSource.RemoteDataSource {

    private static RemoteDataSource INSTANCE;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private RemoteDataSource() {
    }

    /**
     * 创建 远程接口类
     *
     * @param service      ApiService
     * @param <ApiService> ApiService
     * @return ApiService
     */
    @Override
    public <ApiService> ApiService create(Class<ApiService> service) {
        return RetrofitClient.getInstance().create(service);
    }
}

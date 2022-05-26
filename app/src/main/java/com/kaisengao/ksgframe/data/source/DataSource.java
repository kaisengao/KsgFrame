package com.kaisengao.ksgframe.data.source;

/**
 * @ClassName: DataSource
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 13:39
 * @Description: 数据仓库对外接口
 */
public interface DataSource {

    interface RemoteDataSource {

        /**
         * 创建 远程接口类
         *
         * @param service      ApiService
         * @param <ApiService> ApiService
         * @return ApiService
         */
        <ApiService> ApiService create(Class<ApiService> service);
    }

    interface LocalDataSource {

    }

}

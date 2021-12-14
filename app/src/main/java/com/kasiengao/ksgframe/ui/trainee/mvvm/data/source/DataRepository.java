package com.kasiengao.ksgframe.ui.trainee.mvvm.data.source;

import androidx.annotation.NonNull;

/**
 * @ClassName: DataRepository
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 13:44
 * @Description: 数据仓库
 */
public final class DataRepository implements DataSource.RemoteDataSource, DataSource.LocalDataSource {

    private volatile static DataRepository INSTANCE = null;

    private final DataSource.RemoteDataSource mRemoteDataSource;

    private final DataSource.LocalDataSource mLocalDataSource;

    /**
     * 私有构造
     */
    private DataRepository(@NonNull DataSource.RemoteDataSource remoteDataSource,
                           @NonNull DataSource.LocalDataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    /**
     * 创建单例
     *
     * @param remoteDataSource 远程数据
     * @param localDataSource  本地数据
     * @return the {@link DataRepository} instance
     */
    public static DataRepository getInstance(DataSource.RemoteDataSource remoteDataSource,
                                             DataSource.LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (DataRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataRepository(remoteDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
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
        return this.mRemoteDataSource.create(service);
    }
}

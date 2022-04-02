package com.kasiengao.ksgframe.data;

import com.kasiengao.ksgframe.data.source.DataRepository;
import com.kasiengao.ksgframe.data.source.remote.RemoteDataSource;

/**
 * @ClassName: Injection
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 14:04
 * @Description: 注入
 */
public class Injection {

    public static DataRepository provideDataRepository() {
        return DataRepository.getInstance(RemoteDataSource.getInstance(), null);
    }

}

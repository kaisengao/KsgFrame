package com.kaisengao.ksgframe.data;

import com.kaisengao.ksgframe.data.source.DataRepository;
import com.kaisengao.ksgframe.data.source.remote.RemoteDataSource;

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

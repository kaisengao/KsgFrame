package com.kasiengao.ksgframe.ui.trainee.mvvm;

import com.kasiengao.ksgframe.ui.trainee.mvvm.data.source.DataRepository;
import com.kasiengao.ksgframe.ui.trainee.mvvm.data.source.remote.RemoteDataSource;

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

package com.kaisengao.mvvm.base.model;

/**
 * @ClassName: BaseModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 13:21
 * @Description: MVVM BaseModel
 */
public class BaseModel<Repository> {

    protected final Repository mRepository;

    public BaseModel(Repository repository) {
        this.mRepository = repository;
    }

}

package com.kaisengao.mvvm.binding.command;

/**
 * @ClassName: BindingAction
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/23 10:47
 * @Description:
 */
public interface BindingParam<T> {

    void call(T param);
}

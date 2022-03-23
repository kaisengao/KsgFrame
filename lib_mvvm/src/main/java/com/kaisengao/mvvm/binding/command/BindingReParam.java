package com.kaisengao.mvvm.binding.command;

/**
 * @ClassName: BindingReParam
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/23 11:27
 * @Description:
 */
public interface BindingReParam<R, T> {

    R call(T param);

}

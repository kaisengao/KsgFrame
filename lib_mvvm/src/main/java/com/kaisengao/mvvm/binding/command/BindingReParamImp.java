package com.kaisengao.mvvm.binding.command;

/**
 * @ClassName: BindingReParamIml
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/23 11:29
 * @Description:
 */
public class BindingReParamImp<R, T> {

    private final BindingReParam<R, T> mExecute;

    public BindingReParamImp(BindingReParam<R, T> execute) {
        this.mExecute = execute;
    }

    /**
     * 执行带泛型参数的命令
     *
     * @param param 泛型参数
     */
    public R execute(T param) {
        if (mExecute != null) {
            return mExecute.call(param);
        }
        return null;
    }
}

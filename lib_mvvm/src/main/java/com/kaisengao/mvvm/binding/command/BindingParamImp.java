package com.kaisengao.mvvm.binding.command;

/**
 * @ClassName: BindingCommand
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/23 10:47
 * @Description:
 */
public class BindingParamImp<T> {

    private final BindingParam<T> mExecute;

    public BindingParamImp(BindingParam<T> execute) {
        this.mExecute = execute;
    }

    /**
     * 执行带泛型参数的命令
     *
     * @param param 泛型参数
     */
    public void execute(T param) {
        if (mExecute != null) {
            this.mExecute.call(param);
        }
    }
}

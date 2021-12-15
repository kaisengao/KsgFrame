package com.kaisengao.base.loadpage.load;

/**
 * @ClassName: EmptyViewLoad
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/15 13:29
 * @Description: Empty视图
 */
public class EmptyViewLoad extends BaseLoad {

    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected boolean onClickEvent() {
        return true;
    }

}
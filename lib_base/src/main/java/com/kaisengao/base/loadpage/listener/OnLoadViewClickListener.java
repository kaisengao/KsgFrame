package com.kaisengao.base.loadpage.listener;

import com.kaisengao.base.loadpage.load.ILoad;

/**
 * @ClassName: OnLoadViewClickListener
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 17:25
 * @Description: LoadView 点击事件
 */
public interface OnLoadViewClickListener {

    /**
     * 点击事件
     *
     * @param target 绑定的目标
     * @param load   LoadView
     */
    void onLoadClick(Object target, ILoad load);
}

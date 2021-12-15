package com.kaisengao.base.loadpage.load.base;

import android.content.Context;
import android.view.View;

import com.kaisengao.base.loadpage.listener.OnLoadViewClickListener;

/**
 * @ClassName: ILoad
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/15 16:04
 * @Description:
 */
public interface ILoad {

    /**
     * Init
     *
     * @param context               context
     * @param target                绑定的目标
     * @param loadViewClickListener loadViewClickListener
     */
    void init(Context context, Object target, OnLoadViewClickListener loadViewClickListener);

    /**
     * 获取RootView
     *
     * @return LoadView的RootView
     */
    View getRootView();
}
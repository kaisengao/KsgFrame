package com.kaisengao.base.loadpage.load;

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
     * 获取Load的RootView
     *
     * @param context               context
     * @param target                绑定的目标
     * @param loadViewClickListener loadViewClickListener
     * @return RootView
     */
    View getRootView(Context context, Object target, OnLoadViewClickListener loadViewClickListener);
}
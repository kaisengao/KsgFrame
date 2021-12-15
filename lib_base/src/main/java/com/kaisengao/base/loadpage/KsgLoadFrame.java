package com.kaisengao.base.loadpage;

import android.view.ViewGroup;

import com.kaisengao.base.loadpage.helper.TargetHelper;
import com.kaisengao.base.loadpage.listener.OnLoadViewClickListener;
import com.kaisengao.base.loadpage.widget.LoadContainer;

/**
 * @ClassName: LoadFrame
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 17:00
 * @Description: 加载框架
 */
public class KsgLoadFrame {

    private KsgLoadFrame() {
    }

    /**
     * 绑定Load容器
     *
     * @param target 绑定的目标
     * @return {@link LoadContainer}
     */
    public static LoadContainer bindLoadContainer(final Object target) {
        // Return
        return bindLoadContainer(target, null);
    }

    /**
     * 绑定Load容器
     *
     * @param target                绑定的目标
     * @param loadViewClickListener loadView点击事件
     * @return {@link LoadContainer}
     */
    public static LoadContainer bindLoadContainer(final Object target,
                                                  final OnLoadViewClickListener loadViewClickListener) {
        // 获取Target的父亲
        ViewGroup targetParent = TargetHelper.getTargetParent(target);
        // 创建Load容器视图
        LoadContainer container = new LoadContainer(target, targetParent);
        // Bind点击事件
        container.setLoadViewClickListener(loadViewClickListener);
        // Return
        return container;
    }
}
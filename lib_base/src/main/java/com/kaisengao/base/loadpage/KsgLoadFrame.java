package com.kaisengao.base.loadpage;

import com.kaisengao.base.loadpage.helper.TargetHelper;
import com.kaisengao.base.loadpage.listener.OnLoadViewClickListener;
import com.kaisengao.base.loadpage.load.BaseLoad;
import com.kaisengao.base.loadpage.widget.LoadContainer;

/**
 * @ClassName: LoadFrame
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/14 17:00
 * @Description: 加载框架
 */
public class KsgLoadFrame {

    private static volatile KsgLoadFrame sInstance;

    public static KsgLoadFrame getInstance() {
        if (sInstance == null) {
            synchronized (KsgLoadFrame.class) {
                if (sInstance == null) {
                    sInstance = new KsgLoadFrame();
                }
            }
        }
        return sInstance;
    }

    private KsgLoadFrame() {
    }

    /**
     * 绑定Load容器
     *
     * @param target 绑定的目标
     * @return {@link LoadContainer}
     */
    public LoadContainer bindLoadContainer(Object target) {
        // Return
        return bindLoadContainer(target, null, null);
    }

    /**
     * 绑定Load容器
     *
     * @param target                绑定的目标
     * @param loadViewClickListener loadView点击事件
     * @return {@link LoadContainer}
     */
    public LoadContainer bindLoadContainer(Object target, OnLoadViewClickListener loadViewClickListener) {
        // Return
        return bindLoadContainer(target, null, loadViewClickListener);
    }

    /**
     * 绑定Load容器
     *
     * @param target                绑定的目标
     * @param loadViewClickListener loadView点击事件
     * @param defaultLoad           默认Load
     * @return {@link LoadContainer}
     */
    public LoadContainer bindLoadContainer(final Object target,
                                           final Class<? extends BaseLoad> defaultLoad,
                                           final OnLoadViewClickListener loadViewClickListener) {
        // 获取Load容器视图
        LoadContainer loadContainer = TargetHelper.getLoadContainer(target);
        // 默认Load
        loadContainer.setDefaultLoad(defaultLoad);
        // Bind点击事件
        loadContainer.setLoadViewClickListener(loadViewClickListener);
        // Return
        return loadContainer;
    }
}
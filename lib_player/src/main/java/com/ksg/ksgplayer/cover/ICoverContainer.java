package com.ksg.ksgplayer.cover;

import android.view.ViewGroup;

/**
 * @ClassName: ICoverContainer
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 16:20
 * @Description: 覆盖组件视图类
 */
public interface ICoverContainer {

    /**
     * 添加组件
     *
     * @param cover 组件
     */
    void addCover(BaseCover cover);

    /**
     * 移除组件
     *
     * @param cover 组件
     */
    void removeCover(BaseCover cover);

    /**
     * 移除所有组件
     */
    void removeAllCovers();

    /**
     * 获取容器RootView
     *
     * @return RootView
     */
    ViewGroup getRootView();
}

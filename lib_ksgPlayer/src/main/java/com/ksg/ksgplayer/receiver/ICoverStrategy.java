package com.ksg.ksgplayer.receiver;

import android.view.ViewGroup;

/**
 * @author kaisengao
 * @create: 2019/1/29 17:05
 * @describe: 视图 策略模式
 */
public interface ICoverStrategy {

    /**
     * 添加视图
     *
     * @param cover 视图
     */
    void addCover(BaseCover cover);

    /**
     * 删除视图
     *
     * @param cover 视图
     */
    void removeCover(BaseCover cover);

    /**
     * 删除所有视图
     */
    void removeAllCovers();

    /**
     * 判断是否存在某个视图
     *
     * @param cover 视图
     * @return true/false
     */
    boolean isContainsCover(BaseCover cover);

    /**
     * 获取视图数量
     *
     * @return int
     */
    int getCoverCount();

    /**
     * 获取容器RootView
     *
     * @return RootView
     */
    ViewGroup getContainerView();

    /**
     * 开启 状态栏、导航栏适配
     */
    void setFitsSystemWindows();

    /**
     * 清除 状态栏 导航栏 适配 默认添加的padding
     */
    void clearFitsSystemWindows();
}

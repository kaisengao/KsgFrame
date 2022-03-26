package com.ksg.ksgplayer.cover;

import java.util.Map;

/**
 * @ClassName: IReceiverGroup
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 16:41
 * @Description: 覆盖组件管理器
 */
public interface ICoverManager {

    /**
     * Add 覆盖组件
     *
     * @param key   key
     * @param cover cover
     */
    void addCover(String key, ICover cover);

    /**
     * Remove 覆盖组件
     *
     * @param key key
     */
    void removeCover(String key);

    /**
     * 获取覆盖组件
     *
     * @param key key
     */
    ICover getCover(String key);

    /**
     * 获取所有覆盖组件
     */
    Map<String, ICover> getAllCover();

    /**
     * 清空覆盖组件
     */
    void clearCover();


    /**
     * 获取 数据池
     *
     * @return {@link CoverValuePool}
     */
    CoverValuePool getValuePool();

    /**
     * 清空 数据池
     */
    void clearValuePool();

    /**
     * 循环
     *
     * @param onLoopListener onLoopListener
     */
    void forEach(OnLoopListener onLoopListener);

    /**
     * 带有过滤器的循环
     *
     * @param filter         过滤器
     * @param onLoopListener onLoopListener
     */
    void forEach(OnCoverFilter filter, OnLoopListener onLoopListener);

    /**
     * 绑定 Cover添加移除事件
     *
     * @param coverAttachStateChangeListener listener
     */
    void addCoverAttachStateChangeListener(OnCoverAttachStateChangeListener coverAttachStateChangeListener);

    /**
     * 解绑 Cover添加移除事件
     *
     * @param coverAttachStateChangeListener listener
     */
    void removeCoverAttachStateChangeListener(OnCoverAttachStateChangeListener coverAttachStateChangeListener);

    /**
     * Cover添加移除事件
     */
    interface OnCoverAttachStateChangeListener {

        /**
         * 添加事件
         *
         * @param key   key
         * @param cover cover
         */
        void onAttached(String key, ICover cover);

        /**
         * 移除事件
         *
         * @param key   key
         * @param cover cover
         */
        void onDetached(String key, ICover cover);
    }

    /**
     * 循环回调
     */
    interface OnLoopListener {

        /**
         * 寻找每个
         *
         * @param cover cover
         */
        void onEach(ICover cover);
    }

    /**
     * 过滤器
     */
    interface OnCoverFilter {

        /**
         * 过滤器
         *
         * @param cover cover
         * @return True/False
         */
        boolean filter(ICover cover);
    }
}

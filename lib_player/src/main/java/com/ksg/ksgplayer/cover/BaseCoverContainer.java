package com.ksg.ksgplayer.cover;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: BaseCoverContainer
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 16:18
 * @Description: 覆盖组件视图类
 */
public abstract class BaseCoverContainer implements ICoverContainer {

    private final ViewGroup mRootView;

    private final List<BaseCover> mCovers;

    public BaseCoverContainer(Context context) {
        this.mCovers = new ArrayList<>();
        this.mRootView = new FrameLayout(context);
        this.mRootView.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * 添加组件
     *
     * @param cover 组件
     */
    @Override
    public void addCover(BaseCover cover) {
        if (isAvailableCover(cover)) {
            // 添加组件
            this.onCoverAdd(cover);
            // 存入集合
            this.mCovers.add(cover);
        }
    }

    /**
     * 移除组件
     *
     * @param cover 组件
     */
    @Override
    public void removeCover(BaseCover cover) {
        if (isAvailableCover(cover) && mCovers.contains(cover)) {
            // 移除组件
            this.onCoverRemove(cover);
            // 移除集合
            this.mCovers.remove(cover);
        }
    }

    /**
     * 移除所有组件
     */
    @Override
    public void removeAllCovers() {
        // 移除所有组件
        this.onCoversRemoveAll();
        // 清空集合
        this.mCovers.clear();
    }

    /**
     * 判断组件是否存在
     *
     * @param cover 组件
     * @return true/false
     */
    private boolean isAvailableCover(BaseCover cover) {
        return cover != null && cover.getCoverView() != null;
    }

    /**
     * 获取容器RootView
     *
     * @return RootView
     */
    @Override
    public ViewGroup getRootView() {
        return mRootView;
    }

    /**
     * 添加组件
     *
     * @param cover 组件
     */
    protected abstract void onCoverAdd(BaseCover cover);

    /**
     * 移除组件
     *
     * @param cover 组件
     */
    protected abstract void onCoverRemove(BaseCover cover);

    /**
     * 移除所有组件
     */
    protected abstract void onCoversRemoveAll();
}

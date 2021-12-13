package com.ksg.ksgplayer.receiver;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kaisengao
 * @create: 2019/1/29 17:08
 * @describe: 视图容器 基类
 */
public abstract class BaseCoverContainer implements ICoverStrategy {

    Context mContext;

    /**
     * 视图容器
     */
    private ViewGroup mContainerRoot;

    /**
     * 视图集合
     */
    private List<BaseCover> mCovers;

    BaseCoverContainer(Context context) {
        this.mContext = context;
        mCovers = new ArrayList<>();
        mContainerRoot = initContainerRootView();
    }

    /**
     * 初始化视图
     *
     * @return rootView
     */
    protected abstract ViewGroup initContainerRootView();

    @Override
    public void addCover(BaseCover cover) {
        if (isAvailableCover(cover)) {
            // 将视图加入集合中
            mCovers.add(cover);
            // 添加视图
            onCoverAdd(cover);
        }
    }

    /**
     * 添加视图
     *
     * @param cover 视图
     */
    protected abstract void onCoverAdd(BaseCover cover);

    @Override
    public void removeCover(BaseCover cover) {
        if (isAvailableCover(cover)) {
            // 将视图从集合中删除
            mCovers.remove(cover);
            // 删除视图
            onCoverRemove(cover);
        }
    }

    /**
     * 删除视图
     *
     * @param cover 视图
     */
    protected abstract void onCoverRemove(BaseCover cover);

    @Override
    public void removeAllCovers() {
        // 清空视图集合
        mCovers.clear();
        // 清空视图
        onCoversRemoveAll();
    }

    /**
     * 清空视图
     */
    protected abstract void onCoversRemoveAll();

    /**
     * 判断图层是否有效
     *
     * @return true/false
     */
    private boolean isAvailableCover(BaseCover cover) {
        return cover != null && cover.getView() != null;
    }

    @Override
    public boolean isContainsCover(BaseCover cover) {
        if (!isAvailableCover(cover)) {
            return false;
        }
        int index = rootIndexOfChild(cover.getView());
        if (index != -1) {
            return true;
        }
        int childCount = getRootChildCount();
        if (childCount <= 0) {
            return false;
        }
        boolean result = false;
        for (int i = 0; i < childCount; i++) {
            View view = rootGetChildAt(i);
            if (view instanceof ViewGroup && ((ViewGroup) view).indexOfChild(cover.getView()) != -1) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public int getCoverCount() {
        if (mCovers == null) {
            return 0;
        }
        return mCovers.size();
    }

    /**
     * 得到view在父容器中的位置下标
     *
     * @param view view
     */
    private int rootIndexOfChild(View view) {
        if (mContainerRoot == null) {
            return -1;
        }
        return mContainerRoot.indexOfChild(view);
    }

    /**
     * 得到子view数量
     *
     * @return int
     */
    private int getRootChildCount() {
        if (mContainerRoot == null) {
            return 0;
        }
        return mContainerRoot.getChildCount();
    }

    /**
     * 得到指定位置的view视图
     *
     * @param index 视图位置
     * @return 0
     */
    private View rootGetChildAt(int index) {
        if (mContainerRoot == null) {
            return null;
        }
        return mContainerRoot.getChildAt(index);
    }

    @Override
    public ViewGroup getContainerView() {
        return mContainerRoot;
    }
}

package com.ksg.ksgplayer.cover;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @ClassName: DefaultLevelCoverContainer
 * @Author: KaiSenGao
 * @CreateDate: 2022/2/25 16:19
 * @Description: 默认级别视图容器
 */
public class DefaultLevelCoverContainer extends BaseLevelCoverContainer {

    /**
     * 低级别覆盖容器.
     */
    private FrameLayout mLowContainer;
    /**
     * 中级别覆盖容器.
     */
    private FrameLayout mMediumContainer;
    /**
     * 高级别覆盖容器.
     */
    private FrameLayout mHighContainer;

    public DefaultLevelCoverContainer(Context context) {
        super(context);
    }

    /**
     * Init 级别视图容器
     */
    @Override
    protected void initLevelContainers(Context context) {
        // 添加低级别容器
        this.mLowContainer = new FrameLayout(context);
        this.mLowContainer.setBackgroundColor(Color.TRANSPARENT);
        this.addLevelView(mLowContainer, null);
        // 添加中级别容器
        this.mMediumContainer = new FrameLayout(context);
        this.mMediumContainer.setBackgroundColor(Color.TRANSPARENT);
        this.addLevelView(mMediumContainer, null);
        // 添加高级别容器
        this.mHighContainer = new FrameLayout(context);
        this.mHighContainer.setBackgroundColor(Color.TRANSPARENT);
        this.addLevelView(mHighContainer, null);
    }

    @Override
    protected void onCoverAdd(BaseCover cover) {
        // 获取视图的级别
        int level = cover.getCoverLevel();
        if (level < ICover.COVER_LEVEL_MEDIUM) {
            // Low Level Cover 低等级
            this.mLowContainer.addView(cover.getCoverView(), getNewMatchLayoutParams());
        } else if (level < ICover.COVER_LEVEL_HIGH) {
            // Medium Level Cover  中等级
            this.mMediumContainer.addView(cover.getCoverView(), getNewMatchLayoutParams());
        } else {
            // High Level Cover  高等级
            this.mHighContainer.addView(cover.getCoverView(), getNewMatchLayoutParams());
        }
    }

    @Override
    protected void onCoverRemove(BaseCover cover) {
        this.mLowContainer.removeView(cover.getCoverView());
        this.mMediumContainer.removeView(cover.getCoverView());
        this.mHighContainer.removeView(cover.getCoverView());
    }

    @Override
    protected void onCoversRemoveAll() {
        this.mLowContainer.removeAllViews();
        this.mMediumContainer.removeAllViews();
        this.mHighContainer.removeAllViews();
    }

    /**
     * 默认 LayoutParams
     */
    private ViewGroup.LayoutParams getNewMatchLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}

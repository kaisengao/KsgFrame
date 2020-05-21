package com.ksg.ksgplayer.receiver;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author kaisengao
 * @create: 2019/1/29 17:33
 * @describe: 默认级别视图容器管理器
 */
public class DefaultLevelCoverContainer extends BaseLevelCoverContainer {

    /**
     * 低级别覆盖容器.
     */
    private FrameLayout mLevelLowCoverContainer;
    /**
     * 中级别覆盖容器.
     */
    private FrameLayout mLevelMediumCoverContainer;
    /**
     * 高级别覆盖容器.
     */
    private FrameLayout mLevelHighCoverContainer;

    public DefaultLevelCoverContainer(Context context) {
        super(context);
    }

    @Override
    protected void initLevelContainers(Context context) {
        // 添加低级别容器
        mLevelLowCoverContainer = new FrameLayout(context);
        mLevelLowCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addLevelContainerView(mLevelLowCoverContainer, null);

        // 添加中级别容器
        mLevelMediumCoverContainer = new FrameLayout(context);
        mLevelMediumCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addLevelContainerView(mLevelMediumCoverContainer, null);

        // 添加高级别容器
        mLevelHighCoverContainer = new FrameLayout(context);
        mLevelHighCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addLevelContainerView(mLevelHighCoverContainer, null);
    }

    @Override
    protected void onCoverAdd(BaseCover cover) {
        // 获取视图的级别
        int level = cover.getCoverLevel();

        if (level < ICover.COVER_LEVEL_MEDIUM) {
            // Low Level Cover 低等级
            mLevelLowCoverContainer.addView(cover.getView(), getNewMatchLayoutParams());
        } else if (level < ICover.COVER_LEVEL_HIGH) {
            // Medium Level Cover  中等级
            mLevelMediumCoverContainer.addView(cover.getView(), getNewMatchLayoutParams());
        } else {
            // High Level Cover  高等级
            mLevelHighCoverContainer.addView(cover.getView(), getNewMatchLayoutParams());
        }
    }

    @Override
    protected void onCoverRemove(BaseCover cover) {
        mLevelLowCoverContainer.removeView(cover.getView());
        mLevelMediumCoverContainer.removeView(cover.getView());
        mLevelHighCoverContainer.removeView(cover.getView());
    }

    @Override
    protected void onCoversRemoveAll() {
        mLevelLowCoverContainer.removeAllViews();
        mLevelMediumCoverContainer.removeAllViews();
        mLevelHighCoverContainer.removeAllViews();
    }

    /**
     * 开启 状态栏 导航栏 适配
     */
    @Override
    public void setFitsSystemWindows() {
        getContainerView().setFitsSystemWindows(true);
    }

    /**
     * 清除 状态栏 导航栏 适配 默认添加的padding
     */
    @Override
    public void clearFitsSystemWindows() {
        getContainerView().setPadding(0, 0, 0, 0);
    }

    /**
     * 默认 LayoutParams
     */
    private ViewGroup.LayoutParams getNewMatchLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}

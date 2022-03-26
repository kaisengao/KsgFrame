package com.ksg.ksgplayer.cover;

import android.content.Context;
import android.view.ViewGroup;

/**
 * @author kaisengao
 * @create: 2019/1/29 17:31
 * @describe: 覆盖组件视图类
 */
public abstract class BaseLevelCoverContainer extends BaseCoverContainer {

    public BaseLevelCoverContainer(Context context) {
        super(context);
        // Init 级别视图容器
        this.initLevelContainers(context);
    }

    /**
     * Init 级别视图容器
     */
    protected abstract void initLevelContainers(Context context);

    /**
     * AddLevelView
     */
    protected void addLevelView(ViewGroup container, ViewGroup.LayoutParams layoutParams) {
        if (getRootView() != null) {
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            this.getRootView().addView(container, layoutParams);
        }
    }
}

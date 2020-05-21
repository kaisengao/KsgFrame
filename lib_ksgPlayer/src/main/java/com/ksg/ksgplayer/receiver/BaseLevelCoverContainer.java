package com.ksg.ksgplayer.receiver;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author kaisengao
 * @create: 2019/1/29 17:31
 * @describe: 视图级别容器 基类
 */
public abstract class BaseLevelCoverContainer extends BaseCoverContainer {

    BaseLevelCoverContainer(Context context) {
        super(context);
        initLevelContainers(context);
    }

    @Override
    protected ViewGroup initContainerRootView() {
        FrameLayout root = new FrameLayout(mContext);
        root.setBackgroundColor(Color.TRANSPARENT);
        return root;
    }

    /**
     * 初始化图层
     *
     * @param context 上下文
     */
    protected abstract void initLevelContainers(Context context);

    /**
     * 将图层add入 RootView
     */
    void addLevelContainerView(ViewGroup container, ViewGroup.LayoutParams layoutParams) {
        if (getContainerView() != null) {
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            getContainerView().addView(container, layoutParams);
        }
    }
}

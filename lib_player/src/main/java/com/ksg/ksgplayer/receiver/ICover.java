package com.ksg.ksgplayer.receiver;

import android.view.View;

/**
 * @author kaisengao
 * @create: 2019/1/29 16:41
 * @describe:
 */
public interface ICover {

    /**
     * 等级最大覆盖优先级值  32
     */
    int LEVEL_MAX = 1 << 5;

    /**
     * 最低等级
     */
    int COVER_LEVEL_LOW = 0;

    /**
     * 中等级 32
     */
    int COVER_LEVEL_MEDIUM = 1 << 5;

    /**
     * 高等级 64
     */
    int COVER_LEVEL_HIGH = 1 << 6;

    /**
     * 组件 显示隐藏
     *
     * @param visibility visible/gone
     */
    void setCoverVisibility(int visibility);

    /**
     * 获取组件 RootView
     *
     * @return rootView
     */
    View getView();

    /**
     * 获取组件等级
     *
     * @return level
     */
    int getCoverLevel();
}

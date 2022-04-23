package com.ksg.ksgplayer.config;

/**
 * @ClassName: AspectRatio
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 11:14
 * @Description: 画面比例
 */
public interface AspectRatio {

    /**
     * 自定义比例
     */
    int RATIO_CUSTOM = -1;

    /**
     * 默认显示比例
     */
    int RATIO_DEFAULT = 0;

    /**
     * 16:9
     */
    int RATIO_16_9 = 1;

    /**
     * 4:3
     */
    int RATIO_4_3 = 2;

    /**
     * 18:9
     */
    int RATIO_18_9 = 6;

    /**
     * 全屏裁减显示
     */
    int RATIO_FULL = 4;

    /**
     * 全屏拉伸显示
     */
    int RATIO_MATCH_FULL = -4;

}

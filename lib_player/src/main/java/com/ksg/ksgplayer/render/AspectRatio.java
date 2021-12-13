package com.ksg.ksgplayer.render;

import java.io.Serializable;

/**
 * @ClassName: AspectRatio
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 11:14
 * @Description: 宽高比 （视频显示的效果）
 */
public enum AspectRatio implements Serializable {
    AspectRatio_16_9,
    AspectRatio_4_3,
    AspectRatio_MATCH_PARENT,
    AspectRatio_FILL_PARENT,
    AspectRatio_FIT_PARENT,
    AspectRatio_ORIGIN,
    AspectRatio_FILL_WIDTH,
    AspectRatio_FILL_HEIGHT
}

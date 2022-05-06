package com.ksg.ksgplayer.config;

import com.ksg.ksgplayer.renderer.RendererType;

/**
 * @ClassName: PlayerConfig
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/21 13:48
 * @Description: 配置文件
 */
public class PlayerConfig {

    /**
     * 渲染器
     */
    private static int sRendererType = RendererType.TEXTURE;

    /**
     * 获取 渲染器
     *
     * @return {@link RendererType}
     */
    public static int getRendererType() {
        return sRendererType;
    }

    /**
     * 设置 渲染器
     *
     * @param rendererType {@link RendererType}
     */
    public static void setRendererType(int rendererType) {
        sRendererType = rendererType;
    }
}
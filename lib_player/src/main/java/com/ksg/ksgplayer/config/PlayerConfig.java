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
    private static int sRenderType = RendererType.TEXTURE;

    /**
     * 获取 渲染器
     *
     * @return {@link RendererType}
     */
    public static int getRenderType() {
        return sRenderType;
    }

    /**
     * 设置 渲染器
     *
     * @param renderType {@link RendererType}
     */
    public static void setRenderType(int renderType) {
        sRenderType = renderType;
    }
}
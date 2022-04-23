package com.ksg.ksgplayer.renderer;

/**
 * @ClassName: RendererType
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/21 16:49
 * @Description:
 */
public interface RendererType {

    /**
     * TextureView
     */
    int TEXTURE = 0;

    /**
     * SurfaceView
     */
    int SURFACE = 1;

    /**
     * GLSurfaceView
     */
    int GL_SURFACE = 2;

}

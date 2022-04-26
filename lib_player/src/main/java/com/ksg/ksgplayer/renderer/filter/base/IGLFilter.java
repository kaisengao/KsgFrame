package com.ksg.ksgplayer.renderer.filter.base;

/**
 * @ClassName: IGLFilter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/25 16:36
 * @Description:
 */
public interface IGLFilter {

    void onSurfaceCreated();

    void onSurfaceChanged(int width, int height);

    void onSurfaceDrawFrame(int textureId);

    void release();
}
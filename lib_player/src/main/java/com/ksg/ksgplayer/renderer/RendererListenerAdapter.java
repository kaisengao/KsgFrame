package com.ksg.ksgplayer.renderer;

import android.view.Surface;

import com.ksg.ksgplayer.listener.OnRendererListener;

/**
 * @ClassName: RendererListenerAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/22 15:16
 * @Description:
 */
public abstract class RendererListenerAdapter implements RendererListener {

    protected OnRendererListener mRendererListener;

    /**
     * 渲染器事件
     *
     * @param rendererListener rendererListener
     */
    public void setRendererListener(OnRendererListener rendererListener) {
        this.mRendererListener = rendererListener;
    }

    /**
     * Surface 创建
     */
    @Override
    public void onSurfaceCreated(Surface surface, int width, int height) {

    }

    /**
     * Surface 改变
     */
    @Override
    public void onSurfaceChanged(Surface surface, int width, int height) {

    }

    /**
     * Surface 销毁
     */
    @Override
    public void onSurfaceDestroy(Surface surface) {

    }

}
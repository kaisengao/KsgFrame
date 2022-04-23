package com.ksg.ksgplayer.renderer;

import android.graphics.Bitmap;
import android.view.Surface;

import com.ksg.ksgplayer.listener.OnRendererListener;

/**
 * @ClassName: RendererListenerAdapter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/22 15:16
 * @Description:
 */
public abstract class RendererListenerAdapter implements RendererListener {

    private OnRendererListener mRendererListener;

    /**
     * 渲染器事件
     *
     * @param rendererListener rendererListener
     */
    public void setRendererListener(OnRendererListener rendererListener) {
        this.mRendererListener = rendererListener;
    }

    /**
     * 截图
     *
     * @param bitmap bitmap
     */
    @Override
    public void onShotPic(Bitmap bitmap) {
        if (mRendererListener != null) {
            this.mRendererListener.onShotPic(bitmap);
        }
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
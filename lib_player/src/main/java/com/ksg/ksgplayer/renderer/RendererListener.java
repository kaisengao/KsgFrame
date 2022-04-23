package com.ksg.ksgplayer.renderer;

import android.graphics.Bitmap;
import android.view.Surface;

/**
 * @ClassName: RendererListener
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/22 15:18
 * @Description:
 */
public interface RendererListener {

    /**
     * 截图
     *
     * @param bitmap bitmap
     */
    void onShotPic(Bitmap bitmap);

    /**
     * Surface 创建
     */
    void onSurfaceCreated(Surface surface, int width, int height);

    /**
     * Surface 改变
     */
    void onSurfaceChanged(Surface surface, int width, int height);

    /**
     * Surface 销毁
     */
    void onSurfaceDestroy(Surface surface);

}

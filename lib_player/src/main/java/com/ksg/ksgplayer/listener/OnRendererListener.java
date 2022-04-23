package com.ksg.ksgplayer.listener;

import android.graphics.Bitmap;

/**
 * @ClassName: OnRendererListener
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/22 15:52
 * @Description: 渲染器事件
 */
public interface OnRendererListener {

    /**
     * 截图
     *
     * @param bitmap bitmap
     */
    void onShotPic(Bitmap bitmap);
}
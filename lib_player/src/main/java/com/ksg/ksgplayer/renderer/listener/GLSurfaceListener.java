package com.ksg.ksgplayer.renderer.listener;

import android.graphics.SurfaceTexture;

/**     
 * @ClassName: GLSurfaceListener
 * @Author: KaiSenGao  
 * @CreateDate: 2022/4/23 20:32
 * @Description: 
 */
public interface GLSurfaceListener {

    void onSurfaceAvailable(SurfaceTexture surfaceTexture);
}
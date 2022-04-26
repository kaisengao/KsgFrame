package com.ksg.ksgplayer.renderer.glrender;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.os.Looper;

import com.ksg.ksgplayer.config.WeakHandler;
import com.ksg.ksgplayer.renderer.RendererListener;
import com.ksg.ksgplayer.renderer.listener.GLSurfaceListener;
import com.ksg.ksgplayer.renderer.utils.OpenGLESUtils;

import javax.microedition.khronos.opengles.GL10;

/**
 * @ClassName: BaseGLViewRender
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/23 13:45
 * @Description: GLRender
 */
public abstract class BaseGLViewRender implements GLSurfaceView.Renderer {

    protected int mVideoWidth = 0, mVideoHeight = 0;

    protected boolean mTakeShotPic = false;

    protected GLSurfaceView mSurfaceView;

    protected GLSurfaceListener mGLSurfaceListener;

    protected RendererListener mRendererListener;

    private final WeakHandler mHandler = new WeakHandler(Looper.getMainLooper());

    public void setSurfaceView(final GLSurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
    }

    public void setGLSurfaceListener(GLSurfaceListener glSurfaceListener) {
        this.mGLSurfaceListener = glSurfaceListener;
    }

    public void setRendererListener(RendererListener listener) {
        this.mRendererListener = listener;
    }

    public void onSurfaceAvailable(final SurfaceTexture surfaceTexture) {
        if (mGLSurfaceListener != null) {
            this.mHandler.post(() -> mGLSurfaceListener.onSurfaceAvailable(surfaceTexture));
        }
    }

    public void setVideoInfo(int videoWidth, int videoHeight) {
        this.mVideoWidth = videoWidth;
        this.mVideoHeight = videoHeight;
    }

    public int getViewWidth() {
        return mSurfaceView.getWidth();
    }

    public int getViewHeight() {
        return mSurfaceView.getHeight();
    }

    @Override
    public final void onDrawFrame(GL10 gl) {
        this.onSurfaceDrawFrame(gl);
        // 截图
        this.takeShotPic(gl);
    }

    /**
     * 截图
     */
    public void onShotPic() {
        this.mTakeShotPic = true;
        this.mSurfaceView.requestRender();
    }

    /**
     * 截图
     *
     * @param gl GL10
     * @describe: 此方法必须在最后执行
     */
    protected void takeShotPic(GL10 gl) {
        if (mTakeShotPic) {
            this.mTakeShotPic = false;
            if (mRendererListener != null) {
                this.onShotPicCallback(gl);
            }
        }
    }

    /**
     * 截图 回调
     */
    protected void onShotPicCallback(GL10 gl) {
        this.onShotPicCallback(OpenGLESUtils.createBitmap(
                0, 0, getViewWidth(), getViewHeight(), gl));
    }

    /**
     * 截图 回调
     */
    protected void onShotPicCallback(Bitmap bitmap) {
        if (mRendererListener != null) {
            this.mHandler.post(() -> mRendererListener.onShotPic(bitmap));
        }
    }

    public abstract void onSurfaceDrawFrame(GL10 gl);

    public abstract void release();
}
package com.ksg.ksgplayer.renderer.glrender;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Looper;
import android.view.Surface;

import com.ksg.ksgplayer.config.WeakHandler;
import com.ksg.ksgplayer.renderer.RendererListener;
import com.ksg.ksgplayer.renderer.filter.GLFilter;
import com.ksg.ksgplayer.renderer.listener.GLSurfaceListener;

import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * @ClassName: FrameBufferObjectRenderer
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/23 13:45
 * @Description: 帧缓冲区对象渲染器
 */
public abstract class BaseGLViewRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    protected int mCurrViewWidth = 0, mCurrViewHeight = 0;

    protected int mCurrVideoWidth = 0, mCurrVideoHeight = 0;

    protected float[] mMVPMatrix = new float[16];

    protected float[] mSTMatrix = new float[16];

    protected boolean mShotHigh = false;

    protected boolean mTakeShotPic = false;

    protected boolean mChangeProgram = false;

    protected boolean mChangeProgramSupportError = false;

    protected GLSurfaceView mSurfaceView;

    protected GLSurfaceListener mGLSurfaceListener;

    protected RendererListener mRendererListener;

    protected final WeakHandler mHandler = new WeakHandler(Looper.getMainLooper());

    public void initRenderSize() {
        if (mCurrViewWidth != 0 && mCurrViewHeight != 0) {
            Matrix.scaleM(mMVPMatrix, 0, (float) mCurrViewWidth / mSurfaceView.getWidth(),
                    (float) mCurrViewHeight / mSurfaceView.getHeight(), 1);
        }
    }

    public void setSurfaceView(final GLSurfaceView surfaceView) {
        this.mSurfaceView = surfaceView;
    }

    public void onSurfaceAvailable(final Surface surface) {
        if (mGLSurfaceListener != null) {
            this.mHandler.post(() -> mGLSurfaceListener.onSurfaceAvailable(surface));
        }
    }

    public void setGLSurfaceListener(GLSurfaceListener glSurfaceListener) {
        this.mGLSurfaceListener = glSurfaceListener;
    }

    public void setRendererListener(RendererListener listener) {
        this.mRendererListener = listener;
    }

    public float[] getMVPMatrix() {
        return mMVPMatrix;
    }

    public void setMVPMatrix(float[] MVPMatrix) {
        this.mMVPMatrix = MVPMatrix;
    }

    public void setCurrViewWidth(int currViewWidth) {
        this.mCurrViewWidth = currViewWidth;
    }

    public void setCurrViewHeight(int currViewHeight) {
        this.mCurrViewHeight = currViewHeight;
    }

    public void setCurrVideoWidth(int currVideoWidth) {
        this.mCurrVideoWidth = currVideoWidth;
    }

    public void setCurrVideoHeight(int currVideoHeight) {
        this.mCurrVideoHeight = currVideoHeight;
    }

    /**
     * 设置 滤镜效果
     *
     * @param filter filter
     */
    public void setGLFilter(GLFilter filter) {

    }

    /**
     * 获取 当前滤镜
     *
     * @return {@link GLFilter}
     */
    public GLFilter getGLFilter() {
        return null;
    }

    /**
     * 截图
     *
     * @param shotHigh 高清/普通
     */
    public void onShotPic(boolean shotHigh) {
        this.mShotHigh = shotHigh;
        this.mTakeShotPic = true;
    }

    /**
     * 截图
     *
     * @param glUnused GL10
     * @describe: 此方法必须在最后执行
     */
    protected void onShotPic(GL10 glUnused) {
        if (mTakeShotPic) {
            this.mTakeShotPic = false;
            if (mRendererListener != null) {
                Bitmap bitmap = createBitmap(mSurfaceView.getWidth(), mSurfaceView.getHeight(), glUnused);
                this.mHandler.post(() -> mRendererListener.onShotPic(bitmap));
            }
        }
    }

    /**
     * 生成截图
     */
    private Bitmap createBitmap(int w, int h, GL10 gl) {
        int[] bitmapBuffer = new int[w * h];
        int[] bitmapSource = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);
        try {
            gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    int texturePixel = bitmapBuffer[offset1 + j];
                    int blue = (texturePixel >> 16) & 0xff;
                    int red = (texturePixel << 16) & 0x00ff0000;
                    int pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            return null;
        }
        if (mShotHigh) {
            return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
        } else {
            return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.RGB_565);
        }
    }

    public abstract void release();
}
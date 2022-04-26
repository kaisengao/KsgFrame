package com.ksg.ksgplayer.renderer.filter.base;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ksg.ksgplayer.renderer.listener.GLSurfaceListener;
import com.ksg.ksgplayer.renderer.utils.OpenGLESUtils;

/**
 * @ClassName: GLOesFilter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/25 17:07
 * @Description:
 */
public class GLOesFilter extends GLFilter {

    /**
     * oes纹理id
     */
    private int mOesTextureId;

    /**
     * 顶点变换矩阵位置
     */
    private int uMatrixLocation;

    /**
     * 纹理变换矩阵位置
     */
    private int uOesMatrixLocation;

    /**
     * 顶点变换矩阵
     */
    private float[] mMVPMatrix = new float[16];

    /**
     * 纹理变换矩阵
     */
    private float[] mOesMatrix = new float[16];

    private boolean isReadyToDraw = false;

    private SurfaceTexture mSurfaceTexture;

    private GLSurfaceListener mGLSurfaceListener;

    public GLOesFilter(Context context) {
        this(context, "render/base/oes/vertex.frag", "render/base/oes/frag.frag");
    }

    public GLOesFilter(Context context, String vertexFilename, String fragFilename) {
        super(context, vertexFilename, fragFilename);
        this.init();
    }

    private void init() {
        this.setBindFbo(true);
        this.mOesTextureId = OpenGLESUtils.getOesTexture();
        Matrix.setIdentityM(mOesMatrix, 0);
        Matrix.setIdentityM(mMVPMatrix, 0);
    }

    @Override
    public void release() {
        super.release();
        this.onDeleteTexture(mOesTextureId);
    }

    /**
     * 初始化纹理坐标
     */
    @Override
    public void onInitCoordinateBuffer() {
        this.setCoordinateBuffer(OpenGLESUtils.getSquareCoordinateBuffer());
    }

    /**
     * 绘制准备
     */
    @Override
    public boolean onReadyToDraw() {
        if (!isReadyToDraw) {
            if (mGLSurfaceListener != null) {
                if (mSurfaceTexture != null) {
                    this.mSurfaceTexture.release();
                    this.mSurfaceTexture = null;
                }
                this.mSurfaceTexture = new SurfaceTexture(mOesTextureId);
                this.mGLSurfaceListener.onSurfaceAvailable(mSurfaceTexture);
                this.isReadyToDraw = true;
            } else if (mSurfaceTexture != null) {
                this.mSurfaceTexture.attachToGLContext(mOesTextureId);
                this.isReadyToDraw = true;
            }
        }
        return isReadyToDraw;
    }

    /**
     * 绘制之前
     */
    @Override
    public void onDrawPre() {
        super.onDrawPre();
        this.mSurfaceTexture.updateTexImage();
        this.mSurfaceTexture.getTransformMatrix(mOesMatrix);
    }

    /**
     * 初始化着色器
     */
    @Override
    public void onInitLocation() {
        super.onInitLocation();
        this.uMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "uMatrix");
        this.uOesMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "uOesMatrix");
    }

    /**
     * 激活并绑定纹理
     */
    @Override
    public void onActiveTexture(int textureId) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        GLES20.glUniform1i(getSamplerLocation(), 0);
    }

    /**
     * 设置其他数据
     */
    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(uOesMatrixLocation, 1, false, mOesMatrix, 0);
    }

    /**
     * 绘制
     */
    public void onDrawSelf() {
        super.onSurfaceDrawFrame(mOesTextureId);
    }

    /**
     * SurfaceTexture 事件
     */
    public void setGLSurfaceListener(GLSurfaceListener surfaceListener) {
        this.mGLSurfaceListener = surfaceListener;
    }

    public int getMatrixLocation() {
        return uMatrixLocation;
    }

    public int getOesMatrixLocation() {
        return uOesMatrixLocation;
    }

    public float[] getMVPMatrix() {
        return mMVPMatrix;
    }

    public void setMVPMatrix(float[] MVPMatrix) {
        this.mMVPMatrix = MVPMatrix;
    }

    public float[] getOesMatrix() {
        return mOesMatrix;
    }

    public void setOesMatrix(float[] oesMatrix) {
        this.mOesMatrix = oesMatrix;
    }
}
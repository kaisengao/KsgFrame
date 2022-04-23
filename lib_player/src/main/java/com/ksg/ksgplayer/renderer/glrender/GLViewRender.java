package com.ksg.ksgplayer.renderer.glrender;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.text.TextUtils;
import android.view.Surface;

import com.ksg.ksgplayer.renderer.filter.GLFilter;
import com.ksg.ksgplayer.renderer.filter.NoFilter;
import com.ksg.ksgplayer.renderer.utils.OpenGLESUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @ClassName: GLViewRenderer
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/23 13:58
 * @Description:
 */
public class GLViewRender extends BaseGLViewRender {

    private static final int FLOAT_SIZE_BYTES = 4;

    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;

    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;

    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;

    private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    private int mProgram;

    private int mUMVPMatrixHandle;

    private int mUSTMatrixHandle;

    private int mAPositionHandle;

    private int mATextureHandle;

    private final int[] mTextureID = new int[2];

    private boolean mUpdateSurface = false;

    private final FloatBuffer mTriangleVertices;

    private SurfaceTexture mSurfaceTexture;

    private GLFilter mFilter = new NoFilter();

    public GLViewRender() {
        float[] triangleVerticesData = {
                // X, Y, Z, U, V
                -1.0f, -1.0f, 0.0f,
                0.0f, 0.0f, 1.0f,
                -1.0f, 0.0f, 1.0f,
                0.0f, -1.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 1.0f,};
        this.mTriangleVertices = ByteBuffer
                .allocateDirect(triangleVerticesData.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        this.mTriangleVertices.put(triangleVerticesData).position(0);

        Matrix.setIdentityM(mSTMatrix, 0);
        Matrix.setIdentityM(mMVPMatrix, 0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.mProgram = OpenGLESUtils.createProgram(getVertexShader(), getFragmentShader());
        if (mProgram == 0) {
            return;
        }

        this.mAPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        OpenGLESUtils.checkGlError("glGetAttribLocation aPosition");
        if (mAPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }

        this.mATextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        OpenGLESUtils.checkGlError("glGetAttribLocation aTextureCoord");
        if (mATextureHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }

        this.mUMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        OpenGLESUtils.checkGlError("glGetUniformLocation uMVPMatrix");
        if (mUMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }

        this.mUSTMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uSTMatrix");
        OpenGLESUtils.checkGlError("glGetUniformLocation uSTMatrix");
        if (mUSTMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uSTMatrix");
        }

        GLES20.glGenTextures(2, mTextureID, 0);

        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mTextureID[0]);
        OpenGLESUtils.checkGlError("glBindTexture mTextureID");

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        this.mSurfaceTexture = new SurfaceTexture(mTextureID[0]);
        this.mSurfaceTexture.setOnFrameAvailableListener(this);

        this.onSurfaceAvailable(new Surface(mSurfaceTexture));
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this) {
            if (mUpdateSurface) {
                this.mSurfaceTexture.updateTexImage();
                this.mSurfaceTexture.getTransformMatrix(mSTMatrix);
                this.mUpdateSurface = false;
            }
        }
        this.initDrawFrame();
        this.bindDrawFrameTexture();
        this.initPointerAndDraw();
        this.onShotPic(gl);
        GLES20.glFinish();
    }

    @Override
    synchronized public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.mUpdateSurface = true;
    }

    protected void initDrawFrame() {
        if (mChangeProgram) {
            this.mProgram = OpenGLESUtils.createProgram(getVertexShader(), getFragmentShader());
            this.mChangeProgram = false;
        }
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);
        OpenGLESUtils.checkGlError("glUseProgram");
    }

    protected void bindDrawFrameTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mTextureID[0]);
    }

    protected void initPointerAndDraw() {
        this.mTriangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
        GLES20.glVertexAttribPointer(mAPositionHandle, 3, GLES20.GL_FLOAT,
                false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES,
                mTriangleVertices);
        OpenGLESUtils.checkGlError("glVertexAttribPointer maPosition");

        GLES20.glEnableVertexAttribArray(mAPositionHandle);
        OpenGLESUtils.checkGlError("glEnableVertexAttribArray maPositionHandle");

        this.mTriangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
        GLES20.glVertexAttribPointer(mATextureHandle, 3, GLES20.GL_FLOAT,
                false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES,
                mTriangleVertices);
        OpenGLESUtils.checkGlError("glVertexAttribPointer maTextureHandle");

        GLES20.glEnableVertexAttribArray(mATextureHandle);
        OpenGLESUtils.checkGlError("glEnableVertexAttribArray maTextureHandle");

        GLES20.glUniformMatrix4fv(mUMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mUSTMatrixHandle, 1, false, mSTMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        OpenGLESUtils.checkGlError("glDrawArrays");
    }

    /**
     * 设置 滤镜效果
     *
     * @param filter filter
     */
    @Override
    public void setGLFilter(GLFilter filter) {
        if (filter != null) {
            this.mFilter = filter;
        }
        this.mChangeProgram = true;
        this.mChangeProgramSupportError = true;
    }

    /**
     * 获取 当前滤镜
     *
     * @return {@link GLFilter}
     */
    @Override
    public GLFilter getGLFilter() {
        return mFilter;
    }

    public int getProgram() {
        return mProgram;
    }

    public int getUMVPMatrixHandle() {
        return mUMVPMatrixHandle;
    }

    public int getUSTMatrixHandle() {
        return mUSTMatrixHandle;
    }

    protected String getVertexShader() {
        String vertexShader = mFilter.getVertexShader();
        if (TextUtils.isEmpty(vertexShader)) {
            return NoFilter.DEFAULT_VERTEX;
        }
        return vertexShader;
    }

    protected String getFragmentShader() {
        return mFilter.getFragmentShader();
    }

    @Override
    public void release() {

    }
}
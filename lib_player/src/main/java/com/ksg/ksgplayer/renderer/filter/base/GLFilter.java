package com.ksg.ksgplayer.renderer.filter.base;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;

import android.content.Context;
import android.opengl.GLES20;

import com.ksg.ksgplayer.renderer.utils.OpenGLESUtils;

import java.nio.FloatBuffer;

/**
 * @ClassName: GLFilter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/23 21:05
 * @Description: 滤镜
 */
public class GLFilter implements IGLFilter {

    private final Context mContext;
    /**
     * fbo id
     */
    private int mFboId = -1;

    /**
     * vbo id
     */
    private int mVboId;

    /**
     * 纹理 id
     */
    private int mTextureId;

    /**
     * fbo纹理id
     */
    private int mFboTextureId;

    /**
     * vertex shader
     */
    private int mVertexShader;

    /**
     * frag shader
     */
    private int mFragShader;

    /**
     * program
     */
    private int mProgram;

    /**
     * View 宽高
     */
    private int mWidth, mHeight;

    /**
     * 着色器顶点坐标位置
     */
    private int aPosLocation;

    /**
     * 着色器纹理坐标位置
     */
    private int aCoordinateLocation;

    /**
     * 着色器纹理位置
     */
    private int uSamplerLocation;

    /**
     * 顶点坐标维数（即x, y, z）
     */
    private int mVertexSize = 2;

    /**
     * 纹理坐标维数（即x, y, z）
     */
    private int mCoordinateSize = 2;

    /**
     * 顶点坐标步长（即维数 * 字节数）
     */
    private int mVertexStride = mVertexSize * 4;

    /**
     * 纹理坐标步长（即维数 * 字节数）
     */
    private int mCoordinateStride = mCoordinateSize * 4;

    /**
     * 顶点个数
     */
    private int mVertexCount = 4;

    /**
     * 是否绑定Fbo
     */
    private boolean isBindFbo = false;

    /**
     * 顶点着色器代码路径
     */
    private final String mVertexFilename;

    /**
     * 片元着色器代码路径
     */
    private final String mFragFilename;

    /**
     * 顶点坐标
     */
    private FloatBuffer mVertexBuffer;

    /**
     * 纹理坐标
     */
    private FloatBuffer mCoordinateBuffer;

    public GLFilter(Context context) {
        this(context, "render/base/base/vertex.frag", "render/base/base/frag.frag");
    }

    public GLFilter(Context context, String vertexFilename, String fragFilename) {
        this.mContext = context;
        this.mVertexFilename = vertexFilename;
        this.mFragFilename = fragFilename;
    }

    @Override
    public void onSurfaceCreated() {
        this.onClearColor();
        this.onInitVertexBuffer();
        this.onInitCoordinateBuffer();
        this.onInitVbo();
        this.onInitProgram();
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        this.onViewport();
        this.onInitFbo();
    }

    @Override
    public void onSurfaceDrawFrame(int textureId) {
        if (!onReadyToDraw()) {
            return;
        }
        this.onDrawPre();
        this.onClear();
        this.onUseProgram();
        this.onInitLocation();
        this.onBindFbo();
        this.onBindVbo();
        this.onActiveTexture(textureId);
        this.onEnableVertexAttributeArray();
        this.onSetVertexData();
        this.onSetCoordinateData();
        this.onSetOtherData();
        this.onDraw();
        this.onDisableVertexAttributeArray();
        this.onUnBind();
    }

    @Override
    public void release() {
        this.onDeleteProgram(mProgram);
        this.onDeleteShader(mVertexShader);
        this.onDeleteShader(mFragShader);
        this.onDeleteTexture(mTextureId);
        this.onDeleteTexture(mFboTextureId);
        this.onDeleteFbo(mFboId);
        this.onDeleteVbo(mVboId);
    }

    /**
     * 设置背景颜色
     */
    public void onClearColor() {
        GLES20.glClearColor(0, 0, 0, 1f);
    }

    /**
     * 初始化顶点坐标
     */
    public void onInitVertexBuffer() {
        this.mVertexBuffer = OpenGLESUtils.getSquareVertexBuffer();
    }

    /**
     * 初始化纹理坐标
     */
    public void onInitCoordinateBuffer() {
        if (isBindFbo()) {
            this.mCoordinateBuffer = OpenGLESUtils.getSquareCoordinateReverseBuffer();
        } else {
            this.mCoordinateBuffer = OpenGLESUtils.getSquareCoordinateBuffer();
        }
    }

    /**
     * 初始化Vbo
     */
    public void onInitVbo() {
        this.mVboId = OpenGLESUtils.getVbo(mVertexBuffer, mCoordinateBuffer);
    }

    /**
     * 初始化Program
     */
    public void onInitProgram() {
        String vertexShaderCode = OpenGLESUtils.getShaderCode(mContext, mVertexFilename);
        String fragShaderCode = OpenGLESUtils.getShaderCode(mContext, mFragFilename);
        this.mVertexShader = OpenGLESUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        this.mFragShader = OpenGLESUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragShaderCode);
        this.mProgram = OpenGLESUtils.linkProgram(mVertexShader, mFragShader);
    }

    /**
     * 设置窗口尺寸
     */
    public void onViewport() {
        GLES20.glViewport(0, 0, mWidth, mHeight);
    }

    /**
     * 初始化Fbo
     */
    public void onInitFbo() {
        if (!isBindFbo()) {
            return;
        }
        int[] fboData = OpenGLESUtils.getFbo(mWidth, mHeight);
        this.mFboId = fboData[0];
        this.mFboTextureId = fboData[1];
    }

    /**
     * 绘制准备
     */
    public boolean onReadyToDraw() {
        return true;
    }

    /**
     * 绘制之前
     */
    public void onDrawPre() {

    }

    /**
     * 清屏
     */
    public void onClear() {
//        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * 使用Program
     */
    public void onUseProgram() {
        GLES20.glUseProgram(mProgram);
    }

    /**
     * 初始化着色器
     */
    public void onInitLocation() {
        this.aPosLocation = GLES20.glGetAttribLocation(mProgram, "aPos");
        this.aCoordinateLocation = GLES20.glGetAttribLocation(mProgram, "aCoordinate");
        this.uSamplerLocation = GLES20.glGetUniformLocation(mProgram, "uSampler");
    }

    /**
     * 绑定Fbo
     */
    public void onBindFbo() {
        if (!isBindFbo()) {
            return;
        }
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFboId);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, mFboTextureId, 0);
        GLES20.glViewport(0, 0, mWidth, mHeight);
    }

    /**
     * 绑定Vbo
     */
    public void onBindVbo() {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVboId);
    }

    /**
     * 激活并绑定纹理
     */
    public void onActiveTexture(int textureId) {
        this.mTextureId = textureId;
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uSamplerLocation, 0);
    }

    /**
     * 启用顶点坐标
     */
    public void onEnableVertexAttributeArray() {
        GLES20.glEnableVertexAttribArray(aPosLocation);
        GLES20.glEnableVertexAttribArray(aCoordinateLocation);
    }

    /**
     * 设置顶点坐标
     */
    public void onSetVertexData() {
        GLES20.glVertexAttribPointer(aPosLocation, mVertexSize, GLES20.GL_FLOAT, false, mVertexStride, 0);
    }

    /**
     * 设置纹理坐标
     */
    public void onSetCoordinateData() {
        GLES20.glVertexAttribPointer(aCoordinateLocation, mCoordinateSize, GLES20.GL_FLOAT, false, mCoordinateStride, mVertexBuffer.limit() * 4);
    }

    /**
     * 设置其他数据
     */
    public void onSetOtherData() {

    }

    /**
     * 绘制
     */
    public void onDraw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexCount);
    }

    /**
     * 禁用顶点坐标
     */
    public void onDisableVertexAttributeArray() {
        GLES20.glDisableVertexAttribArray(aPosLocation);
        GLES20.glDisableVertexAttribArray(aCoordinateLocation);
    }

    /**
     * 解除绑定
     */
    public void onUnBind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    /**
     * 删除Program
     */
    public void onDeleteProgram(int program) {
        GLES20.glDeleteProgram(program);
    }

    /**
     * 删除Shader
     */
    public void onDeleteShader(int shader) {
        GLES20.glDeleteShader(shader);
    }

    /**
     * 删除纹理
     */
    public void onDeleteTexture(int textureId) {
        GLES20.glDeleteTextures(1, new int[]{textureId}, 0);
    }

    /**
     * 删除Fbo
     */
    public void onDeleteFbo(int fboId) {
        GLES20.glDeleteFramebuffers(1, new int[]{fboId}, 0);
    }

    /**
     * 删除Vbo
     */
    public void onDeleteVbo(int vboId) {
        GLES20.glDeleteBuffers(1, new int[]{vboId}, 0);
    }

    /**
     * 获取元素
     */
    protected final int getHandle(final String name) {
        int location = glGetAttribLocation(mProgram, name);
        if (location == -1) {
            location = glGetUniformLocation(mProgram, name);
        }
        return location;
    }

    public int getFboId() {
        return mFboId;
    }

    public int getVboId() {
        return mVboId;
    }

    public int getTextureId() {
        return mTextureId;
    }

    public int getFboTextureId() {
        return mFboTextureId;
    }

    public int getProgram() {
        return mProgram;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getPosLocation() {
        return aPosLocation;
    }

    public int getCoordinateLocation() {
        return aCoordinateLocation;
    }

    public int getSamplerLocation() {
        return uSamplerLocation;
    }

    public int getVertexSize() {
        return mVertexSize;
    }

    public void setVertexSize(int vertexSize) {
        this.mVertexSize = vertexSize;
    }

    public int getCoordinateSize() {
        return mCoordinateSize;
    }

    public void setCoordinateSize(int coordinateSize) {
        this.mCoordinateSize = coordinateSize;
    }

    public int getVertexStride() {
        return mVertexStride;
    }

    public void setVertexStride(int vertexStride) {
        this.mVertexStride = vertexStride;
    }

    public int getCoordinateStride() {
        return mCoordinateStride;
    }

    public void setCoordinateStride(int coordinateStride) {
        this.mCoordinateStride = coordinateStride;
    }

    public int getVertexCount() {
        return mVertexCount;
    }

    public void setVertexCount(int vertexCount) {
        this.mVertexCount = vertexCount;
    }

    public String getVertexFilename() {
        return mVertexFilename;
    }

    public String getFragFilename() {
        return mFragFilename;
    }

    public boolean isBindFbo() {
        return isBindFbo;
    }

    public void setBindFbo(boolean bindFbo) {
        this.isBindFbo = bindFbo;
    }

    public void setCoordinateBuffer(FloatBuffer coordinateBuffer) {
        this.mCoordinateBuffer = coordinateBuffer;
    }
}
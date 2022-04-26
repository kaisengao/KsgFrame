package com.ksg.ksgplayer.renderer.glrender;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.ksg.ksgplayer.renderer.filter.GLGaussianBlurFilter;
import com.ksg.ksgplayer.renderer.filter.GLPIPFilter;
import com.ksg.ksgplayer.renderer.filter.base.GLFilter;
import com.ksg.ksgplayer.renderer.utils.OpenGLESUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @ClassName: PIPGLViewRender
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/25 23:03
 * @Description: 画中画 Render
 */
public class PIPGLViewRender extends GLViewRender {

    private GLPIPFilter mPipFilter;

    private GLFilter mPipPreviewFilter;

    @Override
    public void setSurfaceView(GLSurfaceView surfaceView) {
        super.setSurfaceView(surfaceView);
        this.mPipFilter = new GLPIPFilter(surfaceView.getContext(), getOesFilter());
        this.mPipPreviewFilter = new GLFilter(surfaceView.getContext());
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        this.mPipFilter.onSurfaceCreated();
        this.mPipPreviewFilter.onSurfaceCreated();
        // 毛玻璃
        GLGaussianBlurFilter filter = new GLGaussianBlurFilter(mSurfaceView.getContext());
        filter.setScaleRatio(2);
        filter.setBlurRadius(30);
        filter.setBlurOffset(2.5f, 2.5f);
        // 设置滤镜
        this.setGLFilter(filter);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        this.mPipFilter.onSurfaceChanged(width, height);
        this.mPipPreviewFilter.onSurfaceChanged(width, height);
    }

    @Override
    public void onSurfaceDrawFrame(GL10 gl) {
        float[] mvpMatrix = getOesFilter().getMVPMatrix();
        // 背景图层
        Matrix.setIdentityM(mvpMatrix, 0);
        Matrix.scaleM(mvpMatrix, 0,
                (float) getViewWidth() / mVideoWidth,
                (float) getViewHeight() / mVideoHeight, 1);
        this.mPipFilter.onDrawSelf();
        this.mPipPreviewFilter.onSurfaceDrawFrame(onFilterDrawFrame(mPipFilter.getFboTextureId()));
        // 预览图层
        Matrix.setIdentityM(mvpMatrix, 0);
        Matrix.scaleM(mvpMatrix, 0, 1f, 1f, 1);
        this.getOesFilter().onDrawSelf();
        GLES20.glViewport(0, (getViewHeight() / 2) - (mVideoHeight / 2), getViewWidth(), mVideoHeight);
        this.getPreviewFilter().onSurfaceDrawFrame(getOesFilter().getFboTextureId());
    }

    /**
     * 截图 回调
     */
    @Override
    protected void onShotPicCallback(GL10 gl) {
        super.onShotPicCallback(OpenGLESUtils.createBitmap(
                0, (getViewHeight() - mVideoHeight) / 2, mVideoWidth, mVideoHeight, gl));
    }

    @Override
    public void release() {
        super.release();
        if (mPipFilter != null) {
            this.mPipFilter.release();
        }
        if (mPipPreviewFilter != null) {
            this.mPipPreviewFilter.release();
        }
    }
}
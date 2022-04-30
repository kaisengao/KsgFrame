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

    private boolean isSetFilter;

    private GLPIPFilter mPipFilter;

    private GLFilter mPipPreviewFilter;

    private GLGaussianBlurFilter mGaussianBlurFilter;

    @Override
    public void setSurfaceView(GLSurfaceView surfaceView) {
        super.setSurfaceView(surfaceView);
        this.mPipFilter = new GLPIPFilter(surfaceView.getContext(), getOesFilter());
        this.mPipPreviewFilter = new GLFilter(surfaceView.getContext());
        // 毛玻璃
        this.mGaussianBlurFilter = new GLGaussianBlurFilter(surfaceView.getContext());
        this.mGaussianBlurFilter.setScaleRatio(2);
        this.mGaussianBlurFilter.setBlurRadius(30);
        this.mGaussianBlurFilter.setBlurOffset(3f, 3f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        this.mPipFilter.onSurfaceCreated();
        this.mPipPreviewFilter.onSurfaceCreated();
        // 设置滤镜
        if (!isSetFilter) {
            this.setGLFilter(mGaussianBlurFilter);
            this.isSetFilter = true;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        this.mPipFilter.onSurfaceChanged(width, height);
        this.mPipPreviewFilter.onSurfaceChanged(width, height);
    }

    @Override
    public void onSurfaceDrawFrame(GL10 gl) {
        // 背景图层
        Matrix.scaleM(getOesFilter().getMVPMatrix(), 0, 2f, 2f, 1);
        this.mPipFilter.onDrawSelf();
        this.mPipPreviewFilter.onSurfaceDrawFrame(onFilterDrawFrame(mPipFilter.getFboTextureId()));
        // 预览图层
        this.updateTexImage(getOesFilter().getOesMatrix());
        Matrix.setIdentityM(getOesFilter().getMVPMatrix(), 0);
        Matrix.scaleM(getOesFilter().getMVPMatrix(), 0, 1f, 1f, 1);
        this.getOesFilter().onDrawSelf();
        GLES20.glViewport(0, (getViewHeight() / 2) - (mVideoHeight / 2), mVideoWidth, mVideoHeight);
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
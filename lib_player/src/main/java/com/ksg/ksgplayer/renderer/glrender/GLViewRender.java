package com.ksg.ksgplayer.renderer.glrender;


import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;

import com.ksg.ksgplayer.renderer.filter.base.GLFilter;
import com.ksg.ksgplayer.renderer.filter.base.GLOesFilter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @ClassName: GLViewRenderer
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/23 13:58
 * @Description: GLRender
 */
public class GLViewRender extends BaseGLViewRender implements SurfaceTexture.OnFrameAvailableListener {

    private boolean isNewFilter = false;

    private GLFilter mFilter;

    private GLOesFilter mOesFilter;

    private GLFilter mPreviewFilter;

    @Override
    public void setSurfaceView(GLSurfaceView surfaceView) {
        super.setSurfaceView(surfaceView);
        this.mOesFilter = new GLOesFilter(surfaceView.getContext());
        this.mPreviewFilter = new GLFilter(surfaceView.getContext());
    }

    public void setGLFilter(GLFilter filter) {
        this.mSurfaceView.queueEvent(() -> {
            if (mFilter != null) {
                this.mFilter.release();
                this.mFilter = null;
            }
            this.mFilter = filter;
            this.isNewFilter = true;
            this.mSurfaceView.requestRender();
        });
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.mOesFilter.onSurfaceCreated();
        this.mOesFilter.setGLSurfaceListener(surfaceTexture -> {
            surfaceTexture.setOnFrameAvailableListener(this);
            this.onSurfaceAvailable(surfaceTexture);
        });
        this.mPreviewFilter.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.mOesFilter.onSurfaceChanged(width, height);
        if (mFilter != null) {
            this.mFilter.onSurfaceChanged(width, height);
        }
        this.mPreviewFilter.onSurfaceChanged(width, height);
    }

    @Override
    public void onSurfaceDrawFrame(GL10 gl) {
        this.mOesFilter.onDrawSelf();
        this.mPreviewFilter.onSurfaceDrawFrame(onFilterDrawFrame(mOesFilter.getFboTextureId()));
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.mSurfaceView.requestRender();
    }

    public int onFilterDrawFrame(int fboTextureId) {
        if (isNewFilter) {
            this.mFilter.onSurfaceCreated();
            this.mFilter.onSurfaceChanged(getViewWidth(), getViewHeight());
            this.isNewFilter = false;
        }
        if (mFilter != null) {
            this.mFilter.onSurfaceDrawFrame(fboTextureId);
            fboTextureId = mFilter.getFboTextureId();
        }
        return fboTextureId;
    }

    public GLOesFilter getOesFilter() {
        return mOesFilter;
    }

    public GLFilter getPreviewFilter() {
        return mPreviewFilter;
    }

    @Override
    public void release() {
        if (mOesFilter != null) {
            this.mOesFilter.release();
        }
        if (mPreviewFilter != null) {
            this.mPreviewFilter.release();
        }
        if (mFilter != null) {
            this.mFilter.release();
            this.mFilter = null;
        }
    }
}
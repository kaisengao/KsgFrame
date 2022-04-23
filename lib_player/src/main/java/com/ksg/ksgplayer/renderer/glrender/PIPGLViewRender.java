package com.ksg.ksgplayer.renderer.glrender;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ksg.ksgplayer.renderer.filter.NoFilter;
import com.ksg.ksgplayer.renderer.utils.OpenGLESUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @ClassName: PIPGLViewRender
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/23 22:01
 * @Description: 画中画
 */
public class PIPGLViewRender extends GLViewRender {

    private int mProgram;

    private final float[] mTransform = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);

        GLES20.glUseProgram(mProgram);

        Matrix.setIdentityM(mTransform, 0);
        Matrix.scaleM(mTransform, 0, (float) mCurrViewWidth / mSurfaceView.getWidth(),
                (float) mCurrViewHeight / mSurfaceView.getHeight(), 1);

        GLES20.glUniformMatrix4fv(getUSTMatrixHandle(), 1, false, mSTMatrix, 0);

        GLES20.glUniformMatrix4fv(getUMVPMatrixHandle(), 1, false, mTransform, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        this.onRewriteShotPic(gl);

        GLES20.glFinish();
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        super.onSurfaceCreated(glUnused, config);
        this.mProgram = OpenGLESUtils.createProgram(getVertexShader(), NoFilter.DEFAULT_FRAGMENT);
    }

    @Override
    public void initRenderSize() {
        if (mCurrVideoWidth > 0 && mCurrVideoHeight > 0) {
            float scale = ((float) mCurrViewHeight / mCurrVideoHeight);
            Matrix.scaleM(mMVPMatrix, 0, scale, scale, 1);
        }
    }

    @Override
    protected void onShotPic(GL10 glUnused) {
    }

    private void onRewriteShotPic(GL10 glUnused) {
        super.onShotPic(glUnused);
    }
}
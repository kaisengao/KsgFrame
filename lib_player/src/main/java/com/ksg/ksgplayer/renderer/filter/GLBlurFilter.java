package com.ksg.ksgplayer.renderer.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.ksg.ksgplayer.renderer.filter.base.GLFilter;

/**
 * @ClassName: GLBlurFilter
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/26 14:38
 * @Description: 毛玻璃
 */
public class GLBlurFilter extends GLFilter {

    private final Blur mXBlur, mYBlur;

    private final GLFilter mPreviewFilter;

    public GLBlurFilter(Context context) {
        super(context);
        this.mXBlur = new Blur(context);
        this.mYBlur = new Blur(context);
        this.mPreviewFilter = new GLFilter(context);

        this.mXBlur.setBindFbo(true);
        this.mYBlur.setBindFbo(true);
        this.mPreviewFilter.setBindFbo(true);
    }

    public void setScaleRatio(int scaleRatio) {
        this.mXBlur.setScaleRatio(scaleRatio);
        this.mYBlur.setScaleRatio(scaleRatio);
    }

    public void setBlurRadius(int blurRadius) {
        this.mXBlur.setBlurRadius(blurRadius);
        this.mYBlur.setBlurRadius(blurRadius);
    }

    public void setBlurOffset(float width, float height) {
        this.mXBlur.setBlurOffset(width, 0);
        this.mYBlur.setBlurOffset(0, height);
    }

    @Override
    public void onSurfaceCreated() {
        this.mXBlur.onSurfaceCreated();
        this.mYBlur.onSurfaceCreated();
        this.mPreviewFilter.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        this.mXBlur.onSurfaceChanged(width, height);
        this.mYBlur.onSurfaceChanged(width, height);
        this.mPreviewFilter.onSurfaceChanged(width, height);
    }

    @Override
    public void onSurfaceDrawFrame(int textureId) {
        this.mXBlur.onSurfaceDrawFrame(textureId);
        this.mYBlur.onSurfaceDrawFrame(mXBlur.getFboTextureId());
        this.mPreviewFilter.onSurfaceDrawFrame(mYBlur.getFboTextureId());
    }

    @Override
    public int getFboTextureId() {
        return mPreviewFilter.getFboTextureId();
    }

    @Override
    public void release() {
        this.mXBlur.release();
        this.mYBlur.release();
        this.mPreviewFilter.release();
    }

    private static class Blur extends GLFilter {

        private int uBlurRadius;
        private int uBlurOffset;
        private int uSumWeight;

        private int mScaleRatio;
        private int mBlurRadius;
        private float mBlurOffsetW, mBlurOffsetH;
        private float mSumWeight;

        public Blur(Context context) {
            super(context,
                    "render/filter/gaussian_blur/vertex.frag",
                    "render/filter/gaussian_blur/frag.frag");
            this.initData();
        }

        private void initData() {
            // 默认缩放因子
            this.setScaleRatio(1);
            // 默认模糊半径
            this.setBlurRadius(0);
            // 默认模糊步长
            this.setBlurOffset(0, 0);
        }

        public void setScaleRatio(int scaleRatio) {
            this.mScaleRatio = scaleRatio;
        }

        public void setBlurRadius(int blurRadius) {
            this.mBlurRadius = blurRadius;
        }

        public void setBlurOffset(float width, float height) {
            this.mBlurOffsetW = width;
            this.mBlurOffsetH = height;
        }

        public void setSumWeight(float sumWeight) {
            this.mSumWeight = sumWeight;
        }

        @Override
        public void onSurfaceChanged(int width, int height) {
            super.onSurfaceChanged(width / mScaleRatio, height / mScaleRatio);
        }

        @Override
        public void onInitLocation() {
            super.onInitLocation();
            this.uBlurRadius = GLES20.glGetUniformLocation(getProgram(), "uBlurRadius");
            this.uBlurOffset = GLES20.glGetUniformLocation(getProgram(), "uBlurOffset");
            this.uSumWeight = GLES20.glGetUniformLocation(getProgram(), "uSumWeight");
        }

        @Override
        public void onSetOtherData() {
            super.onSetOtherData();
            // 计算总权重
            this.calculateSumWeight();

            GLES20.glUniform1i(uBlurRadius, mBlurRadius);
            GLES20.glUniform2f(uBlurOffset, mBlurOffsetW / getWidth(), mBlurOffsetH / getHeight());
            GLES20.glUniform1f(uSumWeight, mSumWeight);
        }

        /**
         * 计算总权重
         */
        private void calculateSumWeight() {
            if (mBlurRadius < 1) {
                this.setSumWeight(0);
                return;
            }
            float sumWeight = 0;
            float sigma = mBlurRadius / 3f;
            for (int i = 0; i < mBlurRadius; i++) {
                float weight = (float) ((1 / Math.sqrt(2 * Math.PI * sigma * sigma)) * Math.exp(-(i * i) / (2 * sigma * sigma)));
                sumWeight += weight;
                if (i != 0) {
                    sumWeight += weight;
                }
            }
            this.setSumWeight(sumWeight);
        }
    }
}

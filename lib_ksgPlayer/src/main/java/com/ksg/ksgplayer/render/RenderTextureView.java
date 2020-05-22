package com.ksg.ksgplayer.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ksg.ksgplayer.player.BaseInternalPlayer;

/**
 * @ClassName: TextureRenderView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 10:51
 * @Description: TextureRenderView
 */
public class RenderTextureView extends TextureView implements IRenderView, TextureView.SurfaceTextureListener {

    @Nullable
    private BaseInternalPlayer mInternalPlayer;

    private Surface mSurface;

    private SurfaceTexture mSurfaceTexture;

    private RenderMeasure mRenderMeasure;

    public RenderTextureView(Context context) {
        super(context);
        this.mRenderMeasure = new RenderMeasure();
        this.setSurfaceTextureListener(this);
    }

    @Override
    public void attachToPlayer(@NonNull BaseInternalPlayer player) {
        this.mInternalPlayer = player;
    }

    @Override
    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            this.mRenderMeasure.setVideoSize(videoWidth, videoHeight);
            this.requestLayout();
        }
    }

    @Override
    public void setVideoRotation(int degree) {
        this.mRenderMeasure.setVideoRotation(degree);
        this.setRotation(degree);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public Bitmap doScreenShot() {
        return getBitmap();
    }

    @Override
    public void release() {
        if (mSurface != null) {
            mSurface.release();
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mRenderMeasure.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mRenderMeasure.getMeasureWidth(), mRenderMeasure.getMeasureHeight());
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (mSurfaceTexture != null) {
            setSurfaceTexture(mSurfaceTexture);
        } else {
            mSurfaceTexture = surfaceTexture;
            mSurface = new Surface(surfaceTexture);
            if (mInternalPlayer != null) {
                mInternalPlayer.setSurface(mSurface);
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}

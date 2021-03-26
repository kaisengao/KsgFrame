package com.ksg.ksgplayer.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.kaisengao.base.util.KLog;
import com.ksg.ksgplayer.player.IKsgPlayer;

import java.lang.ref.WeakReference;

/**
 * @ClassName: RenderTextureView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 11:23
 * @Description: <p>
 * 使用TextureView时，需要开启硬件加速（系统默认是开启的）。
 * 如果硬件加速是关闭的，会造成{@link SurfaceTextureListener#onSurfaceTextureAvailable(SurfaceTexture, int, int)}不执行。
 */
public class RenderTextureView extends TextureView implements IRender {

    final String TAG = "RenderTextureView";

    private IRenderCallback mRenderCallback;
    private RenderMeasure mRenderMeasure;

    private SurfaceTexture mSurfaceTexture;

    private boolean mTakeOverSurfaceTexture;

    private boolean isReleased;

    public RenderTextureView(Context context) {
        this(context, null);
    }

    public RenderTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRenderMeasure = new RenderMeasure();
        setSurfaceTextureListener(new InternalSurfaceTextureListener());
    }

    /**
     * If you want to take over the life cycle of SurfaceTexture,
     * please set the tag to true.
     *
     * @param takeOverSurfaceTexture
     */
    public void setTakeOverSurfaceTexture(boolean takeOverSurfaceTexture) {
        this.mTakeOverSurfaceTexture = takeOverSurfaceTexture;
    }

    public boolean isTakeOverSurfaceTexture() {
        return mTakeOverSurfaceTexture;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mRenderMeasure.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mRenderMeasure.getMeasureWidth(), mRenderMeasure.getMeasureHeight());
    }

    @Override
    public void setRenderCallback(IRenderCallback renderCallback) {
        this.mRenderCallback = renderCallback;
    }

    @Override
    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            mRenderMeasure.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            requestLayout();
        }
    }

    @Override
    public void setVideoRotation(int degree) {
        mRenderMeasure.setVideoRotation(degree);
        setRotation(degree);
    }

    @Override
    public void updateAspectRatio(AspectRatio aspectRatio) {
        mRenderMeasure.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    public void updateVideoSize(int videoWidth, int videoHeight) {
        KLog.d(TAG, "onUpdateVideoSize : videoWidth = " + videoWidth + " videoHeight = " + videoHeight);
        mRenderMeasure.setVideoSize(videoWidth, videoHeight);
        requestLayout();
    }

    @Override
    public View getRenderView() {
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KLog.d(TAG, "onTextureViewAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KLog.d(TAG, "onTextureViewDetachedFromWindow");
        //fixed bug on before android 4.4
        //modify 2018/11/16
        //java.lang.RuntimeException: Error during detachFromGLContext (see logcat for details)
        //   at android.graphics.SurfaceTexture.detachFromGLContext(SurfaceTexture.java:215)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            release();
        }
    }

    @Override
    public void release() {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        setSurfaceTextureListener(null);
        isReleased = true;
    }

    @Override
    public boolean isReleased() {
        return isReleased;
    }

    private Surface mSurface;

    void setSurface(Surface surface) {
        this.mSurface = surface;
    }

    Surface getSurface() {
        return mSurface;
    }

    SurfaceTexture getOwnSurfaceTexture() {
        return mSurfaceTexture;
    }

    private static final class InternalRenderHolder implements IRenderHolder {

        private WeakReference<Surface> mSurfaceRefer;
        private WeakReference<RenderTextureView> mTextureRefer;

        public InternalRenderHolder(RenderTextureView textureView, SurfaceTexture surfaceTexture) {
            mTextureRefer = new WeakReference<>(textureView);
            mSurfaceRefer = new WeakReference<>(new Surface(surfaceTexture));
        }

        RenderTextureView getTextureView() {
            if (mTextureRefer != null) {
                return mTextureRefer.get();
            }
            return null;
        }

        @Override
        public void bindPlayer(IKsgPlayer player) {
            RenderTextureView textureView = getTextureView();
            if (player != null && mSurfaceRefer != null && textureView != null) {
                SurfaceTexture surfaceTexture = textureView.getOwnSurfaceTexture();
                SurfaceTexture useTexture = textureView.getSurfaceTexture();
                boolean isReleased = false;
                //check the SurfaceTexture is released is Android O.
                if (surfaceTexture != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    isReleased = surfaceTexture.isReleased();
                }
                boolean available = surfaceTexture != null && !isReleased;
                //When the user sets the takeover flag and SurfaceTexture is available.
                if (textureView.isTakeOverSurfaceTexture()
                        && available
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    //if SurfaceTexture not set or current is null, need set it.
                    if (!surfaceTexture.equals(useTexture)) {
                        textureView.setSurfaceTexture(surfaceTexture);
                        KLog.d("RenderTextureView", "****setSurfaceTexture****");
                    } else {
                        Surface surface = textureView.getSurface();
                        //release current Surface if not null.
                        if (surface != null) {
                            surface.release();
                        }
                        //create Surface use update SurfaceTexture
                        Surface newSurface = new Surface(surfaceTexture);
                        //set it for player
                        player.setSurface(newSurface);
                        //record the new Surface
                        textureView.setSurface(newSurface);
                        KLog.d("RenderTextureView", "****bindSurface****");
                    }
                } else {
                    Surface surface = mSurfaceRefer.get();
                    if (surface != null) {
                        player.setSurface(surface);
                        //record the Surface
                        textureView.setSurface(surface);
                        KLog.d("RenderTextureView", "****bindSurface****");
                    }
                }
            }
        }

    }

    private class InternalSurfaceTextureListener implements SurfaceTextureListener {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            KLog.d(TAG, "<---onSurfaceTextureAvailable---> : width = " + width + " height = " + height);
            if (mRenderCallback != null) {
                mRenderCallback.onSurfaceCreated(
                        new InternalRenderHolder(RenderTextureView.this, surface), width, height);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            KLog.d(TAG, "onSurfaceTextureSizeChanged : width = " + width + " height = " + height);
            if (mRenderCallback != null) {
                mRenderCallback.onSurfaceChanged(
                        new InternalRenderHolder(RenderTextureView.this, surface), 0, width, height);
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            KLog.d(TAG, "***onSurfaceTextureDestroyed***");
            if (mRenderCallback != null) {
                mRenderCallback.onSurfaceDestroy(
                        new InternalRenderHolder(RenderTextureView.this, surface));
            }
            if (mTakeOverSurfaceTexture)
                mSurfaceTexture = surface;
            //fixed bug on before android 4.4
            //modify 2018/11/16
            //java.lang.RuntimeException: Error during detachFromGLContext (see logcat for details)
            //   at android.graphics.SurfaceTexture.detachFromGLContext(SurfaceTexture.java:215)
            //all return false.
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                return false;
            }
            return !mTakeOverSurfaceTexture;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }

    }

}

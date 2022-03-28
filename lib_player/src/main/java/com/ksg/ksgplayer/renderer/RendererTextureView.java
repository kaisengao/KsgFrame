package com.ksg.ksgplayer.renderer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.ksg.ksgplayer.player.IPlayer;

import java.lang.ref.WeakReference;

/**
 * @ClassName: RenderTextureView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 11:23
 * @Description: <p>
 * 使用TextureView时，需要开启硬件加速（系统默认是开启的）。
 * 如果硬件加速是关闭的，会造成{@link SurfaceTextureListener#onSurfaceTextureAvailable(SurfaceTexture, int, int)}不执行。
 */
public class RendererTextureView extends TextureView implements IRenderer {

    private boolean mTakeOverSurfaceTexture;

    private boolean isReleased;

    private Callback mRenderCallback;

    private Surface mSurface;

    private SurfaceTexture mSurfaceTexture;

    private final RendererMeasure mRenderMeasure;

    private final InternalRenderHolder mInternalRenderHolder;

    public RendererTextureView(Context context) {
        this(context, null);
    }

    public RendererTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRenderMeasure = new RendererMeasure();
        this.mInternalRenderHolder = new InternalRenderHolder(this);
        this.setSurfaceTextureListener(new InternalSurfaceTextureListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 计算视图宽高比
        this.mRenderMeasure.doMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置视图宽高比
        this.setMeasuredDimension(mRenderMeasure.getMeasureWidth(), mRenderMeasure.getMeasureHeight());
    }

    /**
     * 接管SurfaceTexture的生命周期
     *
     * @param takeOverSurfaceTexture takeOverSurfaceTexture
     */
    public void setTakeOverSurfaceTexture(boolean takeOverSurfaceTexture) {
        this.mTakeOverSurfaceTexture = takeOverSurfaceTexture;
    }

    /**
     * 是否接管了SurfaceTexture的生命周期
     */
    public boolean isTakeOverSurfaceTexture() {
        return mTakeOverSurfaceTexture;
    }

    private void setSurface(Surface surface) {
        this.mSurface = surface;
    }

    private Surface getSurface() {
        return mSurface;
    }

    private SurfaceTexture getOwnSurfaceTexture() {
        return mSurfaceTexture;
    }

    /**
     * 设置 Callback
     *
     * @param callback {@link Callback}
     */
    @Override
    public void setCallback(Callback callback) {
        this.mRenderCallback = callback;
    }

    /**
     * 设置视频旋转角度
     *
     * @param degree 角度
     */
    @Override
    public void setVideoRotation(int degree) {
        this.mRenderMeasure.setVideoRotation(degree);
        this.setRotation(degree);
    }

    /**
     * 设置视频采样率
     *
     * @param videoSarNum videoSarNum
     * @param videoSarDen videoSarDen
     */
    @Override
    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            this.mRenderMeasure.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            this.requestLayout();
        }
    }

    /**
     * 设置画面宽高比
     * <p>
     * {@link AspectRatio#AspectRatio_16_9}
     * {@link AspectRatio#AspectRatio_4_3}
     * {@link AspectRatio#AspectRatio_FIT_PARENT}
     * {@link AspectRatio#AspectRatio_FILL_PARENT}
     * {@link AspectRatio#AspectRatio_MATCH_PARENT}
     * {@link AspectRatio#AspectRatio_ORIGIN}
     *
     * @param aspectRatio {@link AspectRatio}
     */
    @Override
    public void updateAspectRatio(AspectRatio aspectRatio) {
        this.mRenderMeasure.setAspectRatio(aspectRatio);
        this.requestLayout();
    }

    /**
     * 设置画面宽高
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    @Override
    public void updateVideoSize(int videoWidth, int videoHeight) {
        this.mRenderMeasure.setVideoSize(videoWidth, videoHeight);
        this.requestLayout();
    }

    /**
     * 返回 渲染器View
     *
     * @return View
     */
    @Override
    public View getRendererView() {
        return this;
    }

    /**
     * onDetachedFromWindow
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 修复了安卓4.4之前的漏洞
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            this.release();
        }
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if (mSurfaceTexture != null) {
            this.mSurfaceTexture.release();
            this.mSurfaceTexture = null;
        }
        if (mSurface != null) {
            this.mSurface.release();
            this.mSurface = null;
        }
        this.setSurfaceTextureListener(null);
        this.isReleased = true;
    }

    /**
     * 是否释放了资源
     *
     * @return boolean
     */
    @Override
    public boolean isReleased() {
        return isReleased;
    }

    private static final class InternalRenderHolder implements Holder {

        private WeakReference<Surface> mSurfaceRefer;

        private final WeakReference<RendererTextureView> mTextureRefer;

        public InternalRenderHolder(RendererTextureView textureView) {
            this.mTextureRefer = new WeakReference<>(textureView);
        }

        public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
            if (mSurfaceRefer != null) {
                this.mSurfaceRefer.clear();
                this.mSurfaceRefer = null;
            }
            this.mSurfaceRefer = new WeakReference<>(new Surface(surfaceTexture));
        }

        private RendererTextureView getTextureView() {
            return mTextureRefer.get();
        }

        @Override
        public void bindPlayer(IPlayer player) {
            RendererTextureView textureView = getTextureView();
            if (player != null && textureView != null) {
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
                    }
                } else {
                    Surface surface = mSurfaceRefer.get();
                    if (surface != null) {
                        player.setSurface(surface);
                        //record the Surface
                        textureView.setSurface(surface);
                    }
                }
            }
        }

    }

    private class InternalSurfaceTextureListener implements SurfaceTextureListener {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            if (mRenderCallback != null) {
                mInternalRenderHolder.setSurfaceTexture(surface);
                mRenderCallback.onSurfaceCreated(mInternalRenderHolder, width, height);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            if (mRenderCallback != null) {
                mInternalRenderHolder.setSurfaceTexture(surface);
                mRenderCallback.onSurfaceChanged(mInternalRenderHolder, 0, width, height);
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            if (mRenderCallback != null) {
                mInternalRenderHolder.setSurfaceTexture(surface);
                mRenderCallback.onSurfaceDestroy(mInternalRenderHolder);
            }
            if (mTakeOverSurfaceTexture)
                mSurfaceTexture = surface;
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

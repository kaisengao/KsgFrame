package com.ksg.ksgplayer.render;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.kasiengao.base.util.KLog;
import com.ksg.ksgplayer.player.IKsgPlayer;

import java.lang.ref.WeakReference;

/**
 * @ClassName: RenderSurfaceView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 11:22
 * @Description: SurfaceView
 */
public class RenderSurfaceView extends SurfaceView implements IRender {

    final String TAG = "RenderSurfaceView";

    private IRenderCallback mRenderCallback;
    private RenderMeasure mRenderMeasure;
    private boolean isReleased;

    public RenderSurfaceView(Context context) {
        this(context, null);
    }

    public RenderSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRenderMeasure = new RenderMeasure();
        getHolder().addCallback(new InternalSurfaceHolderCallback());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        KLog.e(TAG, "surface view not support rotation ... ");
    }

    @Override
    public void updateAspectRatio(AspectRatio aspectRatio) {
        mRenderMeasure.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    public void updateVideoSize(int videoWidth, int videoHeight) {
        mRenderMeasure.setVideoSize(videoWidth, videoHeight);
        fixedSize(videoWidth, videoHeight);
        requestLayout();
    }

    void fixedSize(int videoWidth, int videoHeight) {
        if (videoWidth != 0 && videoHeight != 0) {
            getHolder().setFixedSize(videoWidth, videoHeight);
        }
    }

    @Override
    public View getRenderView() {
        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KLog.d(TAG, "onSurfaceViewDetachedFromWindow");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KLog.d(TAG, "onSurfaceViewAttachedToWindow");
    }

    @Override
    public void release() {
        isReleased = true;
    }

    @Override
    public boolean isReleased() {
        return isReleased;
    }

    private static final class InternalRenderHolder implements IRenderHolder {

        private WeakReference<SurfaceHolder> mSurfaceHolder;

        public InternalRenderHolder(SurfaceHolder surfaceHolder) {
            this.mSurfaceHolder = new WeakReference<>(surfaceHolder);
        }

        @Override
        public void bindPlayer(IKsgPlayer player) {
            if (player != null && mSurfaceHolder.get() != null) {
                player.setDisplay(mSurfaceHolder.get());
            }
        }
    }

    private class InternalSurfaceHolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            KLog.d(TAG, "<---surfaceCreated---->");
            if (mRenderCallback != null) {
                mRenderCallback.onSurfaceCreated(new InternalRenderHolder(holder), 0, 0);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.d(TAG, "surfaceChanged : width = " + width + " height = " + height);
            if (mRenderCallback != null) {
                mRenderCallback.onSurfaceChanged(new InternalRenderHolder(holder), format, width, height);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.d(TAG, "***surfaceDestroyed***");
            if (mRenderCallback != null) {
                mRenderCallback.onSurfaceDestroy(new InternalRenderHolder(holder));
            }
        }

    }


}

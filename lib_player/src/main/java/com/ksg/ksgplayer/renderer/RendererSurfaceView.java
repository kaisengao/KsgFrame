package com.ksg.ksgplayer.renderer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.ksg.ksgplayer.player.IPlayer;

import java.lang.ref.WeakReference;

/**
 * @ClassName: RenderSurfaceView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/25 11:22
 * @Description: SurfaceView
 */
public class RendererSurfaceView extends SurfaceView implements IRenderer, SurfaceHolder.Callback {

    private boolean isReleased;

    private Callback mCallback;

    private final RendererMeasure mRenderMeasure;

    public RendererSurfaceView(Context context) {
        this(context, null);
    }

    public RendererSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 视图宽高比算法
        this.mRenderMeasure = new RendererMeasure();
        // SurfaceHolder
        this.getHolder().addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算视图宽高比
        this.mRenderMeasure.doMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置视图宽高比
        this.setMeasuredDimension(mRenderMeasure.getMeasureWidth(), mRenderMeasure.getMeasureHeight());
    }

    /**
     * 设置 Callback
     *
     * @param callback {@link Callback}
     */
    @Override
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    /**
     * 设置视频旋转角度
     *
     * @param degree 角度
     */
    @Override
    public void setVideoRotation(int degree) {

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
        this.fixedSize(videoWidth, videoHeight);
        this.requestLayout();
    }

    /**
     * 设置固定大小
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    private void fixedSize(int videoWidth, int videoHeight) {
        if (videoWidth != 0 && videoHeight != 0) {
            this.getHolder().setFixedSize(videoWidth, videoHeight);
        }
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
     * 释放资源
     */
    @Override
    public void release() {
        isReleased = true;
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

    /**
     * 弱引用Holder
     */
    private static final class InternalRenderHolder implements Holder {

        private final WeakReference<SurfaceHolder> mSurfaceHolder;

        public InternalRenderHolder(SurfaceHolder surfaceHolder) {
            this.mSurfaceHolder = new WeakReference<>(surfaceHolder);
        }

        /**
         * 绑定播放器
         *
         * @param player {@link IPlayer}
         */
        @Override
        public void bindPlayer(IPlayer player) {
            if (player != null && mSurfaceHolder.get() != null) {
                player.setDisplay(mSurfaceHolder.get());
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCallback != null) {
            this.mCallback.onSurfaceCreated(new InternalRenderHolder(holder), 0, 0);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCallback != null) {
            mCallback.onSurfaceChanged(new InternalRenderHolder(holder), format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCallback != null) {
            mCallback.onSurfaceDestroy(new InternalRenderHolder(holder));
        }
    }
}

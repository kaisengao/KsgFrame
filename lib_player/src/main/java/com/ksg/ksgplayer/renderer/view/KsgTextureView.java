package com.ksg.ksgplayer.renderer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ksg.ksgplayer.config.AspectRatio;
import com.ksg.ksgplayer.helper.MeasureHelper;
import com.ksg.ksgplayer.proxy.PlayerProxy;
import com.ksg.ksgplayer.renderer.Renderer;
import com.ksg.ksgplayer.renderer.RendererListener;
import com.ksg.ksgplayer.renderer.filter.GLFilter;
import com.ksg.ksgplayer.renderer.glrender.BaseGLViewRender;

/**
 * @ClassName: KsgTextureView
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/21 17:13
 * @Description: 使用TextureView时，需要开启硬件加速（系统默认是开启的）。
 * 如果硬件加速是关闭的，会造成{@link SurfaceTextureListener#onSurfaceTextureAvailable(SurfaceTexture, int, int)}不执行。
 */
public class KsgTextureView extends TextureView implements Renderer, TextureView.SurfaceTextureListener {

    private static final String TAG = KsgTextureView.class.getSimpleName();

    private boolean isRelease = false;

    private MeasureHelper mMeasureHelper;

    private SurfaceTexture mSaveTexture;

    private Surface mSurface;

    private PlayerProxy mPlayerProxy;

    private RendererListener mRendererListener;

    public KsgTextureView(@NonNull Context context) {
        this(context, null);
    }

    public KsgTextureView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        // 视图宽高比算法
        this.mMeasureHelper = new MeasureHelper();
        // 事件监听
        this.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
        if (mSaveTexture == null) {
            this.mSaveTexture = surfaceTexture;
            this.mSurface = new Surface(surfaceTexture);
        } else {
            this.setSurfaceTexture(mSaveTexture);
        }
        // 绑定渲染器
        if (mPlayerProxy != null) {
            this.mPlayerProxy.setSurface(mSurface);
        }
        // 回调
        if (mRendererListener != null) {
            this.mRendererListener.onSurfaceCreated(mSurface, width, height);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
        // 回调
        if (mRendererListener != null) {
            this.mRendererListener.onSurfaceChanged(mSurface, width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        // 回调
        if (mRendererListener != null) {
            this.mRendererListener.onSurfaceDestroy(mSurface);
        }
        return (mSaveTexture == null);
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

    }

    /**
     * onMeasure
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mMeasureHelper.prepareMeasure(widthMeasureSpec, heightMeasureSpec, (int) getRotation());
        this.setMeasuredDimension(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());
    }

    /**
     * 绑定 播放器
     *
     * @param playerProxy 播放器代理类
     */
    @Override
    public void bindPlayer(PlayerProxy playerProxy) {
        this.mPlayerProxy = playerProxy;
    }

    /**
     * 设置 回调事件
     *
     * @param listener listener
     */
    @Override
    public void setRendererListener(RendererListener listener) {
        this.mRendererListener = listener;
    }

    /**
     * 设置 画面宽高
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    @Override
    public void setVideoSize(int videoWidth, int videoHeight) {
        this.mMeasureHelper.setVideoSize(videoWidth, videoHeight);
        this.requestLayout();
    }

    /**
     * 设置 画面旋转角度
     *
     * @param degree 角度
     */
    @Override
    public void setRotationDegrees(int degree) {
        this.mMeasureHelper.setRotationDegrees(degree);
        this.setRotation(degree);
        this.requestLayout();
    }

    /**
     * 获取 画面旋转角度
     *
     * @return degree 角度
     */
    @Override
    public int getRotationDegrees() {
        return (int) getRotation();
    }

    /**
     * 设置 视频采样率
     *
     * @param videoSarNum videoSarNum
     * @param videoSarDen videoSarDen
     */
    @Override
    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            this.mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            this.requestLayout();
        }
    }

    /**
     * 设置 画面比例
     *
     * @param aspectRatio {@link AspectRatio}
     */
    @Override
    public void setAspectRatio(int aspectRatio) {
        this.mMeasureHelper.setAspectRatio(aspectRatio);
        this.requestLayout();
    }

    /**
     * 设置 自定义画面比例
     *
     * @param customAspectRatio 自定义比例 （例：16/9 = 1.77）
     */
    @Override
    public void setCustomAspectRatio(int customAspectRatio) {
        this.mMeasureHelper.setCustomAspectRatio(customAspectRatio);
        this.requestLayout();
    }

    /**
     * 设置 滤镜 (仅GLSurfaceView可用)
     *
     * @param filter GLFilter
     */
    @Override
    public void setGLFilter(GLFilter filter) {
        Log.i(TAG, "not support setGLFilter now");
    }

    /**
     * 设置 GLViewRender (仅GLSurfaceView可用)
     *
     * @param viewRender {@link BaseGLViewRender}
     */
    @Override
    public void setGLViewRender(BaseGLViewRender viewRender) {
        Log.i(TAG, "not support setGLViewRender now");
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
     * 截图
     *
     * @param shotHigh 高清/普通
     */
    @Override
    public boolean onShotPic(boolean shotHigh) {
        try {
            Bitmap.Config config;
            if (shotHigh) {
                config = Bitmap.Config.ARGB_8888;
            } else {
                config = Bitmap.Config.RGB_565;
            }
            // 获取帧图
            Bitmap bitmap = getBitmap(Bitmap.createBitmap(getWidth(), getHeight(), config));
            // 回调
            if (mRendererListener != null && bitmap != null) {
                this.mRendererListener.onShotPic(bitmap);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        this.isRelease = true;
        if (mSaveTexture != null) {
            this.mSaveTexture.release();
            this.mSaveTexture = null;
        }
        if (mSurface != null) {
            this.mSurface.release();
            this.mSurface = null;
        }
        this.setSurfaceTextureListener(null);
    }

    /**
     * 是否释放了资源
     *
     * @return boolean
     */
    @Override
    public boolean isReleased() {
        return isRelease;
    }
}
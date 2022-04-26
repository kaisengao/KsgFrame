package com.ksg.ksgplayer.renderer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.ksg.ksgplayer.config.AspectRatio;
import com.ksg.ksgplayer.helper.MeasureHelper;
import com.ksg.ksgplayer.proxy.PlayerProxy;
import com.ksg.ksgplayer.renderer.Renderer;
import com.ksg.ksgplayer.renderer.RendererListener;
import com.ksg.ksgplayer.renderer.filter.base.GLFilter;

/**
 * @ClassName: KsgSurfaceView
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/22 17:28
 * @Description: SurfaceView
 */
public class KsgSurfaceView extends SurfaceView implements Renderer, SurfaceHolder.Callback {

    private static final String TAG = KsgSurfaceView.class.getSimpleName();

    private boolean isRelease = false;

    private MeasureHelper mMeasureHelper;

    private RendererListener mRendererListener;

    public KsgSurfaceView(Context context) {
        this(context, null);
    }

    public KsgSurfaceView(Context context, AttributeSet attrs) {
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
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if (mRendererListener != null) {
            this.mRendererListener.onSurfaceCreated(surfaceHolder.getSurface(), getWidth(), getWidth());
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        if (mRendererListener != null) {
            this.mRendererListener.onSurfaceChanged(surfaceHolder.getSurface(), width, height);
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (mRendererListener != null) {
            this.mRendererListener.onSurfaceDestroy(surfaceHolder.getSurface());
        }
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
        if (playerProxy != null) {
            playerProxy.setDisplay(getHolder());
        }
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
        Log.i(TAG, "not support setRotationDegrees now");
    }

    /**
     * 获取 画面旋转角度
     *
     * @return degree 角度
     */
    @Override
    public int getRotationDegrees() {
        return 0;
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
    public void setViewAspectRatio(int aspectRatio) {
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
     */
    @Override
    public boolean onShotPic() {
        Log.i(TAG, "not support onShotPic now");
        return false;
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        this.isRelease = true;
        this.getHolder().removeCallback(this);
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
package com.ksg.ksgplayer.renderer.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;

import com.kaisengao.base.util.KLog;
import com.ksg.ksgplayer.config.AspectRatio;
import com.ksg.ksgplayer.helper.MeasureHelper;
import com.ksg.ksgplayer.proxy.PlayerProxy;
import com.ksg.ksgplayer.renderer.Renderer;
import com.ksg.ksgplayer.renderer.RendererListener;
import com.ksg.ksgplayer.renderer.filter.GLFilter;
import com.ksg.ksgplayer.renderer.glrender.BaseGLViewRender;
import com.ksg.ksgplayer.renderer.glrender.GLViewRender;
import com.ksg.ksgplayer.renderer.listener.GLSurfaceListener;

/**
 * @ClassName: KsgGLSurfaceView
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/22 21:57
 * @Description: GLSurfaceView
 */
public class KsgGLSurfaceView extends GLSurfaceView implements Renderer, GLSurfaceListener {

    public static final int MODE_LAYOUT_SIZE = 0;

    public static final int MODE_RENDER_SIZE = 1;

    private int mMode = MODE_RENDER_SIZE;

    private int mVideoWidth, mVideoHeight;

    private boolean isRelease = false;

    private MeasureHelper mMeasureHelper;

    private BaseGLViewRender mViewRender;

    private PlayerProxy mPlayerProxy;

    private RendererListener mRendererListener;

    public KsgGLSurfaceView(Context context) {
        this(context, null);
    }

    public KsgGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        this.setEGLContextClientVersion(2);
        // 视图宽高比算法
        this.mMeasureHelper = new MeasureHelper();
        // Render
        this.mViewRender = new GLViewRender();
        // Init GLViewRender
//        this.initViewRender();
    }

    /**
     * Init GLViewRender
     */
    private void initViewRender() {
        this.mViewRender.setSurfaceView(this);
        this.mViewRender.setGLSurfaceListener(this);
        this.mViewRender.setRendererListener(mRendererListener);

        this.setRenderer(mViewRender);
    }

    @Override
    public void onSurfaceAvailable(Surface surface) {
        KLog.d("zzz","onSurfaceAvailable");

        if (mPlayerProxy != null) {
            this.mPlayerProxy.setSurface(surface);
        }
    }

    /**
     * onMeasure
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMode == MODE_RENDER_SIZE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.mMeasureHelper.prepareMeasure(widthMeasureSpec, heightMeasureSpec, (int) getRotation());
            this.updateRenderSize();
        } else {
            this.mMeasureHelper.prepareMeasure(widthMeasureSpec, heightMeasureSpec, (int) getRotation());
            this.setMeasuredDimension(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());
        }
    }

    /**
     * Update 渲染尺寸
     */
    protected void updateRenderSize() {
        if (mViewRender != null && mMode == MODE_RENDER_SIZE) {
            this.mViewRender.setCurrViewWidth(mMeasureHelper.getMeasuredWidth());
            this.mViewRender.setCurrViewHeight(mMeasureHelper.getMeasuredHeight());
            this.mViewRender.setCurrVideoWidth(mVideoWidth);
            this.mViewRender.setCurrVideoHeight(mVideoHeight);
            this.mViewRender.initRenderSize();
        }
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
        this.mViewRender.setRendererListener(listener);
    }

    /**
     * 设置 画面宽高
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    @Override
    public void setVideoSize(int videoWidth, int videoHeight) {
        this.mVideoWidth = videoWidth;
        this.mVideoHeight = videoHeight;
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
     * 设置 滤镜
     *
     * @param filter GLFilter
     */
    @Override
    public void setGLFilter(GLFilter filter) {
        this.mViewRender.setGLFilter(filter);
    }

    /**
     * 设置 GLViewRender (仅GLSurfaceView可用)
     *
     * @param viewRender {@link BaseGLViewRender}
     */
    @Override
    public void setGLViewRender(BaseGLViewRender viewRender) {
        if (viewRender != null) {
            this.mViewRender = viewRender;
            this.mViewRender.setSurfaceView(this);
            // Init GLViewRender
            this.initViewRender();
            // Update 渲染尺寸
            this.updateRenderSize();
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
     * 截图
     *
     * @param shotHigh 高清/普通
     */
    @Override
    public boolean onShotPic(boolean shotHigh) {
        this.mViewRender.onShotPic(shotHigh);
        return true;
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        this.isRelease = true;
        this.mViewRender.release();
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
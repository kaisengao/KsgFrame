package com.ksg.ksgplayer.renderer.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.View;

import com.ksg.ksgplayer.config.AspectRatio;
import com.ksg.ksgplayer.helper.MeasureHelper;
import com.ksg.ksgplayer.proxy.PlayerProxy;
import com.ksg.ksgplayer.renderer.Renderer;
import com.ksg.ksgplayer.renderer.RendererListener;
import com.ksg.ksgplayer.renderer.filter.base.GLFilter;
import com.ksg.ksgplayer.renderer.glrender.GLViewRender;
import com.ksg.ksgplayer.renderer.listener.GLSurfaceListener;

/**
 * @ClassName: KsgGLSurfaceView
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/22 21:57
 * @Description: GLSurfaceView
 */
public class KsgGLSurfaceView extends GLSurfaceView implements Renderer, GLSurfaceListener {

    private static final String TAG = KsgSurfaceView.class.getSimpleName();

    public static final int MODE_LAYOUT_SIZE = 0;

    public static final int MODE_RENDER_SIZE = 1;

    private int mModeSize = MODE_LAYOUT_SIZE;

    private boolean isRelease = false;

    private MeasureHelper mMeasureHelper;

    private GLViewRender mViewRender;

    private PlayerProxy mPlayerProxy;

    public KsgGLSurfaceView(Context context) {
        this(context, null);
    }

    public KsgGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Init
     */
    public void init(GLViewRender viewRender, int modeSize) {
        this.setEGLContextClientVersion(2);
        // 视图宽高比算法
        this.mMeasureHelper = new MeasureHelper();
        // Render
        if (viewRender != null) {
            this.mViewRender = viewRender;
        } else {
            this.mViewRender = new GLViewRender();
        }
        // Mode
        if (modeSize != -1) {
            this.mModeSize = modeSize;
        }
        // Init ViewRender
        this.initViewRender();
    }

    /**
     * Init ViewRender
     */
    private void initViewRender() {
        this.mViewRender.setSurfaceView(this);
        this.mViewRender.setGLSurfaceListener(this);
        this.setRenderer(mViewRender);
        this.setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    /**
     * onSurfaceAvailable
     */
    @Override
    public void onSurfaceAvailable(Surface surface) {
        if (mPlayerProxy != null) {
            this.post(() -> mPlayerProxy.setSurface(surface));
        }
    }

    /**
     * onMeasure
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mModeSize == MODE_RENDER_SIZE) {
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
        if (mViewRender != null) {
            this.mViewRender.setVideoInfo(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());
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
     * 设置 滤镜
     *
     * @param filter GLFilter
     */
    @Override
    public void setGLFilter(GLFilter filter) {
        this.mViewRender.setGLFilter(filter);
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
        this.mViewRender.onShotPic();
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
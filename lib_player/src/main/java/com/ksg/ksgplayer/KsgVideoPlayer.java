package com.ksg.ksgplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.ColorInt;

import com.ksg.ksgplayer.config.PlayerConfig;
import com.ksg.ksgplayer.cover.ICoverEvent;
import com.ksg.ksgplayer.cover.ICoverManager;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.handler.CoverEventHandler;
import com.ksg.ksgplayer.listener.OnCoverEventListener;
import com.ksg.ksgplayer.listener.OnErrorListener;
import com.ksg.ksgplayer.listener.OnPlayerListener;
import com.ksg.ksgplayer.listener.OnRendererListener;
import com.ksg.ksgplayer.player.BasePlayer;
import com.ksg.ksgplayer.player.IPlayer;
import com.ksg.ksgplayer.producer.BaseEventProducer;
import com.ksg.ksgplayer.proxy.PlayerProxy;
import com.ksg.ksgplayer.renderer.Renderer;
import com.ksg.ksgplayer.renderer.RendererListenerAdapter;
import com.ksg.ksgplayer.renderer.RendererType;
import com.ksg.ksgplayer.renderer.glrender.GLViewRender;
import com.ksg.ksgplayer.renderer.view.KsgGLSurfaceView;
import com.ksg.ksgplayer.renderer.view.KsgSurfaceView;
import com.ksg.ksgplayer.renderer.view.KsgTextureView;
import com.ksg.ksgplayer.state.PlayerInfoGetter;
import com.ksg.ksgplayer.state.StateGetter;
import com.ksg.ksgplayer.widget.VideoContainer;

import java.util.List;

/**
 * @ClassName: KsgVideoPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:08
 * @Description: {基于 @link https://juejin.im/post/5b0d4e6bf265da090f7376d2}
 * 加入了一些自己的理解
 */
public class KsgVideoPlayer implements IKsgVideoPlayer {

    private Context mContext;

    private int mGLModeSize = -1;

    private int mRendererType = PlayerConfig.getRendererType();

    private boolean mUserPause;

    private GLViewRender mGLViewRender;

    private PlayerProxy mPlayerProxy;

    private VideoContainer mVideoContainer;

    private CoverEventHandler mCoverEventHandler;

    private Renderer mRenderer;

    private OnPlayerListener mPlayerListener;

    private OnErrorListener mErrorListener;

    private OnCoverEventListener mCoverEventListener;

    public KsgVideoPlayer(Context context) {
        // Init
        this.init(context);
    }

    /**
     * Init
     *
     * @param context context
     */
    private void init(Context context) {
        this.mContext = context;
        // Init Player
        this.initPlayer();
        // Init Container
        initContainer();
    }

    /**
     * Init Container
     */
    private void initContainer() {
        this.mVideoContainer = new VideoContainer(mContext);
        this.mVideoContainer.setStateGetter(mStateGetter);
        this.mVideoContainer.setCoverEventListener(mContainerCoverEventListener);
    }

    /**
     * 设置 背景颜色
     *
     * @param color color
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public void setBackgroundColor(@ColorInt int color) {
        this.mVideoContainer.setBackgroundColor(color);
    }

    /**
     * 设置 覆盖组件管理器
     *
     * @param coverManager coverManager
     */
    @Override
    public void setCoverManager(ICoverManager coverManager) {
        this.mVideoContainer.setCoverManager(coverManager);
    }

    /**
     * 添加自定义事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    @Override
    public void addEventProducer(BaseEventProducer eventProducer) {
        this.mVideoContainer.addEventProducer(eventProducer);
    }

    /**
     * 移除一个事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    @Override
    public void removeEventProducer(BaseEventProducer eventProducer) {
        this.mVideoContainer.removeEventProducer(eventProducer);
    }

    /**
     * 返回事件生产者集合 便于控制
     *
     * @return List
     */
    @Override
    public List<BaseEventProducer> getEventProducers() {
        return mVideoContainer.getEventProducers();
    }

    /**
     * 绑定 视图容器
     *
     * @param container container
     */
    @Override
    public void bindContainer(ViewGroup container) {
        this.bindContainer(container, false);
    }

    /**
     * 绑定 视图容器
     *
     * @param container      container
     * @param updateRenderer 更新渲染器
     */
    @Override
    public void bindContainer(ViewGroup container, boolean updateRenderer) {
        this.unbindContainer();
        container.addView(mVideoContainer, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 解绑 视图容器
     */
    @Override
    public void unbindContainer() {
        ViewParent parent = mVideoContainer.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(mVideoContainer);
        }
    }

    /**
     * 设置 GLViewRender (仅GLSurfaceView可用)
     *
     * @param viewRender {@link GLViewRender}
     * @param modeSize   {@link KsgGLSurfaceView} 测量模式
     */
    @Override
    public void setGLViewRender(GLViewRender viewRender, int modeSize) {
        this.mGLViewRender = viewRender;
        this.mGLModeSize = modeSize;
        // 强制设置为GLSurfaceView模式
        this.setRenderer(RendererType.GL_SURFACE);
    }

    /**
     * 设置 解码器
     *
     * @param decoderView decoderView
     */
    @Override
    public boolean setDecoderView(BasePlayer decoderView) {
        BasePlayer oldDecoderView = mPlayerProxy.getDecoderView();
        // 去重
        if (oldDecoderView != null && oldDecoderView == decoderView) {
            return false;
        }
        // 设置 解码器
        this.mPlayerProxy.setDecoderView(decoderView);
        // 设置 渲染器
        this.setRenderer(getRendererType());
        // Return
        return true;
    }

    /**
     * 获取 解码器
     *
     * @return {@link BasePlayer}
     */
    public BasePlayer getDecoderView() {
        return mPlayerProxy.getDecoderView();
    }

    /**
     * 设置 渲染器类型
     *
     * @param rendererType {@link RendererType}
     */
    @Override
    public void setRendererType(int rendererType) {
        this.mRendererType = rendererType;
    }

    /**
     * 获取 渲染器类型
     *
     * @return {@link RendererType}
     */
    @Override
    public int getRendererType() {
        return mRendererType;
    }

    /**
     * 设置 渲染器
     *
     * @param rendererType {@link Renderer}
     */
    @Override
    public void setRenderer(int rendererType) {
        this.mRendererType = rendererType;
        this.mPlayerProxy.setSurface(null);
        this.mPlayerProxy.setDisplay(null);
        // 释放渲染器
        this.releaseRenderer();
        // 创建渲染器
        Renderer renderer;
        switch (rendererType) {
            case RendererType.SURFACE:
                // SurfaceView
                renderer = new KsgSurfaceView(mContext);
                break;
            case RendererType.GL_SURFACE:
                // GLSurfaceView
                KsgGLSurfaceView glSurfaceView = new KsgGLSurfaceView(mContext);
                glSurfaceView.init(mGLViewRender, mGLModeSize);
                renderer = glSurfaceView;
                break;
            case RendererType.TEXTURE:
            default:
                // TextureView
                renderer = new KsgTextureView(mContext);
                break;
        }
        this.mRenderer = renderer;
        // 绑定
        this.mRenderer.bindPlayer(mPlayerProxy);
        // 回调事件
        this.mRenderer.setRendererListener(mRendererListenerAdapter);
        // 设置渲染器
        this.mVideoContainer.setRenderer(mRenderer.getRendererView());
    }

    /**
     * 获取 渲染器
     *
     * @return {@link Renderer}
     */
    @Override
    public Renderer getRenderer() {
        return mRenderer;
    }

    /**
     * 释放渲染器
     */
    public void releaseRenderer() {
        Renderer renderer = getRenderer();
        if (renderer != null) {
            renderer.setRendererListener(null);
            renderer.release();
            this.mRenderer = null;
        }
    }

    /**
     * 是否处于播放
     *
     * @return boolean
     */
    @Override
    public boolean isItPlaying() {
        return mPlayerProxy.isItPlaying();
    }

    /**
     * Init Player
     */
    @Override
    public void initPlayer() {
        this.mPlayerProxy = new PlayerProxy();
        this.mPlayerProxy.setPlayerListener(mPlayerProxyListener);
        this.mPlayerProxy.setErrorListener(mErrorProxyListener);
        // Cover事件处理程序实现类
        this.mCoverEventHandler = new CoverEventHandler(this);
    }

    /**
     * 获取当前state
     * {@link IPlayer#STATE_DESTROY}
     * {@link IPlayer#STATE_ERROR}
     * {@link IPlayer#STATE_IDLE}
     * {@link IPlayer#STATE_INIT}
     * {@link IPlayer#STATE_PREPARED}
     * {@link IPlayer#STATE_START}
     * {@link IPlayer#STATE_PAUSE}
     * {@link IPlayer#STATE_STOP}
     * {@link IPlayer#STATE_COMPLETE}
     */
    @Override
    public int getState() {
        return mPlayerProxy.getState();
    }

    /**
     * 自定义消息
     *
     * @param code   code
     * @param bundle bundle
     */
    public void option(int code, Bundle bundle) {
        this.mPlayerProxy.option(code, bundle);
    }

    /**
     * 设置数据源
     *
     * @param dataSource 数据源
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        this.mPlayerProxy.setDataSource(dataSource);
    }

    /**
     * 设置渲染器
     *
     * @param surface surface
     */
    @Override
    public void setSurface(Surface surface) {
        this.mPlayerProxy.setSurface(surface);
    }

    /**
     * 设置渲染器
     *
     * @param holder surfaceHolder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        this.mPlayerProxy.setDisplay(holder);
    }

    /**
     * 获取自定义渲染器
     *
     * @return View
     */
    @Override
    public View getCustomRenderer() {
        return mPlayerProxy.getCustomRenderer();
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float left, float right) {
        this.mPlayerProxy.setVolume(left, right);
    }

    /**
     * 设置循环播放
     */
    @Override
    public void setLooping(boolean looping) {
        this.mPlayerProxy.setLooping(looping);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        this.mPlayerProxy.setSpeed(speed);
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        return mPlayerProxy.getSpeed();
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        return mPlayerProxy.getTcpSpeed();
    }

    /**
     * 获取缓冲进度百分比
     */
    @Override
    public int getBufferPercentage() {
        return mPlayerProxy.getBufferPercentage();
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getCurrentPosition() {
        return mPlayerProxy.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        return mPlayerProxy.getDuration();
    }

    /**
     * 获取播放状态
     */
    @Override
    public boolean isPlaying() {
        return mPlayerProxy.isPlaying();
    }

    /**
     * 跳到指定位置
     */
    @Override
    public void seekTo(long msc) {
        this.mPlayerProxy.seekTo(msc);
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        this.mPlayerProxy.start();
    }

    /**
     * 播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void start(long msc) {
        this.mPlayerProxy.start(msc);
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        this.mPlayerProxy.pause();
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        if (!mUserPause) {
            this.mPlayerProxy.resume();
            this.mUserPause = false;
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        this.mPlayerProxy.stop();
    }

    /**
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void replay(long msc) {
        // 2022年05月12日
        // IJK_BUG https://github.com/bilibili/ijkplayer/issues/2786
        // 再重新播放时会导致Surface不更新
        // 目前解决方案 重新Add到Layout下即可
        // 重新设置渲染器 
        Renderer renderer = getRenderer();
        if (renderer != null) {
            this.mVideoContainer.setRenderer(renderer.getRendererView());
        }
        this.mPlayerProxy.replay(msc);
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        this.mPlayerProxy.reset();
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        this.mPlayerProxy.release();
    }

    /**
     * 设置 播放事件
     *
     * @param playerListener playerListener
     */

    @Override
    public void setPlayerListener(OnPlayerListener playerListener) {
        this.mPlayerListener = playerListener;
    }

    /**
     * 设置 错误事件
     *
     * @param errorListener errorListener
     */
    @Override
    public void setErrorListener(OnErrorListener errorListener) {
        this.mErrorListener = errorListener;
    }

    /**
     * 设置 Cover组件事件
     *
     * @param coverEventListener coverEventListener
     */
    @Override
    public void setCoverEventListener(OnCoverEventListener coverEventListener) {
        this.mCoverEventListener = coverEventListener;
    }

    /**
     * 设置 渲染器事件
     *
     * @param rendererListener rendererListener
     */
    @Override
    public void setRendererListener(OnRendererListener rendererListener) {
        this.mRendererListenerAdapter.setRendererListener(rendererListener);
    }

    /**
     * 一些状态获取器
     */
    private final StateGetter mStateGetter = new StateGetter() {

        @Override
        public PlayerInfoGetter getPlayerInfoGetter() {
            return mPlayerProxy.mPlayerInfoGetter;
        }
    };

    /**
     * 播放事件
     */
    private final OnPlayerListener mPlayerProxyListener = new OnPlayerListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            switch (eventCode) {
                case OnPlayerListener.PLAYER_EVENT_ON_PREPARED:
                    // 事件 准备完毕
                    if (bundle != null && mRenderer != null) {
                        int videoWidth = bundle.getInt(EventKey.INT_ARG1);
                        int videoHeight = bundle.getInt(EventKey.INT_ARG2);
                        // 渲染器
                        if (getRenderer() != null) {
                            // 设置 画面宽高
                            getRenderer().setVideoSize(videoWidth, videoHeight);
                        }
                    }
                    break;
                case OnPlayerListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE:
                    // 事件 视频尺寸改变
                    if (bundle != null) {
                        int videoWidth = bundle.getInt(EventKey.INT_ARG1);
                        int videoHeight = bundle.getInt(EventKey.INT_ARG2);
                        int videoSarNum = bundle.getInt(EventKey.INT_ARG3);
                        int videoSarDen = bundle.getInt(EventKey.INT_ARG4);
                        // 渲染器
                        if (getRenderer() != null) {
                            // 设置 画面宽高
                            getRenderer().setVideoSize(videoWidth, videoHeight);
                            // 设置 视频采样率
                            getRenderer().setVideoSampleAspectRatio(videoSarNum, videoSarDen);
                        }
                    }
                    break;
                case OnPlayerListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED:
                    // 事件 视频旋转
                    if (bundle != null) {
                        int degree = bundle.getInt(EventKey.INT_DATA);
                        // 渲染器
                        if (getRenderer() != null) {
                            // 设置 画面旋转角度
                            getRenderer().setRotationDegrees(degree);
                        }
                    }
                    break;
                default:
                    break;
            }
            // 回调给组件
            mVideoContainer.dispatchPlayEvent(eventCode, bundle);
            // 回调上一级
            if (mPlayerListener != null) {
                mPlayerListener.onPlayerEvent(eventCode, bundle);
            }
        }
    };

    /**
     * 错误事件
     */
    private final OnErrorListener mErrorProxyListener = new OnErrorListener() {
        @Override
        public void onErrorEvent(int eventCode, Bundle bundle) {
            // 回调给组件
            mVideoContainer.dispatchErrorEvent(eventCode, bundle);
            // 回调上一级
            if (mErrorListener != null) {
                mErrorListener.onErrorEvent(eventCode, bundle);
            }
        }
    };

    /**
     * 组件事件
     */
    private final OnCoverEventListener mContainerCoverEventListener = new OnCoverEventListener() {
        @Override
        public void onCoverEvent(int eventCode, Bundle bundle) {
            switch (eventCode) {
                case ICoverEvent.CODE_REQUEST_PAUSE:
                    mUserPause = true;
                    break;
                case ICoverEvent.CODE_REQUEST_RESUME:
                    mUserPause = false;
                    break;
            }
            // Cover事件处理程序
            if (mCoverEventHandler != null) {
                mCoverEventHandler.onHandle(eventCode, bundle);
            }
            // 回调上一级
            if (mCoverEventListener != null) {
                mCoverEventListener.onCoverEvent(eventCode, bundle);
            }
        }
    };

    /**
     * 渲染器事件
     */
    private final RendererListenerAdapter mRendererListenerAdapter = new RendererListenerAdapter() {

        /**
         * 截图
         *
         * @param bitmap bitmap
         */
        @Override
        public void onShotPic(Bitmap bitmap) {
            if (mRendererListener != null) {
                mVideoContainer.post(() -> mRendererListener.onShotPic(bitmap));
            }
        }
    };

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.releaseRenderer();
        this.mCoverEventHandler = null;
        this.mPlayerProxy.destroy();
        this.mVideoContainer.destroy();
        this.unbindContainer();
    }
}

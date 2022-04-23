package com.ksg.ksgplayer;

import android.content.Context;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.ksg.ksgplayer.config.AspectRatio;
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
import com.ksg.ksgplayer.renderer.view.KsgGLSurfaceView;
import com.ksg.ksgplayer.renderer.view.KsgSurfaceView;
import com.ksg.ksgplayer.renderer.view.KsgTextureView;
import com.ksg.ksgplayer.state.PlayerStateGetter;
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

    private boolean mUserPause;

    private boolean isBuffering;

    private PlayerProxy mPlayer;

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
        // 初始化视图容器
        this.mVideoContainer = createContainer();
    }

    /**
     * 创建视图容器
     */
    private VideoContainer createContainer() {
        VideoContainer container = new VideoContainer(mContext);
        container.setStateGetter(mStateGetter);
        container.setCoverEventListener(mContainerCoverEventListener);
        return container;
    }

    /**
     * 设置 背景颜色
     *
     * @param resId resId
     */
    @Override
    public void setBackgroundColor(@ColorRes int resId) {
        this.mVideoContainer.setBackgroundColor(ContextCompat.getColor(mContext, resId));
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
     * 设置 解码器
     *
     * @param decoderView decoderView
     */
    @Override
    public boolean setDecoderView(BasePlayer decoderView) {
        BasePlayer oldDecoderView = mPlayer.getDecoderView();
        // 去重
        if (oldDecoderView != null && oldDecoderView == decoderView) {
            return false;
        }
        // 设置 解码器
        this.mPlayer.setDecoderView(decoderView);
        // 设置 渲染器
        this.setRendererType(PlayerConfig.getRenderType());
        // Return
        return true;
    }

    /**
     * 获取 解码器
     *
     * @return {@link BasePlayer}
     */
    public BasePlayer getDecoderView() {
        return mPlayer.getDecoderView();
    }

    /**
     * 设置 渲染器
     *
     * @param rendererType {@link Renderer}
     */
    @Override
    public void setRendererType(int rendererType) {
        Renderer renderer;
        // 释放渲染器
        this.releaseRenderer();
        // 渲染器
        switch (rendererType) {
            case RendererType.SURFACE:
                // SurfaceView
                renderer = new KsgSurfaceView(mContext);
                break;
            case RendererType.GL_SURFACE:
                // GLSurfaceView
                renderer = new KsgGLSurfaceView(mContext);
                break;
            case RendererType.TEXTURE:
            default:
                // TextureView
                renderer = new KsgTextureView(mContext);
                break;
        }
        this.mRenderer = renderer;
        // 初始
        this.mPlayer.setSurface(null);
        this.mPlayer.setDisplay(null);
        // BindPlayer
        this.mRenderer.bindPlayer(mPlayer);
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
     * 设置 画面旋转角度
     *
     * @param degree 角度
     */
    @Override
    public void setRotationDegrees(int degree) {
        if (getRenderer() != null) {
            this.getRenderer().setRotationDegrees(degree);
        }
    }

    /**
     * 获取 画面旋转角度
     *
     * @return degree 角度
     */
    @Override
    public int getRotationDegrees() {
        if (getRenderer() != null) {
            return getRenderer().getRotationDegrees();
        }
        return 0;
    }

    /**
     * 设置 画面比例
     *
     * @param aspectRatio {@link AspectRatio}
     */
    @Override
    public void setAspectRatio(int aspectRatio) {
        if (getRenderer() != null) {
            // 1、调整View去适应比例变化
            this.getRenderer().changeLayoutParams(aspectRatio);
            // 2、设置比例
            this.getRenderer().setAspectRatio(aspectRatio);
        }
    }

    /**
     * 设置 自定义画面比例
     *
     * @param customAspectRatio 自定义比例 （例：16/9 = 1.77）
     */
    @Override
    public void setCustomAspectRatio(int customAspectRatio) {
        if (getRenderer() != null) {
            this.getRenderer().setCustomAspectRatio(customAspectRatio);
        }
    }

    /**
     * 截图
     *
     * @param shotHigh 高清/普通
     * @describe: 使用此方法后监听 {@link OnRendererListener}事件获取截图
     */
    @Override
    public boolean onShotPic(boolean shotHigh) {
        if (mRenderer != null) {
            return mRenderer.onShotPic(shotHigh);
        }
        return false;
    }

    /**
     * 是否处于播放
     *
     * @return boolean
     */
    @Override
    public boolean isItPlaying() {
        return mPlayer.isItPlaying();
    }

    /**
     * Init Player
     */
    @Override
    public void initPlayer() {
        this.mPlayer = new PlayerProxy();
        this.mPlayer.setPlayerListener(mPlayerProxyListener);
        this.mPlayer.setErrorListener(mErrorProxyListener);
        // Cover事件处理程序实现类
        this.mCoverEventHandler = new CoverEventHandler(mPlayer);
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
        return mPlayer.getState();
    }

    /**
     * 自定义消息
     *
     * @param code   code
     * @param bundle bundle
     */
    public void option(int code, Bundle bundle) {
        this.mPlayer.option(code, bundle);
    }

    /**
     * 设置数据源
     *
     * @param dataSource 数据源
     */
    @Override
    public void setDataSource(DataSource dataSource) {
        this.mPlayer.setDataSource(dataSource);
    }

    /**
     * 设置渲染器
     *
     * @param surface surface
     */
    @Override
    public void setSurface(Surface surface) {
        this.mPlayer.setSurface(surface);
    }

    /**
     * 设置渲染器
     *
     * @param holder surfaceHolder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        this.mPlayer.setDisplay(holder);
    }

    /**
     * 获取自定义渲染器
     *
     * @return View
     */
    @Override
    public View getCustomRenderer() {
        return mPlayer.getCustomRenderer();
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float left, float right) {
        this.mPlayer.setVolume(left, right);
    }

    /**
     * 设置循环播放
     */
    @Override
    public void setLooping(boolean looping) {
        this.mPlayer.setLooping(looping);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        this.mPlayer.setSpeed(speed);
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        return mPlayer.getSpeed();
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        return mPlayer.getTcpSpeed();
    }

    /**
     * 获取缓冲进度百分比
     */
    @Override
    public int getBufferPercentage() {
        return mPlayer.getBufferPercentage();
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        return mPlayer.getDuration();
    }

    /**
     * 获取播放状态
     */
    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    /**
     * 跳到指定位置
     */
    @Override
    public void seekTo(long msc) {
        this.mPlayer.seekTo(msc);
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        this.mPlayer.start();
    }

    /**
     * 播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void start(long msc) {
        this.mPlayer.start(msc);
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        if (mPlayer.isItPlaying()) {
            this.mPlayer.pause();
        }
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        if (isItPlaying()) {
            if (!mUserPause) {
                this.mPlayer.resume();
                this.mUserPause = false;
            }
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        this.mPlayer.stop();
    }

    /**
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void replay(long msc) {
        this.mPlayer.replay(msc);
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        this.mPlayer.reset();
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        this.mPlayer.release();
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
     * 播放事件
     */
    private final OnPlayerListener mPlayerProxyListener = new OnPlayerListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            switch (eventCode) {
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
                case OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_START:
                    // 事件 开始缓冲
                    isBuffering = true;
                    break;
                case OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_END:
                    // 事件 结束缓冲
                    isBuffering = false;
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
     * 一些状态获取器
     */
    private final StateGetter mStateGetter = new StateGetter() {
        @Override
        public PlayerStateGetter getPlayerStateGetter() {
            return new PlayerStateGetter() {
                @Override
                public int getState() {
                    return mPlayer.getState();
                }

                @Override
                public long getProgress() {
                    return mPlayer.getCurrentPosition();
                }

                @Override
                public long getDuration() {
                    return mPlayer.getDuration();
                }

                @Override
                public int getBufferPercentage() {
                    return mPlayer.getBufferPercentage();
                }

                @Override
                public boolean isBuffering() {
                    return isBuffering;
                }

                @Override
                public float getSpeed() {
                    return mPlayer.getSpeed();
                }
            };
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
    };

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.releaseRenderer();
        this.mCoverEventHandler = null;
        this.mPlayer.destroy();
        this.mVideoContainer.destroy();
        this.unbindContainer();
    }
}

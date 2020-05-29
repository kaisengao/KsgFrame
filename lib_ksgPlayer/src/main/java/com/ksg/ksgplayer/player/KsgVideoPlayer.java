package com.ksg.ksgplayer.player;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.kasiengao.base.util.KLog;
import com.ksg.ksgplayer.assist.InterEvent;
import com.ksg.ksgplayer.assist.OnVideoViewEventHandler;
import com.ksg.ksgplayer.config.KsgPlayerConfig;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.extension.BaseEventProducer;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.listener.OnReceiverEventListener;
import com.ksg.ksgplayer.producer.NetworkEventProducer;
import com.ksg.ksgplayer.receiver.IReceiverGroup;
import com.ksg.ksgplayer.receiver.PlayerStateGetter;
import com.ksg.ksgplayer.receiver.StateGetter;
import com.ksg.ksgplayer.render.AspectRatio;
import com.ksg.ksgplayer.render.IRender;
import com.ksg.ksgplayer.render.RenderSurfaceView;
import com.ksg.ksgplayer.render.RenderTextureView;
import com.ksg.ksgplayer.widget.KsgContainer;

import java.util.List;

/**
 * @ClassName: KsgVideoPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:08
 * @Description: 播放器  {借鉴 @link https://juejin.im/post/5b0d4e6bf265da090f7376d2  掘金大神的PlayerBase}
 */
public class KsgVideoPlayer implements IKagVideoPlayer {

    private Context mContext;

    private int mVideoWidth;

    private int mVideoHeight;

    private int mVideoSarNum;

    private int mVideoSarDen;

    private int mVideoRotation;

    private boolean mIsBuffering;

    private KsgPlayerProxy mKsgPlayer;

    private KsgContainer mKsgContainer;

    public IRender mRender;

    private IRender.IRenderHolder mRenderHolder;

    private AspectRatio mAspectRatio = AspectRatio.AspectRatio_FIT_PARENT;

    private OnPlayerEventListener mOnPlayerEventListener;

    private OnErrorEventListener mOnErrorEventListener;

    private OnReceiverEventListener mOnReceiverEventListener;

    private OnVideoViewEventHandler mOnVideoViewEventHandler;

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
        // 初始化播放器代理
        this.mKsgPlayer = createKsgPlayer();
        // 初始化容器
        this.mKsgContainer = onCreateKsgContainer(context);
    }

    /**
     * 创建播放器实例
     */
    private KsgPlayerProxy createKsgPlayer() {
        KsgPlayerProxy ksgPlayer = new KsgPlayerProxy();
        ksgPlayer.setOnPlayerEventListener(mInternalPlayerEventListener);
        ksgPlayer.setOnErrorEventListener(mInternalErrorEventListener);
        return ksgPlayer;
    }

    /**
     * 创建容器 默认添加网络状态事件{@link NetworkEventProducer}
     *
     * @param context context
     */
    private KsgContainer onCreateKsgContainer(Context context) {
        KsgContainer ksgContainer = new KsgContainer(context);
        ksgContainer.setStateGetter(mInternalStateGetter);
        ksgContainer.setOnReceiverEventListener(mInternalReceiverEventListener);
        // 添加网络状态更改事件
        if (KsgPlayerConfig.getInstance().isNetworkEventProducer()) {
            ksgContainer.addEventProducer(new NetworkEventProducer(mContext));
        }
        return ksgContainer;
    }

    /**
     * Cover组件组
     *
     * @param receiverGroup receiverGroup
     */
    public void setReceiverGroup(IReceiverGroup receiverGroup) {
        this.mKsgContainer.setReceiverGroup(receiverGroup);
    }

    /**
     * 添加自定义事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    public void addEventProducer(BaseEventProducer eventProducer) {
        this.mKsgContainer.addEventProducer(eventProducer);
    }

    /**
     * 移除一个事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    public void removeEventProducer(BaseEventProducer eventProducer) {
        this.mKsgContainer.removeEventProducer(eventProducer);
    }

    /**
     * 返回事件生产者集合 便于控制
     *
     * @return List
     */
    public List<BaseEventProducer> getEventProducers() {
        return mKsgContainer.getEventProducers();
    }

    /**
     * 返回Ksg容器
     *
     * @return KsgContainer
     */
    public KsgContainer getKsgContainer() {
        return mKsgContainer;
    }

    /**
     * 容器
     *
     * @param userContainer ViewGroup
     */
    @Override
    public void attachContainer(ViewGroup userContainer) {
        this.attachContainer(userContainer, false);
    }

    /**
     * 容器
     *
     * @param userContainer ViewGroup
     * @param updateRender  是否更新渲染view
     */
    @Override
    public void attachContainer(ViewGroup userContainer, boolean updateRender) {
        this.detachSuperContainer();
        // attach container
        if (userContainer != null) {
            userContainer.addView(mKsgContainer, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 初始化容器
     */
    private void detachSuperContainer() {
        ViewParent parent = this.mKsgContainer.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(this.mKsgContainer);
        }
    }

    /**
     * 获取当前state
     * {@link IKsgPlayer#STATE_END}
     * {@link IKsgPlayer#STATE_ERROR}
     * {@link IKsgPlayer#STATE_IDLE}
     * {@link IKsgPlayer#STATE_INITIALIZED}
     * {@link IKsgPlayer#STATE_PREPARED}
     * {@link IKsgPlayer#STATE_STARTED}
     * {@link IKsgPlayer#STATE_PAUSED}
     * {@link IKsgPlayer#STATE_STOPPED}
     * {@link IKsgPlayer#STATE_PLAYBACK_COMPLETE}
     */
    @Override
    public int getState() {
        return mKsgPlayer.getState();
    }

    /**
     * 自定义消息
     *
     * @param code   code
     * @param bundle bundle
     */
    public void option(int code, Bundle bundle) {
        this.mKsgPlayer.option(code, bundle);
    }

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    @Override
    public void setDataSource(String dataSource) {
        // 请求获取音频焦点
        this.requestAudioFocus();
        // 设置数据源
        this.mKsgPlayer.setDataSource(dataSource);
    }

    /**
     * 请求获取音频焦点
     */
    private void requestAudioFocus() {
        KLog.d("zzz", ">>requestAudioFocus<<");
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            // 请求焦点的参数说明：
            // AUDIOFOCUS_GAIN：想要长期占有焦点，失去焦点者stop播放和释放
            // AUDIOFOCUS_GAIN_TRANSIENT：想要短暂占有焦点，失去焦点者pause播放
            // AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK：想要短暂占有焦点，失去焦点者可以继续播放但是音量需要调低
            // AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE：想要短暂占有焦点，但希望失去焦点者不要有声音播放
            audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    /**
     * 释放音频焦点
     */
    private void releaseAudioFocus() {
        KLog.d("zzz", "<<releaseAudioFocus>>");
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.abandonAudioFocus(null);
        }
    }

    /**
     * 设置渲染视图类型
     *
     * @param renderType {@link IRender}
     */
    @Override
    public void setRenderType(int renderType) {
        // 存储类型
        KsgPlayerConfig.getInstance().setDefaultRenderType(renderType);
        // 销毁视图资源
        this.releaseRender();
        // 判断类型
        switch (renderType) {
            case IRender.RENDER_TYPE_SURFACE_VIEW:
                // SurfaceView
                this.mRender = new RenderSurfaceView(mContext);
                break;
            default:
            case IRender.RENDER_TYPE_TEXTURE_VIEW:
                // 默认 TextureView
                this.mRender = new RenderTextureView(mContext);
                ((RenderTextureView) mRender).setTakeOverSurfaceTexture(true);
                break;
        }
        // 初始化 RenderHolder
        this.mRenderHolder = null;
        this.mKsgPlayer.setSurface(null);
        this.mRender.updateAspectRatio(mAspectRatio);
        this.mRender.setRenderCallback(mRenderCallback);
        // 设置画面宽高
        this.mRender.updateVideoSize(mVideoWidth, mVideoHeight);
        this.mRender.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
        // 设置视频旋转角度
        this.mRender.setVideoRotation(mVideoRotation);
        // add to container
        this.mKsgContainer.setRenderView(mRender.getRenderView());
    }

    /**
     * 销毁视图资源
     * <p>
     * {@link RenderTextureView#release()}
     */
    public void releaseRender() {
        if (mRender != null) {
            this.mRender.release();
            this.mRender = null;
        }
    }

    /**
     * 设置解码器
     *
     * @param decoderView decoderView
     */
    @Override
    public void setDecoderView(BaseInternalPlayer decoderView) {
        BaseInternalPlayer oldDecoderView = this.mKsgPlayer.getDecoderView();
        // 如果与已有的解码器一致则不需要切换
        if (oldDecoderView != null && oldDecoderView == decoderView) {
            return;
        }
        // 销毁视图资源
        this.releaseRender();
        // 设置新（播放器）解码器
        this.mKsgPlayer.setDecoderView(decoderView);
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float left, float right) {
        this.mKsgPlayer.setVolume(left, right);
    }

    /**
     * 设置是否循环播放
     */
    @Override
    public void setLooping(boolean isLooping) {
        this.mKsgPlayer.setLooping(isLooping);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        this.mKsgPlayer.setSpeed(speed);
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        return this.mKsgPlayer.getSpeed();
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        return this.mKsgPlayer.getTcpSpeed();
    }

    /**
     * 获取缓冲进度百分比
     *
     * @return 缓冲进度
     */
    @Override
    public int getBufferedPercentage() {
        return this.mKsgPlayer.getBufferPercentage();
    }

    /**
     * 获取当前播放的位置
     *
     * @return 播放进度
     */
    @Override
    public long getCurrentPosition() {
        return this.mKsgPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     *
     * @return 总时长
     */
    @Override
    public long getDuration() {
        return this.mKsgPlayer.getDuration();
    }

    /**
     * 获取播放状态
     *
     * @return 播放状态 true 播放 反之
     */
    @Override
    public boolean isPlaying() {
        return this.mKsgPlayer.isPlaying();
    }

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    @Override
    public void seekTo(long msc) {
        this.mKsgPlayer.seekTo(msc);
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        this.mKsgPlayer.start();
    }

    /**
     * 播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void start(long msc) {
        this.mKsgPlayer.start(msc);
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        this.mKsgPlayer.pause();
    }

    /**
     * 继续播放
     */
    @Override
    public void resume() {
        this.mKsgPlayer.resume();
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        this.mKsgPlayer.stop();
    }

    /**
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    @Override
    public void rePlay(int msc) {
        this.mKsgPlayer.rePlay(msc);
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        this.mKsgPlayer.reset();
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        this.mKsgPlayer.release();
    }

    /**
     * 销毁资源
     */
    @Override
    public void destroy() {
        this.releaseRender();
        this.releaseAudioFocus();
        this.mRenderHolder = null;
        this.mKsgPlayer.destroy();
        this.mKsgContainer.destroy();
        this.detachSuperContainer();
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    public void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        this.mOnErrorEventListener = onErrorEventListener;
    }

    public void setOnReceiverEventListener(OnReceiverEventListener onReceiverEventListener) {
        this.mOnReceiverEventListener = onReceiverEventListener;
    }

    public void setOnVideoViewEventHandler(OnVideoViewEventHandler onVideoViewEventHandler) {
        this.mOnVideoViewEventHandler = onVideoViewEventHandler;
    }

    /**
     * 播放器的基础事件
     */
    private OnPlayerEventListener mInternalPlayerEventListener = new OnPlayerEventListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            switch (eventCode) {
                case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE:
                    // 视频大小改变，更新渲染
                    if (bundle != null) {
                        mVideoWidth = bundle.getInt(EventKey.INT_ARG1);
                        mVideoHeight = bundle.getInt(EventKey.INT_ARG2);
                        mVideoSarNum = bundle.getInt(EventKey.INT_ARG3);
                        mVideoSarDen = bundle.getInt(EventKey.INT_ARG4);
                        if (mRender != null) {
                            // 设置画面宽高
                            mRender.updateVideoSize(mVideoWidth, mVideoHeight);
                            // 设置视频采样率
                            mRender.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
                        }
                        KLog.d("zzz", "onVideoSizeChange : videoWidth = " + mVideoWidth
                                + ", videoHeight = " + mVideoHeight
                                + ", videoSarNum = " + mVideoSarNum
                                + ", videoSarDen = " + mVideoSarDen);
                    }
                    break;
                case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED:
                    // 视频发生旋转，更新渲染
                    if (bundle != null) {
                        // 判断如果需要旋转在更新渲染
                        mVideoRotation = bundle.getInt(EventKey.INT_DATA);
                        KLog.d("zzz", "onVideoRotationChange : videoRotation = " + mVideoRotation);
                        if (mRender != null) {
                            mRender.setVideoRotation(mVideoRotation);
                        }
                    }
                    break;
                case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:
                    mKsgContainer.setKeepScreenOn(true);
                    // 播放器准备完毕
                    if (bundle != null && mRender != null) {
                        mVideoWidth = bundle.getInt(EventKey.INT_ARG1);
                        mVideoHeight = bundle.getInt(EventKey.INT_ARG2);
                        mRender.updateVideoSize(mVideoWidth, mVideoHeight);
                    }
                    // 视图绑定
                    bindRenderHolder(mRenderHolder);
                    break;
                case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                case OnPlayerEventListener.PLAYER_EVENT_ON_STOP:
                    mKsgContainer.setKeepScreenOn(false);
                    break;
                case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START:
                    mIsBuffering = true;
                    break;
                case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END:
                    mIsBuffering = false;
                    break;
                default:
                    break;
            }
            if (mOnPlayerEventListener != null) {
                mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
            }
            mKsgContainer.dispatchPlayEvent(eventCode, bundle);
        }
    };

    /**
     * 播放器的错误事件
     */
    private OnErrorEventListener mInternalErrorEventListener = new OnErrorEventListener() {
        @Override
        public void onErrorEvent(int eventCode, Bundle bundle) {
            if (mOnErrorEventListener != null) {
                mOnErrorEventListener.onErrorEvent(eventCode, bundle);
            }
            mKsgContainer.dispatchErrorEvent(eventCode, bundle);
        }
    };

    /**
     * 播放器的状态获取
     */
    private StateGetter mInternalStateGetter = new StateGetter() {
        @Override
        public PlayerStateGetter getPlayerStateGetter() {
            return new PlayerStateGetter() {
                @Override
                public int getState() {
                    return mKsgPlayer.getState();
                }

                @Override
                public long getProgress() {
                    return mKsgPlayer.getCurrentPosition();
                }

                @Override
                public long getDuration() {
                    return mKsgPlayer.getDuration();
                }

                @Override
                public int getBufferPercentage() {
                    return mKsgPlayer.getBufferPercentage();
                }

                @Override
                public boolean isBuffering() {
                    return mIsBuffering;
                }
            };
        }
    };

    /**
     * 组件 事件回调
     */
    private OnReceiverEventListener mInternalReceiverEventListener = new OnReceiverEventListener() {
        @Override
        public void onReceiverEvent(int eventCode, Bundle bundle) {
            if (eventCode == InterEvent.CODE_REQUEST_NOTIFY_TIMER) {
                mKsgPlayer.setUseTimerProxy(true);
            } else if (eventCode == InterEvent.CODE_REQUEST_STOP_TIMER) {
                mKsgPlayer.setUseTimerProxy(false);
            }
            if (mOnVideoViewEventHandler != null) {
                mOnVideoViewEventHandler.onAssistHandle(KsgVideoPlayer.this, eventCode, bundle);
            }
            if (mOnReceiverEventListener != null) {
                mOnReceiverEventListener.onReceiverEvent(eventCode, bundle);
            }
        }
    };

    /**
     * 视图绑定
     *
     * @param renderHolder renderHolder
     */
    private void bindRenderHolder(IRender.IRenderHolder renderHolder) {
        if (renderHolder != null) {
            renderHolder.bindPlayer(mKsgPlayer);
        }
    }

    /**
     * 视图渲染回调
     */
    private IRender.IRenderCallback mRenderCallback = new IRender.IRenderCallback() {
        @Override
        public void onSurfaceCreated(IRender.IRenderHolder renderHolder, int width, int height) {
            KLog.d("zzz", "onSurfaceCreated : width = " + width + ", height = " + height);
            //on surface create ,try to attach player.
            mRenderHolder = renderHolder;
            bindRenderHolder(mRenderHolder);
        }

        @Override
        public void onSurfaceChanged(IRender.IRenderHolder renderHolder,
                                     int format, int width, int height) {
            //not handle some...
        }

        @Override
        public void onSurfaceDestroy(IRender.IRenderHolder renderHolder) {
            KLog.d("zzz", "onSurfaceDestroy...");
            //on surface destroy detach player
            mRenderHolder = null;
        }
    };
}

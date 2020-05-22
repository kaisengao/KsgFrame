package com.ksg.ksgplayer.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ksg.ksgplayer.KsgPlayerProxy;
import com.ksg.ksgplayer.assist.OnVideoViewEventHandler;
import com.ksg.ksgplayer.config.PlayerConfig;
import com.ksg.ksgplayer.extension.BaseEventProducer;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.listener.OnReceiverEventListener;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.producer.NetworkEventProducer;
import com.ksg.ksgplayer.receiver.IReceiverGroup;
import com.ksg.ksgplayer.receiver.PlayerStateGetter;
import com.ksg.ksgplayer.receiver.StateGetter;
import com.ksg.ksgplayer.render.RenderTextureView;

import java.util.List;

/**
 * @ClassName: KsgVideoPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:08
 * @Description: 播放器
 */
public class KsgVideoPlayer extends FrameLayout implements IKagVideoPlayer {

    private Context mContext;

    private boolean mIsBuffering;

    private KsgPlayerProxy mKsgPlayer;

    private KsgContainer mKsgContainer;

    private OnPlayerEventListener mOnPlayerEventListener;

    private OnErrorEventListener mOnErrorEventListener;

    private OnReceiverEventListener mOnReceiverEventListener;

    private OnVideoViewEventHandler mOnVideoViewEventHandler;
    private RenderTextureView mRenderView;

    public KsgVideoPlayer(Context context) {
        this(context, null);
    }

    public KsgVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KsgVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        // AddView
        this.addView(mKsgContainer, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
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
    protected KsgContainer onCreateKsgContainer(Context context) {
        KsgContainer ksgContainer = new KsgContainer(context);
        ksgContainer.setStateGetter(mInternalStateGetter);
        ksgContainer.setOnReceiverEventListener(mInternalReceiverEventListener);
        // 添加网络状态更改事件
        if (PlayerConfig.getInstance().isNetworkEventProducer()) {
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
        // 设置RenderView
        this.setRenderView();
        // 设置数据源
        this.mKsgPlayer.setDataSource(dataSource);
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
        // 设置新（播放器）解码器
        this.mKsgPlayer.setDecoderView(decoderView);
    }

    /**
     * 设置 RenderView
     */
    private void setRenderView() {

        this.releaseRender();

        mRenderView = new RenderTextureView(mContext);
        mRenderView.attachToPlayer(mKsgPlayer.getDecoderView());
        // 添加到容器内
        this.mKsgContainer.setRenderView(mRenderView.getView());
    }

    /**
     * 释放 RenderView
     * <p>
     * {@link RenderTextureView#release()}
     */
    private void releaseRender() {
        if (this.mRenderView != null) {
            this.mRenderView.release();
            this.mRenderView = null;
        }
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float v1, float v2) {
        this.mKsgPlayer.setVolume(v1, v2);
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
        return this.mKsgPlayer.getBufferedPercentage();
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
    public void seekTo(int msc) {
        this.mKsgPlayer.seekTo(msc);
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        this.start(0);
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
        this.mKsgPlayer.destroy();
        this.mKsgContainer.destroy();
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
                case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:
                    mKsgContainer.setKeepScreenOn(true);
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
                    return mKsgPlayer.getBufferedPercentage();
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
            if (mOnVideoViewEventHandler != null) {
                mOnVideoViewEventHandler.onAssistHandle(KsgVideoPlayer.this, eventCode, bundle);
            }
            if (mOnReceiverEventListener != null) {
                mOnReceiverEventListener.onReceiverEvent(eventCode, bundle);
            }
        }
    };
}

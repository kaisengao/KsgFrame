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
import com.ksg.ksgplayer.entity.DataSource;
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

import java.util.List;

/**
 * @author kaisengao
 * @create: 2019/1/11 9:58
 * @describe: 播放器 客户类
 */
public class KsgVideoPlayer extends FrameLayout implements IKagVideoPlayer {

    private Context mContext;

    private boolean mIsBuffering;

    private KsgPlayerProxy mKsgPlayer;

    private KsgContainer mKsgContainer;

    private BaseInternalPlayer mInternalPlayer;

    private OnPlayerEventListener mOnPlayerEventListener;

    private OnErrorEventListener mOnErrorEventListener;

    private OnReceiverEventListener mOnReceiverEventListener;

    private OnVideoViewEventHandler mOnVideoViewEventHandler;

    public KsgVideoPlayer(Context context) {
        this(context, null);
    }

    public KsgVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KsgVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
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

    private void init(Context context) {
        this.mContext = context;
        // 初始化播放器代理
        mKsgPlayer = createKsgPlayer();
        // 初始化容器
        mKsgContainer = onCreateKsgContainer(context);

        addView(mKsgContainer, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
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
     * 添加自定义事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    public void addEventProducer(BaseEventProducer eventProducer) {
        mKsgContainer.addEventProducer(eventProducer);
    }

    /**
     * 移除一个事件生产者
     *
     * @param eventProducer 自定义事件生产者
     */
    public void removeEventProducer(BaseEventProducer eventProducer) {
        mKsgContainer.removeEventProducer(eventProducer);
    }

    /**
     * 返回事件生产者集合 便于控制
     *
     * @return List
     */
    public List<BaseEventProducer> getEventProducers() {
        return mKsgContainer.getEventProducers();
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        mKsgPlayer.setDataSource(dataSource);
    }

    /**
     * 设置解码器
     *
     * @param decoderView decoderView
     */
    @Override
    public void setDecoderView(BaseInternalPlayer decoderView) {

        // 如果与已有的解码器一致则不需要切换
        if (mInternalPlayer != null && mInternalPlayer == decoderView) {
            return;
        }

        mInternalPlayer = decoderView;

        mKsgPlayer.setDecoderView(decoderView);

        mKsgContainer.setRenderView(decoderView.getPlayerView());
    }

    /**
     * 获取解码器
     *
     * @return BaseInternalPlayer
     */
    @Override
    public BaseInternalPlayer getDecoderView() {
        return mInternalPlayer;
    }

    /**
     * 自定义消息
     *
     * @param code   code
     * @param bundle bundle
     */
    public void option(int code, Bundle bundle) {
        mKsgPlayer.option(code, bundle);
    }

    /**
     * 自定义设置接收器组,自己处理事件接收
     *
     * @param receiverGroup receiverGroup
     */
    public void setReceiverGroup(IReceiverGroup receiverGroup) {
        mKsgContainer.setReceiverGroup(receiverGroup);
    }

    @Override
    public boolean isInPlaybackState() {
        int state = getState();
        return state != IKsgPlayer.STATE_END
                && state != IKsgPlayer.STATE_ERROR
                && state != IKsgPlayer.STATE_IDLE
                && state != IKsgPlayer.STATE_INITIALIZED
                && state != IKsgPlayer.STATE_STOPPED;
    }

    @Override
    public boolean isPlaying() {
        return mKsgPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        return mKsgPlayer.getProgress();
    }

    @Override
    public int getDuration() {
        return mKsgPlayer.getDuration();
    }

    /**
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

    public void rePlay(int msc) {
        mKsgPlayer.rePlay(msc);
    }

    @Override
    public void start() {
        mKsgPlayer.start();
    }

    @Override
    public void start(int msc) {
        mKsgPlayer.start(msc);
    }

    @Override
    public void pause() {
        mKsgPlayer.pause();
    }

    @Override
    public void resume() {
        mKsgPlayer.resume();
    }

    @Override
    public void seekTo(int msc) {
        mKsgPlayer.seekTo(msc);
    }

    @Override
    public void stop() {
        mKsgPlayer.stop();
    }

    @Override
    public void destroy() {
        mKsgPlayer.destroy();

        mKsgContainer.destroy();
    }

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
     * 播放器错误回调接口
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

    private StateGetter mInternalStateGetter = new StateGetter() {
        @Override
        public PlayerStateGetter getPlayerStateGetter() {
            return new PlayerStateGetter() {
                @Override
                public int getState() {
                    return mKsgPlayer.getState();
                }

                @Override
                public int getProgress() {
                    return mKsgPlayer.getProgress();
                }

                @Override
                public int getDuration() {
                    return mKsgPlayer.getDuration();
                }

                @Override
                public int getBufferPercentage() {
                    return mKsgPlayer.getBuffer();
                }

                @Override
                public boolean isBuffering() {
                    return mIsBuffering;
                }
            };
        }
    };

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

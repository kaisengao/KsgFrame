package com.ksg.ksgplayer;

import android.os.Bundle;
import android.view.View;

import com.ksg.ksgplayer.config.PlayerConfig;
import com.ksg.ksgplayer.entity.DataSource;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.IKsgPlayer;
import com.ksg.ksgplayer.player.IKsgPlayerProxy;
import com.ksg.ksgplayer.record.PlayValueGetter;
import com.ksg.ksgplayer.record.RecordProxyPlayer;

/**
 * @author kaisengao
 * @create: 2019/1/7 15:41
 * @describe: 播放器代理
 */
public final class KsgPlayerProxy implements IKsgPlayer {

    /**
     * 媒体播放器
     */
    private BaseInternalPlayer mInternalPlayer;

    /**
     * 缓存
     */
    private IKsgPlayerProxy mKsgPlayerRecord;

    /**
     * 你可以设置url、uri、标题等
     */
    private DataSource mDataSource;

    /**
     * 播放器状态事件
     */
    private OnPlayerEventListener mOnPlayerEventListener;

    /**
     * 播放器错误事件
     */
    private OnErrorEventListener mOnErrorEventListener;

    public KsgPlayerProxy() {
        // 初始化缓存代理
        handleRecordProxy();
    }

    /**
     * 缓存代理
     */
    private void handleRecordProxy() {
        if (PlayerConfig.getInstance().isPlayRecordState()) {
            mKsgPlayerRecord = new RecordProxyPlayer(new PlayValueGetter() {
                @Override
                public int getProgress() {
                    return KsgPlayerProxy.this.getProgress();
                }

                @Override
                public int getBuffer() {
                    return KsgPlayerProxy.this.getBuffer();
                }

                @Override
                public int getDuration() {
                    return KsgPlayerProxy.this.getDuration();
                }

                @Override
                public int getState() {
                    return KsgPlayerProxy.this.getState();
                }
            });
        }
    }

    /**
     * 设置解码器
     *
     * @param decoderView decoderView
     */

    public void setDecoderView(BaseInternalPlayer decoderView) {
        destroy();

        this.mInternalPlayer = decoderView;
    }

    @Override
    public View getPlayerView() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getPlayerView();
        }
        return null;
    }

    @Override
    public void option(int code, Bundle bundle) {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.option(code, bundle);
        }
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.mDataSource = dataSource;
        // 初始化数据源
        resetListener();
        // 当数据源更新时，设置回调。
        initListener();
        // 设置数据源
        interPlayerSetDataSource(dataSource);
    }

    private void interPlayerSetDataSource(DataSource dataSource) {
        if (isPlayRecordOpen()) {
            this.mKsgPlayerRecord.onDataSourceReady(dataSource);
        }
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setDataSource(dataSource);
        }
    }

    /**
     * 设置回调
     */
    private void initListener() {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setOnPlayerEventListener(mInternalPlayerEventListener);
            this.mInternalPlayer.setOnErrorEventListener(mInternalErrorEventListener);
        }
    }

    /**
     * 摧毁回调
     */
    private void resetListener() {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.setOnPlayerEventListener(null);
            this.mInternalPlayer.setOnErrorEventListener(null);
        }
    }

    /**
     * 播放器回调接口
     */
    private OnPlayerEventListener mInternalPlayerEventListener = new OnPlayerEventListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            if (isPlayRecordOpen()) {
                mKsgPlayerRecord.onPlayerEvent(eventCode, bundle);
            }
            callBackPlayEventListener(eventCode, bundle);
        }
    };

    /**
     * 播放器错误回调接口
     */
    private OnErrorEventListener mInternalErrorEventListener = new OnErrorEventListener() {
        @Override
        public void onErrorEvent(int eventCode, Bundle bundle) {
            callBackErrorEventListener(eventCode, bundle);
        }
    };

    /**
     * 必须持续回调事件监听器,因为包在回调后会被回收
     */
    private void callBackPlayEventListener(int eventCode, Bundle bundle) {
        if (mOnPlayerEventListener != null) {
            this.mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
        }
    }

    /**
     * 必须持续回调事件监听器,因为包在回调后会被回收
     */
    private void callBackErrorEventListener(int eventCode, Bundle bundle) {
        if (mOnErrorEventListener != null) {
            this.mOnErrorEventListener.onErrorEvent(eventCode, bundle);
        }
    }

    @Override
    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    @Override
    public void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        this.mOnErrorEventListener = onErrorEventListener;
    }

    @Override
    public boolean isPlaying() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getProgress() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getProgress();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getBuffer() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getBuffer();
        }
        return 0;
    }

    @Override
    public int getState() {
        if (isPlayerAvailable()) {
            return this.mInternalPlayer.getState();
        }
        return 0;
    }

    @Override
    public void start() {
        int record = getRecord(mDataSource);
        start(record);
    }

    /**
     * 获取播放记录缓存
     */
    private int getRecord(DataSource dataSource) {
        if (dataSource != null && isPlayRecordOpen()) {
            return mKsgPlayerRecord.getRecord(dataSource);
        }
        return mDataSource != null ? mDataSource.getStartPos() : 0;
    }

    @Override
    public void start(int msc) {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.start(msc);
        }
    }

    @Override
    public void pause() {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.pause();
        }
    }

    @Override
    public void resume() {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.resume();
        }
    }

    public void rePlay(int msc) {
        if (mDataSource != null && isPlayerAvailable()) {
            interPlayerSetDataSource(mDataSource);
            this.mInternalPlayer.start(msc);
        }
    }

    @Override
    public void seekTo(int msc) {
        if (isPlayerAvailable()) {
            this.mInternalPlayer.seekTo(msc);
        }
    }

    @Override
    public void stop() {
        if (isPlayRecordOpen()) {
            this.mKsgPlayerRecord.onIntentStop();
        }
        if (isPlayerAvailable()) {
            this.mInternalPlayer.stop();
        }
    }

    @Override
    public void reset() {
        if (isPlayRecordOpen()) {
            this.mKsgPlayerRecord.onIntentReset();
        }
        if (isPlayerAvailable()) {
            this.mInternalPlayer.reset();
        }
    }

    @Override
    public void destroy() {
        if (isPlayRecordOpen()) {
            this.mKsgPlayerRecord.onIntentDestroy();
        }
        if (isPlayerAvailable()) {
            this.mInternalPlayer.destroy();
        }
        resetListener();
    }

    private boolean isPlayRecordOpen() {
        return PlayerConfig.getInstance().isPlayRecordState() && this.mKsgPlayerRecord != null;
    }

    private boolean isPlayerAvailable() {
        return this.mInternalPlayer != null;
    }
}

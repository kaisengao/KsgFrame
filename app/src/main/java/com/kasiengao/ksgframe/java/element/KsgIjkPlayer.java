package com.kasiengao.ksgframe.java.element;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.kasiengao.base.configure.ThreadPool;
import com.ksg.ksgplayer.BuildConfig;
import com.ksg.ksgplayer.entity.DataSource;
import com.ksg.ksgplayer.event.BundlePool;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.BaseInternalPlayer;
import com.ksg.ksgplayer.player.IKsgPlayer;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @ClassName: KsgMediaPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/21 16:19
 * @Description: IjkMediaPlayer 播放器
 */
public class KsgIjkPlayer extends BaseInternalPlayer {

    private Context mContext;

    private int mStartSeekPos;

    private DataSource mDataSource;

    private IjkMediaPlayer mMediaPlayer;

    public KsgIjkPlayer(Context context) {
        this.mContext = context;
        // 初始化
        this.updateStatus(IKsgPlayer.STATE_INITIALIZED);
        // 初始化 Ijk
        this.initIjkMediaPlayer();
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        // 验证初始化
        if (mMediaPlayer == null) {
            this.initIjkMediaPlayer();
        } else {
            stop();
            reset();
            resetListener();
        }

        this.mDataSource = dataSource;

        // 发送数据源
        Bundle bundle = BundlePool.obtain();
        bundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET, bundle);
        // 播放准备
        updateStatus(IKsgPlayer.STATE_PREPARED);
    }

    /**
     * Init Ijk
     */
    private void initIjkMediaPlayer() {
        this.mMediaPlayer = new IjkMediaPlayer();
        // native日志
        IjkMediaPlayer.native_setLogLevel(BuildConfig.DEBUG ? IjkMediaPlayer.IJK_LOG_INFO : IjkMediaPlayer.IJK_LOG_SILENT);
        this.mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    /**
     * 验证
     */
    private boolean available() {
        return this.mMediaPlayer != null;
    }

    @Override
    public View getPlayerView() {
        return null;
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void start() {
        if (available()) {
            if (getState() == IKsgPlayer.STATE_PREPARED
                    || getState() == IKsgPlayer.STATE_PAUSED
                    || getState() == IKsgPlayer.STATE_PLAYBACK_COMPLETE
                    || getState() == IKsgPlayer.STATE_STOPPED) {

                try {
                    Uri uri = Uri.parse(mDataSource.getUrl());
                    if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(uri.getScheme())) {
                        RawDataSourceProvider rawDataSourceProvider = RawDataSourceProvider.create(mContext, uri);
                        mMediaPlayer.setDataSource(rawDataSourceProvider);
                    } else {
                        mMediaPlayer.setDataSource(mContext, uri);
                    }
                } catch (Exception e) {
//                    mPlayerEventListener.onError();
                }
//
//                mVodPlayer.setAutoPlay(true);
//                int result = mVodPlayer.startPlay(mDataSource.getUrl());
//                if (result == 0) {
//                    // 开始播放通知
//                    updateStatus(IKsgPlayer.STATE_STARTED);
//                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_START, null);
//                    mVodPlayer.resume();
//                } else {
//                    updateStatus(IKsgPlayer.STATE_ERROR);
//                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_UNKNOWN, "播放地址异常!");
//                    Toast.makeText(mContext, "播放地址异常!", Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }

    @Override
    public void start(int msc) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void seekTo(int msc) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void destroy() {

    }

    /**
     * 重置回调
     */
    private void resetListener() {
        mMediaPlayer.setOnErrorListener(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnInfoListener(null);
        mMediaPlayer.setOnBufferingUpdateListener(null);
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.setOnVideoSizeChangedListener(null);
        ThreadPool.DefaultThreadPool.getInstance().submit(() -> mMediaPlayer.release());
    }

}

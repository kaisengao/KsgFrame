package com.ksg.ksgplayer.handler;

import android.os.Bundle;
import android.util.Log;

import com.ksg.ksgplayer.KsgVideoPlayer;
import com.ksg.ksgplayer.cover.ICoverEvent;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.EventKey;

/**
 * @ClassName: CoverEventHandler
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/1 17:22
 * @Description: Cover事件处理程序实现类
 */
public class CoverEventHandler implements ICoverEventHandler {

    private final KsgVideoPlayer mPlayer;

    public CoverEventHandler(KsgVideoPlayer player) {
        this.mPlayer = player;
    }

    /**
     * 事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    @Override
    public void onHandle(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case ICoverEvent.CODE_REQUEST_OPTION:
                this.requestOption(bundle);
                break;
            case ICoverEvent.CODE_REQUEST_START:
                this.requestStart(bundle);
                break;
            case ICoverEvent.CODE_REQUEST_PAUSE:
                this.requestPause(bundle);
                break;
            case ICoverEvent.CODE_REQUEST_RESUME:
                this.requestResume(bundle);
                break;
            case ICoverEvent.CODE_REQUEST_SEEK:
                this.requestSeekTo(bundle);
                break;
            case ICoverEvent.CODE_REQUEST_STOP:
                this.requestStop(bundle);
                break;
            case ICoverEvent.CODE_REQUEST_RESET:
                this.requestReset(bundle);
                break;
            case ICoverEvent.CODE_REQUEST_REPLAY:
                this.requestReplay(bundle);
                break;
            case ICoverEvent.CODE_REQUEST_SPEED:
                this.requestSpeed(bundle);
                break;
            default:
                break;
        }
    }

    /**
     * 自定义 通知
     *
     * @param bundle bundle
     */
    @Override
    public void requestOption(Bundle bundle) {
        this.mPlayer.option(bundle.getInt(EventKey.INT_DATA), bundle);
    }

    /**
     * 播放
     *
     * @param bundle bundle
     */
    @Override
    public void requestStart(Bundle bundle) {
        this.mPlayer.start();
    }

    /**
     * 暂停
     *
     * @param bundle bundle
     */
    @Override
    public void requestPause(Bundle bundle) {
        if (mPlayer.isItPlaying()) {
            this.mPlayer.pause();
        } else {
            this.mPlayer.stop();
        }
    }

    /**
     * 继续播放
     *
     * @param bundle bundle
     */
    @Override
    public void requestResume(Bundle bundle) {
        if (mPlayer.isItPlaying()) {
            this.mPlayer.resume();
        } else {
            this.requestReplay(bundle);
        }
    }

    /**
     * 跳转进度
     *
     * @param bundle bundle
     */
    @Override
    public void requestSeekTo(Bundle bundle) {
        this.mPlayer.seekTo(bundle.getLong(EventKey.LONG_DATA));
    }

    /**
     * 停止播放
     *
     * @param bundle bundle
     */
    @Override
    public void requestStop(Bundle bundle) {
        this.mPlayer.stop();
    }

    /**
     * 重置播放器
     *
     * @param bundle bundle
     */
    @Override
    public void requestReset(Bundle bundle) {
        this.mPlayer.stop();
        this.mPlayer.reset();
    }

    /**
     * 重新播放
     *
     * @param bundle bundle
     */
    @Override
    public void requestReplay(Bundle bundle) {
        long msc = 0;
        if (bundle != null) {
            msc = bundle.getLong(EventKey.LONG_DATA);
        }
        this.mPlayer.replay(msc);
    }

    /**
     * 重新播放 新的数据源
     *
     * @param bundle bundle
     */
    @Override
    public void requestPlayDataSource(Bundle bundle) {
        if (bundle != null) {
            DataSource dataSource = (DataSource) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
            if (dataSource == null) {
                Log.e("OnVideoViewEventHandler", "requestPlayDataSource need legal data source");
                return;
            }
            this.mPlayer.setDataSource(dataSource);
            this.mPlayer.start();
        }
    }

    /**
     * 倍速
     *
     * @param bundle bundle
     */
    @Override
    public void requestSpeed(Bundle bundle) {
        this.mPlayer.setSpeed(bundle.getFloat(EventKey.FLOAT_DATA));
    }

}
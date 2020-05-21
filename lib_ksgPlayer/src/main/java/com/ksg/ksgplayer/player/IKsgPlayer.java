package com.ksg.ksgplayer.player;

import android.os.Bundle;
import android.view.View;

import com.ksg.ksgplayer.entity.DataSource;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;


/**
 * @author kaisengao
 * @create: 2019/1/4 14:13
 * @describe: 播放器一些基本状态
 */
public interface IKsgPlayer {

    /**
     * 播放 完成
     */
    int STATE_END = -2;
    /**
     * 播放 出错
     */
    int STATE_ERROR = -1;
    /**
     * 播放 默认 闲置
     */
    int STATE_IDLE = 0;
    /**
     * 播放 初始化
     */
    int STATE_INITIALIZED = 1;
    /**
     * 播放 准备
     */
    int STATE_PREPARED = 2;
    /**
     * 播放 开始
     */
    int STATE_STARTED = 3;
    /**
     * 播放 暂停
     */
    int STATE_PAUSED = 4;
    /**
     * 播放 停止
     */
    int STATE_STOPPED = 5;
    /**
     * 播放 完成
     */
    int STATE_PLAYBACK_COMPLETE = 6;

    /**
     * 返回渲染视图view
     *
     * @return view
     */
    View getPlayerView();

    /**
     * 使用此方法,您可以发送一些播放器init参数或开关设置。
     * 比如一些配置选项(使用媒体编解码器或超时或连接等等)解码器init。
     *
     * @param code   code
     * @param bundle bundle
     */
    void option(int code, Bundle bundle);

    /**
     * 设置视频基本信息
     *
     * @param dataSource 数据源
     */
    void setDataSource(DataSource dataSource);

    /**
     * 设置 播放器状态事件
     *
     * @param onPlayerEventListener onPlayerEventListener
     */
    void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener);

    /**
     * 设置 播放器错误事件
     *
     * @param onErrorEventListener onErrorEventListener
     */
    void setOnErrorEventListener(OnErrorEventListener onErrorEventListener);

    /**
     * 获取当前视频
     *
     * @return 缓冲进度
     */
    int getBuffer();

    /**
     * 获取当前视频
     *
     * @return 播放进度
     */
    int getProgress();

    /**
     * 获取当前视频
     *
     * @return 总时长
     */
    int getDuration();

    /**
     * 获取当前视频
     *
     * @return 播放状态 true 播放 反之
     */
    boolean isPlaying();

    /**
     * 获取当前state
     *
     * @return 返回状态
     */
    int getState();

    /**
     * start
     */
    void start();

    /**
     * start
     *
     * @param msc 在指定的位置开始播放
     */
    void start(int msc);

    /**
     * pause
     */
    void pause();

    /**
     * resume
     */
    void resume();

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    void seekTo(int msc);

    /**
     * stop
     */
    void stop();

    /**
     * reset
     */
    void reset();

    /**
     * destroy
     */
    void destroy();

}

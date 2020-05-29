package com.ksg.ksgplayer.player;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;

/**
 * @ClassName: IKsgPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:22
 * @Description: 播放器抽象接口
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
     * 获取当前state
     *
     * @return 返回状态
     */
    int getState();

    /**
     * 初始化 播放器
     */
    void initPlayer();

    /**
     * 自定义事件
     *
     * @param code   code
     * @param bundle bundle
     */
    void option(int code, Bundle bundle);

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    void setDataSource(String dataSource);

    /**
     * 设置渲染视频的View,主要用于TextureView
     *
     * @param surface surface
     */
    void setSurface(Surface surface);

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     *
     * @param holder surfaceHolder
     */
    void setDisplay(SurfaceHolder holder);

    /**
     * 设置音量
     */
    void setVolume(float left, float right);

    /**
     * 设置是否循环播放
     */
    void setLooping(boolean isLooping);

    /**
     * 设置播放速度
     */
    void setSpeed(float speed);

    /**
     * 获取播放速度
     */
    float getSpeed();

    /**
     * 获取当前缓冲的网速
     */
    long getTcpSpeed();

    /**
     * 获取缓冲进度百分比
     *
     * @return 缓冲进度
     */
    int getBufferPercentage();

    /**
     * 获取当前播放的位置
     *
     * @return 播放进度
     */
    long getCurrentPosition();

    /**
     * 获取视频总时长
     *
     * @return 总时长
     */
    long getDuration();

    /**
     * 获取播放状态
     *
     * @return 播放状态 true 播放 反之
     */
    boolean isPlaying();

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    void seekTo(long msc);

    /**
     * 准备开始播放（异步）
     */
    void prepareAsync();

    /**
     * 播放
     */
    void start();

    /**
     * 播放
     *
     * @param msc 在指定的位置开始播放
     */
    void start(long msc);

    /**
     * 暂停
     */
    void pause();

    /**
     * 继续播放
     */
    void resume();

    /**
     * 停止
     */
    void stop();

    /**
     * 重置播放器
     */
    void reset();

    /**
     * 释放播放器
     */
    void release();

    /**
     * 销毁资源
     */
    void destroy();

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
}

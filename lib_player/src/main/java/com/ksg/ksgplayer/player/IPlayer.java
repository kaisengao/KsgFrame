package com.ksg.ksgplayer.player;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.listener.OnErrorListener;
import com.ksg.ksgplayer.listener.OnPlayerListener;

/**
 * @ClassName: Player
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:22
 * @Description: 播放器抽象接口
 */
public interface IPlayer {

    /**
     * 播放 销毁
     */
    int STATE_DESTROY = -2;
    /**
     * 播放 出错
     */
    int STATE_ERROR = -1;
    /**
     * 播放 空闲
     */
    int STATE_IDLE = 0;
    /**
     * 播放 已初始化
     */
    int STATE_INIT = 1;
    /**
     * 播放 准备
     */
    int STATE_PREPARED = 2;
    /**
     * 播放 开始
     */
    int STATE_START = 3;
    /**
     * 播放 暂停
     */
    int STATE_PAUSE = 4;
    /**
     * 播放 停止
     */
    int STATE_STOP = 5;
    /**
     * 播放 完成
     */
    int STATE_COMPLETE = 6;

    /**
     * 是否处于播放
     *
     * @return boolean
     */
    boolean isItPlaying();

    /**
     * Init Player
     */
    void initPlayer();

    /**
     * 获取当前state
     *
     * @return 返回状态
     */
    int getState();

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
    void setDataSource(DataSource dataSource);

    /**
     * 设置渲染器
     *
     * @param surface surface
     */
    void setSurface(Surface surface);

    /**
     * 设置渲染器
     *
     * @param holder surfaceHolder
     */
    void setDisplay(SurfaceHolder holder);

    /**
     * 获取渲染器
     *
     * @return View
     */
    View getRenderer();

    /**
     * 设置音量
     */
    void setVolume(float left, float right);

    /**
     * 设置循环播放
     */
    void setLooping(boolean looping);

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
     */
    int getBufferPercentage();

    /**
     * 获取当前播放的位置
     */
    long getCurrentPosition();

    /**
     * 获取视频总时长
     */
    long getDuration();

    /**
     * 获取播放状态
     */
    boolean isPlaying();

    /**
     * 跳到指定位置
     */
    void seekTo(long msc);

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
     * 设置 播放事件
     *
     * @param playerListener playerListener
     */
    void setPlayerListener(OnPlayerListener playerListener);

    /**
     * 设置 错误事件
     *
     * @param errorListener errorListener
     */
    void setErrorListener(OnErrorListener errorListener);
}

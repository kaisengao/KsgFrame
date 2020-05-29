package com.ksg.ksgplayer.player;

import android.os.Bundle;
import android.view.ViewGroup;

import com.ksg.ksgplayer.render.IRender;

/**
 * @ClassName: IKagVideoPlayer
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:09
 * @Description: 播放器 基参
 */
public interface IKagVideoPlayer {

    /**
     * 容器
     *
     * @param userContainer ViewGroup
     */
    void attachContainer(ViewGroup userContainer);

    /**
     * 容器
     *
     * @param userContainer ViewGroup
     * @param updateRender  是否更新渲染view
     */
    void attachContainer(ViewGroup userContainer, boolean updateRender);

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
    void setDataSource(String dataSource);

    /**
     * 设置渲染视图类型
     *
     * @param renderType {@link IRender}
     */
    void setRenderType(int renderType);

    /**
     * 设置（播放器）解码器
     *
     * @param decoderView {@link BaseInternalPlayer}
     */
    void setDecoderView(BaseInternalPlayer decoderView);

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
    int getBufferedPercentage();

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
     * 播放
     */
    void start();

    /**
     * start
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
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    void rePlay(int msc);

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
}

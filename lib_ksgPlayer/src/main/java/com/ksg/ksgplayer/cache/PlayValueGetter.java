package com.ksg.ksgplayer.cache;

import com.ksg.ksgplayer.player.IKsgPlayer;

/**
 * @ClassName: PlayValueGetter
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 13:24
 * @Description: 播放器的一些状态信息
 */
public interface PlayValueGetter {

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
     * 当前播放状态 {@link IKsgPlayer}
     *
     * @return 播放状态
     */
    int getState();
}

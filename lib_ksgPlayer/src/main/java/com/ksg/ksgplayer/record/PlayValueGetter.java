package com.ksg.ksgplayer.record;

/**
 * @author kaisengao
 * @create: 2019/1/15 15:32
 * @describe: 播放状态
 */
public interface PlayValueGetter {

    /**
     * 当前播放进度
     *
     * @return 播放进度
     */
    int getProgress();

    /**
     * 当前缓冲进度
     *
     * @return 缓冲进度
     */
    int getBuffer();

    /**
     * 当前视频总时长
     *
     * @return 总时长
     */
    int getDuration();

    /**
     * 当前播放状态
     *
     * @return 播放状态
     */
    int getState();
}

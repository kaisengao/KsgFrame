package com.ksg.ksgplayer.listener;

/**
 * @author kaisengao
 * @create: 2019/1/18 16:14
 * @describe: 缓冲 更新回调
 */
public interface OnPlaybackProgressListener {

    /**
     * 播放器进度
     *
     * @param progress 当前播放位置
     * @param duration 视频总时长
     * @param buffer   缓冲位置
     */
    void onPlaybackProgress(int progress, int duration, int buffer);
}

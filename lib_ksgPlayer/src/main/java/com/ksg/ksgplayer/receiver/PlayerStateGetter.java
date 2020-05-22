package com.ksg.ksgplayer.receiver;

import com.ksg.ksgplayer.player.IKsgPlayer;

/**
 * @author kaisengao
 * @create: 2019/1/29 13:47
 * @describe: 获取播放状态
 */
public interface PlayerStateGetter {

    /**
     * 获取状态.
     * <p>
     * See also
     * {@link IKsgPlayer#STATE_END}
     * {@link IKsgPlayer#STATE_ERROR}
     * {@link IKsgPlayer#STATE_IDLE}
     * {@link IKsgPlayer#STATE_INITIALIZED}
     * {@link IKsgPlayer#STATE_PREPARED}
     * {@link IKsgPlayer#STATE_STARTED}
     * {@link IKsgPlayer#STATE_PAUSED}
     * {@link IKsgPlayer#STATE_STOPPED}
     * {@link IKsgPlayer#STATE_PLAYBACK_COMPLETE}
     *
     * @return state
     */
    int getState();

    /**
     * 获取播放进度.
     *
     * @return current
     */
    long getProgress();

    /**
     * 获取总时长
     *
     * @return duration
     */
    long getDuration();

    /**
     * 获取缓冲进度.
     *
     * @return bufferPercentage
     */
    int getBufferPercentage();

    /**
     * 是否正在缓冲
     *
     * @return true/false
     */
    boolean isBuffering();
}

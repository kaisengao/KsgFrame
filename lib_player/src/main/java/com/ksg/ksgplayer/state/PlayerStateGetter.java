package com.ksg.ksgplayer.state;

import com.ksg.ksgplayer.player.IPlayer;

/**
 * @author kaisengao
 * @create: 2019/1/29 13:47
 * @describe: 播放状态
 */
public interface PlayerStateGetter {

    /**
     * 获取状态.
     * <p>
     * See also
     * {@link IPlayer#STATE_DESTROY}
     * {@link IPlayer#STATE_ERROR}
     * {@link IPlayer#STATE_IDLE}
     * {@link IPlayer#STATE_INIT}
     * {@link IPlayer#STATE_PREPARED}
     * {@link IPlayer#STATE_START}
     * {@link IPlayer#STATE_PAUSE}
     * {@link IPlayer#STATE_STOP}
     * {@link IPlayer#STATE_COMPLETE}
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

    /**
     * 获取速度
     * @return speed
     */
    float getSpeed();
}

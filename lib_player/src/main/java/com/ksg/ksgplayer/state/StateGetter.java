package com.ksg.ksgplayer.state;

/**
 * @author kaisengao
 * @create: 2019/1/29 13:46
 * @describe: 一些状态获取器
 */
public interface StateGetter {

    /**
     * 播放状态获取
     *
     * @return 播放状态
     */
    PlayerStateGetter getPlayerStateGetter();
}

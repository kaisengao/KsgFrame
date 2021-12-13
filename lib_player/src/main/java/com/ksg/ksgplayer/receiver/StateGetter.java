package com.ksg.ksgplayer.receiver;

/**
 * @author kaisengao
 * @create: 2019/1/29 13:46
 * @describe: 播放器的状态获取, 也许你需要获得一些附加状态
 */
public interface StateGetter {

    /**
     * 播放状态获取器
     *
     * @return 播放状态
     */
    PlayerStateGetter getPlayerStateGetter();
}

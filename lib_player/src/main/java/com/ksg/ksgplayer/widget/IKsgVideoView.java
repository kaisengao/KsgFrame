package com.ksg.ksgplayer.widget;

import com.ksg.ksgplayer.IKsgVideoPlayer;

/**
 * @ClassName: IKsgVideoView
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/26 10:29
 * @Description: 播放器View
 */
public interface IKsgVideoView extends IKsgVideoPlayer {

    /**
     * 返回 播放器对象
     *
     * @return {@link IKsgVideoPlayer}
     */
    IKsgVideoPlayer getPlayer();
}

package com.ksg.ksgplayer.widget;

import com.ksg.ksgplayer.entity.DataSource;
import com.ksg.ksgplayer.player.BaseInternalPlayer;

/**
 * @author kaisengao
 * @create: 2019/1/11 10:13
 * @describe:
 */
public interface IKagVideoPlayer {

    void setDataSource(DataSource dataSource);

    void setDecoderView(BaseInternalPlayer decoderView);

    BaseInternalPlayer getDecoderView();

    boolean isInPlaybackState();

    boolean isPlaying();

    int getProgress();

    int getDuration();

    int getState();

    void start();

    void start(int msc);

    void pause();

    void resume();

    void seekTo(int msc);

    void stop();

    void destroy();
}

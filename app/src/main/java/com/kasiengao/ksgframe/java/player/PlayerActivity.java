package com.kasiengao.ksgframe.java.player;

import android.os.Bundle;
import android.view.View;

import com.kasiengao.base.util.KLog;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.player.cover.ControllerCover;
import com.kasiengao.ksgframe.java.player.cover.LoadingCover;
import com.kasiengao.mvp.java.BaseToolbarActivity;
import com.ksg.ksgplayer.assist.DataInter;
import com.ksg.ksgplayer.assist.OnVideoViewEventHandler;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.KsgVideoPlayer;
import com.ksg.ksgplayer.receiver.ReceiverGroup;
import com.ksg.ksgplayer.widget.KsgVideoView;

/**
 * @ClassName: PlayerVideo
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:33
 * @Description: 视频播放器
 */
public class PlayerActivity extends BaseToolbarActivity implements View.OnClickListener {

    private KsgVideoView mVideoView;

    private KsgVideoPlayer mVideoPlayer;

    private ReceiverGroup mReceiverGroup;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        // Toolbar Title
        this.setTitle(R.string.player_title);
        // KsgVideoPlayer
        this.mVideoView = findViewById(R.id.player);
        this.mVideoView.setDecoderView(new KsgIjkPlayer(this));

        this.mVideoPlayer = mVideoView.getVideoPlayer();

        this.mReceiverGroup = new ReceiverGroup();
        this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_LOADING_COVER, new LoadingCover(this));
        this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER, new ControllerCover(this));

        this.mVideoPlayer.setReceiverGroup(mReceiverGroup);
        this.mVideoPlayer.setOnVideoViewEventHandler(new OnVideoViewEventHandler());

        this.mVideoPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onPlayerEvent(int eventCode, Bundle bundle) {
                KLog.d("zzz", "onPlayerEvent eventCode = " + eventCode);
            }
        });
        this.mVideoPlayer.setOnErrorEventListener(new OnErrorEventListener() {
            @Override
            public void onErrorEvent(int eventCode, Bundle bundle) {
                KLog.d("zzz", "onErrorEvent eventCode = " + eventCode);
            }
        });
        // onClick
        findViewById(R.id.player_start).setOnClickListener(this);
        findViewById(R.id.player_resume).setOnClickListener(this);
        findViewById(R.id.player_pause).setOnClickListener(this);
        findViewById(R.id.player_stop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_start:
                // 播放
                this.mVideoView.setDataSource("http://vfx.mtime.cn/Video/2019/05/24/mp4/190524093650003718.mp4");
                this.mVideoPlayer.setLooping(true);
                this.mVideoPlayer.start();
                break;
            case R.id.player_resume:
                // 继续
                this.mVideoPlayer.resume();
                break;
            case R.id.player_pause:
                // 暂停
                this.mVideoPlayer.pause();
                break;
            case R.id.player_stop:
                // 停止
                this.mVideoPlayer.stop();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mVideoPlayer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mVideoPlayer.pause();
    }
}

package com.kasiengao.ksgframe.java.player;

import android.os.Bundle;
import android.view.View;

import com.kasiengao.base.util.KLog;
import com.kasiengao.ksgframe.R;
import com.kasiengao.mvp.java.BaseToolbarActivity;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.widget.KsgVideoPlayer;

/**
 * @ClassName: PlayerVideo
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:33
 * @Description: 视频播放器
 */
public class PlayerActivity extends BaseToolbarActivity implements View.OnClickListener {

    private KsgVideoPlayer mVideoPlayer;

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
        this.mVideoPlayer = findViewById(R.id.player);
        this.mVideoPlayer.setDecoderView(new KsgIjkPlayer(this));
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
                this.mVideoPlayer.setDataSource("http://vfx.mtime.cn/Video/2019/05/24/mp4/190524093650003718.mp4");
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
    protected void onDestroy() {
        super.onDestroy();
        // 销毁
        this.mVideoPlayer.destroy();
    }
}

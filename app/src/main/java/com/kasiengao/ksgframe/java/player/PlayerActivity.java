package com.kasiengao.ksgframe.java.player;

import android.os.Bundle;
import android.view.View;

import com.kasiengao.base.util.KLog;
import com.kasiengao.ksgframe.R;
import com.kasiengao.mvp.java.BaseToolbarActivity;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.receiver.ReceiverGroup;
import com.ksg.ksgplayer.widget.KsgVideoView;

/**
 * @ClassName: PlayerVideo
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:33
 * @Description: 视频播放器
 */
public class PlayerActivity extends BaseToolbarActivity implements View.OnClickListener {

    private KsgVideoView mKsgVideoView;

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
        this.mKsgVideoView = findViewById(R.id.player);
        this.mKsgVideoView.setDecoderView(new KsgIjkPlayer(this));
        this.mKsgVideoView.getVideoPlayer().setReceiverGroup(new ReceiverGroup());
        this.mKsgVideoView.getVideoPlayer().setOnPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onPlayerEvent(int eventCode, Bundle bundle) {
                KLog.d("zzz", "onPlayerEvent eventCode = " + eventCode);
            }
        });
        this.mKsgVideoView.getVideoPlayer().setOnErrorEventListener(new OnErrorEventListener() {
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
                this.mKsgVideoView.setDataSource("http://vfx.mtime.cn/Video/2019/05/24/mp4/190524093650003718.mp4");
                this.mKsgVideoView.setLooping(true);
                this.mKsgVideoView.start();
                break;
            case R.id.player_resume:
                // 继续
                this.mKsgVideoView.resume();
                break;
            case R.id.player_pause:
                // 暂停
                this.mKsgVideoView.pause();
                break;
            case R.id.player_stop:
                // 停止
                this.mKsgVideoView.stop();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁
        this.mKsgVideoView.destroy();
    }
}

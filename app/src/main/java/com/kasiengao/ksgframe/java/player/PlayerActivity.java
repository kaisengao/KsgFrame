package com.kasiengao.ksgframe.java.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kasiengao.base.util.DensityUtil;
import com.kasiengao.base.util.KLog;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.player.cover.ControllerCover;
import com.kasiengao.ksgframe.java.player.cover.GestureCover;
import com.kasiengao.ksgframe.java.player.cover.LoadingCover;
import com.kasiengao.ksgframe.java.player.cover.ScreenState;
import com.kasiengao.mvp.java.BaseToolbarActivity;
import com.ksg.ksgplayer.assist.DataInter;
import com.ksg.ksgplayer.assist.OnVideoViewEventHandler;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnErrorEventListener;
import com.ksg.ksgplayer.listener.OnPlayerEventListener;
import com.ksg.ksgplayer.player.KsgVideoPlayer;
import com.ksg.ksgplayer.receiver.ReceiverGroup;
import com.ksg.ksgplayer.widget.KsgVideoView;

import org.jetbrains.annotations.NotNull;

/**
 * @ClassName: PlayerVideo
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:33
 * @Description: 视频播放器
 */
public class PlayerActivity extends BaseToolbarActivity implements View.OnClickListener {

    private ScreenState mScreenState;

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
        this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_GESTURE_COVER, new GestureCover(this));
        this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_LOADING_COVER, new LoadingCover(this));
        this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER, new ControllerCover(this));

        this.mVideoPlayer.setReceiverGroup(mReceiverGroup);
        this.mVideoPlayer.setOnVideoViewEventHandler(new OnVideoViewEventHandler() {
            @Override
            public void onAssistHandle(KsgVideoPlayer assist, int eventCode, Bundle bundle) {
                super.onAssistHandle(assist, eventCode, bundle);
                switch (eventCode) {
                    case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                        // 回退
                        onBackPressed();
                        break;
                    case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                        // 全屏切换事件
                        setRequestedOrientation(mScreenState==ScreenState.LandscapeFullScreen ?
                                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
                                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        break;
                    case DataInter.Event.EVENT_CODE_REQUEST_VOLUME_ALTER:
                        // 声音开关事件
                        boolean volumeStatus = bundle.getBoolean(EventKey.BOOL_DATA, false);
                        if (volumeStatus) {
                            // 开放
                            assist.setVolume(1f, 1f);
                        } else {
                            // 静音
                            assist.setVolume(0f, 0f);
                        }
                        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_VOLUME_ALTER, !volumeStatus);
                        break;
                    default:
                        break;
                }
            }
        });

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
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.setVideoHeight(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
        // ActionBar
        this.actionBarStatus();
    }

    /**
     * 修改播放器的宽高比
     */
    private void setVideoHeight(boolean landscape) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mVideoView.getLayoutParams();
        if (landscape) {
            mScreenState = ScreenState.LandscapeFullScreen;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            mScreenState = ScreenState.normal;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = (int) getScreenSize(this);
        }
        this.mVideoView.setLayoutParams(layoutParams);
        this.mReceiverGroup.getGroupValue().putObject(DataInter.Key.KEY_IS_LANDSCAPE, mScreenState);
    }

    /**
     * @return 16比9的高度
     */
    public static float getScreenSize(Context context) {
        float width = DensityUtil.getWidthInPx(context);
        return width * 9 / 16;
    }

    /**
     * ActionBar
     */
    private void actionBarStatus() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (mScreenState == ScreenState.LandscapeFullScreen) {
                supportActionBar.hide();
            } else {
                supportActionBar.show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ActionBar
        this.actionBarStatus();
        this.mVideoPlayer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mVideoPlayer.pause();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onBackPressed() {
        if (mScreenState == ScreenState.LandscapeFullScreen) {
            super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.onBackPressed();
    }
}

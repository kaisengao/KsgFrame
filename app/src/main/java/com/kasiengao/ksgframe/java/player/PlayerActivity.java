package com.kasiengao.ksgframe.java.player;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.kasiengao.base.util.DensityUtil;
import com.kasiengao.base.util.StatusBarUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.java.player.cover.ControllerCover;
import com.kasiengao.ksgframe.java.player.cover.GestureCover;
import com.kasiengao.ksgframe.java.player.cover.LoadingCover;
import com.kasiengao.ksgframe.java.util.AnimUtil;
import com.kasiengao.mvp.java.BaseToolbarActivity;
import com.ksg.ksgplayer.assist.DataInter;
import com.ksg.ksgplayer.assist.OnVideoViewEventHandler;
import com.ksg.ksgplayer.event.EventKey;
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

    private boolean mFullscreen;

    private KsgVideoView mVideoView;

    private KsgVideoPlayer mVideoPlayer;

    private ReceiverGroup mReceiverGroup;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    protected boolean isDisplayToolbar() {
        return false;
    }

    @Override
    protected void initBefore() {
        StatusBarUtil.StatusBarLightMode(this);
        StatusBarUtil.transparencyBar(this);
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
        this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER, new ControllerCover(this));
        this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_LOADING_COVER, new LoadingCover(this));
        this.mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_GESTURE_COVER, new GestureCover(this));

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
                    case DataInter.Event.EVENT_CODE_REQUEST_SCREEN_ORIENTATION:
                        // 横竖屏切换
                        boolean screenOrientation = bundle.getBoolean(EventKey.BOOL_DATA, false);
                        // 改变横竖屏
                        setRequestedOrientation(screenOrientation
                                ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                // 横屏自动旋转 180°
                                : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        break;
                    case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                        // 全屏切换事件
                        boolean fullscreen = bundle.getBoolean(EventKey.BOOL_DATA, false);
                        // 屏幕改变
                        onFullscreen(fullscreen);
                        break;
                    default:
                        break;
                }
            }
        });
        // onClick
        AppCompatButton start = findViewById(R.id.player_start);
        start.setOnClickListener(this);
        findViewById(R.id.player_resume).setOnClickListener(this);
        findViewById(R.id.player_pause).setOnClickListener(this);
        findViewById(R.id.player_stop).setOnClickListener(this);

        start.performClick();
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

    /**
     * 屏幕改变
     *
     * @param fullscreen 屏幕状态
     */
    public void onFullscreen(boolean fullscreen) {
        this.mFullscreen = fullscreen;
        // 获取View在屏幕上的高度位置
        int viewHeight = (int) (DensityUtil.getWidthInPx(this) * 9 / 16);
        int screenHeight = (int) DensityUtil.getHeightInPx(this);
        // 全屏动画
        AnimUtil.fullScreenAnim(mVideoView, fullscreen, viewHeight, screenHeight, animation -> {
            // 更新高度
            this.mVideoView.getLayoutParams().height = (int) animation.getAnimatedValue();
            this.mVideoView.requestLayout();
        }, null);
        // 通知组件横屏幕改变
        this.mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_FULLSCREEN, fullscreen);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 验证是否横屏状态
        boolean isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        // 通知组件横竖屏切换
        this.mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_SCREEN_ORIENTATION, isLandscape);
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

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onBackPressed() {
//        if (mScreenState == ScreenState.LandscapeFullScreen) {
//            super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            return;
//        }
        super.onBackPressed();
    }
}

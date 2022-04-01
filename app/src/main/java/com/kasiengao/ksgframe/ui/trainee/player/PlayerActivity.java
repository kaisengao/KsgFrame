package com.kasiengao.ksgframe.ui.trainee.player;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.kaisengao.base.util.DensityUtil;
import com.kaisengao.base.util.StatusBarUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.util.AnimUtil;
import com.kasiengao.ksgframe.common.util.SystemUiUtil;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.kasiengao.ksgframe.player.cover.ControllerCover;
import com.kasiengao.ksgframe.player.cover.GestureCover;
import com.kasiengao.ksgframe.player.cover.LoadingCover;
import com.kasiengao.ksgframe.player.KsgExoPlayer;
import com.kasiengao.mvp.java.BaseToolbarActivity;
import com.ksg.ksgplayer.cover.CoverManager;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.widget.KsgVideoView;

/**
 * @ClassName: PlayerVideo
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:33
 * @Description: 视频播放器
 */
public class PlayerActivity extends BaseToolbarActivity {

    private KsgVideoView mPlayer;

    private AppCompatTextView mPlayerDecoder;

    private boolean mUserPause;

    private boolean mFullscreen;

    private boolean mIsLandscape;

    private CoverManager mCoverManager;

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
        // FindView
        this.mPlayer = findViewById(R.id.player);
        this.mPlayerDecoder = findViewById(R.id.player_decoder);
        // InitPlayer
        this.initPlayer();
    }

    /**
     * InitPlayer
     */
    private void initPlayer() {
        this.mPlayer.setDecoderView(new KsgExoPlayer(this));
        // 创建 Cover管理器
        this.mCoverManager = new CoverManager();
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_LOADING, new LoadingCover(this));
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_GESTURE, new GestureCover(this));
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_CONTROLLER, new ControllerCover(this));
        // 设置 覆盖组件管理器
        this.mPlayer.getPlayer().setCoverManager(mCoverManager);
        // Cover组件回调事件
        this.mPlayer.getPlayer().setCoverEventListener((eventCode, bundle) -> {
            switch (eventCode) {
                case CoverConstant.CoverEvent.CODE_REQUEST_PAUSE:
                    // 标记是手动暂停
                    this.mUserPause = true;
                    break;
                case CoverConstant.CoverEvent.CODE_REQUEST_BACK:
                    // 回退
                    this.onBackPressed();
                    break;
                case CoverConstant.CoverEvent.CODE_REQUEST_HL_SCREEN_TOGGLE:
                    // 横竖屏切换
                    boolean screenOrientation = bundle.getBoolean(EventKey.BOOL_DATA, false);
                    // 改变横竖屏
                    this.setRequestedOrientation(screenOrientation
                            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            // 横屏自动旋转 180°
                            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    break;
//                case CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_TOGGLE:
//                    // 全屏切换事件
//                    boolean fullscreen = bundle.getBoolean(EventKey.BOOL_DATA, false);
//                    // 如果在横屏状态下退出了全屏模式需要设置回竖屏
//                    if (mIsLandscape && !fullscreen) {
//                        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//                    }
//                    // 屏幕改变
//                    this.onFullscreen(fullscreen);
//                    break;
                default:
                    break;
            }
        });
        // 播放
        this.onPlay();
    }

    /**
     * 播放
     */
    private void onPlay() {
        this.onPlay("http://vfx.mtime.cn/Video/2019/05/24/mp4/190524093650003718.mp4");
    }

    /**
     * 播放
     */
    private void onPlay(String url) {
        DataSource dataSource = new DataSource(url);
        // 添加容器 播放
        this.mPlayer.setDataSource(dataSource);
        this.mPlayer.start();
        this.mPlayer.setLooping(true);
        // 当前解码器
        this.mPlayerDecoder.setText(mPlayer.getDecoderView().getClass().getSimpleName());
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
        AnimUtil.fullScreenAnim(mPlayer, fullscreen, viewHeight, screenHeight, animation -> {
            // 更新高度
            this.mPlayer.getLayoutParams().height = (int) animation.getAnimatedValue();
            this.mPlayer.requestLayout();
        }, null);
        // 通知组件横屏幕改变
        this.mCoverManager.getValuePool().putObject(CoverConstant.ValueKey.KEY_FULLSCREEN_TOGGLE, fullscreen);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 验证是否横屏状态
        this.mIsLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        // 横屏
        if (mIsLandscape) {
            // 如果直接选择的横屏且屏幕不在全屏状态下 默认设置横屏且全屏
            if (!mFullscreen) {
                // 改为全屏布局
                this.onFullscreen(true);
            }
            // 隐藏系统Ui
            SystemUiUtil.hideVideoSystemUI(this);
        } else {
            // 竖屏
            this.onFullscreen(mFullscreen);
            // 恢复系统Ui
            SystemUiUtil.recoverySystemUI(this);
        }
        // 通知组件横竖屏切换
        this.mCoverManager.getValuePool().putObject(CoverConstant.ValueKey.KEY_HL_SCREEN_TOGGLE, mIsLandscape);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayer.isItPlaying()) {
            if (!mUserPause) {
                this.mPlayer.resume();
            }
        }
        if (mIsLandscape) {
            // 隐藏系统Ui
            SystemUiUtil.hideVideoSystemUI(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer.isItPlaying()) {
            this.mPlayer.pause();
        } else {
            this.mPlayer.stop();
        }
    }

    @Override
    public void onBackPressed() {
        // 1、验证横竖屏
        if (mIsLandscape) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        // 2、验证全屏
        if (mFullscreen) {
            this.onFullscreen(false);
            return;
        }
        // 剩下的基操给系统
        super.onBackPressed();
    }
//
//    public void onItemSelected(int position) {
//        if (mInitSpinner) {
//            this.mInitSpinner = false;
//            return;
//        }
//        switch (position) {
//            case 0:
//                if (mKsgAssistView.setDecoderView(createExo())) {
//                    this.onPlay();
//                }
//                break;
//            case 1:
//                if (mKsgAssistView.setDecoderView(createTxVod())) {
//                    this.onPlay();
//                }
//                break;
//            case 2:
//                if (mKsgAssistView.setDecoderView(createTxLive())) {
////                    this.onPlay("rtmp://play.lifecrystal.cn/live/shbxyshh01", true);
////                    this.onPlay("rtmp://play.lifecrystal.cn/live/1400238383_IM8615612341234", true);
//                }
//                break;
//            default:
//                break;
//        }
//    }
}

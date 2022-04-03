package com.kasiengao.ksgframe.ui.trainee.player;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.kaisengao.base.util.DensityUtil;
import com.kaisengao.base.util.StatusBarUtil;
import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kasiengao.ksgframe.BR;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.util.AnimUtil;
import com.kasiengao.ksgframe.common.util.SystemUiUtil;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.kasiengao.ksgframe.databinding.ActivityPlayerBinding;
import com.kasiengao.ksgframe.player.KsgExoPlayer;
import com.kasiengao.ksgframe.player.cover.ControllerCover;
import com.kasiengao.ksgframe.player.cover.GestureCover;
import com.kasiengao.ksgframe.player.cover.LoadingCover;
import com.ksg.ksgplayer.cover.CoverManager;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.widget.KsgVideoView;

/**
 * @ClassName: PlayerVideo
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:33
 * @Description: Player
 */
public class PlayerActivity extends BaseVmActivity<ActivityPlayerBinding, PlayerViewModel> {

    private int[] mScreenSize;

    private boolean mUserPause;

    private boolean mFullscreen;

    private boolean isLandscape;

    private CoverManager mCoverManager;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        this.mScreenSize = DensityUtil.getScreenSize(this);
        // Init Player
        this.initPlayer();
    }

    /**
     * Init Player
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private void initPlayer() {
        this.mBinding.player.setDecoderView(new KsgExoPlayer(this));
        // 创建 Cover管理器
        this.mCoverManager = new CoverManager();
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_LOADING, new LoadingCover(this));
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_GESTURE, new GestureCover(this));
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_CONTROLLER, new ControllerCover(this));
        // 设置 覆盖组件管理器
        this.mBinding.player.setCoverManager(mCoverManager);
        // Cover组件回调事件
        this.mBinding.player.getPlayer().setCoverEventListener((eventCode, bundle) -> {
            switch (eventCode) {
                case CoverConstant.CoverEvent.CODE_REQUEST_PAUSE:
                    // 标记是手动暂停
                    this.mUserPause = true;
                    break;
                case CoverConstant.CoverEvent.CODE_REQUEST_BACK:
                    // 回退
                    this.onBackPressed();
                    break;
                case CoverConstant.CoverEvent.CODE_REQUEST_LP_SCREEN_TOGGLE:
                    // 横竖屏切换
                    boolean lpScreen = bundle.getBoolean(EventKey.BOOL_DATA, false);
                    // 改变横竖屏
                    this.setRequestedOrientation(lpScreen
                            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            // 横屏自动旋转 180°
                            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    break;
                case CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_TOGGLE:
                    // 全屏切换
                    boolean fullscreen = bundle.getBoolean(EventKey.BOOL_DATA, false);
                    // 如果在横屏状态下退出了全屏模式恢复为竖屏
                    if (isLandscape && !fullscreen) {
                        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                    // 全屏切换
                    this.onFullscreen(fullscreen);
                    break;
                default:
                    break;
            }
        });
        // 播放
        this.onPlay("http://vfx.mtime.cn/Video/2019/05/24/mp4/190524093650003718.mp4");
    }

    /**
     * 播放
     */
    private void onPlay(String url) {
        KsgVideoView player = mBinding.player;
        player.setDataSource(new DataSource(url));
        player.start();
        player.setLooping(true);
        // 当前解码器
        this.getCurrentDecoder();
    }

    /**
     * 获取当前解码器
     */
    private void getCurrentDecoder() {
        this.mBinding.playerDecoder.setText(mBinding.player.getDecoderView().getClass().getSimpleName());
    }

    /**
     * 屏幕改变
     *
     * @param fullscreen 屏幕状态
     */
    public void onFullscreen(boolean fullscreen) {
        this.mFullscreen = fullscreen;
        // 获取View在屏幕上的高度位置
        int viewHeight = (int) (mScreenSize[0] * 9 / 16) + StatusBarUtil.getStatusBarHeight(this);
        // 全屏动画
        AnimUtil.fullScreenAnim(mBinding.player, fullscreen, viewHeight, mScreenSize[1], animation -> {
            // 更新高度
            this.mBinding.player.getLayoutParams().height = (int) animation.getAnimatedValue();
            this.mBinding.player.requestLayout();
        }, null);
        // 通知组件横屏幕改变
        this.mCoverManager.getValuePool().putObject(CoverConstant.ValueKey.KEY_FULLSCREEN_TOGGLE, fullscreen);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 验证是否横屏状态
        this.isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        // 横屏
        if (isLandscape) {
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
        this.mCoverManager.getValuePool().putObject(CoverConstant.ValueKey.KEY_LP_SCREEN_TOGGLE, isLandscape);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBinding.player.isItPlaying()) {
            if (!mUserPause) {
                this.mBinding.player.resume();
            }
        }
        if (isLandscape) {
            // 隐藏系统Ui
            SystemUiUtil.hideVideoSystemUI(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBinding.player.isItPlaying()) {
            this.mBinding.player.pause();
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onBackPressed() {
        // 1、验证横竖屏
        if (isLandscape) {
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
}

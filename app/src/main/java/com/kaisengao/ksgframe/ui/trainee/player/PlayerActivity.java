package com.kaisengao.ksgframe.ui.trainee.player;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;

import com.kaisengao.base.util.DensityUtil;
import com.kaisengao.base.util.StatusBarUtil;
import com.kaisengao.ksgframe.common.util.AnimUtil;
import com.kaisengao.ksgframe.common.util.SystemUiUtil;
import com.kaisengao.ksgframe.constant.CoverConstant;
import com.kaisengao.ksgframe.player.KsgExoPlayer;
import com.kaisengao.ksgframe.player.cover.ControllerCover;
import com.kaisengao.ksgframe.player.cover.GestureCover;
import com.kaisengao.ksgframe.player.cover.LoadingCover;
import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kaisengao.ksgframe.BR;
import com.kaisengao.ksgframe.R;
import com.kaisengao.ksgframe.databinding.ActivityPlayerBinding;
import com.ksg.ksgplayer.config.AspectRatio;
import com.ksg.ksgplayer.cover.CoverManager;
import com.ksg.ksgplayer.data.DataSource;
import com.ksg.ksgplayer.event.EventKey;
import com.ksg.ksgplayer.listener.OnRendererListener;
import com.ksg.ksgplayer.renderer.RendererType;
import com.ksg.ksgplayer.renderer.glrender.PIPGLViewRender;
import com.ksg.ksgplayer.renderer.view.KsgGLSurfaceView;
import com.ksg.ksgplayer.widget.KsgVideoView;

/**
 * @ClassName: PlayerVideo
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:33
 * @Description: Player
 */
public class PlayerActivity extends BaseVmActivity<ActivityPlayerBinding, PlayerViewModel> {

    private int[] mScreenSize;

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
    //记住切换数据源类型
    private int mType = 0;
    protected int mRotate;

    @Override
    protected void initWidget() {
        super.initWidget();
        this.mScreenSize = DensityUtil.getScreenSize(this);
        // Init Player
        this.initPlayer();

        this.mBinding.playerShotPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.player.getRenderer().onShotPic();
            }
        });

        this.mBinding.playerAspectRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType == 0) {
                    mType = 1;
                } else if (mType == 1) {
                    mType = 2;
                } else if (mType == 2) {
                    mType = 3;
                } else if (mType == 3) {
                    mType = 4;
                } else if (mType == 4) {
                    mType = 0;
                }
                resolveTypeUI();
            }
        });

        this.mBinding.playerDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mBinding.player.getRenderer().getRotationDegrees() - mRotate) == 270) {
                    mBinding.player.getRenderer().setRotationDegrees(mRotate);
                } else {
                    mBinding.player.getRenderer().setRotationDegrees( mBinding.player.getRenderer().getRotationDegrees() + 90);
                }
            }
        });

        this.mBinding.playerFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!v.isSelected()) {
//                    mBinding.player.getRenderer().setGLFilter(new GaussianBlurFilter());
//                }else {
//                    mBinding.player.getRenderer().setGLFilter(new NoFilter());
//                }
                v.setSelected(!v.isSelected());
            }
        });
    }
    private void resolveTypeUI() {
        if (mType == 1) {
//            mMoreScale.setText("16:9");
            mBinding.player.getRenderer().setAspectRatio(AspectRatio.RATIO_16_9);
        } else if (mType == 2) {
//            mMoreScale.setText("4:3");
            mBinding.player.getRenderer().setAspectRatio(AspectRatio.RATIO_4_3);
        } else if (mType == 3) {
//            mMoreScale.setText("全屏");
            mBinding.player.getRenderer().setAspectRatio(AspectRatio.RATIO_FULL);
        } else if (mType == 4) {
//            mMoreScale.setText("拉伸全屏");
            mBinding.player.getRenderer().setAspectRatio(AspectRatio.RATIO_MATCH_FULL);
        } else if (mType == 0) {
//            mMoreScale.setText("默认比例");
            mBinding.player.getRenderer().setAspectRatio(AspectRatio.RATIO_DEFAULT);
        }
    }
    /**
     * Init Player
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private void initPlayer() {
        KsgVideoView player = mBinding.player;
        // 1 (注意调用顺序，否则不生效)
        player.setGLViewRender(new PIPGLViewRender(), KsgGLSurfaceView.MODE_RENDER_SIZE);
        // 2、3
        player.setDecoderView(new KsgExoPlayer(this));
        player.setRenderer(RendererType.GL_SURFACE);

        // 创建 Cover管理器
        this.mCoverManager = new CoverManager();
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_LOADING, new LoadingCover(this));
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_GESTURE, new GestureCover(this));
        this.mCoverManager.addCover(CoverConstant.CoverKey.KEY_CONTROLLER, new ControllerCover(this));
        // 设置 覆盖组件管理器
        player.setCoverManager(mCoverManager);
        // Cover组件事件
        player.setCoverEventListener((eventCode, bundle) -> {
            switch (eventCode) {
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
        // 渲染器事件
        player.setRendererListener(new OnRendererListener() {

            @Override
            public void onShotPic(Bitmap bitmap) {
                mBinding.playerShotPicInfo.setImageBitmap(bitmap);
                mBinding.playerShotPicInfo.setRotation(player.getRenderer().getRotationDegrees());
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
        this.mBinding.playerCurrDecoder.setText(mBinding.player.getDecoderView().getClass().getSimpleName());
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
        this.mBinding.player.resume();
        if (isLandscape) {
            // 隐藏系统Ui
            SystemUiUtil.hideVideoSystemUI(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mBinding.player.pause();
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

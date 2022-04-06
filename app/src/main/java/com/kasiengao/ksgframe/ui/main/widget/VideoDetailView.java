package com.kasiengao.ksgframe.ui.main.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kaisengao.base.util.BlurUtil;
import com.kaisengao.base.util.CommonUtil;
import com.kaisengao.base.util.StatusBarUtil;
import com.kasiengao.ksgframe.BR;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.util.SystemUiUtil;
import com.kasiengao.ksgframe.common.widget.PlayerContainerView;
import com.kasiengao.ksgframe.common.widget.SlidingLayout;
import com.kasiengao.ksgframe.constant.CoverConstant;
import com.kasiengao.ksgframe.databinding.LayoutVideoDetailBinding;
import com.kasiengao.ksgframe.player.cover.DanmakuCover;
import com.kasiengao.ksgframe.player.cover.LandControllerCover;
import com.kasiengao.ksgframe.player.cover.SmallControllerCover;
import com.kasiengao.ksgframe.player.cover.UploaderCover;
import com.kasiengao.ksgframe.ui.main.player.ListPlayer;
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel;
import com.ksg.ksgplayer.cover.CoverManager;
import com.ksg.ksgplayer.player.IPlayer;

/**
 * @ClassName: PlayerDetailView
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/30 14:55
 * @Description: 视频详情
 */
public class VideoDetailView extends FrameLayout {

    private int mViewWidth, mViewHeight;

    private int mStatusBarHeight;

    private boolean isOpenDetail;

    private boolean mFullscreen;

    private PlayerContainerView mListContainer;

    private MainViewModel mViewModel;

    private LayoutVideoDetailBinding mBinding;

    private BottomSheetBehavior<RelativeLayout> mInfoBehavior;

    private LandControllerCover mLandControllerCover;

    private SmallControllerCover mSmallControllerCover;

    private DanmakuCover mDanmakuCover;

    public VideoDetailView(Context context) {
        this(context, null);
    }

    public VideoDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Init
        this.init();
    }

    /**
     * Init
     */
    private void init() {
        this.mStatusBarHeight = StatusBarUtil.getStatusBarHeight(getContext());
        // DataBinding
        this.mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()), R.layout.layout_video_detail, this, true);
        // 绑定BottomSheet
        this.mInfoBehavior = BottomSheetBehavior.from(mBinding.infoCard);
        this.mInfoBehavior.addBottomSheetCallback(mBottomSheetCallback);
        // 添加状态栏高度
        StatusBarUtil.setStatusBarPadding(getContext(), mBinding.back);
        // 背景图事件同步管理控制器状态
        this.mBinding.coverImage.setOnClickListener(v -> {
            if (ListPlayer.getInstance().getPlayer().getState() != IPlayer.STATE_COMPLETE) {
                ListPlayer.getInstance().getCoverManager()
                        .getValuePool()
                        .putObject(CoverConstant.ValueKey.KEY_SWITCH_CONTROLLER, null);
            }
        });
        // 注册视图树中全局布局事件
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mViewWidth = getWidth();
                mViewHeight = getHeight();
                // 重新赋值PeekHeight
                mInfoBehavior.setPeekHeight(mBinding.infoProfileDetailTitle.getBottom());
                // 设置背景图高度
                mBinding.coverImage.setHeight(mViewHeight - mInfoBehavior.getPeekHeight() + 40);
            }
        });
        // 侧滑关闭
        this.<SlidingLayout>findViewById(R.id.sliding).setSlidingListener(this::closeDetail);
        // Init Player
        this.initPlayer();
    }

    /**
     * 绑定 ViewModel
     *
     * @param viewModel VM
     */
    public void bindViewModel(LifecycleOwner owner, MainViewModel viewModel) {
        if (mBinding != null) {
            this.mViewModel = viewModel;
            this.mBinding.setLifecycleOwner(owner);
            this.mBinding.setVariable(BR.viewModel, viewModel);
            // Init DataObserve
            this.initDataObserve(owner);
        }
    }

    /**
     * Init DataObserve
     */
    private void initDataObserve(LifecycleOwner owner) {
        // 弹幕数据
        this.mViewModel.getMDanmakuData().observe(owner, data -> {
            if (data != null && !data.isEmpty()) {
                this.getDanmakuCover().updateData(data);
            }
        });
    }

    /**
     * Init Player
     */
    private void initPlayer() {
        CoverManager coverManager = ListPlayer.getInstance().getCoverManager();
        // 小型控制器
        coverManager.addCover(CoverConstant.CoverKey.KEY_SMALL_CONTROLLER, getSmallControllerCover());
        // UP主信息
        coverManager.addCover(CoverConstant.CoverKey.KEY_UPLOADER, new UploaderCover(getContext()));
        // 事件
        ListPlayer.getInstance().setListPlayerListener(new ListPlayer.OnListPlayerListener() {

            /**
             * Back
             */
            @Override
            public void onBack() {
                onBackPressed();
            }

            /**
             * 打开详情页
             *
             * @param position      当前位置
             * @param listContainer 当前容器
             */
            @Override
            public void onOpenDetail(int position, @NonNull PlayerContainerView listContainer) {
                mListContainer = listContainer;
                // 刷新数据
                mViewModel.refreshInfo(position);
                // 初始高度
                mBinding.infoOther.setHeight(0f);
                // 背景图
                Bitmap blurBitmap = BlurUtil.blurDrawable(getContext(), mListContainer.getCoverImage(), 20f);
                if (blurBitmap != null) {
                    mBinding.coverImage.setImageBitmap(blurBitmap);
                }
                // 绑定视频容器
                ListPlayer.getInstance().bindNewContainer(mBinding.playerContainer);
                // 打开详情页
                openDetail();
            }

            /**
             * 全屏切换
             */
            @Override
            public void onFullscreen(boolean fullscreen) {
                VideoDetailView.this.onFullscreen(fullscreen);
            }
        });
    }

    /**
     * 打开详情页
     */
    public void openDetail() {
        this.isOpenDetail = true;
        // Show
        this.setVisibility(View.VISIBLE);
        // 显示信息页
        this.mBinding.infoCard.setVisibility(View.VISIBLE);
        // 获取列表容器的坐标
        int[] location = new int[2];
        this.mListContainer.getLocationOnScreen(location);
        // 计算比例16/9
        float containerHeight = (mViewWidth * 9 / 16f);
        // 入场动画
        AnimatorSet detailAnimator = new AnimatorSet();
        detailAnimator
                .play(
                        // 视频容器平移居中
                        ObjectAnimator.ofFloat(
                                mBinding.playerContainerCard, View.Y, location[1],
                                ((mViewHeight - mInfoBehavior.getPeekHeight()) / 2f - containerHeight / 2) + mStatusBarHeight / 2f))
                .with(
                        // 设置视频容器 宽
                        ObjectAnimator.ofFloat(
                                mBinding.playerContainer, "Width",
                                mListContainer.getWidth(), mViewWidth))
                .with(
                        // 设置视频容器 高
                        ObjectAnimator.ofFloat(
                                mBinding.playerContainer, "Height",
                                mListContainer.getHeight(), containerHeight));
        detailAnimator.addListener(mDetailAnimatorAdapter);
        detailAnimator.setInterpolator(new LinearInterpolator());
        detailAnimator.setDuration(150);
        detailAnimator.start();
    }

    /**
     * 关闭详情页
     */
    private void closeDetail() {
        this.isOpenDetail = false;
        // 切回至旧容器 (原列表容器)
        ListPlayer.getInstance().onExitDetail(mListContainer);
        // Hide
        this.setVisibility(View.GONE);
        // 隐藏back
        this.mBinding.back.setVisibility(View.GONE);
        // 隐藏信息页
        this.mBinding.infoCard.setVisibility(View.GONE);
        // 初始背景图
        this.mBinding.coverImage.setImageDrawable(null);
        // 关闭信息页
        this.mInfoBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    /**
     * 全屏
     *
     * @param fullscreen 全屏
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public void onFullscreen(boolean fullscreen) {
        this.mFullscreen = fullscreen;
        ListPlayer.getInstance().setFullscreen(mFullscreen);
        // 横竖屏切换
        AppCompatActivity activity = CommonUtil.scanForActivity(getContext());
        // 横竖屏
        if (fullscreen) {
            // 隐藏系统Ui
            SystemUiUtil.hideVideoSystemUI(getContext());
            // 设置横屏（可180°旋转）
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            // 恢复系统Ui
            SystemUiUtil.recoverySystemUI(getContext());
            // 设置竖屏
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 配置已更改
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 横竖屏配置
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 请求弹幕数据
            this.mViewModel.requestDanmakuData();
            // 打开详情页
            if (!isOpenDetail) {
                ListPlayer.getInstance().onOpenDetail();
            }
            this.mBinding.playContainerFullscreen.setVisibility(View.VISIBLE);
            // 设置Cover
            CoverManager coverManager = ListPlayer.getInstance().getCoverManager();
            coverManager.removeCover(CoverConstant.CoverKey.KEY_SMALL_CONTROLLER);
            coverManager.addCover(CoverConstant.CoverKey.KEY_DANMAKU, getDanmakuCover());
            coverManager.addCover(CoverConstant.CoverKey.KEY_LAND_CONTROLLER, getLandControllerCover());
            // 绑定全屏容器
            ListPlayer.getInstance().bindNewContainer(mBinding.playContainerFullscreen);
        } else {
            this.mBinding.playContainerFullscreen.setVisibility(View.GONE);
            // 设置Cover
            CoverManager coverManager = ListPlayer.getInstance().getCoverManager();
            coverManager.removeCover(CoverConstant.CoverKey.KEY_DANMAKU);
            coverManager.removeCover(CoverConstant.CoverKey.KEY_LAND_CONTROLLER);
            coverManager.addCover(CoverConstant.CoverKey.KEY_SMALL_CONTROLLER, getSmallControllerCover());
            // 恢复视频容器
            ListPlayer.getInstance().bindNewContainer(mBinding.playerContainer);
        }
    }

    /**
     * Cover 横屏控制器
     */
    private LandControllerCover getLandControllerCover() {
        if (mLandControllerCover == null) {
            this.mLandControllerCover = new LandControllerCover(getContext());

        }
        return mLandControllerCover;
    }

    /**
     * Cover 小型控制器
     */
    private SmallControllerCover getSmallControllerCover() {
        if (mSmallControllerCover == null) {
            this.mSmallControllerCover = new SmallControllerCover(getContext());
        }
        return mSmallControllerCover;
    }

    /**
     * Cover 弹幕
     */
    private DanmakuCover getDanmakuCover() {
        if (mDanmakuCover == null) {
            this.mDanmakuCover = new DanmakuCover(getContext());
        }
        return mDanmakuCover;
    }

    /**
     * onBackPressed
     */
    public boolean onBackPressed() {
        if (getVisibility() == View.VISIBLE) {
            // 验证全屏
            if (mFullscreen) {
                this.onFullscreen(false);
                return true;
            }
            // 关闭详情页
            this.closeDetail();
            return true;
        }
        return false;
    }

    /**
     * BottomSheet 事件
     */
    private final BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            // 切换背景为圆角/直角
            mBinding.infoCard.setSelected(newState != BottomSheetBehavior.STATE_COLLAPSED);
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            // 计算视频容器移动的Y坐标
            float moveY = (bottomSheet.getY() / 2f - mBinding.playerContainerCard.getHeight() / 2f) + mStatusBarHeight / 2f;
            // 更新视频容器的高度
            mBinding.playerContainerCard.setY(moveY);
            // 更新背景图的高度
            mBinding.coverImage.setHeight(bottomSheet.getY() + 40);
        }
    };

    /**
     * 动画 事件
     */
    private final AnimatorListenerAdapter mDetailAnimatorAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            // 显示back
            mBinding.back.setVisibility(View.VISIBLE);
            // 视频容器 宽度设置MATCH_PARENT
            mBinding.playerContainer.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            // 其他信息页 高度设置 (高度 - 状态栏 - 视频容器 - peekHeight= 剩余可用高度 )
            mBinding.infoOther.setHeight(
                    mViewHeight
                            - mStatusBarHeight
                            - mBinding.playerContainerCard.getHeight()
                            - mInfoBehavior.getPeekHeight()
            );
        }
    };

    /**
     * Detached
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 解绑
        this.mInfoBehavior.removeBottomSheetCallback(mBottomSheetCallback);
        // 释放
        this.mBinding.unbind();
        this.mBinding = null;
    }
}
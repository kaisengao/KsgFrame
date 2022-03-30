package com.kasiengao.ksgframe.ui.main.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.kaisengao.base.util.BlurUtil;
import com.kaisengao.base.util.StatusBarUtil;
import com.kasiengao.ksgframe.BR;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.widget.PlayerContainerView;
import com.kasiengao.ksgframe.common.widget.SlidingLayout;
import com.kasiengao.ksgframe.databinding.LayoutVideoDetailBinding;
import com.kasiengao.ksgframe.player.ListPlayer;
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel;

/**
 * @ClassName: PlayerDetailView
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/30 14:55
 * @Description: 视频详情
 */
public class VideoDetailView extends SlidingLayout {

    private int mViewWidth, mViewHeight;

    private int mStatusBarHeight;

    private PlayerContainerView mListContainer;

    private MainViewModel mViewModel;

    private LayoutVideoDetailBinding mBinding;

    private BottomSheetBehavior<MaterialCardView> mInfoBehavior;

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
        // 注册视图树中全局布局事件
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // 重新赋值PeekHeight
                mInfoBehavior.setPeekHeight(mBinding.infoCommentTitle.getBottom());
                // 设置背景图高度
                mBinding.coverImage.setHeight(mViewHeight - mInfoBehavior.getPeekHeight() + 20);
            }
        });
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


    }

    /**
     * Init Player
     */
    private void initPlayer() {
        ListPlayer.getInstance().setListPlayerListener((position, listContainer) -> {
            this.mListContainer = listContainer;
            // 刷新数据
            this.mViewModel.refreshInfo(position);
            // 初始高度
            this.mBinding.infoOther.setHeight(0f);
            // 背景图
            Bitmap blurBitmap = BlurUtil.blurDrawable(getContext(), mListContainer.getCoverImage(), 2f);
            if (blurBitmap != null) {
                this.mBinding.coverImage.setImageBitmap(blurBitmap);
            }
            // 绑定视频容器
            ListPlayer.getInstance().bindNewContainer(mBinding.playerContainer);
            // 打开详情页
            this.openDetail();
        });
    }

    /**
     * 打开详情页
     *
     * @param position      当前位置
     * @param listContainer 列表容器
     */
    public void openDetail() {
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
        // 切回至列表容器
        ListPlayer.getInstance().bindNewContainer(mListContainer);
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
     * 关闭
     */
    @Override
    public void close() {
        this.closeDetail();
    }

    /**
     * onBackPressed
     */
    public boolean onBackPressed() {
        if (getVisibility() == View.VISIBLE) {
            // 2关闭详情页
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
            Log.d("zzz", "onStateChanged() called with: bottomSheet = [" + bottomSheet + "], newState = [" + newState + "]");
//            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                setChildIntercept(true);
//            } else {
//                setChildIntercept(false);
//            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            // 计算视频容器移动的Y坐标
            float moveY = (bottomSheet.getY() / 2f - mBinding.playerContainerCard.getHeight() / 2f) + mStatusBarHeight / 2f;
            // 更新视频容器的高度
            mBinding.playerContainerCard.setY(moveY);
            // 更新背景图的高度
            mBinding.coverImage.setHeight(bottomSheet.getY() + 20);
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
     * SizeChanged
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mViewWidth = w;
        this.mViewHeight = h;
    }

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
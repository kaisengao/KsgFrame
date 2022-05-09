package com.kasiengao.ksgframe.ui.main.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.kaisengao.base.util.BlurUtil
import com.kaisengao.base.util.CommonUtil
import com.kaisengao.base.util.StatusBarUtil
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.SystemUiUtil
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.constant.CoverConstant
import com.kasiengao.ksgframe.databinding.LayoutXbbDetailBinding
import com.kasiengao.ksgframe.player.cover.DanmakuCover
import com.kasiengao.ksgframe.player.cover.LandControllerCover
import com.kasiengao.ksgframe.player.cover.SmallControllerCover
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel
import com.ksg.ksgplayer.KsgSinglePlayer
import com.kuaishou.akdanmaku.data.DanmakuItemData

/**
 * @ClassName: XBBDetailView
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/30 14:55
 * @Description: 视频详情
 */
class XBBDetailView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), View.OnClickListener {

    private var mViewWidth = 0

    private var mViewHeight = 0

    private var isOpenDetail = false

    private var mFullscreen = false

    private var mListContainer: PlayerContainerView? = null

    private val mStatusBarHeight: Int by lazy { StatusBarUtil.getStatusBarHeight(context) }

    private val mSignalPlayer: KsgSinglePlayer by lazy { KsgSinglePlayer.getInstance() }

    private val mBinding: LayoutXbbDetailBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.layout_xbb_detail, this, true
        )
    }

    private val mInfoBehavior: BottomSheetBehavior<RelativeLayout> by lazy {
        BottomSheetBehavior.from(mBinding.infoCard)
    }

    private val mLandControllerCover: LandControllerCover by lazy { LandControllerCover(context) }

    private val mSmallControllerCover: SmallControllerCover by lazy { SmallControllerCover(context) }

    private val mDanmakuCover: DanmakuCover by lazy { DanmakuCover(context) }

    private lateinit var mViewModel: MainViewModel

    var mVideoListener: XBBVideosView.OnVideoListener? = null

    init {
        // 添加状态栏高度
        this.mBinding.back.setPadding(0, mStatusBarHeight, 0, 0)
        // OnClick
        this.mBinding.root.setOnClickListener(null)
        this.mBinding.back.setOnClickListener(this)
//        // 背景图事件同步管理控制器状态
//        this.mBinding.coverImage.setOnClickListener { v: View? -> }
        // Init
        this.post {
            this.mViewWidth = width
            this.mViewHeight = height
            // 设置PeekHeight
            this.mInfoBehavior.peekHeight = mBinding.infoProfileDetailTitle.bottom
            // 设置背景图高度
            this.mBinding.coverImage.setHeight((mViewHeight - mInfoBehavior.peekHeight + 40).toFloat())
            // BottomSheet
            this.mInfoBehavior.addBottomSheetCallback(mBottomSheetCallback)
        }
    }

    /**
     * 绑定 ViewModel
     *
     * @param viewModel VM
     */
    fun bindViewModel(owner: LifecycleOwner, viewModel: MainViewModel) {
        this.mViewModel = viewModel
        this.mBinding.lifecycleOwner = owner
        this.mBinding.setVariable(BR.viewModel, viewModel)
        // Init DataObserve
        this.initDataObserve(owner)
    }

    /**
     * Init DataObserve
     */
    private fun initDataObserve(owner: LifecycleOwner) {
        // 弹幕数据
        this.mViewModel.mDanmakuData.observe(owner) { data: List<DanmakuItemData> ->
            if (data.isNotEmpty()) {
                this.mDanmakuCover.updateData(data)
            }
        }
    }

    /**
     * 打开详情页
     */
    fun openDetail(position: Int, listContainer: PlayerContainerView) {
        this.isOpenDetail = true
        this.mListContainer = listContainer
        // 刷新数据
        this.mViewModel.refreshInfo(position)
        // 初始高度
        this.mBinding.infoOther.setHeight(0f)
        // 背景图
        BlurUtil.blurDrawable(context, mListContainer?.coverImage, 20f)?.let {
            this.mBinding.coverImage.setImageBitmap(it)
        }
        // 同步信息
        this.mBinding.playerContainer.isIntercept = listContainer.isIntercept
        this.mBinding.playerContainer.coverImage = listContainer.coverImage
        // 绑定视频容器
        this.mSignalPlayer.bindContainer(mBinding.playerContainer, false)
        // 通知 隐藏控制器
        this.mSignalPlayer.coverManager.valuePool
            .putObject(CoverConstant.ValueKey.KEY_HIDE_CONTROLLER, null, false)
        // Show
        this.visibility = VISIBLE
        // 显示信息页
        this.mBinding.infoCard.visibility = VISIBLE
        // 获取列表容器的坐标
        val location = IntArray(2)
        this.mListContainer?.getLocationOnScreen(location)
        // 比例16/9
        val containerHeight = mViewWidth * 9 / 16f
        // 入场动画
        with(AnimatorSet()) {
            play( // 视频容器平移居中
                ObjectAnimator.ofFloat(
                    mBinding.playerContainerCard, View.Y, location[1].toFloat(),
                    ((mViewHeight - mInfoBehavior.peekHeight) / 2f - containerHeight / 2f) + mStatusBarHeight / 2f
                )
            ).with(
                // 设置视频容器 宽
                mListContainer?.width?.toFloat()?.let {
                    ObjectAnimator.ofFloat(
                        mBinding.playerContainer, "Width",
                        it, mViewWidth.toFloat()
                    )
                }
            ).with(
                // 设置视频容器 高
                mListContainer?.height?.toFloat()?.let {
                    ObjectAnimator.ofFloat(
                        mBinding.playerContainer, "Height",
                        it, containerHeight
                    )
                }
            )
            addListener(mDetailAnimatorAdapter)
            interpolator = LinearInterpolator()
            duration = 150
            start()
        }
    }

    /**
     * 关闭详情页
     */
    private fun closeDetail() {
        this.isOpenDetail = false
        // 切回至旧容器 (原列表容器)
        this.mSignalPlayer.bindContainer(mListContainer, false)
        // Hide
        this.visibility = GONE
        // 隐藏back
        this.mBinding.back.visibility = GONE
        // 隐藏信息页
        this.mBinding.infoCard.visibility = GONE
        // 初始背景图
        this.mBinding.coverImage.setImageDrawable(null)
        // 关闭信息页
        this.mInfoBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        // 关闭详情页
        this.mVideoListener?.closeDetail()
    }

    /**
     * 全屏
     *
     * @param fullscreen 全屏
     */
    @SuppressLint("SourceLockedOrientationActivity")
    fun onFullscreen(fullscreen: Boolean, position: Int, container: PlayerContainerView?) {
        if (mFullscreen == fullscreen) {
            return
        }
        this.mFullscreen = fullscreen
        // 横竖屏切换
        val activity = CommonUtil.scanForActivity(context)
        // 横竖屏
        if (fullscreen) {
            // 打开详情页
            if (!isOpenDetail) {
                this.openDetail(position, container!!)
            }
            // 请求弹幕数据
            this.mViewModel.requestDanmakuData()
            // 隐藏系统Ui
            SystemUiUtil.hideVideoSystemUI(context)
            // 设置横屏（可180°旋转）
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            // 恢复系统Ui
            SystemUiUtil.recoverySystemUI(context)
            // 设置竖屏
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    /**
     * 配置已更改
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 横竖屏配置
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.mBinding.playContainerFullscreen.visibility = VISIBLE
            // 设置Cover
            val coverManager = mSignalPlayer.coverManager
            coverManager.removeCover(CoverConstant.CoverKey.KEY_SMALL_CONTROLLER)
            coverManager.addCover(CoverConstant.CoverKey.KEY_DANMAKU, mDanmakuCover)
            coverManager.addCover(CoverConstant.CoverKey.KEY_LAND_CONTROLLER, mLandControllerCover)
            // 绑定全屏容器
            this.mSignalPlayer.bindContainer(mBinding.playContainerFullscreen, false)
        } else {
            this.mBinding.playContainerFullscreen.visibility = GONE
            // 设置Cover
            val coverManager = mSignalPlayer.coverManager
            coverManager.removeCover(CoverConstant.CoverKey.KEY_DANMAKU)
            coverManager.removeCover(CoverConstant.CoverKey.KEY_LAND_CONTROLLER)
            coverManager.addCover(
                CoverConstant.CoverKey.KEY_SMALL_CONTROLLER,
                mSmallControllerCover
            )
            // 恢复视频容器
            this.mSignalPlayer.bindContainer(mBinding.playerContainer, false)
        }
    }

    /**
     * BottomSheet 事件
     */
    private val mBottomSheetCallback: BottomSheetCallback = object : BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            // 切换背景为圆角/直角
            mBinding.infoCard.isSelected = newState != BottomSheetBehavior.STATE_COLLAPSED
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            // 计算视频容器移动的Y坐标
            val moveY =
                bottomSheet.y / 2f - mBinding.playerContainerCard.height / 2f + mStatusBarHeight / 2f
            // 更新视频容器的高度
            mBinding.playerContainerCard.y = moveY
            // 更新背景图的高度
            mBinding.coverImage.setHeight(bottomSheet.y + 40)
        }
    }

    /**
     * 动画 事件
     */
    private val mDetailAnimatorAdapter: AnimatorListenerAdapter =
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // 显示back
                mBinding.back.visibility = VISIBLE
                // 视频容器 宽度设置MATCH_PARENT
                mBinding.playerContainer.setWidth(ViewGroup.LayoutParams.MATCH_PARENT.toFloat())
                // 其他信息页 高度设置 (高度 - 状态栏 - 视频容器 - peekHeight= 剩余可用高度 )
                mBinding.infoOther.setHeight(
                    (mViewHeight
                            - mStatusBarHeight
                            - mBinding.playerContainerCard.height
                            - mInfoBehavior.peekHeight).toFloat()
                )
            }
        }

    /**
     * onClick
     */
    override fun onClick(v: View) {
        if (v.id == R.id.back) {
            // 关闭详情页
            this.closeDetail()
        }
    }

    /**
     * onBackPressed
     */
    fun onBackPressed(): Boolean {
        if (visibility == VISIBLE) {
            // 退出全屏
            if (mFullscreen) {
                this.onFullscreen(false, 0, null)
                return true
            }
            // 关闭详情页
            this.closeDetail()
            return true
        }
        return false
    }

    /**
     * Detached
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 解绑
        this.mInfoBehavior.removeBottomSheetCallback(mBottomSheetCallback)
    }
}
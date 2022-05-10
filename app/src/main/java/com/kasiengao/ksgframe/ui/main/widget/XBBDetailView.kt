package com.kasiengao.ksgframe.ui.main.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.kaisengao.base.util.StatusBarUtil
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.ColorUtil
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
@SuppressLint("ClickableViewAccessibility")
class XBBDetailView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs), View.OnClickListener, View.OnTouchListener {

    private var mDownX: Float = 0F
    private var mDownY: Float = 0F

    private var mMoveY: Float = 0F

    private var mSlideOffset: Float = 250F

    private var isOpenDetail = false

    private var mFullscreen = false

    private val mBgColor: String by lazy {
        Integer
            .toHexString(ContextCompat.getColor(context, R.color.background))
            .removePrefix("ff")
    }

    private val mStatusBarHeight: Int by lazy { StatusBarUtil.getStatusBarHeight(context) }

    private val mSignalPlayer: KsgSinglePlayer by lazy { KsgSinglePlayer.getInstance() }

    private val mBinding: LayoutXbbDetailBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.layout_xbb_detail, this, true
        )
    }

    private val mLandControllerCover: LandControllerCover by lazy { LandControllerCover(context) }

    private val mSmallControllerCover: SmallControllerCover by lazy { SmallControllerCover(context) }

    private val mDanmakuCover: DanmakuCover by lazy { DanmakuCover(context) }

    private lateinit var mViewModel: MainViewModel

    private lateinit var mListContainer: PlayerContainerView

    var mVideoListener: XBBVideosView.OnVideoListener? = null

    init {
        this.mBinding.statusBar.setHeight(mStatusBarHeight.toFloat())
        this.setBackgroundColor(Color.parseColor("#$mBgColor"))
        // OnClick
        this.mBinding.back.setOnClickListener(this)
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
        // 同步信息
        this.mBinding.playerContainer.let {
            it.isIntercept = listContainer.isIntercept
            it.coverImage = listContainer.coverImage
            it.setHeight(listContainer.height.toFloat())
            // 绑定 视图容器
            this.mSignalPlayer.bindContainer(it, false)
        }
        // 通知 隐藏控制器
        this.mSignalPlayer.coverManager.valuePool
            .putObject(CoverConstant.ValueKey.KEY_HIDE_CONTROLLER, null, false)
        // Show
        this.visibility = VISIBLE
        // TouchEvent
        this.setOnTouchListener(this)
        this.setBackgroundColor(Color.parseColor("#$mBgColor"))
        // 列表容器坐标
        val location = IntArray(2)
        this.mListContainer.getLocationOnScreen(location)
        // 动画
        ObjectAnimator.ofFloat(this@XBBDetailView, Y, location[1].toFloat(), 0F).let {
            it.interpolator = LinearInterpolator()
            it.duration = 150
            it.start()
        }
    }

    /**
     * 关闭详情页
     */
    private fun closeDetail() {
        this.isOpenDetail = false
        // HideView
        this.onHideView()
        this.setBackgroundColor(Color.TRANSPARENT)
        // 列表容器坐标
        val location = IntArray(2)
        this.mListContainer.getLocationOnScreen(location)
        // 动画
        with(AnimatorSet()) {
            val startX = mBinding.playerContainer.x
            val startY = mBinding.playerContainer.y
            playTogether(
                ObjectAnimator.ofFloat(mBinding.playerContainer, X, startX, location[0].toFloat()),
                ObjectAnimator.ofFloat(mBinding.playerContainer, Y, startY, location[1].toFloat())
            )
            addListener(mCloseDetailAnimatorListener)
            interpolator = LinearInterpolator()
            duration = 150
            start()
        }
    }

    /**
     * 关闭详情页动画事件
     */
    private val mCloseDetailAnimatorListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            // Hide
            visibility = GONE
            // TouchEvent
            setOnTouchListener(null)
            // 切回至旧容器 (原列表容器)
            mSignalPlayer.bindContainer(mListContainer, false)
            // 关闭详情页
            mVideoListener?.closeDetail()
            // 复位
            mBinding.playerContainer.x = 0F
            mBinding.playerContainer.y = mStatusBarHeight.toFloat()
            // ShowView
            onShowView()
            setBackgroundColor(Color.parseColor("#$mBgColor"))
        }
    }
//
//    /**
//     * 全屏
//     *
//     * @param fullscreen 全屏
//     */
//    @SuppressLint("SourceLockedOrientationActivity")
//    fun onFullscreen(fullscreen: Boolean, position: Int, container: PlayerContainerView?) {
//        if (mFullscreen == fullscreen) {
//            return
//        }
//        this.mFullscreen = fullscreen
//        // 横竖屏切换
//        val activity = CommonUtil.scanForActivity(context)
//        // 横竖屏
//        if (fullscreen) {
//            // 打开详情页
//            if (!isOpenDetail) {
//                this.openDetail(position, container!!)
//            }
//            // 请求弹幕数据
//            this.mViewModel.requestDanmakuData()
//            // 隐藏系统Ui
//            SystemUiUtil.hideVideoSystemUI(context)
//            // 设置横屏（可180°旋转）
//            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
//        } else {
//            // 恢复系统Ui
//            SystemUiUtil.recoverySystemUI(context)
//            // 设置竖屏
//            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        }
//    }
//
//    /**
//     * 配置已更改
//     */
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        // 横竖屏配置
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            this.mBinding.playContainerFullscreen.visibility = VISIBLE
//            // 设置Cover
//            val coverManager = mSignalPlayer.coverManager
//            coverManager.removeCover(CoverConstant.CoverKey.KEY_SMALL_CONTROLLER)
//            coverManager.addCover(CoverConstant.CoverKey.KEY_DANMAKU, mDanmakuCover)
//            coverManager.addCover(CoverConstant.CoverKey.KEY_LAND_CONTROLLER, mLandControllerCover)
//            // 绑定全屏容器
//            this.mSignalPlayer.bindContainer(mBinding.playContainerFullscreen, false)
//        } else {
//            this.mBinding.playContainerFullscreen.visibility = GONE
//            // 设置Cover
//            val coverManager = mSignalPlayer.coverManager
//            coverManager.removeCover(CoverConstant.CoverKey.KEY_DANMAKU)
//            coverManager.removeCover(CoverConstant.CoverKey.KEY_LAND_CONTROLLER)
//            coverManager.addCover(
//                CoverConstant.CoverKey.KEY_SMALL_CONTROLLER,
//                mSmallControllerCover
//            )
//            // 恢复视频容器
//            this.mSignalPlayer.bindContainer(mBinding.playerContainer, false)
//        }
//    }

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
     * onTouch
     */
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (mBinding.infoScroll.scrollTop > 0) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.mDownX = event.x
                this.mDownY = event.y
            }
            MotionEvent.ACTION_UP -> {
                // 验证滑动距离是否抵达了偏移量
                if (mMoveY >= mSlideOffset) {
                    // 关闭详情页
                    this.closeDetail()
                } else {
                    // 复位
                    this.onSlideReset()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.x - mDownX
                this.mMoveY = (event.y - mDownY)
                this.mBinding.playerContainer.x = moveX
                this.mBinding.playerContainer.y = mMoveY + mStatusBarHeight.toFloat()
                if (mMoveY > 0) {
                    this.onHideView()
                } else {
                    this.onShowView()
                }
                val percent = (mMoveY / mSlideOffset) * 100
                val bgColor = ColorUtil.percentColor((100 - percent), mBgColor)
                this.setBackgroundColor(Color.parseColor(bgColor))
            }
        }
        return true
    }

    /**
     * 事件分发
     */
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        var intercept = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.mDownX = event.x
                this.mDownY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (mDownY < event.y && mBinding.infoScroll.scrollTop <= 0) {
                    intercept = true
                }
            }
        }
        return intercept
    }

    /**
     * 滑动复位
     */
    private fun onSlideReset() {
        with(AnimatorSet()) {
            val container = mBinding.playerContainer
            playTogether(
                ObjectAnimator.ofFloat(container, X, container.x, 0F),
                ObjectAnimator.ofFloat(container, Y, container.y, mStatusBarHeight.toFloat())
            )
            interpolator = LinearInterpolator()
            duration = 100
            start()
        }
        this.onShowView()
    }

    /**
     * 显示View
     */
    private fun onShowView() {
        this.mBinding.back.visibility = VISIBLE
        this.mBinding.infoRoot.visibility = VISIBLE
        this.mBinding.playerBgLayer.visibility = VISIBLE
    }

    /**
     * 隐藏View
     */
    private fun onHideView() {
        this.mBinding.back.visibility = GONE
        this.mBinding.infoRoot.visibility = GONE
        this.mBinding.playerBgLayer.visibility = GONE
    }

    /**
     * onBackPressed
     */
    fun onBackPressed(): Boolean {
        if (visibility == VISIBLE) {
            // 退出全屏
            if (mFullscreen) {
//                this.onFullscreen(false, 0, null)
                return true
            }
            // 关闭详情页
            this.closeDetail()
            return true
        }
        return false
    }

}
package com.kasiengao.ksgframe.ui.main.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.kaisengao.base.util.SnackbarUtil
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.widget.CNestedScrollView
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.databinding.LayoutXbbDetailBinding
import com.kasiengao.ksgframe.ui.main.bean.VideoBean

/**
 * @ClassName: XBBDetailItemView
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/30 14:55
 * @Description: 视频详情 ItemView
 */
class XBBDetailItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    private var mViewWidth = 0

    private var mViewHeight = 0

    private val mBinding: LayoutXbbDetailBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.layout_xbb_detail, this, true
        )
    }

    init {
        // OnClick
        this.mBinding.infoFollow.setOnClickListener { v ->
            SnackbarUtil.with(v).setMessage("关注一波啊，喂！").show()
        }
    }

    fun init(videoBean: VideoBean) {
        // 刷新数据
        this.mBinding.videoBean = videoBean
        // Init
        this.post {
            this.mViewWidth = width
            this.mViewHeight = height
            // 比例16/9
            this.mBinding.playerContainer.setHeight((mViewWidth * 9 / 16f))
            // 封面图
            this.mBinding.playerContainer.setCoverImage(
                videoBean.coverImg,
                ImageView.ScaleType.CENTER_CROP
            )
            this.mBinding.infoScroll.scrollTo(0, 0)
            this.mBinding.playerContainer.onShowView()
        }
    }

    fun getPlayerContainer(): PlayerContainerView {
        return mBinding.playerContainer
    }

    fun getScrollView(): CNestedScrollView {
        return mBinding.infoScroll
    }

    /**
     * 显示 View
     *
     * 注：在打开完成后显示所有View
     */
    fun onOpenedShowView() {
        this.mBinding.infoRoot.visibility = VISIBLE
        this.mBinding.playerBgLayer.visibility = VISIBLE
    }

    /**
     * 隐藏 View
     *
     * 注：在关闭过程中需要隐藏除「playerContainer」外的所有View
     */
    fun onClosedHideView() {
        this.mBinding.infoRoot.visibility = GONE
        this.mBinding.playerBgLayer.visibility = GONE
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
}
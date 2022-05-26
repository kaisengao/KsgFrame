package com.kaisengao.ksgframe.ui.main.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.kaisengao.base.util.SnackbarUtil
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.common.widget.CNestedScrollView
import com.kaisengao.ksgframe.common.widget.PlayerContainerView
import com.kaisengao.ksgframe.databinding.LayoutXbbDetailItemBinding
import com.kaisengao.ksgframe.ui.main.bean.VideoBean

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

    private val mBinding: LayoutXbbDetailItemBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.layout_xbb_detail_item, this, true
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
}
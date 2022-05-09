package com.kasiengao.ksgframe.ui.main.fragment

import android.view.ViewGroup
import com.kaisengao.mvvm.base.fragment.BaseVmFragment
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.TextUtil
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.databinding.FragmentXbbBinding
import com.kasiengao.ksgframe.ui.main.MainActivity
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel
import com.kasiengao.ksgframe.ui.main.widget.XBBVideosView

/**
 * @ClassName: XBBFragment
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/26 21:30
 * @Description: 象拔蚌
 */
class XBBFragment : BaseVmFragment<FragmentXbbBinding, MainViewModel>() {

    private var mHidden = true

    override fun getContentLayoutId(): Int = R.layout.fragment_xbb

    override fun initVariableId(): Int = BR.viewModel

    override fun getTitle(): String = TextUtil.getString(R.string.XBB_title)

    override fun initWidget() {
        super.initWidget()
        // 设置PaddingTop为Toolbar的高度、使得布局下移不会被Toolbar覆盖
        this.mBinding.root.post {
            (activity as MainActivity).getToolbarHeight().let {
                (mBinding.xbbRefresh.layoutParams as ViewGroup.MarginLayoutParams).topMargin = it
            }
        }
        // Refresh
        this.mBinding.xbbRefresh.setOnRefreshListener {
            this.mViewModel.requestVideos()
        }
        // Init Videos
        this.initVideos()
        // Init ViewDetail
        this.initViewDetail()
        // Init DataObserve
        this.initDataObserve()
    }

    /**
     * 懒加载
     */
    override fun lazyLoad() {
        // 请求 视频列表
        this.mViewModel.requestVideos()
    }

    /**
     * FragmentHidden
     */
    override fun onHiddenChanged(hidden: Boolean) {
        this.mHidden = hidden
        // Init Player
        if (!mHidden) {
            this.mBinding.xbbVideos.initPlayer()
        }
    }

    /**
     * Init Videos
     */
    private fun initVideos() {
        // 事件
        this.mBinding.xbbVideos.mVideoListener = mVideoListener
        this.mBinding.xbbVideoDetail.mVideoListener = mVideoListener
    }

    /**
     * 事件
     */
    private val mVideoListener = object : XBBVideosView.OnVideoListener {

        /**
         * Back
         */
        override fun onBack() {
            mBinding.xbbVideoDetail.onBackPressed()
        }

        /**
         * 全屏
         */
        override fun onFullscreen(
            fullscreen: Boolean,
            position: Int,
            container: PlayerContainerView
        ) {
            (activity as MainActivity).let {
                if (fullscreen) {
                    it.lockVpScroll()
                    it.supportActionBar?.hide()
                }
            }
            mBinding.xbbVideoDetail.onFullscreen(fullscreen, position, container)
        }

        /**
         * 打开详情页
         *
         * @param position      列表位置
         * @param listContainer 列表容器
         */
        override fun openDetail(position: Int, listContainer: PlayerContainerView?) {
            listContainer?.let { container ->
                (activity as MainActivity).let {
                    it.lockVpScroll()
                    it.supportActionBar?.hide()
                }
                mBinding.xbbVideoDetail.openDetail(position, container)
            }
        }

        /**
         * 关闭详情页
         */
        override fun closeDetail() {
            (activity as MainActivity).let {
                it.unlockVpScroll()
                it.supportActionBar?.show()
            }
        }
    }

    /**
     * Init ViewDetail
     */
    private fun initViewDetail() {
        this.mBinding.xbbVideoDetail.bindViewModel(this, mViewModel)
    }

    /**
     * Init DataObserve
     */
    private fun initDataObserve() {
        // Videos
        this.mViewModel.mVideos.observe(
            this
        ) { videos ->
            videos?.let {
                this.mBinding.xbbVideos.setData(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        this.mBinding.xbbVideos.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.mBinding.xbbVideos.onPause()
    }

    override fun onBackPressed(): Boolean {
        return mBinding.xbbVideoDetail.onBackPressed()
    }
}
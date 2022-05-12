package com.kasiengao.ksgframe.ui.main.fragment

import com.kaisengao.mvvm.base.fragment.BaseVmFragment
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.TextUtil
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.databinding.FragmentXbbBinding
import com.kasiengao.ksgframe.ui.main.MainActivity
import com.kasiengao.ksgframe.ui.main.bean.VideoBean
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel
import com.kasiengao.ksgframe.ui.main.widget.XBBDetailView
import com.kasiengao.ksgframe.ui.main.widget.XBBVideosView

/**
 * @ClassName: XBBFragment
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/26 21:30
 * @Description: 象拔蚌
 */
class XBBFragment : BaseVmFragment<FragmentXbbBinding, MainViewModel>() {

    private var mHidden = true

    private val mDetailView: XBBDetailView by lazy { XBBDetailView(context!!) }

    override fun getContentLayoutId(): Int = R.layout.fragment_xbb

    override fun initVariableId(): Int = BR.viewModel

    override fun getTitle(): String = TextUtil.getString(R.string.XBB_title)

    override fun initWidget() {
        super.initWidget()
        // 设置PaddingTop使得布局下移不会被Toolbar覆盖
        this.mBinding.root.post {
            (activity as MainActivity).getToolbarHeight().let {
                this.mBinding.root.setPadding(0, it, 0, 0)
            }
        }
        // Refresh
        this.mBinding.xbbRefresh.setOnRefreshListener {
            this.mViewModel.requestVideos()
        }
        // Init Videos
        this.initVideos()
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
        this.mDetailView.init(activity!!, mBinding.xbbVideos)
        // 事件
        this.mDetailView.mVideoListener = mVideoListener
        this.mBinding.xbbVideos.mVideoListener = mVideoListener
    }

    /**
     * 事件
     */
    private val mVideoListener = object : XBBVideosView.OnVideoListener {

        /**
         * Back
         */
        override fun onBack() {
//            mDetailView.onBackPressed()
        }

        /**
         * 全屏
         */
        override fun onFullscreen(
            fullscreen: Boolean,
            position: Int,
            container: PlayerContainerView
        ) {
//            (activity as MainActivity).let {
//                if (fullscreen) {
//                    it.lockVpScroll()
//                    it.supportActionBar?.hide()
//                }
//            }
//            mBinding.xbbVideoDetail.onFullscreen(fullscreen, position, container)
        }

        /**
         * 打开详情页
         *
         * @param position      列表位置
         * @param data          数据源
         * @param listContainer 列表容器
         */
        override fun openDetail(
            position: Int,
            data: List<VideoBean>,
            listContainer: PlayerContainerView
        ) {
            mDetailView.openDetail(position, data, listContainer)
        }

        /**
         * 关闭详情页
         */
        override fun closeDetail() {
            mDetailView.closeDetail()
        }
    }

    /**
     * Init DataObserve
     */
    private fun initDataObserve() {
        // Videos
        this.mViewModel.mVideos.observe(this) { videos ->
            videos?.let {
                this.mBinding.xbbVideos.setData(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mHidden) {
            this.mBinding.xbbVideos.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (!mHidden) {
            this.mBinding.xbbVideos.onPause()
        }
    }

    override fun onBackPressed(): Boolean {
        return mDetailView.onBackPressed()
    }
}
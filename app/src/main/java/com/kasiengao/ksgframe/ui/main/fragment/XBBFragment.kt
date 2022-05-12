package com.kasiengao.ksgframe.ui.main.fragment

import com.kaisengao.mvvm.base.fragment.BaseVmFragment
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.TextUtil
import com.kasiengao.ksgframe.databinding.FragmentXbbBinding
import com.kasiengao.ksgframe.ui.main.MainActivity
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel

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
     * Init Videos
     */
    private fun initVideos() {
        this.mBinding.xbbVideos.init(activity!!)
        this.mBinding.xbbVideos.bindViewModel(this, mViewModel)
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
        return mBinding.xbbVideos.onBackPressed()
    }
}
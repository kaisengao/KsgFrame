package com.kasiengao.ksgframe.ui.main.fragment

import com.kaisengao.mvvm.base.fragment.BaseVmFragment
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.TextUtil
import com.kasiengao.ksgframe.databinding.FragmentPpxBinding
import com.kasiengao.ksgframe.ui.main.adapter.PPXAdapter
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel

/**
 * @ClassName: PPXFragment
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/26 23:03
 * @Description: 皮皮虾
 */
class PPXFragment : BaseVmFragment<FragmentPpxBinding, MainViewModel>() {

    private lateinit var mAdapter: PPXAdapter

    override fun getContentLayoutId(): Int = R.layout.fragment_ppx

    override fun initVariableId(): Int = BR.viewModel

    override fun getTitle(): String = TextUtil.getString(R.string.PPX_title)

    override fun initWidget() {
        super.initWidget()
        // Init Adapter
        this.initAdapter()
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
     * Init Adapter
     */
    private fun initAdapter() {
        // Adapter
        this.mAdapter = PPXAdapter()
        // Viewpager2
        this.mBinding.xbbVideos.adapter = mAdapter
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
                this.mAdapter.setList(it)
            }
        }
    }
}
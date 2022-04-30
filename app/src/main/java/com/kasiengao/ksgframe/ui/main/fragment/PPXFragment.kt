package com.kasiengao.ksgframe.ui.main.fragment

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kaisengao.mvvm.base.fragment.BaseVmFragment
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.TextUtil
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.databinding.FragmentPpxBinding
import com.kasiengao.ksgframe.ui.main.adapter.PPXAdapter
import com.kasiengao.ksgframe.ui.main.player.ListPlayer
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
        this.mBinding.xbbVideos.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                val childAt = mBinding.xbbVideos.getChildAt(0)
                val videoBean = mAdapter.data[position]
                (childAt as RecyclerView).let {
                    val findViewByPosition = it.layoutManager?.findViewByPosition(position)
                    val findViewById =
                        findViewByPosition?.findViewById<PlayerContainerView>(R.id.item_player_container)
                            ListPlayer.getInstance().onPlay(position, videoBean, findViewById)
                    }
                }

        })
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
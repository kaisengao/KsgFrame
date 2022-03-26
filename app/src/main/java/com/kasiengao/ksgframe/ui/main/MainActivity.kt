package com.kasiengao.ksgframe.ui.main

import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaisengao.base.util.StatusBarUtil

import com.kaisengao.mvvm.base.activity.BaseVmActivity
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.databinding.ActivityMainBinding
import com.kasiengao.ksgframe.ui.main.adapter.VideosAdapter
import com.kasiengao.ksgframe.ui.main.bean.VideoBean

import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel
import com.kasiengao.ksgframe.ui.trainee.TraineeActivity


/**
 * @ClassName: MainActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 15:36
 * @Description: 启动页面
 */
class MainActivity : BaseVmActivity<ActivityMainBinding, MainViewModel>() {

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun initVariableId(): Int = BR.viewModel

    override fun initBefore() {
        super.initBefore()
        // AppBar 添加状态栏高度
        StatusBarUtil.setStatusBarPadding(this, mBinding.mainAppbar)
    }

    override fun initWidget() {
        super.initWidget()
        // Init VideoAdapter
        this.initVideoAdapter()
        // 侧滑抽屉
        this.mBinding.mainBottomBar.setNavigationOnClickListener {
            this.mBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
        // 练习生页面
        this.mBinding.mainTrainee.setOnClickListener {
            TraineeActivity.startActivity(this)
        }
    }

    override fun initData() {
        super.initData()
        // 请求 视频列表
        this.mViewModel.requestVideos()
    }

    /**
     * Init VideoAdapter
     */
    private fun initVideoAdapter() {
        // Adapter
        val adapter = VideosAdapter()
        // RecyclerView
        val recyclerView = mBinding.mainVideos
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        // Data
        this.mViewModel.mVideos.observe(this,
            { videos ->
                if (videos != null) {
                    adapter.setList(videos)
                }
            })
    }
}

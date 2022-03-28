package com.kasiengao.ksgframe.ui.main

import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaisengao.base.configure.ActivityManager
import com.kaisengao.base.util.StatusBarUtil
import com.kaisengao.mvvm.base.activity.BaseVmActivity
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.databinding.ActivityMainBinding
import com.kasiengao.ksgframe.player.ListPlayer
import com.kasiengao.ksgframe.ui.main.adapter.VideosAdapter
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel


/**
 * @ClassName: MainActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 15:36
 * @Description: 启动页面
 */
class MainActivity : BaseVmActivity<ActivityMainBinding, MainViewModel>() {

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun initVariableId(): Int = BR.viewModel

    override fun initWidget() {
        super.initWidget()
        // Init VideoAdapter
        this.initVideoAdapter()
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
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        // Data
        this.mViewModel.mVideos.observe(this,
            { videos ->
                videos?.let {
                    // Player
                    ListPlayer.getInstance().setVideoUrls(it)
                    // Adapter
                    adapter.setList(it)
                    // 自动播放
                    recyclerView.smoothScrollBy(0, 1)
                }
            })
    }

    /**
     * 菜单的响应事件
     */
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // 侧滑抽屉
            this.mBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }


    override fun onResume() {
        super.onResume()
        // 继续播放
        ListPlayer.getInstance().onResume()
    }

    override fun onPause() {
        super.onPause()
        // 暂停播放
        ListPlayer.getInstance().onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // 释放播放器
        ListPlayer.getInstance().release()
        // 结束所有Activity
        ActivityManager.getInstance().finishAllActivity()
    }
}

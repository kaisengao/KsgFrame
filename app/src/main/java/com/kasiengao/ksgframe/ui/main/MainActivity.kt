package com.kasiengao.ksgframe.ui.main

import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import com.kaisengao.base.configure.ActivityManager
import com.kaisengao.base.util.StatusBarUtil
import com.kaisengao.mvvm.base.activity.BaseVmActivity
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.databinding.ActivityMainBinding
import com.kasiengao.ksgframe.ui.main.player.ListPlayer
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

    override fun initBefore() {
        super.initBefore()
        // 添加状态栏高度
        StatusBarUtil.setStatusBarPadding(this, mBinding.mainAppbar)
    }

    override fun initWidget() {
        super.initWidget()
        // Init ViewDetail
        this.initViewDetail()
        // Init DrawerLayout
        this.initDrawer()
        // Init DataObserve
        this.initDataObserve()
    }

    override fun initData() {
        super.initData()
        // 请求 视频列表
        this.mViewModel.requestVideos()
    }

    /**
     * Init ViewDetail
     */
    private fun initViewDetail() {
        this.mBinding.mainVideoDetail.bindViewModel(this, mViewModel)
    }

    /**
     * Init DrawerLayout
     */
    private fun initDrawer() {
        // 抽屉手势开启与关闭
        this.mBinding.drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
                mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            override fun onDrawerClosed(drawerView: View) {
                mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
        // 抽屉默认关闭手势
        this.mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    /**
     * Init DataObserve
     */
    private fun initDataObserve() {
        // Videos
        this.mViewModel.mVideos.observe(this,
            { videos ->
                videos?.let {
                    this.mBinding.mainVideos.setData(it)
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
        // 详情页
        if (mBinding.mainVideoDetail.onBackPressed()) {
            return
        }
        // Super
        super.onBackPressed()
        // 释放播放器
        ListPlayer.getInstance().release()
        // 结束所有Activity
        ActivityManager.getInstance().finishAllActivity()
    }
}

package com.kasiengao.ksgframe.ui.main

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.viewpager.widget.ViewPager
import com.kaisengao.base.configure.ActivityManager
import com.kaisengao.base.util.StatusBarUtil
import com.kaisengao.mvvm.base.activity.BaseVmActivity
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.ColorUtil
import com.kasiengao.ksgframe.databinding.ActivityMainBinding
import com.kasiengao.ksgframe.ui.main.adapter.MainAdapter
import com.kasiengao.ksgframe.ui.main.fragment.PPXFragment
import com.kasiengao.ksgframe.ui.main.fragment.XBBFragment
import com.kasiengao.ksgframe.ui.main.player.ListPlayer
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel


/**
 * @ClassName: MainActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 15:36
 * @Description: 启动页面
 */
class MainActivity : BaseVmActivity<ActivityMainBinding, MainViewModel>() {

    private val mToolbarBgColor: String by lazy {
        Integer
            .toHexString(ContextCompat.getColor(this, R.color.colorPrimary))
            .removePrefix("ff")
    }

    private val mPPXToolbarTextColor: Int by lazy {
        ContextCompat.getColor(this@MainActivity, R.color.PPXToolbarTextColor)
    }

    private val mXBBToolbarTextColor: Int by lazy {
        ContextCompat.getColor(this@MainActivity, R.color.XBBToolbarTextColor)
    }

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun initVariableId(): Int = BR.viewModel

    override fun initBefore() {
        super.initBefore()
        // 添加状态栏高度
        StatusBarUtil.setStatusBarPadding(this, mBinding.mainActionbar)
    }

    override fun initWidget() {
        super.initWidget()
        // Init Adapter
        this.initAdapter()
        // Init DrawerLayout
        this.initDrawer()
    }

    /**
     * Init Adapter
     */
    private fun initAdapter() {
        this.setToolbarColor(mPPXToolbarTextColor)
        // Fragments
        val fragments = mutableListOf(
            PPXFragment(),
            XBBFragment()
        )
        // ViewPager
        val viewPager = mBinding.mainPager
        // Adapter
        viewPager.adapter = MainAdapter(supportFragmentManager, fragments)
        // TabLayout
        this.mBinding.mainTabs.setupWithViewPager(viewPager)
        // 事件
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (positionOffset > 0) {
                    val offset = (positionOffset * 100)
                    // 背景颜色与阴影
                    val bgColor = ColorUtil.percentColor(offset, mToolbarBgColor)
                    mBinding.mainActionbar.setBackgroundColor(Color.parseColor(bgColor))
                    mBinding.mainActionbar.elevation = offset
                }
            }

            override fun onPageSelected(position: Int) {
                // 字体颜色
                when (position) {
                    0 -> setToolbarColor(mPPXToolbarTextColor)
                    1 -> setToolbarColor(mXBBToolbarTextColor)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    /**
     * Init DrawerLayout
     */
    private fun initDrawer() {
        // 抽屉事件
        this.mBinding.drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
                ListPlayer.getInstance().setOverlap(true)
                mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            override fun onDrawerClosed(drawerView: View) {
                ListPlayer.getInstance().setOverlap(false)
                mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })
        // 抽屉默认关闭
        this.mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    /**
     * 设置 Toolbar与TabLayout的颜色
     */
    private fun setToolbarColor(color: Int) {
        this.mBinding.toolbar.setTitleTextColor(color)
        this.mBinding.toolbar.setNavigationIconTint(color)
        this.mBinding.mainTabs.setTabTextColors(Color.WHITE, color)
        this.mBinding.mainTabs.setSelectedTabIndicatorColor(color)
    }

    public fun getActionBarHeight(): Int {
        return mBinding.mainActionbar.height
    }

    /**
     * 菜单的响应事件
     */
    override

    fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // 侧滑抽屉
            this.mBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        // 继续播放
        if (!mBinding.drawerLayout.isDrawerOpen(mBinding.mainTrainee)) {
            ListPlayer.getInstance().onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        // 暂停播放
        if (!mBinding.drawerLayout.isDrawerOpen(mBinding.mainTrainee)) {
            ListPlayer.getInstance().onPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解绑事件
        this.mBinding.mainPager.clearOnPageChangeListeners()
    }

    override fun onBackPressed() {
        // 侧滑抽屉
        if (mBinding.drawerLayout.isDrawerOpen(mBinding.mainTrainee)) {
            this.mBinding.drawerLayout.closeDrawer(mBinding.mainTrainee)
            return
        }
//        // 详情页
//        if (mBinding.mainVideoDetail.onBackPressed()) {
//            return
//        }
        // Super
        super.onBackPressed()
        // 释放播放器
        ListPlayer.getInstance().release()
        // 结束所有Activity
        ActivityManager.getInstance().finishAllActivity()
    }
}

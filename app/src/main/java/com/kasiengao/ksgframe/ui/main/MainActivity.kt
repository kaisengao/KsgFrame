package com.kasiengao.ksgframe.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import com.kaisengao.base.configure.ActivityManager
import com.kaisengao.base.util.DensityUtil
import com.kaisengao.base.util.StatusBarUtil
import com.kaisengao.mvvm.base.activity.BaseVmActivity
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.databinding.ActivityMainBinding
import com.kasiengao.ksgframe.player.ListPlayer
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel


/**
 * @ClassName: MainActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 15:36
 * @Description: 启动页面
 */
class MainActivity : BaseVmActivity<ActivityMainBinding, MainViewModel>() {

    private val mScreenSize: IntArray by lazy {
        DensityUtil.getScreenSize(this)
    }

    private val mStatusBarHeight: Float by lazy {
        StatusBarUtil.getStatusBarHeight(this).toFloat()
    }

    private var mListContainer: PlayerContainerView? = null

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun initVariableId(): Int = BR.viewModel

    override fun initBefore() {
        super.initBefore()
        // 添加状态栏高度
        StatusBarUtil.setStatusBarPadding(this, mBinding.mainAppbar)
        StatusBarUtil.setStatusBarPadding(this, mBinding.mainPlayerDetailBack)
    }

    override fun initWidget() {
        super.initWidget()
        // Init Player
        this.initPlayer()
        // Init DrawerLayout
        this.initDrawer()
        // Init DataObserve
        this.initDataObserve()
        // Init 手势滑动关闭事件
        this.mBinding.mainPlayerDetail.setSlidingListener { closeDetail() }
        // Init FAB事件
        this.mBinding.mainShowHideInfo.setOnClickListener {
            val selected = mBinding.mainShowHideInfo.isSelected
            if (selected) {
                // 关闭信息页
                this.closeInfo()
            } else {
                // 打开信息页
                this.openInfo()
            }
            mBinding.mainShowHideInfo.isSelected = !selected
        }
    }

    override fun initData() {
        super.initData()
        // 请求 视频列表
        this.mViewModel.requestVideos()
    }

    /**
     * Init Player
     */
    private fun initPlayer() {
        // 事件
        ListPlayer.getInstance().setListPlayerListener { listContainer ->
            this.mListContainer = listContainer
            // 毛玻璃背景
            listContainer.coverImage.let {
                mBinding.mainPlayerBlur.setImageBlur(it)
            }
            // 初始信息页的高度
            mBinding.mainPlayerInfo.setHeight(0f)
            // 显示详情页
            mBinding.mainPlayerDetail.visibility = View.VISIBLE
            // 初始FAB选择状态
            this.mBinding.mainShowHideInfo.isSelected = false
            // 打开详情页
            this.openDetail(listContainer)
            // 绑定视频容器
            ListPlayer.getInstance().bindNewContainer(mBinding.mainPlayerContainer)
        }
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
     * 打开详情页
     *
     * @param listContainer 列表中的容器
     */
    private fun openDetail(listContainer: PlayerContainerView) {
        // 获取当前容器的坐标
        val location = IntArray(2)
        listContainer.getLocationOnScreen(location)
        // 计算比例16/9
        val containerHeight = (mScreenSize[0] * 9 / 16f)
        // 总动画集合
        val detailAnimator = AnimatorSet().also {
            it
                .play(
                    // 视频容器平移居中
                    ObjectAnimator.ofFloat(
                        mBinding.mainPlayerContainerCard, View.Y, location[1].toFloat(),
                        mScreenSize[1] / 2f - containerHeight / 2
                    )
                )
                .with(
                    // 设置视频容器 宽
                    ObjectAnimator.ofFloat(
                        mBinding.mainPlayerContainer, "Width", listContainer.width.toFloat(),
                        mScreenSize[0].toFloat()
                    )
                )
                .with(
                    // 设置视频容器 高
                    ObjectAnimator.ofFloat(
                        mBinding.mainPlayerContainer, "Height",
                        listContainer.height.toFloat(), containerHeight
                    ),
                )
                .with(
                    // 毛玻璃背景 高
                    ObjectAnimator.ofFloat(
                        mBinding.mainPlayerBlur, "Height", 0f,
                        mScreenSize[1].toFloat()
                    )
                )
            it.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    // 显示back
                    mBinding.mainPlayerDetailBack.visibility = View.VISIBLE
                    // 显示FAB按钮
                    mBinding.mainShowHideInfo.visibility = View.VISIBLE
                    // 视频容器 宽度设置MATCH_PARENT
                    mBinding.mainPlayerContainer.setWidth(ViewGroup.LayoutParams.MATCH_PARENT.toFloat())
                    // 毛玻璃背景 高度设置 MATCH_PARENT
                    mBinding.mainPlayerBlur.setHeight(ViewGroup.LayoutParams.MATCH_PARENT.toFloat())
                }
            })
            it.interpolator = LinearInterpolator()
            it.duration = 300
        }
        detailAnimator.start()
    }

    /**
     * 关闭详情页
     */
    private fun closeDetail() {
        this.mListContainer.let {
            // 隐藏详情页
            mBinding.mainPlayerDetail.visibility = View.GONE
            // 隐藏back
            mBinding.mainPlayerDetailBack.visibility = View.GONE
            // 隐藏FAB按钮
            mBinding.mainShowHideInfo.visibility = View.INVISIBLE
            // 初始毛玻璃背景
            mBinding.mainPlayerBlur.setImageDrawable(null)
            // 切回至列表容器
            ListPlayer.getInstance().bindNewContainer(mListContainer)
        }
    }

    /**
     * 打开信息页
     */
    private fun openInfo() {
        val infoAnimator = AnimatorSet().also {
            it.playTogether(
                // 视频容器平移顶部
                ObjectAnimator.ofFloat(
                    mBinding.mainPlayerContainerCard, View.Y,
                    mBinding.mainPlayerContainerCard.height.toFloat(), mStatusBarHeight
                ),
                // 详情页高度更新
                ObjectAnimator.ofFloat(
                    mBinding.mainPlayerInfo, "Height", 0f,
                    mBinding.mainPlayerDetail.height - mStatusBarHeight - mBinding.mainPlayerContainer.height - 20
                )
            )
            it.interpolator = LinearInterpolator()
            it.duration = 100
        }
        infoAnimator.start()
    }

    /**
     * 关闭信息页
     */
    private fun closeInfo() {
        val infoAnimator = AnimatorSet().also {
            it.playTogether(
                // 视频容器平移居中
                ObjectAnimator.ofFloat(
                    mBinding.mainPlayerContainerCard, View.Y, mBinding.mainPlayerContainerCard.y,
                    mScreenSize[1] / 2f - mBinding.mainPlayerContainer.height / 2f
                ),
                // 详情页高度更新
                ObjectAnimator.ofFloat(
                    mBinding.mainPlayerInfo, "Height", mBinding.mainPlayerInfo.height.toFloat(), 0f
                )
            )
            it.interpolator = LinearInterpolator()
            it.duration = 100
        }
        infoAnimator.start()
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
        // 验证当前是否在详情页
        if (mBinding.mainPlayerDetail.visibility == View.VISIBLE) {
            // 关闭详情页
            this.closeDetail()
            // Return
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

package com.kaisengao.ksgframe.ui.main.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Rect
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.contains
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.common.util.SystemUiUtil
import com.kaisengao.ksgframe.common.widget.PlayerContainerView
import com.kaisengao.ksgframe.constant.CoverConstant
import com.kaisengao.ksgframe.player.cover.*
import com.kaisengao.ksgframe.ui.main.adapter.XBBAdapter
import com.kaisengao.ksgframe.ui.main.bean.VideoBean
import com.kaisengao.ksgframe.ui.main.viewmodel.MainViewModel
import com.ksg.ksgplayer.KsgSinglePlayer
import com.ksg.ksgplayer.config.PlayerConfig
import com.ksg.ksgplayer.data.DataSource
import com.ksg.ksgplayer.listener.OnCoverEventListener
import com.ksg.ksgplayer.player.IPlayer
import com.kuaishou.akdanmaku.data.DanmakuItemData


/**
 * @ClassName: XBBVideosView
 * @Author: KaiSenGao
 * @CreateDate: 2022/5/8 18:17
 * @Description: 象拔蚌 视频列表
 */
class XBBVideosView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private lateinit var mActivity: Activity

    private var mPosition: Int = 0

    private var mFullscreen = false

    private var mItemContainer: PlayerContainerView? = null

    private val mSignalPlayer: KsgSinglePlayer by lazy { KsgSinglePlayer.getInstance() }

    private val mDetailView: XBBDetailView by lazy { XBBDetailView(context) }

    private val mFullScreenView: PlayerContainerView by lazy {
        PlayerContainerView(context).also {
            it.isIntercept = true
        }
    }

    private val mSmallController: SmallControllerCover by lazy { SmallControllerCover(context) }

    private val mLandController: LandControllerCover by lazy { LandControllerCover(context) }

    private val mUploader: UploaderCover by lazy { UploaderCover(context) }

    private val mDanmaku: DanmakuCover by lazy { DanmakuCover(context) }

    private val mAdapter: XBBAdapter by lazy { XBBAdapter() }

    private lateinit var mViewModel: MainViewModel

    init {
        // Init Adapter
        this.initAdapter()
    }

    /**
     * Init
     */
    fun init(activity: Activity) {
        this.mActivity = activity
        this.mDetailView.init(activity, this)
    }

    /**
     * Init Adapter
     */
    private fun initAdapter() {
        // Recycler
        this.layoutManager = LinearLayoutManager(context)
        this.adapter = mAdapter
        // EmptyView
        this.mAdapter.setEmptyView(R.layout.item_xbb_videos_empty)
        // ItemClick
        this.mAdapter.setOnItemClickListener { _, _, position ->
            // 打开详情页
            this.openDetail(position)
        }
        // ChildClick
        this.mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.item_player_container ->
                    // 播放
                    this.onPlay(position, (view as PlayerContainerView))
                R.id.item_xbb_interact_comment -> {
                    // 打开详情页
                    this.openDetail(position)
                }
            }
        }
        // Init
        this.post {
            // 注册滚动事件
            this.addOnScrollListener(mScrollListener)
        }
    }

    /**
     * Init Player
     */
    fun initPlayer() {
        this.mSignalPlayer.player.reset()
        // 设置 默认渲染器
        if (mSignalPlayer.player.renderer is GLSurfaceView) {
            this.mSignalPlayer.player.rendererType = PlayerConfig.getRendererType()
        }
        // CoverManager
        this.mSignalPlayer.coverManager.let {
            // 移除Cover
            it.removeAllCover { cover ->
                ((cover is LoadingCover) || (cover is GestureCover))
            }
            // Controller
            it.addCover(
                CoverConstant.CoverKey.KEY_SMALL_CONTROLLER, mSmallController
            )
            // Uploader
            it.addCover(
                CoverConstant.CoverKey.KEY_UPLOADER, mUploader
            )
            // 手势提示UI 恢复padding值
            it.valuePool.putObject(CoverConstant.ValueKey.KEY_GESTURE_PADDING_TOP, 0, false)
        }
        // Cover事件
        this.mSignalPlayer.player.setCoverEventListener(mCoverEventListener)
        // 自动播放
        if (mItemContainer != null) {
            this.mItemContainer?.performClick()
        } else {
            this.smoothScrollBy(0, 1)
        }
    }

    /**
     * 播放事件
     */
    private val mCoverEventListener = OnCoverEventListener { eventCode, _ ->
        when (eventCode) {
            CoverConstant.CoverEvent.CODE_REQUEST_BACK ->
                this.onBackPressed()
            CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_ENTER ->
                this.onFullscreen(true)
            CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_EXIT ->
                this.onFullscreen(false)
        }
    }

    /**
     * 绑定 ViewModel
     *
     * @param viewModel VM
     */
    fun bindViewModel(owner: LifecycleOwner, viewModel: MainViewModel) {
        this.mViewModel = viewModel
        // Init DataObserve
        this.initDataObserve(owner)
    }

    /**
     * Init DataObserve
     */
    private fun initDataObserve(owner: LifecycleOwner) {
        // 弹幕数据
        this.mViewModel.mDanmakuData.observe(owner) { data: List<DanmakuItemData> ->
            if (data.isNotEmpty()) {
                this.mDanmaku.updateData(data)
            }
        }
    }

    /**
     * 播放
     *
     * @param position 坐标
     * @param container 视频容器
     */
    private fun onPlay(position: Int, container: PlayerContainerView) {
        // 重置容器
        this.resetContainer()
        // 初始容器
        container.onHideView()
        container.isIntercept = true
        // 数据源
        val videoBean = mAdapter.data[position]
        // 记录 坐标
        this.mPosition = position
        // 记录 视图容器
        this.mItemContainer = container
        // 绑定 视图容器
        this.mSignalPlayer.bindContainer(container, true)
        // 开始
        this.onStart(videoBean)
    }

    /**
     * 获取当前视频容器
     */
    private fun getCurrContainer(): PlayerContainerView {
        return if (mDetailView.isOpenDetail) {
            mDetailView.mItemView.getPlayerContainer()
        } else {
            mItemContainer!!
        }
    }

    /**
     * 重置容器
     */
    fun resetContainer() {
        this.mItemContainer?.onShowView()
        this.mItemContainer?.isIntercept = false
        this.mItemContainer = null
    }

    /**
     * 打开详情页
     */
    private fun openDetail(position: Int) {
        val playerContainer = getItemPlayContainer(position)
        // 列表截取
        val subList = mAdapter.data.subList(position, mAdapter.data.size)
        // 验证与当前播放的是否是同一个
        if (mPosition == position && mItemContainer != null && mItemContainer === playerContainer) {
            // 打开详情页
            this.mDetailView.openDetail(position, subList, playerContainer)
            return
        }
        // 播放
        this.onPlay(position, playerContainer)
        // 打开详情页
        this.mDetailView.openDetail(position, subList, playerContainer)
    }


    /**
     * 滚动事件
     */
    private val mScrollListener: OnScrollListener = object : OnScrollListener() {

        private var visibleCount = 0

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // 滚动状态
            if (newState == SCROLL_STATE_IDLE) {
                // 停止滚动
                onScrollIdle()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // 计算当前正在播放的组件是否已经滚出了屏幕
            mItemContainer?.let {
                if (!it.getLocalVisibleRect(Rect())) {
                    // 设置 拦截状态
                    it.isIntercept = false
                    // 解绑 视图容器
                    mSignalPlayer.unbindContainer()
                    // 暂停播放
                    mSignalPlayer.onPause()
                }
            }
        }

        /**
         * 停止滚动
         */
        private fun onScrollIdle() {
            // 计算当前可视Item数量
            val layoutManager = layoutManager
            if (layoutManager is LinearLayoutManager) {
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                visibleCount = lastVisibleItem - firstVisibleItem
            }
            // 计算当前可播放的Item
            for (i in 0 until visibleCount) {
                val itemView = getChildAt(i)
                val container =
                    itemView?.findViewById<PlayerContainerView>(R.id.item_player_container)
                if (container != null) {
                    // 获取PlayContainer的可视区域
                    val rect = Rect()
                    container.getLocalVisibleRect(rect)
                    // 验证 PlayContainer是否完全可视 (Rect.top 恒为 0)
                    if (rect.top != 0) {
                        continue
                    }
                    // 获取当前Item的Position
                    val position = getChildLayoutPosition(itemView)
                    // 验证当前可视的Item是否是正在播放的Item
                    if (position == mPosition
                        && mItemContainer != null
                        && mItemContainer!! === container
                    ) {
                        when (mSignalPlayer.player.state) {
                            IPlayer.STATE_PAUSE -> {
                                // 设置 拦截状态
                                mItemContainer?.let {
                                    // 设置 拦截状态
                                    it.isIntercept = true
                                    // 绑定 视图容器
                                    mSignalPlayer.bindContainer(it, false)
                                }
                                // 恢复播放
                                mSignalPlayer.onResume()
                                break
                            }
                            IPlayer.STATE_PREPARED,
                            IPlayer.STATE_START,
                            IPlayer.STATE_COMPLETE -> {
                                break
                            }
                        }
                    }
                    // 播放
                    onPlay(position, container)
                    // Break
                    break
                }
            }
        }
    }

    /**
     * 指定Item滑动到顶部
     */
    fun scrollToPositionWithOffset(position: Int) {
        (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
    }

    /**
     * 获取Item中的视频容器View
     */
    fun getItemPlayContainer(position: Int): PlayerContainerView {
        return (findViewHolderForLayoutPosition(position) as XBBAdapter.ViewHolder).mPlayContainer
    }

    /**
     * 开始
     */
    fun onStart(videoBean: VideoBean) {
        // 播放
        this.mSignalPlayer.onStart(DataSource(videoBean.videoUrl))
        // 设置 循环播放
        this.mSignalPlayer.setLooping(false)
        // 配置UP主信息
        this.mSignalPlayer.coverManager.valuePool
            .putObject(CoverConstant.ValueKey.KEY_UPLOADER_DATA, videoBean, false)
    }

    /**
     * 设置 数据源
     */
    fun setData(videos: List<VideoBean>) {
        // Adapter
        this.mAdapter.setList(videos)
        // 自动播放
        this.post {
            this.mAdapter.getViewByPosition(0, R.id.item_player_container)?.performClick()
        }
    }

    /**
     * 更新 参数
     */
    fun renewParam(position: Int, container: PlayerContainerView) {
        this.mPosition = position
        this.mItemContainer = container
    }

    /**
     * 全屏
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private fun onFullscreen(fullscreen: Boolean) {
        this.mFullscreen = fullscreen
        // 横竖屏
        if (fullscreen) {
            // 隐藏系统Ui
            SystemUiUtil.hideVideoSystemUI(context)
            // 请求弹幕数据
            this.mViewModel.requestDanmakuData()
            // 全屏容器添加到View层级下
            this.mActivity.findViewById<ViewGroup>(android.R.id.content)?.let {
                if (!it.contains(this)) {
                    it.addView(mFullScreenView)
                }
            }
            // 设置横屏
            this.mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            // 恢复系统Ui
            SystemUiUtil.recoverySystemUI(context)
            // 全屏容器从View层级中移除
            this.mActivity.findViewById<ViewGroup>(android.R.id.content)
                ?.removeView(mFullScreenView)
            // 设置竖屏
            this.mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    /**
     * 配置已更改
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        // 横竖屏配置
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 设置Cover
            val coverManager = mSignalPlayer.coverManager
            coverManager.removeCover(CoverConstant.CoverKey.KEY_SMALL_CONTROLLER)
            coverManager.addCover(CoverConstant.CoverKey.KEY_DANMAKU, mDanmaku)
            coverManager.addCover(CoverConstant.CoverKey.KEY_LAND_CONTROLLER, mLandController)
            // 绑定全屏容器
            this.mSignalPlayer.bindContainer(mFullScreenView, false)
        } else {
            // 设置Cover
            val coverManager = mSignalPlayer.coverManager
            coverManager.removeCover(CoverConstant.CoverKey.KEY_DANMAKU)
            coverManager.removeCover(CoverConstant.CoverKey.KEY_LAND_CONTROLLER)
            coverManager.addCover(CoverConstant.CoverKey.KEY_SMALL_CONTROLLER, mSmallController)
            // 恢复视频容器
            this.mSignalPlayer.bindContainer(getCurrContainer(), false)
        }
    }

    /**
     * onResume
     */
    fun onResume() {
        this.mSignalPlayer.onResume()
    }

    /**
     * onPause
     */
    fun onPause() {
        this.mSignalPlayer.onPause()
    }

    /**
     * Detached
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 解绑滚动事件
        this.clearOnScrollListeners()
    }

    /**
     * onBackPressed
     */
    fun onBackPressed(): Boolean {
        if (mFullscreen) {
            this.onFullscreen(false)
            return true
        }
        return mDetailView.onBackPressed()
    }
}
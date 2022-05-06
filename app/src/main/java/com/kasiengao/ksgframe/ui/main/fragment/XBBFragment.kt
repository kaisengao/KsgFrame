package com.kasiengao.ksgframe.ui.main.fragment

import android.graphics.Rect
import android.opengl.GLSurfaceView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaisengao.mvvm.base.fragment.BaseVmFragment
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.TextUtil
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.databinding.FragmentXbbBinding
import com.kasiengao.ksgframe.ui.main.MainActivity
import com.kasiengao.ksgframe.ui.main.adapter.XBBAdapter
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel
import com.ksg.ksgplayer.KsgSinglePlayer
import com.ksg.ksgplayer.config.PlayerConfig
import com.ksg.ksgplayer.data.DataSource
import com.ksg.ksgplayer.listener.OnPlayerListener
import com.ksg.ksgplayer.player.IPlayer

/**
 * @ClassName: XBBFragment
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/26 21:30
 * @Description: 象拔蚌
 */
class XBBFragment : BaseVmFragment<FragmentXbbBinding, MainViewModel>() {

    companion object {
        const val CURR_POSITION: Int = 2010
        const val CURR_CONTAINER: Int = 2020
    }

    private var mHideContainer = false

    private val mSignalPlayer: KsgSinglePlayer by lazy { KsgSinglePlayer.getInstance() }

    private lateinit var mAdapter: XBBAdapter

    override fun getContentLayoutId(): Int = R.layout.fragment_xbb

    override fun initVariableId(): Int = BR.viewModel

    override fun getTitle(): String = TextUtil.getString(R.string.XBB_title)

    override fun initWidget() {
        super.initWidget()
        // 设置PaddingTop为Toolbar的高度、使得布局下移不会被Toolbar覆盖
        this.mBinding.root.post {
            (activity as MainActivity).getToolbarHeight().let {
                this.mBinding.root.setPadding(0, it, 0, 0)
            }
        }
        // Refresh
        this.mBinding.xbbRefresh.setOnRefreshListener {
            this.mViewModel.requestVideos()
        }
        // Init Adapter
        this.initAdapter()
        // Init ViewDetail
        this.initViewDetail()
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
     * Init Player
     */
    private fun initPlayer() {
        this.mSignalPlayer.player.reset()
        // 设置 默认渲染器
        if (mSignalPlayer.player.renderer is GLSurfaceView) {
//            this.mSignalPlayer.player.rendererType = PlayerConfig.getRendererType()
            this.mSignalPlayer.player.setRenderer(PlayerConfig.getRendererType())
        }
        // 播放事件
        this.mSignalPlayer.player.setPlayerListener(mPlayerListener)
        // 自动播放
        this.mSignalPlayer.getVariable(CURR_POSITION, -1).let {
            if (it == -1) {
                this.mBinding.xbbVideos.smoothScrollBy(0, 1)
            } else {
                this.mAdapter.getViewByPosition(
                    it, R.id.item_player_container
                )?.performClick()
            }
        }
    }

    /**
     * Init Adapter
     */
    private fun initAdapter() {
        // Adapter
        this.mAdapter = XBBAdapter()
        // Recycler
        this.mBinding.xbbVideos.layoutManager = LinearLayoutManager(context)
        this.mBinding.xbbVideos.adapter = mAdapter
        // EmptyView
        this.mAdapter.setEmptyView(R.layout.item_main_video_empty)
        // ChildClick
        this.mAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.item_player_container -> onPlay(position, (view as PlayerContainerView))
            }
        }
        // 注册滚动事件
        this.mBinding.xbbVideos.addOnScrollListener(mScrollListener)
    }

    /**
     * Init ViewDetail
     */
    private fun initViewDetail() {
//        this.mBinding.xbbVideoDetail.bindViewModel(this, mViewModel)
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
                // 自动播放
                this.mBinding.xbbVideos.post {
                    this.mAdapter.getViewByPosition(0, R.id.item_player_container)?.performClick()
                }
            }
        }
    }

    /**
     * 滚动事件
     */
    private val mScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {

            private var visibleCount = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                // 验证是否可操作
//                if (!ListPlayer.getInstance().isOperable) {
//                    return
//                }
                // 滚动状态
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 停止滚动
                    onScrollIdle()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                // 验证是否可操作
//                if (!ListPlayer.getInstance().isOperable) {
//                    return
//                }
                // 计算当前正在播放的组件是否已经滚出了屏幕
                mSignalPlayer.getVariable<PlayerContainerView>(CURR_CONTAINER)?.let {
                    if (!it.getLocalVisibleRect(Rect())) {
                        mHideContainer = true
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
                val layoutManager = mBinding.xbbVideos.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    visibleCount = lastVisibleItem - firstVisibleItem
                }
                // 计算当前可播放的Item
                for (i in 0 until visibleCount) {
                    val itemView = mBinding.xbbVideos.getChildAt(i)
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
                        val position: Int =
                            mBinding.xbbVideos.getChildLayoutPosition(itemView)
                        // 验证当前可视的Item是否是正在播放的Item
                        val currContainer = mSignalPlayer.getVariable<PlayerContainerView>(
                            CURR_CONTAINER
                        )
                        if (position == mSignalPlayer.getVariable(CURR_POSITION)
                            && currContainer != null
                            && currContainer.equals(container)
                        ) {
                            when (mSignalPlayer.player.state) {
                                IPlayer.STATE_PAUSE -> {
                                    mSignalPlayer.onResume()
                                    break
                                }
                                IPlayer.STATE_PREPARED,
                                IPlayer.STATE_START -> {
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
     * 播放
     *
     * @param position 坐标
     * @param container 视频容器
     */
    private fun onPlay(position: Int, container: PlayerContainerView) {
        // 设置状态位
        container.setPlayerState(IPlayer.STATE_PREPARED)
        // 重置 缓存
        this.setPlayerState(false, IPlayer.STATE_IDLE)?.let {
            // 移除缓存
            this.mSignalPlayer.removeVariable(CURR_CONTAINER)
        }
        // 解绑 视图容器ß
        this.mSignalPlayer.unbindContainer()
        // 记录 坐标
        this.mSignalPlayer.putVariable(CURR_POSITION, position)
        // 记录 视图容器
        this.mSignalPlayer.putVariable(CURR_CONTAINER, container)
        // 播放
        this.mSignalPlayer.onPlay(DataSource(mAdapter.data[position].videoUrl))
        // 设置 循环播放
        this.mSignalPlayer.setLooping(true)
    }

    /**
     * 修改状态位
     */
    private fun setPlayerState(intercept: Boolean, playerState: Int): PlayerContainerView? {
        this.mSignalPlayer.getVariable<PlayerContainerView>(CURR_CONTAINER)?.let {
            // 拦截状态
            it.isIntercept = intercept
            // 播放状态
            it.setPlayerState(playerState)
            return it
        }
        return null
    }

    /**
     * 播放事件
     */
    private val mPlayerListener = OnPlayerListener { eventCode, _ ->
        when (eventCode) {
            OnPlayerListener.PLAYER_EVENT_ON_PREPARED -> {
                // 修改状态位
                this.setPlayerState(true, IPlayer.STATE_START)?.let {
                    // 绑定 视图容器
                    this.mSignalPlayer.bindContainer(it, false)
                }
            }
            OnPlayerListener.PLAYER_EVENT_ON_RESUME -> {
                // 修改状态位
                this.setPlayerState(true, IPlayer.STATE_START)?.let {
                    // 绑定 视图容器
                    if (mHideContainer) {
                        this.mSignalPlayer.bindContainer(it, false)
                    }
                    this.mHideContainer = false
                }
            }
            OnPlayerListener.PLAYER_EVENT_ON_PAUSE -> {
                // 修改状态位
                this.setPlayerState(false, IPlayer.STATE_PAUSE)
                // 解绑 视图容器
                if (mHideContainer) {
                    this.mSignalPlayer.unbindContainer()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Init Player
        this.initPlayer()
    }

    override fun onPause() {
        super.onPause()
        // 暂停播放
        this.mSignalPlayer.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解绑滚动事件
        this.mBinding.xbbVideos.removeOnScrollListener(mScrollListener)
    }
}
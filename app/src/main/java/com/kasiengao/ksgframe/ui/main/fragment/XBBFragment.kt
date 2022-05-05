package com.kasiengao.ksgframe.ui.main.fragment

import android.graphics.Rect
import android.os.Bundle
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
import com.kasiengao.ksgframe.ui.main.player.ListPlayer
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel
import com.ksg.ksgplayer.KsgSinglePlayer
import com.ksg.ksgplayer.data.DataSource
import com.ksg.ksgplayer.listener.OnPlayerListener
import com.ksg.ksgplayer.player.IPlayer
import com.ksg.ksgplayer.renderer.RendererType

/**
 * @ClassName: XBBFragment
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/26 21:30
 * @Description: 象拔蚌
 */
class XBBFragment : BaseVmFragment<FragmentXbbBinding, MainViewModel>() {

    private val mPlayer: KsgSinglePlayer by lazy { KsgSinglePlayer.getInstance() }

    private var mLastPosition: Int = -1

    private var mCurrContainer: PlayerContainerView? = null

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
                val currContainer = ListPlayer.getInstance().currContainer
                if (currContainer != null && !currContainer.getLocalVisibleRect(Rect())) {
                    // 暂停播放
                    ListPlayer.getInstance().onPause()
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
                        val layoutPosition: Int =
                            mBinding.xbbVideos.getChildLayoutPosition(itemView)
                        // 验证当前可视的Item是否是正在播放的Item
                        val currContainer = mPlayer.currContainer
                        if (layoutPosition == mPlayer.position
                            && currContainer != null
                            && currContainer === container
                        ) {
                            // 判断状态 如果是暂停状态则继续播放
                            if (mPlayer.player.state == IPlayer.STATE_PAUSE) {
                                // 继续播放
                                mPlayer.onResume()
                                // Break
                                break
                            }
                            // 判断状态 如果是正在播放则跳出方法
                            if (mPlayer.player.state == IPlayer.STATE_START) {
                                // Break
                                break
                            }
                        }
                        // play
                        onPlay(layoutPosition, container)
                        // Break
                        break
                    }
                }
            }
        }

    /**
     * 重置当前容器
     */
    private fun resetCurrContainer() {
        this.mCurrContainer?.let {
            it.isIntercept = false
            it.setPlayerState(IPlayer.STATE_IDLE)
        }
        this.mCurrContainer = null
    }

    /**
     * 播放
     *
     * @param position 坐标
     * @param container 视频容器
     */
    private fun onPlay(position: Int, container: PlayerContainerView) {
        // 重置当前容器
        this.resetCurrContainer()
        // 缓存当前容器
        this.mCurrContainer = container
        // 记录 坐标
        this.mPlayer.position = position
        // 设置状态位
        container.setPlayerState(IPlayer.STATE_INIT)
        // 播放
        if (!mPlayer.isOverlap) {
            container.setPlayerState(IPlayer.STATE_PREPARED)
            // 播放
            this.mPlayer.onPlay(DataSource(mAdapter.data[position].videoUrl))
        }
        this.mLastPosition = position
    }

    override fun onResume() {
        super.onResume()
        // 播放事件
        this.mPlayer.player.setPlayerListener { eventCode: Int, bundle: Bundle? ->
            if (eventCode == OnPlayerListener.PLAYER_EVENT_ON_PREPARED) {
                if (mCurrContainer != null) {
                    // 设置状态
                    mCurrContainer!!.isIntercept = true
                    mCurrContainer!!.setPlayerState(IPlayer.STATE_START)
                    // 绑定容器
                    this.mPlayer.bindContainer(mCurrContainer, false)
                }
            }
        }
        // 切换至普通渲染器
        this.mPlayer.player.setRendererType(RendererType.TEXTURE)
        // 自动播放
        this.mAdapter.getViewByPosition(mLastPosition, R.id.item_player_container)?.performClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解绑滚动事件
        this.mBinding.xbbVideos.removeOnScrollListener(mScrollListener)
    }
}
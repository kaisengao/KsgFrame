package com.kasiengao.ksgframe.ui.main.widget

import android.content.Context
import android.graphics.Rect
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.constant.CoverConstant
import com.kasiengao.ksgframe.player.cover.GestureCover
import com.kasiengao.ksgframe.player.cover.LoadingCover
import com.kasiengao.ksgframe.player.cover.SmallControllerCover
import com.kasiengao.ksgframe.player.cover.UploaderCover
import com.kasiengao.ksgframe.ui.main.adapter.XBBAdapter
import com.kasiengao.ksgframe.ui.main.bean.VideoBean
import com.ksg.ksgplayer.KsgSinglePlayer
import com.ksg.ksgplayer.config.PlayerConfig
import com.ksg.ksgplayer.data.DataSource
import com.ksg.ksgplayer.listener.OnCoverEventListener
import com.ksg.ksgplayer.player.IPlayer


/**
 * @ClassName: XBBVideosView
 * @Author: KaiSenGao
 * @CreateDate: 2022/5/8 18:17
 * @Description: 象拔蚌 视频列表
 */
class XBBVideosView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private var mPosition: Int = 0

    private var mContainer: PlayerContainerView? = null

    private val mSignalPlayer: KsgSinglePlayer by lazy { KsgSinglePlayer.getInstance() }

    private val mController: SmallControllerCover by lazy { SmallControllerCover(context) }

    private val mUploader: UploaderCover by lazy { UploaderCover(context) }

    private val mAdapter: XBBAdapter by lazy { XBBAdapter() }

    var mVideoListener: OnVideoListener? = null

    init {
        // Init Adapter
        this.initAdapter()
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
                CoverConstant.CoverKey.KEY_SMALL_CONTROLLER, mController
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
        if (mContainer != null) {
            this.mContainer?.performClick()
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
                this.mVideoListener?.onBack()
            CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_ENTER ->
                this.onFullscreen(true)
            CoverConstant.CoverEvent.CODE_REQUEST_FULLSCREEN_EXIT ->
                this.onFullscreen(false)
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
        // 设置 拦截状态
        container.isIntercept = true
        // 数据源
        val videoBean = mAdapter.data[position]
        // 记录 坐标
        this.mPosition = position
        // 记录 视图容器
        this.mContainer = container
        // 绑定 视图容器
        this.mSignalPlayer.bindContainer(container, true)
        // 播放
        this.mSignalPlayer.onPlay(DataSource(videoBean.videoUrl))
        // 设置 循环播放
        this.mSignalPlayer.setLooping(false)
        // 配置UP主信息
        this.mSignalPlayer.coverManager.valuePool
            .putObject(CoverConstant.ValueKey.KEY_UPLOADER_DATA, videoBean, false)
    }

    /**
     * 重置容器
     */
    private fun resetContainer() {
        this.mContainer?.isIntercept = false
        this.mContainer = null
    }

    /**
     * 全屏
     */
    private fun onFullscreen(fullscreen: Boolean) {
        this.mVideoListener?.onFullscreen(fullscreen, mPosition, mContainer!!)
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

    private fun openDetail(position: Int) {
        (findViewHolderForLayoutPosition(position) as XBBAdapter.ViewHolder).let {
            // 验证与当前播放的是否是同一个
            if (mPosition == position && mContainer != null && mContainer === it.mPlayContainer) {
                // 打开详情页
                this.mVideoListener?.openDetail(position, it.mPlayContainer)
                return
            }
            // 播放
            this.onPlay(position, it.mPlayContainer)
            // 打开详情页
            this.mVideoListener?.openDetail(position, it.mPlayContainer)
        }
    }

    /**
     * 滚动事件
     */
    private val mScrollListener: OnScrollListener = object : OnScrollListener() {

        private var visibleCount = 0

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
//                // 验证是否可操作
//                if (!ListPlayer.getInstance().isOperable) {
//                    return
//                }
            // 滚动状态
            if (newState == SCROLL_STATE_IDLE) {
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
            mContainer?.let {
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
                        && mContainer != null
                        && mContainer!! === container
                    ) {
                        when (mSignalPlayer.player.state) {
                            IPlayer.STATE_PAUSE -> {
                                // 设置 拦截状态
                                mContainer?.let {
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
     * 事件
     */
    interface OnVideoListener {

        /**
         * Back
         */
        fun onBack()

        /**
         * 全屏
         */
        fun onFullscreen(fullscreen: Boolean, position: Int, container: PlayerContainerView)

        /**
         * 打开详情页
         *
         * @param position      列表位置
         * @param listContainer 列表容器
         */
        fun openDetail(position: Int, listContainer: PlayerContainerView?)

        /**
         * 关闭详情页
         */
        fun closeDetail()
    }
}
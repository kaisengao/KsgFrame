package com.kaisengao.ksgframe.ui.main.fragment

import android.opengl.GLSurfaceView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kaisengao.base.util.TextUtil
import com.kaisengao.ksgframe.BR
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.common.widget.PlayerContainerView
import com.kaisengao.ksgframe.constant.CoverConstant
import com.kaisengao.ksgframe.databinding.FragmentPpxBinding
import com.kaisengao.ksgframe.player.cover.GestureCover
import com.kaisengao.ksgframe.player.cover.LoadingCover
import com.kaisengao.ksgframe.player.cover.PPXControllerCover
import com.kaisengao.ksgframe.ui.main.MainActivity
import com.kaisengao.ksgframe.ui.main.adapter.PPXAdapter
import com.kaisengao.ksgframe.ui.main.viewmodel.MainViewModel
import com.kaisengao.mvvm.base.fragment.BaseVmFragment
import com.ksg.ksgplayer.KsgSinglePlayer
import com.ksg.ksgplayer.data.DataSource
import com.ksg.ksgplayer.renderer.glrender.PIPGLViewRender
import com.ksg.ksgplayer.renderer.view.KsgGLSurfaceView

/**
 * @ClassName: PPXFragment
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/26 23:03
 * @Description: 皮皮虾
 */
class PPXFragment : BaseVmFragment<FragmentPpxBinding, MainViewModel>() {

    companion object {
        const val CURR_POSITION: Int = 1010
        const val CURR_CONTAINER: Int = 1020
    }

    private var mHidden = true

    private val mSignalPlayer: KsgSinglePlayer by lazy { KsgSinglePlayer.getInstance() }

    private val mController: PPXControllerCover by lazy { PPXControllerCover(context) }

    private lateinit var mAdapter: PPXAdapter

    override fun getContentLayoutId(): Int = R.layout.fragment_ppx

    override fun initVariableId(): Int = BR.viewModel

    override fun getTitle(): String = TextUtil.getString(R.string.PPX_title)

    override fun initWidget() {
        super.initWidget()
        // Refresh
        this.mBinding.ppxRefresh.setOnRefreshListener {
            this.mViewModel.requestVideos()
        }
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

    override fun onHiddenChanged(hidden: Boolean) {
        this.mHidden = hidden
        // Init Player
        if (!mHidden) {
            this.initPlayer()
        }
    }

    /**
     * Init Player
     */
    private fun initPlayer() {
        this.mSignalPlayer.player.reset()
        // 设置 GL渲染器
        if (mSignalPlayer.player.renderer !is GLSurfaceView) {
            this.mSignalPlayer.player.setGLViewRender(
                PIPGLViewRender(),
                KsgGLSurfaceView.MODE_RENDER_SIZE
            )
        }
        // CoverManager
        this.mSignalPlayer.coverManager.let {
            // 移除Cover
            it.removeAllCover { cover ->
                ((cover is LoadingCover) || (cover is GestureCover))
            }
            // Controller
            it.addCover(
                CoverConstant.CoverKey.KEY_PPX_CONTROLLER, mController
            )
            // 手势提示UI 设置PaddingTop使得布局下移不会被Toolbar覆盖
            val toolbarHeight = (activity as MainActivity).getToolbarHeight()
            it.valuePool.putObject(CoverConstant.ValueKey.KEY_GESTURE_PADDING_TOP, toolbarHeight,false)
        }
        // Cover事件
        this.mSignalPlayer.player.setCoverEventListener(null)
        // 自动播放
        this.onPlay(mBinding.ppxVideos.currentItem)
    }

    /**
     * Init Adapter
     */
    private fun initAdapter() {
        // Adapter
        this.mAdapter = PPXAdapter()
        // Viewpager2
        this.mBinding.ppxVideos.adapter = mAdapter
        // 注册滑动事件
        this.mBinding.ppxVideos.registerOnPageChangeCallback(mPageChangeCallback)
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

    /**
     * 滚动事件
     */
    private val mPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            // 播放
            onPlay(position)
        }
    }

    /**
     * 播放
     *
     * @param position 坐标
     */
    private fun onPlay(position: Int) {
        (mBinding.ppxVideos.getChildAt(0) as RecyclerView).let {
            val itemView = it.layoutManager?.findViewByPosition(position)
            itemView?.findViewById<PlayerContainerView>(R.id.item_player_container)
                ?.let { container: PlayerContainerView ->
                    // 记录 坐标
                    this.mSignalPlayer.putVariable(CURR_POSITION, position)
                    // 记录 视图容器
                    this.mSignalPlayer.putVariable(CURR_CONTAINER, container)
                    // 绑定 视图容器
                    this.mSignalPlayer.bindContainer(container, false)
                    // 播放
                    this.mSignalPlayer.onStart(DataSource(mAdapter.data[position].videoUrl))
                    // 设置 循环播放
                    this.mSignalPlayer.setLooping(true)
                }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mHidden) {
            this.mSignalPlayer.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (!mHidden) {
            this.mSignalPlayer.onPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解绑滑动事件
        this.mBinding.ppxVideos.unregisterOnPageChangeCallback(mPageChangeCallback)
    }
}
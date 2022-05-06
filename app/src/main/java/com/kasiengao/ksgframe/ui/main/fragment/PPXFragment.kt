package com.kasiengao.ksgframe.ui.main.fragment

import android.opengl.GLSurfaceView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kaisengao.mvvm.base.fragment.BaseVmFragment
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.TextUtil
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.databinding.FragmentPpxBinding
import com.kasiengao.ksgframe.ui.main.adapter.PPXAdapter
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel
import com.ksg.ksgplayer.KsgSinglePlayer
import com.ksg.ksgplayer.data.DataSource
import com.ksg.ksgplayer.listener.OnPlayerListener
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

    private val mSignalPlayer: KsgSinglePlayer by lazy { KsgSinglePlayer.getInstance() }

    private lateinit var mAdapter: PPXAdapter

    override fun getContentLayoutId(): Int = R.layout.fragment_ppx

    override fun initVariableId(): Int = BR.viewModel

    override fun getTitle(): String = TextUtil.getString(R.string.PPX_title)

    override fun initWidget() {
        super.initWidget()
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
        // 播放事件
        this.mSignalPlayer.player.setPlayerListener(null)
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
                    this.mSignalPlayer.onPlay(DataSource(mAdapter.data[position].videoUrl))
                    // 设置 循环播放
                    this.mSignalPlayer.setLooping(true)
                }
        }
    }

    /**
     * 播放事件
     */
    private val mPlayerListener = OnPlayerListener { eventCode, _ ->
        when (eventCode) {
            OnPlayerListener.PLAYER_EVENT_ON_PREPARED -> {
            }
            OnPlayerListener.PLAYER_EVENT_ON_RESUME -> {
            }
            OnPlayerListener.PLAYER_EVENT_ON_PAUSE -> {
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
        // 解绑滑动事件
        this.mBinding.ppxVideos.unregisterOnPageChangeCallback(mPageChangeCallback)
    }
}
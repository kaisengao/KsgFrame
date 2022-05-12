package com.kasiengao.ksgframe.ui.main.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.kaisengao.base.util.StatusBarUtil
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.ColorUtil
import com.kasiengao.ksgframe.common.widget.CView
import com.kasiengao.ksgframe.common.widget.PlayerContainerView
import com.kasiengao.ksgframe.constant.CoverConstant
import com.kasiengao.ksgframe.databinding.LayoutXbbDetailVpBinding
import com.kasiengao.ksgframe.ui.main.adapter.XBBAdapter
import com.kasiengao.ksgframe.ui.main.bean.VideoBean
import com.ksg.ksgplayer.KsgSinglePlayer
import com.ksg.ksgplayer.data.DataSource

/**
 * @ClassName: XBBDetailView
 * @Author: KaiSenGao
 * @CreateDate: 2022/5/11 11:38
 * @Description: 视频详情 ViewPager
 */
class XBBDetailView(context: Context) : RelativeLayout(context) {

    companion object {
        const val PAGE_FINISH = 0
    }

    private lateinit var mActivity: Activity

    private var mDownX: Float = 0F
    private var mDownY: Float = 0F

    private var mMoveY: Float = 0F

    private var mListPosition = 0

    private var mCurrentItem = PAGE_FINISH + 1

    private var mSlideOffset: Float = 250F

    private var mFirstLayout = false

    private var isOpenDetail = false

    private val mBgColor: String by lazy {
        Integer
            .toHexString(ContextCompat.getColor(context, R.color.background))
            .removePrefix("ff")
    }

    private val mStatusBarHeight: Int by lazy { StatusBarUtil.getStatusBarHeight(context) }

    private val mSignalPlayer: KsgSinglePlayer by lazy { KsgSinglePlayer.getInstance() }

    private val mBinding: LayoutXbbDetailVpBinding by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.layout_xbb_detail_vp, this, true
        )
    }

    private val mAdapter: Adapter by lazy { Adapter() }

    private lateinit var mItemView: XBBDetailItemView

    private lateinit var mVideosView: XBBVideosView

    private lateinit var mListContainer: PlayerContainerView

    var mVideoListener: XBBVideosView.OnVideoListener? = null

    init {
        this.mBinding.statusBar.setHeight(mStatusBarHeight.toFloat())
        this.mBinding.detailVp.adapter = mAdapter
        // Back
        this.mBinding.back.setOnClickListener { closeDetail() }
    }

    /**
     * Init
     */
    fun init(activity: Activity, videosView: XBBVideosView) {
        this.mActivity = activity
        this.mVideosView = videosView
    }

    /**
     * 打开详情页
     *
     * @param position      列表位置
     * @param data          数据源
     * @param listContainer 列表容器
     */
    fun openDetail(position: Int, data: List<VideoBean>, listContainer: PlayerContainerView) {
        this.mFirstLayout = true
        this.isOpenDetail = true
        this.mListPosition = position
        this.mListContainer = listContainer
        // 添加到View层级下
        this.mActivity.findViewById<ViewGroup>(android.R.id.content)?.let {
            if (!it.contains(this)) {
                it.addView(this)
            }
        }
        // 绑定数据
        this.mAdapter.mData = data.toMutableList()
        // 列表容器坐标
        val location = IntArray(2)
        this.mListContainer.getLocationOnScreen(location)
        // 动画
        ObjectAnimator.ofFloat(
            mBinding.detailVp, Y,
            location[1].toFloat(), mStatusBarHeight.toFloat()
        ).let {
            it.interpolator = LinearInterpolator()
            it.duration = 150
            it.start()
        }
        // Init
        this.post {
            this.mBinding.detailVp.setCurrentItem(mCurrentItem, false)
        }
    }

    /**
     * 关闭详情页
     */
    fun closeDetail() {
        // HideView
        this.onClosedHideView()
        this.mItemView.setBackgroundColor(Color.TRANSPARENT)
        // 列表容器坐标
        val location = IntArray(2)
        this.mListContainer.getLocationOnScreen(location)
        // 动画
        with(AnimatorSet()) {
            val currContainer = mItemView.getPlayerContainer()
            playTogether(
                ObjectAnimator.ofFloat(
                    currContainer, "width",
                    currContainer.width.toFloat(), mListContainer.width.toFloat()
                ),
                ObjectAnimator.ofFloat(currContainer, X, currContainer.x, location[0].toFloat()),
                ObjectAnimator.ofFloat(
                    currContainer, Y, currContainer.y,
                    (location[1] - mStatusBarHeight.toFloat())
                )
            )
            addListener(mCloseDetailAnimatorListener)
            interpolator = LinearInterpolator()
            duration = 150
            start()
        }
    }

    /**
     * 关闭详情页
     */
    private fun finishDetail() {
        this.isOpenDetail = false
        // 从View层级中移除
        this.mActivity.findViewById<ViewGroup>(android.R.id.content)
            ?.removeView(this@XBBDetailView)
        // 初始
        this.mCurrentItem = PAGE_FINISH + 1
        this.mBinding.detailVp.setCurrentItem(0, false)
        // 复位
        val currContainer = mItemView.getPlayerContainer()
        currContainer.x = 0F
        currContainer.y = 0F
        currContainer.setWidth(ViewGroup.LayoutParams.MATCH_PARENT.toFloat())
        // ShowView
        this.onOpenedShowView()
    }

    /**
     * 关闭详情页动画事件
     */
    private val mCloseDetailAnimatorListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            // 更新列表参数
            mVideosView.renewParam(getListRealPosition(), mListContainer)
            // 切回至旧容器 (原列表容器)
            mSignalPlayer.bindContainer(mListContainer, false)
            // Finish
            finishDetail()
        }
    }

    /**
     * 滑动事件
     */
    private val mPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            // 初始
            if (mFirstLayout) {
                bindContainer(position, mListContainer, false)
                mFirstLayout = false
                return
            }
            // 关闭页
            if (position == PAGE_FINISH) {
                // 更新列表参数
                mVideosView.renewParam(getListRealPosition(), mListContainer)
                // 切回至旧容器 (原列表容器)
                mSignalPlayer.bindContainer(mListContainer, false)
                return
            }
            // 同步列表Item的位置
            (mVideosView.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(getListRealPosition(), 0)
        }

        override fun onPageScrollStateChanged(state: Int) {
            when (state) {
                ViewPager.SCROLL_STATE_DRAGGING -> {
                    mBinding.back.visibility = GONE
                }
                ViewPager.SCROLL_STATE_IDLE -> {
                    mBinding.back.visibility = VISIBLE
                    val currentItem = mBinding.detailVp.currentItem
                    // 初始
                    if (mFirstLayout) {
                        return
                    }
                    // 关闭页
                    if (currentItem == PAGE_FINISH) {
                        // Finish
                        finishDetail()
                        return
                    }
                    // Normal
                    if (mCurrentItem != currentItem) {
                        // 数据源
                        val videoBean = mAdapter.mData[currentItem]
                        // 重置容器
                        resetContainer()
                        // 同步列表的容器
                        (mVideosView.findViewHolderForLayoutPosition(getListRealPosition()) as XBBAdapter.ViewHolder).let {
                            mListContainer = it.mPlayContainer
                        }
                        // 绑定 视图容器
                        bindContainer(currentItem, mListContainer, true)
                        // 初始容器
                        mItemView.getPlayerContainer().let {
                            it.onHideView()
                            it.isIntercept = true
                        }
                        mListContainer.onHideView()
                        mListContainer.isIntercept = true
                        // 播放
                        mSignalPlayer.onPlay(DataSource(videoBean.videoUrl))
                        // 设置 循环播放
                        mSignalPlayer.setLooping(false)
                        // 配置UP主信息
                        mSignalPlayer.coverManager.valuePool
                            .putObject(CoverConstant.ValueKey.KEY_UPLOADER_DATA, videoBean, false)

                        mCurrentItem = currentItem
                    }
                }
            }
        }
    }

    /**
     * 获取 列表的真实坐标
     */
    private fun getListRealPosition(): Int {
        val currentItem = mBinding.detailVp.currentItem
        return if (currentItem == PAGE_FINISH) {
            this.mListPosition + currentItem
        } else {
            this.mListPosition + currentItem - 1
        }
    }

    /**
     * 绑定 视图容器
     */
    private fun bindContainer(
        position: Int,
        listContainer: PlayerContainerView,
        updateRenderer: Boolean
    ) {
        this.mItemView = mAdapter.mViews[position]
        this.mItemView.setBackgroundColor(Color.parseColor("#$mBgColor"))
        // 绑定 视图容器
        this.mItemView.getPlayerContainer().let {
            it.isIntercept = listContainer.isIntercept
            it.setHeight(listContainer.height.toFloat())
            this.mSignalPlayer.bindContainer(it, updateRenderer)
        }
        // 通知 隐藏控制器
        this.mSignalPlayer.coverManager.valuePool
            .putObject(CoverConstant.ValueKey.KEY_HIDE_CONTROLLER, null, false)
    }

    /**
     * 重置容器
     */
    private fun resetContainer() {
        this.mListContainer.onShowView()
        this.mListContainer.isIntercept = false
        this.mItemView.onOpenedShowView()
        this.mItemView.getPlayerContainer().let {
            it.onShowView()
            it.isIntercept = false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mItemView.getScrollView().isOnTop) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.mDownX = event.x
                this.mDownY = event.y
            }
            MotionEvent.ACTION_UP -> {
                if (mMoveY >= mSlideOffset) {
                    this.closeDetail()
                } else {
                    this.onSlideReset()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.x - mDownX
                this.mMoveY = (event.y - mDownY)
                this.mItemView.getPlayerContainer().x = moveX
                this.mItemView.getPlayerContainer().y = mMoveY
                if (mMoveY > 0) {
                    this.onClosedHideView()
                } else {
                    this.onOpenedShowView()
                }
                val percent = (mMoveY / (height / 4F)) * 100
                val bgColor = ColorUtil.percentColor((100 - percent), mBgColor)
                this.mItemView.setBackgroundColor(Color.parseColor(bgColor))
            }
        }
        return true
    }

    /**
     * 事件分发
     */
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        var intercept = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.mDownX = event.x
                this.mDownY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if ((event.y - mDownY) > 23) {
                    if (mItemView.getScrollView().isOnTop) {
                        intercept = true
                    }
                }
            }
        }
        return intercept
    }

    /**
     * 滑动复位
     */
    private fun onSlideReset() {
        with(AnimatorSet()) {
            val container = mItemView.getPlayerContainer()
            playTogether(
                ObjectAnimator.ofFloat(container, X, container.x, 0F),
                ObjectAnimator.ofFloat(container, Y, container.y, 0F)
            )
            interpolator = LinearInterpolator()
            duration = 100
            start()
        }
        this.onOpenedShowView()
    }

    /**
     * 显示 View
     *
     * 注：在打开完成后显示所有View
     */
    private fun onOpenedShowView() {
        this.mItemView.onOpenedShowView()
        this.mBinding.back.visibility = VISIBLE
    }

    /**
     * 隐藏 View
     *
     * 注：在关闭过程中需要隐藏除「XBBDetailView」外的所有View
     */
    private fun onClosedHideView() {
        this.mItemView.onClosedHideView()
        this.mBinding.back.visibility = GONE
    }

    /**
     * onAttached
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.mBinding.detailVp.addOnPageChangeListener(mPageChangeListener)
    }

    /**
     * onDetached
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        this.mBinding.detailVp.removeOnPageChangeListener(mPageChangeListener)
    }

    /**
     * onBackPressed
     */
    fun onBackPressed(): Boolean {
        if (isOpenDetail) {
            this.closeDetail()
            return true
        }
        return false
    }

    private inner class Adapter : PagerAdapter() {

        var mData: MutableList<VideoBean> = mutableListOf()
            set(value) {
                field.clear()
                field.add(0, VideoBean("", "", "", "", ""))
                field.addAll(value)
                this.notifyDataSetChanged()
            }

        var mViews: SparseArray<XBBDetailItemView> = SparseArray()

        private val mCloseView: CView by lazy { CView(context) }

        private var mViewCaches: MutableList<XBBDetailItemView> = mutableListOf()

        override fun getCount(): Int = mData.size

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView: View = if (position == PAGE_FINISH) {
                mCloseView
            } else {
                val detailView = if (mViewCaches.isEmpty()) {
                    XBBDetailItemView(container.context)
                } else {
                    this.mViewCaches.removeFirstOrNull()!!
                }
                detailView.init(mData[position])
                mViews.put(position, detailView)
                detailView
            }
            container.addView(itemView)
            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            if (`object` is XBBDetailItemView) {
                this.mViewCaches.add(`object`)
            }
            container.removeView(`object` as View)
        }
    }
}
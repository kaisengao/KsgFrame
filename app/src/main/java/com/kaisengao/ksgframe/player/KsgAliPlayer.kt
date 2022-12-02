package com.kaisengao.ksgframe.player

import android.content.Context
import android.os.Bundle
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import com.aliyun.player.AliListPlayer
import com.aliyun.player.AliPlayer
import com.aliyun.player.AliPlayerFactory
import com.aliyun.player.IPlayer
import com.aliyun.player.bean.InfoCode
import com.aliyun.player.source.UrlSource
import com.aliyun.player.videoview.AliDisplayView
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.constant.CoverConstant
import com.ksg.ksgplayer.data.DataSource
import com.ksg.ksgplayer.event.BundlePool
import com.ksg.ksgplayer.event.EventKey
import com.ksg.ksgplayer.listener.OnErrorListener
import com.ksg.ksgplayer.listener.OnPlayerListener
import com.ksg.ksgplayer.player.BasePlayer

/**
 * @ClassName: KsgAliPlayer
 * @Author: GaoXin
 * @CreateDate: 2022/8/8 18:09
 * @Description: 阿里云解码器
 */
class KsgAliPlayer(context: Context) : BasePlayer(context) {

    private var mStartPos: Long = -1

    private var mCurrPosition: Long = 0L

    private var mDataSource: DataSource? = null

    private var mAliPlayer: AliListPlayer? = null

    private var mVideoView: AliDisplayView? = null

    /**
     * 初始化 播放器
     */
    override fun initPlayer() {
        // 创建播放器
        val aliPlayer: AliListPlayer = AliPlayerFactory.createAliListPlayer(mContext)
        // 埋点日志上报功能默认开启，当traceId设置为DisableAnalytics时，则关闭埋点日志上报。当traceId设置为其他参数时，则开启埋点日志上报。
        // 建议传递traceId，便于跟踪日志。traceId为设备或用户的唯一标识符，通常为imei或idfa。
        aliPlayer.setTraceId("traceId")
        // DisplayView
        aliPlayer.setDisplayView(getVideoView())

        this.mAliPlayer = aliPlayer
    }

    /**
     * 创建 AliDisplayView
     *
     * @return AliDisplayView
     */
    private fun getVideoView(): AliDisplayView? {
        if (mVideoView == null) {
            val inflate = View.inflate(mContext, R.layout.layout_ali_surface, null)
            this.mVideoView = inflate.findViewById(R.id.ali_surface)
            this.mVideoView?.setSurfaceReuse(false)
        }
        return mVideoView
    }

    /**
     * 设置视频播放地址
     *
     * @param dataSource 播放地址
     */
    override fun setDataSource(dataSource: DataSource) {
        this.mDataSource = dataSource
        try {
            if (mAliPlayer == null) {
                this.initPlayer()
            } else {
                this.stop()
                this.reset()
                this.release()
            }
            // 事件监听
            this.mAliPlayer?.setOnStateChangedListener(mStateListener)
            this.mAliPlayer?.setOnPreparedListener(mPreparedListener)
            this.mAliPlayer?.setOnRenderingStartListener(mRenderStartListener)
            this.mAliPlayer?.setOnVideoSizeChangedListener(mSizeChangedListener)
            this.mAliPlayer?.setOnCompletionListener(mCompletionListener)
            this.mAliPlayer?.setOnErrorListener(mErrorListener)
            this.mAliPlayer?.setOnInfoListener(mInfoListener)
            this.mAliPlayer?.setOnLoadingStatusListener(mLoadingStatusListener)
            this.mAliPlayer?.setOnSeekCompleteListener(mSeekCompleteListener)
            // DataSource
            this.mAliPlayer?.setDataSource(UrlSource().apply {
                uri = dataSource.url
            })
            this.mAliPlayer?.isAutoPlay = true
            this.mAliPlayer?.prepare()
            val bundle = BundlePool.obtain()
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource)
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_DATA_SOURCE_SET, bundle)
        } catch (e: Exception) {
            this.sendErrorEvent(OnErrorListener.ERROR_EVENT_DATA_SOURCE, e.message)
        }
    }

    /**
     * 设置渲染器
     *
     * @param surface surface
     */
    override fun setSurface(surface: Surface?) {
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SURFACE_UPDATE, null)
    }

    /**
     * 设置渲染器
     *
     * @param holder surfaceHolder
     */
    override fun setDisplay(holder: SurfaceHolder?) {
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SURFACE_HOLDER_UPDATE, null)
    }

    /**
     * 渲染器 改变事件
     */
    override fun onSurfaceChanged() {

    }

    /**
     * 设置 播放器自定义的视图
     *
     * @return View
     */
    override fun getCustomRenderer(): View? = getVideoView()

    /**
     * 设置音量
     */
    override fun setVolume(left: Float, right: Float) {

    }

    /**
     * 设置是否循环播放
     */
    override fun setLooping(looping: Boolean) {
        this.mAliPlayer?.isLoop = looping
    }

    /**
     * 设置播放速度
     */
    override fun setSpeed(speed: Float) {
        this.mAliPlayer?.speed = speed
    }

    /**
     * 获取播放速度
     */
    override fun getSpeed(): Float = mAliPlayer?.speed ?: 0F

    /**
     * 获取当前缓冲的网速
     */
    override fun getTcpSpeed(): Long = 0L

    /**
     * 获取缓冲进度百分比
     *
     * @return 缓冲进度
     */
    override fun getBufferPercentage(): Int = mBufferPercentage

    /**
     * 获取当前播放的位置
     *
     * @return 播放进度
     */
    override fun getCurrentPosition(): Long = mCurrPosition

    /**
     * 获取视频总时长
     *
     * @return 总时长
     */
    override fun getDuration(): Long = mAliPlayer?.duration ?: 0L

    /**
     * 获取播放状态
     *
     * @return 播放状态 true 播放 反之
     */
    override fun isPlaying(): Boolean = (state == STATE_START)

    /**
     * seekTo
     *
     * @param msc 在指定的位置播放
     */
    override fun seekTo(msc: Long) {
        // 计算百分比
        val percent = msc.toFloat() / duration
        // 计算当前位置
        val currentPosition = (duration * percent).toInt()
        // Seek
        this.mAliPlayer?.seekTo(currentPosition.toLong())
        val bundle = BundlePool.obtain()
        bundle.putInt(EventKey.LONG_DATA, currentPosition)
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SEEK_TO, bundle)
    }

    /**
     * 播放
     */
    override fun start() {
        this.mAliPlayer?.start()
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_START, null)
    }

    /**
     * start
     *
     * @param msc 在指定的位置开始播放
     */
    override fun start(msc: Long) {
        this.mStartPos = msc
        this.start()
    }

    /**
     * 暂停
     */
    override fun pause() {
        val state = state
        if (state != STATE_DESTROY
            && state != STATE_ERROR
            && state != STATE_IDLE
            && state != STATE_INIT
            && state != STATE_PAUSE
            && state != STATE_STOP
            && state != STATE_COMPLETE
        ) {
            this.mAliPlayer?.pause()
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PAUSE, null)
        }
    }

    /**
     * 继续播放
     */
    override fun resume() {
        if (state == STATE_PAUSE) {
            this.mAliPlayer?.start()
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_RESUME, null)
        }
    }

    /**
     * 停止
     */
    override fun stop() {
        val state = state
        if (state == STATE_PREPARED
            || state == STATE_START
            || state == STATE_PAUSE
            || state == STATE_COMPLETE
        ) {
            this.mAliPlayer?.stop()
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_STOP, null)
        }
    }

    /**
     * 重新播放
     *
     * @param msc 在指定的位置开始播放
     */
    override fun replay(msc: Long) {
        this.mDataSource?.let {
            this.setDataSource(it)
            this.start(msc)
        }
    }

    /**
     * 重置播放器
     */
    override fun reset() {
        this.stop()
        this.release()
        this.mAliPlayer?.reset()
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_RESET, null)
    }

    /**
     * 释放播放器
     */
    override fun release() {
        this.mAliPlayer?.setOnStateChangedListener(null)
        this.mAliPlayer?.setOnPreparedListener(null)
        this.mAliPlayer?.setOnRenderingStartListener(null)
        this.mAliPlayer?.setOnVideoSizeChangedListener(null)
        this.mAliPlayer?.setOnCompletionListener(null)
        this.mAliPlayer?.setOnErrorListener(null)
        this.mAliPlayer?.setOnInfoListener(null)
        this.mAliPlayer?.setOnLoadingStatusListener(null)
        this.mAliPlayer?.setOnSeekCompleteListener(null)
    }

    /**
     * 销毁资源
     */
    override fun destroy() {
        this.release()
        this.mAliPlayer?.release()
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_DESTROY, null)
    }

    /**
     * 自定义事件
     *
     * @param code   code
     * @param bundle bundle
     */
    override fun option(code: Int, bundle: Bundle?) {
        when (code) {
            // 自定义事件 阿里播放器 预加载个数
            CoverConstant.OptionEvent.OPTION_ALI_PRELOAD_COUNT -> {
                bundle?.let {
                    this.mAliPlayer?.setPreloadCount(it.getInt(EventKey.INT_DATA, 1))
                }
            }
            // 自定义事件 阿里播放器 预加载地址
            CoverConstant.OptionEvent.OPTION_ALI_PRELOAD_URL -> {
                bundle?.let {
                    val preDataSource: DataSource =
                        it.getSerializable(EventKey.SERIALIZABLE_DATA) as DataSource
                    this.mAliPlayer?.addUrl(preDataSource.url, preDataSource.url)
                }
            }
            // 自定义事件 阿里播放器 播放预加载视频源
            CoverConstant.OptionEvent.OPTION_ALI_PRELOAD_MOVE_TO -> {
                bundle?.let {
                    val preDataSource: DataSource =
                        it.getSerializable(EventKey.SERIALIZABLE_DATA) as DataSource
                    this.mAliPlayer?.moveTo(preDataSource.url)
                }
            }
        }
    }

    /**
     * 事件 播放状态
     */
    private val mStateListener = IPlayer.OnStateChangedListener { newState ->
        when (newState) {
            AliPlayer.idle -> updateState(STATE_IDLE)
            AliPlayer.initalized -> updateState(STATE_INIT)
            AliPlayer.prepared -> updateState(STATE_PREPARED)
            AliPlayer.started -> updateState(STATE_START)
            AliPlayer.paused -> updateState(STATE_PAUSE)
            AliPlayer.stopped -> updateState(STATE_STOP)
            AliPlayer.completion -> updateState(STATE_COMPLETE)
            AliPlayer.error -> updateState(STATE_ERROR)
        }
    }

    /**
     * 事件 准备完成
     */
    private val mPreparedListener = IPlayer.OnPreparedListener {
        val bundle = BundlePool.obtain()
        bundle.putInt(EventKey.INT_ARG1, mAliPlayer?.videoWidth ?: 0)
        bundle.putInt(EventKey.INT_ARG2, mAliPlayer?.videoHeight ?: 0)
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PREPARED, bundle)
        if (mStartPos > 0 && duration > 0) {
            this.seekTo(mStartPos)
            this.mStartPos = -1
        }
        if (isPlaying) {
            this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_START, null)
        }
    }

    /**
     * 事件 首帧渲染显示
     */
    private val mRenderStartListener = IPlayer.OnRenderingStartListener {
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_VIDEO_RENDER_START, null)
    }

    /**
     * 事件 视频大小改变
     */
    private val mSizeChangedListener = IPlayer.OnVideoSizeChangedListener { width, height ->
        val bundle = BundlePool.obtain()
        bundle.putInt(EventKey.INT_ARG1, width)
        bundle.putInt(EventKey.INT_ARG2, height)
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE, bundle)
    }

    /**
     * 事件 视频播放完成
     */
    private val mCompletionListener = IPlayer.OnCompletionListener {
        this.mAliPlayer?.stop()
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE, null)
    }

    /**
     * 事件 错误事件
     */
    private val mErrorListener = IPlayer.OnErrorListener { errInfo ->
        this.mAliPlayer?.stop()
        this.sendErrorEvent(
            OnErrorListener.ERROR_EVENT_UNKNOWN,
            "${errInfo.code} | ${errInfo.msg} | ${errInfo.extra}"
        )
    }

    /**
     * 事件 基本数据
     */
    private val mInfoListener = IPlayer.OnInfoListener { infoBean ->
        when (infoBean.code) {
            InfoCode.CurrentPosition -> {
                this.mCurrPosition = infoBean.extraValue
            }
            InfoCode.BufferedPosition -> {
                this.bufferPercentage = infoBean.extraValue.toInt()
            }
            InfoCode.LoopingStart -> {

            }
            InfoCode.SwitchToSoftwareVideoDecoder -> {
                // 切换至软解
            }
            else -> {}
        }
    }

    /**
     * 事件 加载状态
     */
    private val mLoadingStatusListener = object : IPlayer.OnLoadingStatusListener {
        override fun onLoadingBegin() {
            sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_START, null)
        }

        override fun onLoadingProgress(percent: Int, netSpeed: Float) {
        }

        override fun onLoadingEnd() {
            sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_BUFFERING_END, null)
        }
    }

    /**
     * 事件 加载状态
     */
    private val mSeekCompleteListener = IPlayer.OnSeekCompleteListener {
        this.sendPlayerEvent(OnPlayerListener.PLAYER_EVENT_ON_SEEK_COMPLETE, null)
    }
}
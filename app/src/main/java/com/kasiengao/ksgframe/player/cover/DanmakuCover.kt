package com.kasiengao.ksgframe.player.cover

import android.content.Context
import android.os.Bundle
import android.view.View
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.constant.CoverConstant
import com.ksg.ksgplayer.cover.BaseCover
import com.ksg.ksgplayer.cover.ICoverEvent
import com.ksg.ksgplayer.event.EventKey
import com.ksg.ksgplayer.listener.OnPlayerListener
import com.ksg.ksgplayer.player.IPlayer
import com.kuaishou.akdanmaku.DanmakuConfig
import com.kuaishou.akdanmaku.data.DanmakuItemData
import com.kuaishou.akdanmaku.render.SimpleRenderer
import com.kuaishou.akdanmaku.ui.DanmakuPlayer

/**
 * @ClassName: DanmakuCover
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/1 14:51
 * @Description: 弹幕
 */
class DanmakuCover(context: Context) : BaseCover(context) {

    private lateinit var mDanmakuPlayer: DanmakuPlayer

    private var config = DanmakuConfig().apply {
        textSizeScale = 0.8f
    }

    override fun onCreateCoverView(context: Context?): View =
        View.inflate(context, R.layout.layout_cover_danmaku, null)

    /**
     * InitViews
     */
    override fun initViews() {
        // Init DanmakuPlayer
        this.initDanmaku()
    }

    /**
     * Init DanmakuPlayer
     */
    private fun initDanmaku() {
        this.mDanmakuPlayer = DanmakuPlayer(SimpleRenderer()).also {
            it.bindView(findViewById(R.id.cover_danmaku))
        }
    }

    /**
     * View 与页面视图绑定
     */
    override fun onCoverViewBind() {
        if (mDanmakuPlayer.isReleased) {
            this.initDanmaku()
        }
        // 初始状态
        if (playerInfoGetter != null) {
            playerInfoGetter.state.also {
                if (it == IPlayer.STATE_PAUSE) {
                    this.mDanmakuPlayer.pause()
                } else if (it == IPlayer.STATE_START) {
                    this.mDanmakuPlayer.start(config)
                    this.mDanmakuPlayer.seekTo(playerInfoGetter.progress)
                }
            }
        }
    }

    /**
     * View 与页面视图解绑
     */
    override fun onCoverViewUnBind() {
        this.mDanmakuPlayer.pause()
    }

    /**
     * 值 过滤器
     *
     * @return {"Key1","Key2","Key3"}
     */
    override fun getValueFilters(): Array<String> = arrayOf()

    /**
     * 值 事件
     *
     * @param key   key
     * @param value value
     */
    override fun onValueEvent(key: String?, value: Any?) {

    }

    /**
     * 播放事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    override fun onPlayerEvent(eventCode: Int, bundle: Bundle?) {
        when (eventCode) {
            OnPlayerListener.PLAYER_EVENT_ON_SEEK_COMPLETE -> {
                // 进度跳转完成
                bundle?.let {
                    this.mDanmakuPlayer.seekTo(it.getLong(EventKey.LONG_DATA))
                }
                playerInfoGetter.let {
                    this.setPlayState(it.state)
                }
            }
            OnPlayerListener.PLAYER_EVENT_ON_PLAY_COMPLETE -> {
                // 播放完成
                this.mDanmakuPlayer.pause()
            }
            OnPlayerListener.PLAYER_EVENT_ON_STATE_CHANGE -> {
                // 播放状态改变
                bundle?.let {
                    this.setPlayState(it.getInt(EventKey.INT_DATA))
                }
            }
        }
    }

    /**
     * 错误事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    override fun onErrorEvent(eventCode: Int, bundle: Bundle?) {

    }

    /**
     * Cover事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    override fun onCoverEvent(eventCode: Int, bundle: Bundle?) {
        when(eventCode){
            ICoverEvent.CODE_REQUEST_SPEED ->{
                // 倍速
                bundle?.let {
                    this.mDanmakuPlayer.updatePlaySpeed(it.getFloat(EventKey.FLOAT_DATA, 1.0f))
                }
            }
        }
    }

    /**
     * Cover私有事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    override fun onPrivateEvent(eventCode: Int, bundle: Bundle?) {
        when (eventCode) {
            CoverConstant.PrivateEvent.CODE_REQUEST_DANMAKU_OPEN -> {
                // 弹幕开
                this.setDanmakuVisibility(true)
            }
            CoverConstant.PrivateEvent.CODE_REQUEST_DANMAKU_CLOSE -> {
                // 弹幕关
                this.setDanmakuVisibility(false)
            }
        }
    }

    /**
     * 生产者事件
     *
     * @param eventCode eventCode
     * @param bundle    bundle
     */
    override fun onProducerEvent(eventCode: Int, bundle: Bundle?) {

    }

    /**
     * 生产者事件 数据
     *
     * @param key  key
     * @param data data
     */
    override fun onProducerData(key: String?, data: Any?) {

    }

    /**
     * 更新弹幕数据
     */
    fun updateData(data: List<DanmakuItemData>) {
        this.mDanmakuPlayer.updateData(data)
    }

    /**
     * 设置状态
     */
    private fun setPlayState(state: Int) {
        if (state == IPlayer.STATE_PAUSE) {
            this.mDanmakuPlayer.pause()
        } else if (state == IPlayer.STATE_START) {
            this.mDanmakuPlayer.start(config)
        }
    }

    /**
     * 设置显示隐藏
     */
    private fun setDanmakuVisibility(visible: Boolean) {
        config = config.copy(visibility = visible)
        this.mDanmakuPlayer.updateConfig(config)
    }

    /**
     * 获取组件等级
     *
     * @return level
     */
    override fun getCoverLevel(): Int {
        return levelLow(10)
    }

    /**
     * 释放
     */
    override fun release() {
        super.release()
        this.mDanmakuPlayer.release()
    }
}
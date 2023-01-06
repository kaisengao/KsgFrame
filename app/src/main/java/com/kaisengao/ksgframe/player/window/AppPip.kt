package com.kaisengao.ksgframe.player.window

import android.app.Activity
import android.util.Log
import android.view.MotionEvent
import com.kaisengao.ksgframe.constant.CoverConstant
import com.kaisengao.ksgframe.player.window.constant.PIPConstant
import com.kaisengao.ksgframe.player.window.widget.PIPPlayerView
import com.ksg.ksgplayer.cache.AssistCachePool
import com.ksg.ksgplayer.cover.ICover
import com.ksg.ksgplayer.cover.ICoverManager
import com.ksg.ksgplayer.widget.KsgAssistView
import java.util.*

/**
 * @ClassName: AppPip
 * @Author: GaoXin
 * @CreateDate: 2023/1/6 14:06
 * @Description: 画中画
 */
class AppPip {

    private val mAppInPip by lazy { AppInPip() }

    private val mAppOutPip by lazy { AppOutPip() }

    private var mCurrAppPip: IAppPip? = null

    private var mCurrAssistUUID: String = "" // 当前播放器的唯一标识
    private var mCurrAssistView: KsgAssistView? = null // 当前播放器的实例

    /**
     * 显示 悬浮窗
     *
     * @param activity 上下文
     * @param assist 放器的实例
     */
    fun showAppPip(activity: Activity, assist: KsgAssistView?) {
        if (isShowing() || assist == null) return
        // Assist
        this.mCurrAssistUUID = UUID.randomUUID().toString()
        this.mCurrAssistView = assist
        // 显示 APP悬浮窗播放
        if (isOpenAppOutPip()) {
            this.mCurrAppPip = mAppOutPip
        }
        // 显示 APP内部小窗播放
        if (isOpenAppInPip()) {
            this.mCurrAppPip = mAppInPip
        }
        // 显示 悬浮窗
        if (mCurrAppPip != null) {
            this.showAppPip(activity)
            return
        }
        // 以上验证均为通过 -> 停止播放
        this.stopPlayer()
    }

    /**
     * 显示 悬浮窗
     */
    private fun showAppPip(activity: Activity) {
        try {
            this.mCurrAppPip = mAppInPip
            // 创建 画中画视图
            this.mCurrAppPip?.showPip(activity, mCurrAssistUUID) { container ->
                if (container != null) {
                    // Show 画中画
                    this.showPip(container)
                } else {
                    // 未获取到容器 -> 释放
                    this.releasePip()
                }
            } ?: kotlin.run {
                // 未获取画中画视图 -> 释放
                this.releasePip()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "showAppInPip() 出现异常 -> 释放播放器 Error = ${e.message}")
            // 出现异常 -> 释放
            this.releasePip()
        }
    }

    /**
     * 播放 画中画
     */
    private fun showPip(container: PIPPlayerView) {
        this.mCurrAssistView?.let { assistView ->
            assistView.bindContainer(container)
            // 移除除Loading以外的所有Cover
            assistView.player?.coverManager?.removeAllCover(object :
                ICoverManager.OnCoverFilter {
                override fun filter(cover: ICover?): Boolean {
                    if (cover == null) return false
                    return cover.key == CoverConstant.CoverKey.KEY_LOADING
                }
            })
            // 缓存当前播放实例
            AssistCachePool.getInstance().addCache(mCurrAssistUUID, mCurrAssistView)
        } ?: kotlin.run {
            // 未获取到播放器 -> 释放
            this.releasePip()
        }
    }

    /**
     * 停止 播放器
     */
    private fun stopPlayer() {
        AssistCachePool.getInstance().removeCache(mCurrAssistUUID)
        this.mCurrAssistUUID = ""
        this.mCurrAssistView?.destroy()
    }

    /**
     * 关闭 画中画
     */
    fun dismissPip() {
        this.mCurrAppPip?.dismissPip()
        this.mCurrAppPip = null
    }

    /**
     * 释放 画中画
     */
    fun releasePip() {
        this.stopPlayer()
        this.dismissPip()
    }

    /**
     * 设置 TouchEvent 事件传递
     */
    fun setTouchEvent(onTouchEvent: ((event: MotionEvent) -> Unit)? = null) {
        this.mCurrAppPip?.setTouchEvent(onTouchEvent)
    }

    /*
     * 获取 当前画中画
     */
    fun getCurrPip(): IAppPip? {
        return mCurrAppPip
    }

    /**
     * 获取 播放器的唯一标识
     */
    fun getCurrAssistUUID(): String {
        return mCurrAssistUUID
    }

    /**
     * 是否 正在显示
     */
    private fun isShowing(): Boolean {
        return mCurrAppPip?.isShowing() ?: false
    }

    /**
     * 是否 APP内部小窗播放
     */
    private fun isOpenAppInPip(): Boolean {
        return PIPConstant.appInPip
    }

    /**
     * 是否 APP悬浮窗播放
     */
    private fun isOpenAppOutPip(): Boolean {
        return PIPConstant.appOutPip
    }

    companion object {
        const val TAG = "AppPip"

        val instance: AppPip by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppPip()
        }
    }
}
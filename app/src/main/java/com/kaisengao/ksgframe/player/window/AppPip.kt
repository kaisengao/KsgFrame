package com.kaisengao.ksgframe.player.window

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import com.kaisengao.base.configure.ActivityManager
import com.kaisengao.ksgframe.constant.CoverConstant
import com.kaisengao.ksgframe.factory.AppFactory
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
     * 设置 缓存实例
     */
    fun setCurrAssistView(assist: KsgAssistView?) {
        this.mCurrAssistUUID = getUUID()
        this.mCurrAssistView = assist
    }

    /**
     * 清空 缓存实例
     */
    fun clearCurrAssistView() {
        this.mCurrAssistUUID = ""
        this.mCurrAssistView = null
    }

    /**
     * 显示 悬浮窗
     *
     * @param activity 上下文
     * @param assist 放器的实例
     */
    fun showAppPip(
        activity: Activity,
        assist: KsgAssistView?,
    ) {
        this.showAppPip(activity, assist = assist)
    }

    /**
     * 显示 悬浮窗
     *
     * @param activity 上下文
     * @param assist 放器的实例
     */
    fun showAppPip(
        activity: Activity,
        uuid: String = getUUID(),
        assist: KsgAssistView?,
    ) {
        if (isShowing() || assist == null) return
        // Assist
        this.mCurrAssistUUID = uuid
        this.mCurrAssistView = assist
        // 显示 画中画
        if (!isOpenAppPip()) {
            // 未开启画中画 -> 停止播放
            this.stopPlayer()
            return
        }
        // 显示 悬浮窗播放
        if (isOpenAlertWindow()) {
            this.mCurrAppPip = mAppOutPip
        } else {
            // 显示 小窗播放
            this.mCurrAppPip = mAppInPip
        }
        // ShowPip
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
     * 继续 播放器
     */
    fun resumePlayer() {
        if (isShowing() && !(mCurrAppPip is AppOutPip)) {
            this.mCurrAssistView?.resume()
        }
    }

    /**
     * 暂停 画中画
     */
    fun pausePlayer() {
        if (!isOpenAlertWindow()) {
            // 弹出 引导打开悬浮窗权限弹窗
            this.showOpenAlertWindowDialog()
            // 暂停 画中画
            this.mCurrAssistView?.pause()
        }
    }

    /**
     * 停止 播放器
     */
    private fun stopPlayer() {
        AssistCachePool.getInstance().removeCache(mCurrAssistUUID)
        this.mCurrAssistView?.destroy()
        this.clearCurrAssistView()
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
        this.dismissPip()
        this.stopPlayer()
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
     * 从前台到后台时触发
     */
    fun handleOnBackground(activity: Activity?) {
        if (activity == null) return
        // 1、校验是否开启了悬浮窗
        if (isOpenAlertWindow()) {
            // 已开启悬浮窗，打开悬浮窗
            this.showAppPip(activity, mCurrAssistUUID, mCurrAssistView)
        } else {
            // 画中画
            this.pausePlayer()
        }
    }

    /**
     * 从后台回到前台时触发
     */
    fun handleOnForeground(activity: Activity?) {
        // 画中画
        this.resumePlayer()
    }

    /**
     * 引导打开悬浮窗权限弹窗
     */
    private fun showOpenAlertWindowDialog() {
        ActivityManager.getInstance().currentActivity()?.let { activity ->
            AlertDialog
                .Builder(activity)
                .setMessage("APP外部显示视频悬浮窗，需要开启权限哦~")
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("去开启") { dialog, _ ->
                    dialog.dismiss()
                    this.requestSettingCanDrawOverlays()
                }
                .show()
        }
    }

    /**
     * 是否 小窗播放
     */
    private fun isOpenAppPip(): Boolean {
        return PIPConstant.appPip
    }

    /**
     * 是否 开启了悬浮窗权限
     */
    private fun isOpenAlertWindow(): Boolean {
        return canDrawOverlays(AppFactory.application())
    }

    /**
     * 生成 UUID
     */
    private fun getUUID(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * 检查 是否开启了悬浮窗权限
     */
    private fun canDrawOverlays(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            true
        } else {
            Settings.canDrawOverlays(context)
        }
    }

    /**
     * 请求 悬浮窗权限
     */
    private fun requestSettingCanDrawOverlays() {
        ActivityManager.getInstance().currentActivity()?.let { activity ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // 无需处理
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.data = Uri.parse("package:" + activity.packageName)
                activity.startActivityForResult(intent, OPEN_OVERLAY_PERMISSION)
            }
        }
    }

    companion object {
        const val TAG = "AppPip"
        const val OPEN_OVERLAY_PERMISSION = 1011;

        val instance: AppPip by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppPip()
        }
    }
}
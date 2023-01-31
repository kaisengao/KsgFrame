package com.kaisengao.ksgframe.player.window

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import com.kaisengao.base.util.DensityUtil
import com.kaisengao.base.util.ToastUtil.setGravity
import com.kaisengao.ksgframe.BuildConfig
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.common.float.app.FxAnimationImpl
import com.kaisengao.ksgframe.common.float.app.FxConfigStorageToSpImpl
import com.kaisengao.ksgframe.constant.CoverConstant
import com.kaisengao.ksgframe.factory.AppFactory
import com.kaisengao.ksgframe.player.window.widget.PIPPlayerView
import com.ksg.ksgplayer.cache.AssistCachePool
import com.ksg.ksgplayer.cover.ICover
import com.ksg.ksgplayer.cover.ICoverManager
import com.ksg.ksgplayer.widget.KsgAssistView
import com.petterp.floatingx.FloatingX
import com.petterp.floatingx.assist.FxGravity
import com.petterp.floatingx.listener.IFxScrollListener
import java.io.IOException

/**
 * @ClassName: AppInPip
 * @Author: GaoXin
 * @CreateDate: 2023/1/5 11:07
 * @Description: APP 免权限画中画
 */
class AppInPip : IAppPip {

    private var mOnTouchEvent: ((event: MotionEvent) -> Unit)? = null

    init {
        // 初始 窗口
        this.initWindow()
    }

    /**
     * 初始 窗口
     */
    private fun initWindow() {
        FloatingX.init {
            // 悬浮窗View
            setLayout(R.layout.layout_player_container_pip)
            // 设置悬浮窗默认方向
            setGravity(FxGravity.RIGHT_OR_TOP)
            // 启用辅助方向,具体参加方法注释
            setEnableAssistDirection(t = DensityUtil.getHeightInPx(AppFactory.application()) * 0.3f)
            // 设置启用悬浮窗位置修复
            setEnableAbsoluteFix(true)
            // 设置启用边缘吸附
            setEnableEdgeAdsorption(true)
            // 设置启用悬浮窗可屏幕外回弹
            setEnableScrollOutsideScreen(true)
            // 设置启用动画
            setEnableAnimation(true)
            // 设置启用动画实现
            setAnimationImpl(FxAnimationImpl())
            // 设置可移动范围内相对屏幕底部偏移量
            setBottomBorderMargin(45F)

            // 1.设置是否允许所有activity都进行显示,默认true
            setEnableAllInstall(true)
            // 2.禁止插入Activity的页面, setEnableAllBlackClass(true)时,此方法生效 (黑名单)
//            addInstallBlackClass( )
            // 3.允许插入Activity的页面, setEnableAllBlackClass(false)时,此方法生效 (白名单)
//            addInstallWhiteClass( )
            // 设置是否启用日志
            setEnableLog(BuildConfig.DEBUG, AppPip.TAG)

            setScrollListener(object : IFxScrollListener {
                override fun down() {}
                override fun up() {}
                override fun dragIng(event: MotionEvent, x: Float, y: Float) {}
                override fun eventIng(event: MotionEvent) {
                    mOnTouchEvent?.invoke(event)
                }
            })
        }
        // 设置允许保存方向
        FloatingX.configControl()
            .setEnableSaveDirection(FxConfigStorageToSpImpl(AppFactory.application()), true)
    }

    /**
     * 显示 画中画
     */
    override fun showPip(
        activity: Activity, uuid: String, callback: (container: PIPPlayerView?) -> Unit
    ) {
        FloatingX.control().show()
        val viewHolder = FloatingX.control().getViewHolder()
        if (viewHolder != null) {
            callback.invoke(viewHolder.getView(R.id.pipContainer))
        }
    }

    /**
     * 关闭 画中画
     */
    override fun dismissPip() {
        FloatingX.control().cancel()
    }

    /**
     * 设置 TouchEvent 事件传递
     */
    override fun setTouchEvent(onTouchEvent: ((event: MotionEvent) -> Unit)?) {
        this.mOnTouchEvent = onTouchEvent
    }

    /**
     * 是否 正在显示
     */
    override fun isShowing(): Boolean {
        return FloatingX.control().isShow()
    }

}
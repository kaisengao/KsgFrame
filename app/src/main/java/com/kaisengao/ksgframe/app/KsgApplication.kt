package com.kaisengao.ksgframe.app

import android.util.Log
import android.view.View
import androidx.core.view.updateLayoutParams
import com.kaisengao.base.BaseApplication
import com.kaisengao.base.util.DensityUtil
import com.kaisengao.base.util.ToastUtil.setGravity
import com.kaisengao.ksgframe.BuildConfig
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.common.float.app.FxAnimationImpl
import com.kaisengao.ksgframe.common.float.app.FxConfigStorageToSpImpl
import com.kaisengao.ksgframe.factory.AppFactory
import com.petterp.floatingx.FloatingX
import com.petterp.floatingx.assist.FxGravity
import com.petterp.floatingx.listener.IFxViewLifecycle

/**
 * @ClassName: KsgApplication
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:31
 * @Description:
 */
class KsgApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        // 初始 (播放器)应用内部的悬浮窗
        this.initPlayerAppFloat()
    }

    /**
     * 初始 (播放器)应用内部的悬浮窗
     */
    private fun initPlayerAppFloat() {
        FloatingX.init {
            // 悬浮窗View
            setLayout(R.layout.layout_player_app_float)
            // 设置悬浮窗默认方向
            setGravity(FxGravity.RIGHT_OR_TOP)
            // 启用辅助方向,具体参加方法注释
            setEnableAssistDirection(t = DensityUtil.getHeightInPx(this@KsgApplication) * 0.3f)
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

            // 1.设置是否允许所有activity都进行显示,默认true
            setEnableAllInstall(true)
            // 2.禁止插入Activity的页面, setEnableAllBlackClass(true)时,此方法生效 (黑名单)
//            addInstallBlackClass( )
            // 3.允许插入Activity的页面, setEnableAllBlackClass(false)时,此方法生效 (白名单)
//            addInstallWhiteClass( )

            // 设置是否启用日志
            setEnableLog(BuildConfig.DEBUG, "zzz")
        }
        FloatingX.configControl()
            .setEnableSaveDirection(FxConfigStorageToSpImpl(AppFactory.application()), true)
    }
}
package com.kaisengao.ksgframe.app

import com.kaisengao.base.BaseApplication
import com.kaisengao.ksgframe.player.window.AppInPip

/**
 * @ClassName: KsgApplication
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:31
 * @Description:
 */
class KsgApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        // 初始 应用内部的悬浮窗
        AppInPip.instance.initWindow(this)
    }
}

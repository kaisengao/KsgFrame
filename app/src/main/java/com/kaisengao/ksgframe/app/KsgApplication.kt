package com.kaisengao.ksgframe.app

import android.app.Activity
import android.util.Log
import com.kaisengao.base.BaseApplication
import com.kaisengao.ksgframe.player.window.AppPip

/**
 * @ClassName: KsgApplication
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:31
 * @Description:
 */
class KsgApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        // Init ActivityLifecycle
        this.initActivityLifecycle()
    }

    /**
     * Init ActivityLifecycle
     */
    private fun initActivityLifecycle() {
        this.setBackgroundCallback(object : OnBackgroundCallback {

            /**
             * 从前台到后台时触发
             */
            override fun handleOnBackground(activity: Activity?) {
                Log.d("zzz", "进入后台")
                // 画中画
                AppPip.instance.handleOnBackground(activity)
            }

            /**
             * 从后台回到前台时触发
             */
            override fun handleOnForeground(activity: Activity?) {
                Log.d("zzz", "回到前台")
                // 画中画
                AppPip.instance.handleOnForeground(activity)
            }
        })
    }
}

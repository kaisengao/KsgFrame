package com.kaisengao.ksgframe.app

import android.app.Activity
import android.app.Application
import android.util.Log
import com.jeremyliao.liveeventbus.LiveEventBus
import com.kaisengao.base.BaseApplication
import com.kaisengao.ksgframe.constant.BusConstant
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
        // Init 事件总线
        this.initLiveDataBus(this)
        // Init ActivityLifecycle
        this.initActivityLifecycle()
    }

    /**
     * Init 事件总线
     */
    private fun initLiveDataBus(application: Application?) {
        LiveEventBus.config().setContext(application).lifecycleObserverAlwaysActive(true)
            .autoClear(true)
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
//                // 画中画
//                AppPip.instance.handleOnBackground(activity)
                // EventBus
                LiveEventBus.get<Int>(BusConstant.APP_BACK).post(0)
            }

            /**
             * 从后台回到前台时触发
             */
            override fun handleOnForeground(activity: Activity?) {
                Log.d("zzz", "回到前台")
//                // 画中画
//                AppPip.instance.handleOnForeground(activity)
                // EventBus
                LiveEventBus.get<Int>(BusConstant.APP_FORE).post(0)
            }
        })
    }
}

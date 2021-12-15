package com.kasiengao.ksgframe.app

import com.kaisengao.base.BaseApplication
import com.ksg.ksgplayer.config.KsgPlayerConfig

/**
 * @ClassName: KsgApplication
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/22 14:31
 * @Description:
 */
class KsgApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        // 播放器 全局配置
        KsgPlayerConfig.getInstance().isPlayProgressCache = true
        KsgPlayerConfig.getInstance().isNetworkEventProducer = false
    }


}
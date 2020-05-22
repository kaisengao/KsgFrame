package com.kasiengao.ksgframe

import com.kasiengao.base.BaseApplication
import com.ksg.ksgplayer.config.PlayerConfig

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
        PlayerConfig.getInstance().isPlayProgressCache = true
        PlayerConfig.getInstance().isNetworkEventProducer = false
    }
}
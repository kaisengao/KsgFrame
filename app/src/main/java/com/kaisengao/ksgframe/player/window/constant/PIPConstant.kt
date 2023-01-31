package com.kaisengao.ksgframe.player.window.constant

/**
 * @ClassName: FloatConstant
 * @Author: GaoXin
 * @CreateDate: 2022/12/3 20:28
 * @Description: 悬浮窗
 */
object PIPConstant {

    /**
     * 悬浮窗 比例 (16:9)
     */
    val WINDOW_SCALE: FloatArray by lazy { floatArrayOf(0.5F, 0.7F, 0.9F, 1F) }

    /**
     * 测试用 APP小窗播放
     */
    var appPip: Boolean = false
}
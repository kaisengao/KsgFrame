package com.kaisengao.ksgframe.player.window

import android.app.Activity
import android.view.MotionEvent
import com.kaisengao.ksgframe.player.window.widget.PIPPlayerView

/**
 * @ClassName: IAppPip
 * @Author: GaoXin
 * @CreateDate: 2023/1/6 16:15
 * @Description:
 */
interface IAppPip {

    /**
     * 显示 画中画
     */
    fun showPip(activity: Activity, uuid: String, callback: ((container: PIPPlayerView?) -> Unit))

    /**
     * 关闭 画中画
     */
    fun dismissPip()

    /**
     * 设置 TouchEvent 事件传递
     */
    fun setTouchEvent(onTouchEvent: ((event: MotionEvent) -> Unit)? = null)

    /**
     * 是否 正在显示
     */
    fun isShowing(): Boolean
}
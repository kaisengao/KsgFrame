package com.kaisengao.ksgframe.player.window

import android.app.Activity
import android.text.TextUtils
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import com.badlogic.gdx.scenes.scene2d.actions.Actions.show
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.constant.CoverConstant
import com.kaisengao.ksgframe.player.window.widget.PIPPlayerView
import com.ksg.ksgplayer.cache.AssistCachePool
import com.ksg.ksgplayer.cover.ICover
import com.ksg.ksgplayer.cover.ICoverManager
import com.ksg.ksgplayer.widget.KsgAssistView
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.petterp.floatingx.FloatingX
import java.util.*

/**
 * @ClassName: AppOutPip
 * @Author: GaoXin
 * @CreateDate: 2023/1/5 14:36
 * @Description: APP 悬浮窗画中画
 */
class AppOutPip : IAppPip {

    private var mCurrUUID: String = ""

    private var mOnTouchEvent: ((event: MotionEvent) -> Unit)? = null

    /**
     * 显示 画中画
     */
    override fun showPip(
        activity: Activity,
        uuid: String,
        callback: (container: PIPPlayerView?) -> Unit
    ) {
        EasyFloat
            .with(activity)
            .setLayout(R.layout.layout_player_container_pip) { rootView ->
                callback.invoke(rootView?.findViewById(R.id.pipContainer))
            }
            .setSidePattern(SidePattern.RESULT_HORIZONTAL)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setDragEnable(true)
            .setTag(uuid)
            .registerCallback {
                touchEvent { _, event ->
                    mOnTouchEvent?.invoke(event)
                }
            }.show()
        this.mCurrUUID = uuid
    }


    /**
     * 关闭 画中画
     */
    override fun dismissPip() {
        if (!TextUtils.isEmpty(mCurrUUID)) {
            EasyFloat.dismiss(mCurrUUID)
        }
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
        if (TextUtils.isEmpty(mCurrUUID)) return false
        return EasyFloat.isShow(mCurrUUID)
    }
}
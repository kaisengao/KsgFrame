package com.kaisengao.ksgframe.player.window

import android.app.Activity
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
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

/**
 * @ClassName: AppOutPip
 * @Author: GaoXin
 * @CreateDate: 2023/1/5 14:36
 * @Description: APP 悬浮窗画中画
 */
class AppOutPip {
    var mCurrUUID: String? = null

    var mOnTouchEvent: ((event: MotionEvent) -> Unit)? = null

    private var mCurrAssist: KsgAssistView? = null

    /**
     * 显示 画中画
     */
    fun showPip(activity: Activity, uuid: String, assist: KsgAssistView?) {
        // 1、获取 播放器实例
        this.mCurrAssist = assist ?: return
        this.mCurrUUID = uuid
        AssistCachePool.getInstance().addCache(uuid, assist)
        // 2、创建 画中画视图
        EasyFloat
            .with(activity)
            .setLayout(R.layout.layout_player_container_pip) { rootView ->
                // container
                val container = rootView?.findViewById<PIPPlayerView>(R.id.pipContainer)
                if (container != null) {
                    this.showPip(container)
                }
            }
            .setSidePattern(SidePattern.RESULT_HORIZONTAL)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setDragEnable(true)
            .registerCallback {

                touchEvent{ _, event ->
                    mOnTouchEvent?.invoke(event)
                }
            }
            .show()
    }

    /**
     * 播放 画中画
     */
    private fun showPip(container: PIPPlayerView) {
        this.mCurrAssist?.bindContainer(container)
        this.mCurrAssist?.player?.coverManager?.removeAllCover(object :
            ICoverManager.OnCoverFilter {
            override fun filter(cover: ICover?): Boolean {
                if (cover == null) return false
                return cover.key == CoverConstant.CoverKey.KEY_LOADING
            }
        })
    }

    companion object {
        private const val TAG = "AppInPip"

        val instance: AppOutPip by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
          AppOutPip()
        }
    }

}
package com.kaisengao.ksgframe.player.window.app

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.kaisengao.base.util.DensityUtil
import com.kaisengao.ksgframe.common.widget.PlayerContainerView
import com.kaisengao.ksgframe.player.window.app.constant.FloatConstant

/**
 * @ClassName: FloatPlayerView
 * @Author: GaoXin
 * @CreateDate: 2022/12/3 20:18
 * @Description: 悬浮窗View
 */
@SuppressLint("ClickableViewAccessibility")
class FloatPlayerView(context: Context, attrs: AttributeSet?) :
    PlayerContainerView(context, attrs), View.OnTouchListener {

    private val TAG = "zzz"

    private var mCurrWindowScale: Int = 0 // 悬浮窗缩放比例

    private val mScreenSize by lazy { DensityUtil.getScreenSize(getContext()) }

    private val mGestureDetector by lazy {
        GestureDetector(getContext(), mSimpleOnGestureListener);
    }

    init {
        // 时间 Touch
        this.setOnTouchListener(this)
        // 初始 尺寸
        this.renewSize()
    }

    /**
     * 更新 尺寸
     */
    private fun renewSize() {
        // 获取缩放比例
        this.post {
            FloatConstant.WINDOW_SCALE[mCurrWindowScale].let { scale ->

                val viewWidth = mScreenSize[0] * scale
                val viewHeight = viewWidth * 9 / 16

                this.setWidthHeight(viewWidth, viewHeight)

                this.mCurrWindowScale = (mCurrWindowScale + 1) % FloatConstant.WINDOW_SCALE.size;

                Log.d(
                    TAG,
                    "renewSize() called with: scale = $scale viewWidth = $viewWidth viewHeight = $viewHeight"
                )
            }
        }
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                Log.d("zzz", "FloatPlayerView 悬浮窗点击事件 ")
//            }
//        }
//        return super.onTouchEvent(event)
//    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return mGestureDetector.onTouchEvent(event)
    }

    /**
     * GestureDetector
     */
    private val mSimpleOnGestureListener by lazy {
        object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                // 更新 尺寸
                renewSize()
                // Return
                return super.onDoubleTap(e)
            }
        }
    }
}
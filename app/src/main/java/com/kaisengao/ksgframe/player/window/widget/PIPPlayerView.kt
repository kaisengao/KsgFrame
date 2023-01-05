package com.kaisengao.ksgframe.player.window.widget

import android.app.AppOpsManager
import android.content.Context
import android.os.Process
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.kaisengao.base.util.DensityUtil
import com.kaisengao.ksgframe.common.widget.PlayerContainerView
import com.kaisengao.ksgframe.databinding.LayoutPlayerPipBinding
import com.kaisengao.ksgframe.player.window.AppInPip
import com.kaisengao.ksgframe.player.window.AppOutPip
import com.kaisengao.ksgframe.player.window.constant.PIPConstant
import com.kaisengao.ksgframe.ui.trainee.player.PlayerActivity
import java.lang.reflect.Method


/**
 * @ClassName: FloatPlayerView
 * @Author: GaoXin
 * @CreateDate: 2022/12/3 20:18
 * @Description: 悬浮窗View
 */
class PIPPlayerView(context: Context, attrs: AttributeSet?) : PlayerContainerView(context, attrs) {

    private val mBinding by lazy {
        LayoutPlayerPipBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    private var mCurrWindowScale: Int = 0 // 悬浮窗缩放比例

    private val mScreenSize by lazy { DensityUtil.getScreenSize(getContext()) }

    private val mGestureDetector by lazy {
        GestureDetector(getContext(), mSimpleOnGestureListener)
    }

    init {
        this.post {
            // Init
            this.init()
            // 初始 尺寸
            this.renewSize()
        }
    }

    /**
     * Init
     */
    private fun init() {
        this.mBinding.root.visibility = View.GONE
        // Close
        this.mBinding.ivClose.setOnClickListener {
            // 释放 画中画
            AppInPip.instance.releasePip()
        }
        // Open
        this.mBinding.ivOpen.setOnClickListener {
            // 打开 原Ac
//            AppInPip.instance.cancelPip()
//            PlayerActivity.startAc(context, AppOutPip.instance.mCurrUUID)

            if (isAllowed()) {
                Log.d("zzz", "init() called isAllowed = true")
            } else {
                Log.d("zzz", "init() called isAllowed = false")
            }
        }
        // 事件传递
//        AppInPip.instance.mOnTouchEvent = { event ->
//            this.mGestureDetector.onTouchEvent(event)
//        }
//        AppOutPip.instance.mOnTouchEvent = { event ->
//            this.mGestureDetector.onTouchEvent(event)
//        }
    }

    /**
     * 更新 UI
     */
    private fun renewUI() {
        val selected = mBinding.root.isSelected
        if (selected) {
            this.mBinding.root.visibility = View.VISIBLE
        } else {
            this.mBinding.root.visibility = View.GONE
        }
        this.mBinding.root.isSelected = !selected
    }

    /**
     * 更新 尺寸
     */
    private fun renewSize() {
        this.mBinding.root.visibility = View.GONE
        // 获取缩放比例
        PIPConstant.WINDOW_SCALE[mCurrWindowScale].let { scale ->
            val viewWidth = mScreenSize[0] * scale
            val viewHeight = viewWidth * 9 / 16
            this.setWidthHeight(viewWidth, viewHeight)
            this.mCurrWindowScale = (mCurrWindowScale + 1) % PIPConstant.WINDOW_SCALE.size;
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return this.mGestureDetector.onTouchEvent(event)

    }

    private fun isAllowed(): Boolean {
        val ops = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager?
        try {
            val op = 10021
            val method: Method = ops!!.javaClass.getMethod(
                "checkOpNoThrow", Int::class.java, Int::class.java, String::class.java
            )
            return method.invoke(
                ops, op, Process.myUid(), context.packageName
            ) == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
        }
        return false
    }

    /**
     * GestureDetector
     */
    private val mSimpleOnGestureListener by lazy {
        object : GestureDetector.SimpleOnGestureListener() {

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                Log.d("zzz", "onSingleTapConfirmed() called with: e = $e")
                // 更新 UI
                renewUI()
                // Return
                return super.onSingleTapConfirmed(e)
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                Log.d("zzz", "onDoubleTap() called with: e = $e")
                // 更新 尺寸
                renewSize()
                // Return
                return super.onDoubleTap(e)
            }
        }
    }
}
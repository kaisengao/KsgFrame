package com.kasiengao.ksgframe.common.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.common.util.BitmapUtils

/**
 * @ClassName: PlayStateView
 * @Author: KaiSenGao
 * @CreateDate: 2022/4/15 21:17
 * @Description: 播放状态位
 */
class PlayStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {

        const val STATE_START = 1

        const val STATE_PAUSE = 2
    }

    var mCurrState = STATE_START

    private var mAlpha: Float = 1f

    private var mDegree: Float = 0f

    private lateinit var mBitPaint: Paint

    private val mColorMatrix: ColorMatrix by lazy {
        ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, mAlpha, 0f,
            )
        )
    }

    var mBitStart: Bitmap? = null

    var mBitPause: Bitmap? = null

    private val mMatrixStart: Matrix by lazy { Matrix() }

    private val mMatrixPause: Matrix by lazy { Matrix() }

    private var mAnimator: ValueAnimator? = null

    /**
     * Init
     */
    init {
        // Init 画笔
        this.initPaint()
        // InitIcon
        this.mBitStart = BitmapUtils.getVectorBitmap(getContext(), R.drawable.ic_start)
        this.mBitPause = BitmapUtils.getVectorBitmap(getContext(), R.drawable.ic_pause)
    }

    /**
     * Init 画笔
     */
    private fun initPaint() {
        this.mBitPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.isFilterBitmap = true
            it.isAntiAlias = true
            it.isDither = true
            it.colorFilter = ColorMatrixColorFilter(mColorMatrix)
        }
    }

    /**
     * 切换 播放
     */
    fun switchToStart() {
        this.setState(STATE_START)
    }

    /**
     * 切换 暂停
     */
    fun switchToPause() {
        this.setState(STATE_PAUSE)
    }

    /**
     * 设置 状态位
     *
     * @param state 见顶部
     */
    private fun setState(state: Int) {
        // 更新当前状态位
        this.mCurrState = state
        // 重绘
        this.invalidate()
        // 启动动画
        this.startAnim()
    }

    /**
     * 启动动画
     */
    private fun startAnim() {
        this.stopAnim()
        if (mAnimator == null) {
            this.mAnimator = ValueAnimator.ofFloat(0f, 90f)
            this.mAnimator?.addUpdateListener { anim ->
                (anim.animatedValue as Float).let {
                    // 度
                    this.mDegree = it
                    // 透明度
                    this.mAlpha = mDegree / 90f
                    // 重绘
                    this.invalidate()
                }
            }
            this.mAnimator?.duration = 200
        }
        this.mAnimator?.start()
    }

    /**
     * 停止动画
     */
    private fun stopAnim() {
        this.mAnimator?.cancel()
    }

    /**
     * Draw
     *
     * @param canvas canvas
     */
    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            when (mCurrState) {
                STATE_START -> onDrawStart(canvas)
                STATE_PAUSE -> onDrawPause(canvas)
            }
        }
    }

    /**
     * 绘制 开始
     */
    private fun onDrawStart(canvas: Canvas) {
        this.onDrawIcon(canvas, mBitStart, mMatrixStart, (mDegree - 90f), mAlpha)
        this.onDrawIcon(canvas, mBitPause, mMatrixPause, mDegree, (1 - mAlpha))
    }

    /**
     * 绘制 暂停
     */
    private fun onDrawPause(canvas: Canvas) {
        this.onDrawIcon(canvas, mBitStart, mMatrixStart, -mDegree, (1 - mAlpha))
        this.onDrawIcon(canvas, mBitPause, mMatrixPause, (90f - mDegree), mAlpha)
    }

    /**
     * 绘制 图标
     */
    private fun onDrawIcon(
        canvas: Canvas,
        bitmap: Bitmap?,
        matrix: Matrix,
        degree: Float,
        alpha: Float
    ) {
        if (bitmap == null) {
            return
        }
        val centerX = bitmap.width * 0.5f
        val centerY = bitmap.height * 0.5f
        matrix.reset()
        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)
        matrix.postRotate(degree, centerX, centerY)
        matrix.postTranslate(
            (canvas.width * 0.5f) - centerX,
            (canvas.height * 0.5f) - centerY
        )
        this.mColorMatrix.array[18] = alpha
        this.mBitPaint.colorFilter = ColorMatrixColorFilter(mColorMatrix)
        canvas.drawBitmap(bitmap, matrix, mBitPaint)
    }
}
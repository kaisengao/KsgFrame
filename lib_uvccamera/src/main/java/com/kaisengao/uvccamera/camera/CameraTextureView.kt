package com.kaisengao.uvccamera.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import androidx.core.content.ContextCompat
import com.kaisengao.uvccamera.CameraProcessLib
import com.kaisengao.uvccamera.utils.FileUtil
import com.kaisengao.uvccamera.utils.TimeUtil
import com.kaisengao.uvccamera.videotape.IRecordProvider
import com.kaisengao.uvccamera.videotape.RecordEncoder
import kotlinx.coroutines.*
import java.io.File
import java.util.*


/**
 * @ClassName: UsbCameraTextureView
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/6 11:18
 * @Description: UsbCamera TextureView
 */
class CameraTextureView : TextureView, TextureView.SurfaceTextureListener,
    IRecordProvider<Bitmap> {

    companion object {
        private val TAG = CameraTextureView::class.java.simpleName
        private const val PREVIEW_WIDTH = 640
        private const val PREVIEW_HEIGHT = 480

        const val FAILED_PERMISSION = -1
        const val FAILED_PROCESS_CAMERA = -2
    }

    private var mDuration: Int = 60

    private var mProcessFailedCount: Int = 0

    private val mBitmap: Bitmap by lazy {
        Bitmap.createBitmap(PREVIEW_WIDTH, PREVIEW_HEIGHT, Bitmap.Config.RGB_565)
    }

    private val mSrcRect: Rect by lazy { Rect(0, 0, mBitmap.width, mBitmap.height) }

    private val mDstRect: Rect by lazy { Rect(0, 0, width, height) }

    private val mTimePaint: Paint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DEV_KERN_TEXT_FLAG)
        paint.textSize = 25.0f
        paint.color = Color.WHITE
        paint.setShadowLayer(
            3f, 0f, 0f,
            ContextCompat.getColor(context, android.R.color.background_dark)
        )
        paint
    }
    private val mTimeCanvas: Canvas by lazy { Canvas(mBitmap) }

    private var mJob: Job? = null

    private var mRecordEncoder: RecordEncoder? = null

    private var mCameraListener: OnCameraListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet? = null) : super(context, attributeSet)

    init {
        Log.i(TAG, "init")
        // 初始值
        this.mProcessFailedCount = 0
        // 设置背景透明，记住这里是[是否不透明]
        this.isOpaque = false
        // 设置监听
        this.surfaceTextureListener = this
    }

    /**
     * 设置 摄像头状态事件监听
     */
    fun setCameraListener(cameraListener: OnCameraListener) {
        this.mCameraListener = cameraListener
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Log.i(TAG, "onSurfaceTextureAvailable")
        // 准备UsbCamera摄像头画面
        val prepare = CameraProcessLib.prepareUsbCamera(PREVIEW_WIDTH, PREVIEW_HEIGHT)
        // 验证
        if (prepare == FAILED_PERMISSION) {
            Log.i(TAG, "prepareUsbCamera error $prepare")
            // 摄像头异常事件
            this.mCameraListener?.onCameraError(FAILED_PERMISSION, "打开摄像头失败!")

            return
        }
        // 开启协程
        this.processUsbCameraJob().also { this.mJob = it }
        // 摄像头可用事件
        this.mCameraListener?.onCameraAvailable()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
//        Log.i(TAG, "onSurfaceTextureSizeChanged: ")
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
//        Log.i(TAG, "onSurfaceTextureUpdated")
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        Log.i(TAG, "onSurfaceTextureDestroyed: ")
        // 停止协程
        this.stopUsbCameraJob()
        // 释放Bitmap
        this.releaseBitmap()
        // 释放UsbCamera
        CameraProcessLib.releaseUsbCamera()
        // 摄像头销毁事件
        this.mCameraListener?.onCameraDestroyed()

        return true
    }

    /**
     * 释放Bitmap
     */
    private fun releaseBitmap() {
        if (!mBitmap.isRecycled) {
            this.mBitmap.recycle()
        }
    }

    /**
     * 预览协程
     */
    private fun processUsbCameraJob(): Job = GlobalScope.launch {
        // 启动一个新协程并保持对这个作业的引用
        while (isActive) {
            // 验证预览失败次数如果大于了最大值
            // 就代表出现了花屏等情况（属于C文件问题 目前还没有能力修改）
            if (mProcessFailedCount >= 3) {
                Log.i(TAG, "processUsbCamera error $mProcessFailedCount")
                // 协程中切换线程
                withContext(Dispatchers.Main) {
                    // 摄像头异常事件
                    mCameraListener?.onCameraError(FAILED_PROCESS_CAMERA, "摄像头预览失败!")
                }
                // 停止预览协程
                this@CameraTextureView.stopUsbCameraJob()
            }
            // 处理UsbCamera画面
            val cameraProcess = CameraProcessLib.processUsbCamera()
            // 将UsbCamera画面转换Bitmap
            CameraProcessLib.pixelToBmp(mBitmap)
            // 绘制到画布上
            this@CameraTextureView.drawBitmap(mBitmap)
            // 预览失败计数
            if (!cameraProcess) {
                this@CameraTextureView.mProcessFailedCount++
            }
        }
    }

    /**
     * 停止预览协程
     */
    private fun stopUsbCameraJob() = runBlocking {
        this@CameraTextureView.mJob?.cancelAndJoin()
    }

    /**
     * 绘制Bitmap到画布
     */
    private fun drawBitmap(bitmap: Bitmap) {
        // 锁定画布
        val canvas = lockCanvas()
        if (canvas != null) {
            // 将日期时间画到画布上
            val dateTime = TimeUtil.millis2String(System.currentTimeMillis())
            val measureText = mTimePaint.measureText(dateTime)
            this.mTimeCanvas.drawText(dateTime, (bitmap.width - measureText) - 10f, 25f, mTimePaint)
            // 清空画布
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            // 将bitmap画到画布上
            canvas.drawBitmap(bitmap, mSrcRect, mDstRect, null)
            // 解锁画布同时提交
            this.unlockCanvasAndPost(canvas)
        }
    }

    /**
     * 设置 录制时长 (单位 分)
     */
    fun setDuration(duration: Int) {
        this.mDuration = duration
    }

    /**
     * 开启录制
     */
    fun startRecord() {
        if (mRecordEncoder == null) {
            this.mRecordEncoder = RecordEncoder(
                this, File(
                    FileUtil.getFileDir(context, "/Record/", "${System.currentTimeMillis()}.mp4")
                )
            )
        }
        this.mRecordEncoder?.start()
    }

    /**
     * 停止录制
     */
    fun stopRecord() {
        this.mRecordEncoder?.stop()
        this.mRecordEncoder = null
    }

    //（（分*60）*每秒帧数）
    override fun size(): Int = ((mDuration * 60) * 30)

    override fun next(): Bitmap = mBitmap

    override fun progress(progress: Float) {
        Log.i(TAG, "progress = ${progress * 100}%")
    }
}

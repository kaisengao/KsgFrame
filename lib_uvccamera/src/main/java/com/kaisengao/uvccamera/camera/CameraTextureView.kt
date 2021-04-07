package com.kaisengao.uvccamera.camera

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import com.kaisengao.uvccamera.CameraProcessLib
import com.kaisengao.uvccamera.videotape.IVideotapeProvider
import com.kaisengao.uvccamera.videotape.VideotapeEncoder
import kotlinx.coroutines.*
import java.io.File

/**
 * @ClassName: UsbCameraTextureView
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/6 11:18
 * @Description: UsbCamera TextureView
 */
class CameraTextureView : TextureView, TextureView.SurfaceTextureListener,
    IVideotapeProvider<Bitmap> {

    companion object {
        private val TAG = CameraTextureView::class.java.simpleName
        private const val PREVIEW_WIDTH = 640
        private const val PREVIEW_HEIGHT = 480
    }

    var mDuration: Int = 60

    private val mRecordingPath: String by lazy { "${galleryPath}${System.currentTimeMillis()}.mp4" }

    private val mBitmap: Bitmap by lazy {
        Bitmap.createBitmap(PREVIEW_WIDTH, PREVIEW_HEIGHT, Bitmap.Config.RGB_565)
    }

    private val mSrcRect: Rect by lazy { Rect(0, 0, mBitmap.width, mBitmap.height) }

    private val mDstRect: Rect by lazy { Rect(0, 0, width, height) }

    private var mJob: Job? = null

    private val mVideotape: VideotapeEncoder by lazy {
        VideotapeEncoder(
            this, File(mRecordingPath)
        )
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet? = null) : super(context, attributeSet)

    init {
        Log.i(TAG, "init")
        // 设置背景透明，记住这里是[是否不透明]
        this.isOpaque = false
        // 设置监听
        this.surfaceTextureListener = this
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Log.i(TAG, "onSurfaceTextureAvailable")
        // 准备UsbCamera摄像头画面
        val ret = CameraProcessLib.prepareUsbCamera(PREVIEW_WIDTH, PREVIEW_HEIGHT)
        // 验证
        if (ret == -1) {
            Log.i(TAG, "prepareUsbCamera error ret:$ret")
            return
        }
        // 开启协程
        this.processUsbCameraJob().also { this.mJob = it }
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
     * 协程
     */
    private fun processUsbCameraJob(): Job = GlobalScope.launch {
        // 启动一个新协程并保持对这个作业的引用
        while (isActive) {
            // 处理UsbCamera画面
            CameraProcessLib.processUsbCamera()
            // 将UsbCamera画面转换Bitmap
            CameraProcessLib.pixelToBmp(mBitmap)
            // 绘制到画布上
            drawBitmap(mBitmap)
        }
    }

    /**
     * 停止协程
     */
    private fun stopUsbCameraJob() = runBlocking {
        mJob?.cancelAndJoin()
    }

    /**
     * 绘制Bitmap到画布
     */
    private fun drawBitmap(bitmap: Bitmap) {
        //锁定画布
        val canvas = lockCanvas()
        if (canvas != null) {
            //清空画布
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            //将bitmap画到画布上
            canvas.drawBitmap(bitmap, mSrcRect, mDstRect, null)
            //解锁画布同时提交
            unlockCanvasAndPost(canvas)
        }
    }

    /**
     * 开启录制
     */
    fun startRecording() {
        this.mVideotape.start()
    }

    /**
     * 停止录制
     */
    fun stopRecording() {
        this.mVideotape.finish()
    }

    // （（秒*60）*每秒帧数）
    override fun size(): Int = ((mDuration * 60) * 30)

    override fun next(): Bitmap = mBitmap

    override fun progress(progress: Float) {
        if (progress >= 1f) {
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val uri: Uri = Uri.fromFile(File(mRecordingPath))
            intent.data = uri
            context?.sendBroadcast(intent)
        }
        Log.i(TAG, "progress = ${progress * 100}%")
    }

    private val galleryPath: String
        get() {
            val result = (Environment.getExternalStorageDirectory().toString()
                    + File.separator + Environment.DIRECTORY_DCIM
                    + File.separator + "Camera" + File.separator)
            val file = File(result)
            if (!file.exists()) {
                file.mkdir()
            }
            return result + File.separator
        }
}

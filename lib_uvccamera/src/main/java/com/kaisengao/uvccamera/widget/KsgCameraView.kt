package com.kaisengao.uvccamera.widget

import android.content.Context
import android.hardware.usb.UsbDevice
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.kaisengao.base.util.KLog
import com.kaisengao.uvccamera.R
import com.kaisengao.uvccamera.camera.CameraTextureView
import com.kaisengao.uvccamera.camera.OnCameraListener
import com.kaisengao.uvccamera.exe.ExeCommand
import com.kaisengao.uvccamera.usb.OnDeviceListener
import com.kaisengao.uvccamera.usb.USBMonitor
import com.kaisengao.uvccamera.utils.DialogUtil
import com.kaisengao.uvccamera.utils.RuntimeUtil


/**
 * @ClassName: KsgCameraView
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/7 16:20
 * @Description: UVC USB
 */
class KsgCameraView : FrameLayout, View.OnAttachStateChangeListener {

    private var isAttach = false

    private var mUsbMonitor: USBMonitor? = null

    private var mCameraView: CameraTextureView? = null

    private var mStatusRoot: View? = null

    private var mStatusView: AppCompatTextView? = null

    private val mExeCommand: ExeCommand by lazy { ExeCommand() }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        // 添加 状态View
        this.addStatusView()
        // Init USBMonitor
        this.initUSBMonitor()
        // 绑定窗口事件
        this.addOnAttachStateChangeListener(this)
    }

    /**
     * 添加 状态View
     */
    private fun addStatusView() {
        // 设置默认背景色
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        // AddStatusView
        val inflate =
            LayoutInflater.from(context)?.inflate(R.layout.layout_camera_status, this, true)
        this.mStatusRoot = inflate?.findViewById(R.id.camera_status_root)
        this.mStatusView = inflate?.findViewById(R.id.camera_status)
        // 设置默认状态
        this.setStatusView(STATUS_DISCONNECT)
    }

    /**
     * 可视界面的时候调用
     *
     * @param v View
     */
    override fun onViewAttachedToWindow(v: View) {
        // 注册USB广播
        this.mUsbMonitor?.registerUSB()
    }

    /**
     * 移除界面的时候调用
     *
     * @param v View
     */
    override fun onViewDetachedFromWindow(v: View) {
        // 解绑窗口事件
        this.removeOnAttachStateChangeListener(this)
        // 注销USB广播
        this.mUsbMonitor?.unregisterUSB()
    }

    /**
     * Init USBMonitor
     */
    private fun initUSBMonitor() {
        this.mUsbMonitor = USBMonitor(context, object : OnDeviceListener {
            /**
             * 设备已接入
             *
             * @param device device
             */
            override fun onAttached(device: UsbDevice) {
                if (!isAttach) {
                    // 执行节点权限命令
                    mExeCommand.run("chmod 777 /dev/video0", 1000)
                    // 设置连接状态 连接中
                    setStatusView(STATUS_CONNECTING)
                    // 创建摄像头组件
                    mCameraView = CameraTextureView(context)
                    // 设置 摄像头状态事件
                    mCameraView?.setCameraListener(mCameraListener)
                    // 设置 录制时长（单位 分）
                    mCameraView?.setDuration(60)
                    // 添加摄像头组件
                    this@KsgCameraView.addView(
                        mCameraView, 0, ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                    // 标记已接入
                    isAttach = true
                }
                KLog.d(USBMonitor.TAG, "onAttached isAttach = $isAttach")
            }

            /**
             * 设备已断开
             *
             * @param device device
             */
            override fun onDetached(device: UsbDevice) {
                if (isAttach && mCameraView != null) {
                    // 移除摄像头组件
                    this@KsgCameraView.removeView(mCameraView)
                    // 释放资源
                    mCameraView = null
                    // 标记已断开
                    isAttach = false
                }
                KLog.d(USBMonitor.TAG, "onDetached isAttach = $isAttach")
            }
        })
    }

    /**
     * 摄像头状态事件
     */
    private val mCameraListener = object : OnCameraListener {
        /**
         * 摄像头 可用
         */
        override fun onCameraAvailable() {
            // 设置连接状态 已连接
            setStatusView(STATUS_CONNECT)
        }

        /**
         * 摄像头 销毁
         */
        override fun onCameraDestroyed() {
            // 设置连接状态 未连接
            setStatusView(STATUS_DISCONNECT)
        }

        /**
         * 摄像头 异常
         *
         * @param errorCode Code
         * @param errorMessage Message
         */
        override fun onCameraError(errorCode: Int, errorMessage: String) {
            KLog.d("onCameraError errorCode: $errorCode errorMessage: $errorMessage")
            // 设置连接状态 异常
            setStatusView(STATUS_CONNECT_FAILED, "$errorMessage  $errorCode")
            // 重点异常做提示
            when (errorCode) {
                // 摄像头预览失败 （C里面缓存的问题 目前还没有能力修改）（重启手机就完了）
                CameraTextureView.FAILED_PROCESS_CAMERA -> {
                    // 重启提示对话框
                    DialogUtil.getRebootDialog(context).also {
                        it.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                            Toast.makeText(context, R.string.reboot, Toast.LENGTH_LONG).show()
                            // 执行重启命令
                            RuntimeUtil.runCommand("reboot")
                        }
                    }
                }
            }
        }
    }

    /**
     * 开启录制
     */
    fun startRecording() {
        this.mCameraView?.startRecord()
    }

    /**
     * 停止录制
     */
    fun stopRecording() {
        this.mCameraView?.stopRecord()
    }

    /**
     * 设置 连接状态信息
     *
     * @param status status
     */
    private fun setStatusView(status: Int) {
        this.setStatusView(status, "")
    }

    /**
     * 设置 连接状态信息
     *
     * @param status status
     * @param failedMessage 错误信息
     */
    private fun setStatusView(status: Int, failedMessage: String) {
        var text: String? = null
        var drawableIds: Int? = null
        var visibility: Int? = null
        when (status) {
            // 连接失败 （出错）
            STATUS_CONNECT_FAILED -> {
                text = failedMessage
                drawableIds = R.drawable.ic_camera_failed
                visibility = View.VISIBLE
            }
            // 未连接
            STATUS_DISCONNECT -> {
                text = context.getString(R.string.camera_disconnect)
                drawableIds = R.drawable.ic_camera_disconnect
                visibility = View.VISIBLE
            }
            // 连接中
            STATUS_CONNECTING -> {
                text = context.getString(R.string.camera_connecting)
                drawableIds = 0
                visibility = View.VISIBLE
            }
            // 已连接
            STATUS_CONNECT -> {
                text = context.getString(R.string.camera_connect)
                drawableIds = 0
                visibility = View.GONE
            }
        }
        if (text != null && drawableIds != null && visibility != null) {
            this.mStatusView?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, drawableIds, 0, 0
            )
            this.mStatusView?.text = text
            this.mStatusRoot?.visibility = visibility
        }
    }

    companion object {
        /**
         * 连接失败 （出错）
         */
        const val STATUS_CONNECT_FAILED: Int = 0

        /**
         * 未连接
         */
        const val STATUS_DISCONNECT: Int = 1

        /**
         * 连接中
         */
        const val STATUS_CONNECTING: Int = 2

        /**
         * 已连接
         */
        const val STATUS_CONNECT: Int = 3
    }
}
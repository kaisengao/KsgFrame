package com.kaisengao.uvccamera.camera

import android.content.Context
import android.hardware.usb.UsbDevice
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.kaisengao.base.util.KLog
import com.kaisengao.uvccamera.usb.OnUsbDeviceListener
import com.kaisengao.uvccamera.usb.USBMonitor
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

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        // 绑定窗口事件
        this.addOnAttachStateChangeListener(this)
        // Init USBMonitor
        this.initUSBMonitor()
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
        this.mUsbMonitor = USBMonitor(context, object : OnUsbDeviceListener {
            /**
             * 设备已接入
             *
             * @param device device
             */
            override fun onAttached(device: UsbDevice) {
                if (!isAttach) {
                    // 执行节点权限命令
                    RuntimeUtil.runCommand("chmod 777 /dev/video0")
                    // 创建摄像头组件
                    mCameraView = CameraTextureView(context)
                    // 设置录制时长（单位秒）
                    mCameraView?.mDuration = 1
                    // 添加摄像头组件
                    this@KsgCameraView.addView(
                        mCameraView, ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                    // 标记已接入
                    isAttach = true
                }
                KLog.d(USBMonitor.TAG, "onAttached device isAttach = $isAttach")
            }

            /**
             * 设备已断开
             *
             * @param device device
             */
            override fun onDetached(device: UsbDevice) {
                if (isAttach) {
                    // 移除摄像头组件
                    this@KsgCameraView.removeView(mCameraView)
                    // 释放资源
                    mCameraView = null
                    // 标记已断开
                    isAttach = false
                }
                KLog.d(USBMonitor.TAG, "onDetached device isAttach = $isAttach")
            }
        })
    }
}
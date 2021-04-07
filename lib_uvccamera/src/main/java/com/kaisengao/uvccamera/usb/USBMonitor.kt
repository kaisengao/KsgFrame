package com.kaisengao.uvccamera.usb

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager

/**
 * @ClassName: USBMonitor
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/7 14:31
 * @Description: USB监视器
 */
class USBMonitor(
    private val mContext: Context,
    private val mUsbDeviceListener: OnUsbDeviceListener
) {

    private val mUsbManager: UsbManager by lazy { mContext.getSystemService(Context.USB_SERVICE) as UsbManager }

    private var mUsbBroadcast: UsbBroadcast? = null

    private val mUsbIntentFilter: IntentFilter by lazy { IntentFilter() }

    init {
        // USB 插拔事件
        this.mUsbIntentFilter.addAction(ACTION_USB_PERMISSION)
        this.mUsbIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        this.mUsbIntentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
    }

    /**
     * 注册USB广播
     */
    fun registerUSB() {
        // 注销USB广播
        this.unregisterUSB()
        // 创建USB广播
        if (mUsbBroadcast == null) {
            this.mUsbBroadcast = UsbBroadcast(mUsbDeviceListener)
            this.mContext.registerReceiver(mUsbBroadcast, mUsbIntentFilter)
        }
        // 检查USB设备连接状态
        this.checkUSBConnect()
    }

    /**
     * 注销USB广播
     */
    fun unregisterUSB() {
        if (mUsbBroadcast != null) {
            this.mContext.unregisterReceiver(mUsbBroadcast)
            this.mUsbBroadcast = null
        }
    }

    /**
     * 检查USB设备连接状态
     */
    private fun checkUSBConnect() {
        // 获取设备列表，如果列表不为空就代表有USB设备插入
        val deviceList = mUsbManager.deviceList
        if (deviceList.isEmpty()) {
            return
        }
        // 获取设备信息
        for (device in deviceList.values) {
            // 创建广播 Intent
            val intent = Intent(ACTION_USB_PERMISSION)
            intent.putExtra(UsbManager.EXTRA_DEVICE, device)
            // 发送USB连接广播
            this.mContext.sendBroadcast(intent)

            break
        }
    }

    companion object {
        const val TAG = "USBMonitor"
        const val ACTION_USB_PERMISSION = "com.kaisengao.USB_CAMERA_PERMISSION"
    }
}
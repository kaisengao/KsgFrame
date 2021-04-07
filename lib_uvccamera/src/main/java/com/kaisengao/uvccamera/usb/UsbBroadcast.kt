package com.kaisengao.uvccamera.usb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.kaisengao.base.util.KLog
import com.kaisengao.uvccamera.R

/**
 * @ClassName: UsbBroadcast
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/7 14:37
 * @Description: 检测USB设备热插拔广播
 */
class UsbBroadcast(private val mUsbDeviceListener: OnUsbDeviceListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            // 检测USB设备插入
            USBMonitor.ACTION_USB_PERMISSION,
            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                // 获取USB设备信息
                val usbDevice = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                // 设备过滤匹配
                this.deviceFilter(context, usbDevice)?.let {
                    // 通知设备已插入
                    this.mUsbDeviceListener.onAttached(it)
                }
            }
            // 检测USB设备拔出
            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                // 获取USB设备信息
                val usbDevice = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                // 设备过滤匹配
                this.deviceFilter(context, usbDevice)?.let {
                    // 通知设备已拔出
                    this.mUsbDeviceListener.onDetached(it)
                }
            }
        }
    }

    /**
     * 设备过滤匹配
     *
     * @param context context
     * @param device  device
     */
    private fun deviceFilter(context: Context, device: UsbDevice): UsbDevice? {
        // 过滤列表
        val filters = DeviceFilter.getDeviceFilters(context, R.xml.device_filter)
        // 匹配设备
        for (filter in filters) {
            if (filter.matches(device) || filter.mSubclass == device.deviceSubclass) {
                if (!filter.isExclude) {
                    KLog.i("Yes UVCCamera")
                    return device
                }
            }
        }
        KLog.i("No UVCCamera")
        return null
    }
}
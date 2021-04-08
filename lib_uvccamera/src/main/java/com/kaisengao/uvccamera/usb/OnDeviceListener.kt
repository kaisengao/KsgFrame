package com.kaisengao.uvccamera.usb

import android.hardware.usb.UsbDevice

/**
 * @ClassName: OnDeviceListener
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/7 10:21
 * @Description: 设备连接事件
 */
interface OnDeviceListener {

    /**
     * 设备已接入
     *
     * @param device device
     */
    fun onAttached(device: UsbDevice)

    /**
     * 设备已断开
     *
     * @param device device
     */
    fun onDetached(device: UsbDevice)

}
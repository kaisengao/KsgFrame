package com.kaisengao.uvccamera.camera

/**
 * @ClassName: OnCameraListener
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/8 10:57
 * @Description: 摄像头状态事件
 */
interface OnCameraListener {

    /**
     * 摄像头 可用
     */
    fun onCameraAvailable()

    /**
     * 摄像头 销毁
     */
    fun onCameraDestroyed()

    /**
     * 摄像头 异常
     *
     * @param errorCode Code
     * @param errorMessage Message
     */
    fun onCameraError(errorCode: Int, errorMessage: String)
}
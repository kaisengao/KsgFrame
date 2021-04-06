package com.kasiengao.ksgframe.kt.camera

import android.util.Log
import com.kaisengao.mvvm.base.activity.BaseVmActivity
import com.kaisengao.uvccamera.utils.RuntimeUtil
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.databinding.ActivityCameraBinding

/**
 * @ClassName: CameraActivity
 * @Author: KaiSenGao
 * @CreateDate: 2021/4/2 10:22
 * @Description: UVC Camera JNI
 */
class CameraActivity : BaseVmActivity<ActivityCameraBinding, CameraViewModel>() {

    override fun getContentLayoutId(): Int = R.layout.activity_camera

    override fun initVariableId(): Int = BR.viewModel

    override fun initBefore() {
        super.initBefore()
        Log.i("CameraTextureView", "initBefore")
        // 执行节点权限命令
        RuntimeUtil.runCommand("chmod 777 /dev/video0")
    }

    override fun initWidget() {
        super.initWidget()
        Log.i("CameraTextureView", "initWidget")
        // Title
        this.setTitle(R.string.camera_title)
        // Init Click
        this.initClick()
    }

    /**
     * Init Click
     */
    private fun initClick() {
        // 打开摄像头
        this.mBinding.cameraOpen.setOnClickListener {

        }
        // 关闭摄像头
        this.mBinding.cameraClose.setOnClickListener {

        }
        // 拍照
        this.mBinding.cameraPhotograph.setOnClickListener {

        }
        // 开始录制
        this.mBinding.cameraStartRecording.setOnClickListener {
            this.mBinding.cameraView.startRecording()
        }
        // 停止录制
        this.mBinding.cameraStopRecording.setOnClickListener {

        }
    }
}
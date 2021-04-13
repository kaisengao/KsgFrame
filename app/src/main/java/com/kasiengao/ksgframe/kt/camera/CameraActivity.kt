package com.kasiengao.ksgframe.kt.camera

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaisengao.base.configure.ThreadPool
import com.kaisengao.mvvm.base.activity.BaseVmActivity
import com.kaisengao.uvccamera.utils.FileUtil
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

    private val fileDir: String by lazy { FileUtil.getFileDir(this, "/Record/", "") }

    private val mRecordAdapter: CameraRecordAdapter by lazy { CameraRecordAdapter() }

    override fun getContentLayoutId(): Int = R.layout.activity_camera

    override fun initVariableId(): Int = BR.viewModel

    override fun initWidget() {
        super.initWidget()
        // Title
        this.setTitle(R.string.camera_title)
        // Init Click
        this.initClick()
        // Init Adapter
        this.initAdapter()
    }

    /**
     * Init Click
     */
    private fun initClick() {
        // 拍照
        this.mBinding.cameraPhotograph.setOnClickListener {

        }
        // 开始录制
        this.mBinding.cameraStartRecording.setOnClickListener {
            this.mBinding.cameraView.startRecording()
            this.mBinding.cameraStartRecording.isEnabled = false
            this.mViewModel.startRecordTime()
        }
        // 停止录制
        this.mBinding.cameraStopRecording.setOnClickListener {
            this.mBinding.cameraView.stopRecording()
            this.mBinding.cameraStartRecording.isEnabled = true
            this.mViewModel.stopRecordTime()
            // 延时1.5秒刷新列表
            ThreadPool.MainThreadHandler.getInstance().post({
                // 刷新列表
                this.mRecordAdapter.setList(FileUtil.getFileVideos(fileDir))
            }, 1500)
        }
    }

    /**
     * Init Adapter
     */
    private fun initAdapter() {
        // Adapter
        this.mRecordAdapter.setOnItemClickListener { _, _, position ->
            // 视频预览
            val videoFilePath = mRecordAdapter.data[position]
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(videoFilePath), "video/mp4")
            this.startActivity(intent)
        }
        this.mRecordAdapter.setOnItemLongClickListener { _, _, position ->
            val deleteDialog = getDeleteDialog(this)
            deleteDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                // 视频地址
                val videoFilePath = mRecordAdapter.data[position]
                // 删除视频
                FileUtil.deleteFile(videoFilePath)
                // 延时1.5秒刷新列表
                ThreadPool.MainThreadHandler.getInstance().post({
                    // 刷新列表
                    this.mRecordAdapter.setList(FileUtil.getFileVideos(fileDir))
                }, 1500)

                deleteDialog.dismiss()
            }
            return@setOnItemLongClickListener false
        }
        // Recycler
        this.mBinding.cameraRecord.layoutManager = LinearLayoutManager(this)
        this.mBinding.cameraRecord.adapter = mRecordAdapter
        // Data
        this.mRecordAdapter.setList(FileUtil.getFileVideos(fileDir))
    }


    /**
     * 提示 确认删除对话框
     */
    private fun getDeleteDialog(context: Context?): AlertDialog {
        val deleteDialog: AlertDialog = AlertDialog.Builder(
            context!!
        )
            .setTitle("提示")
            .setMessage("确定删除吗?")
            .setPositiveButton("确定", null)
            .setNegativeButton("取消", null)
            .create()
        deleteDialog.show()
        deleteDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        deleteDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
            ContextCompat.getColor(
                context, R.color.color_999999
            )
        )
        return deleteDialog
    }
}
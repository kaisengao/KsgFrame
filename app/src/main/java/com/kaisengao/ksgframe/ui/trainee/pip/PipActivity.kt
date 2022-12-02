package com.kaisengao.ksgframe.ui.trainee.pip

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.library.baseAdapters.BR
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.databinding.ActivityPipBinding
import com.kaisengao.mvvm.base.activity.BaseVmActivity

/**
 * @ClassName: PipActivity
 * @Author: GaoXin
 * @CreateDate: 2022/12/2 17:43
 * @Description: 8.0 画中画模式
 */
class PipActivity : BaseVmActivity<ActivityPipBinding, PipViewModel>() {

    override fun getContentLayoutId() = R.layout.activity_pip

    override fun initVariableId() = BR.viewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initWidget() {
        super.initWidget()
        // 事件 Pip
        this.mBinding.btnPip.setOnClickListener {
            val builder = PictureInPictureParams.Builder()
            enterPictureInPictureMode(builder.build())
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        // 验证 画中画模式
        if (isInPictureInPictureMode) {

        } else {

        }
        Log.d(
            "zzz",
            "onPictureInPictureModeChanged() called with: isInPictureInPictureMode = $isInPictureInPictureMode, newConfig = $newConfig"
        )
    }
}
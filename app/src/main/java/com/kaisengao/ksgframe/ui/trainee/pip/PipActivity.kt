package com.kaisengao.ksgframe.ui.trainee.pip

import android.app.PictureInPictureParams
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.library.baseAdapters.BR
import com.kaisengao.base.util.ToastUtil
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.databinding.ActivityPipBinding
import com.kaisengao.mvvm.base.activity.BaseVmActivity
import com.petterp.floatingx.FloatingX
import com.petterp.floatingx.util.createFx

/**
 * @ClassName: PipActivity
 * @Author: GaoXin
 * @CreateDate: 2022/12/2 17:43
 * @Description: 8.0 画中画模式
 */
class PipActivity : BaseVmActivity<ActivityPipBinding, PipViewModel>() {

    override fun getContentLayoutId() = R.layout.activity_pip

    override fun initVariableId() = BR.viewModel

    override fun initWidget() {
        super.initWidget()
        // 事件 8.0Pip
        this.mBinding.btnPip.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val builder = PictureInPictureParams.Builder()
                enterPictureInPictureMode(builder.build())
            } else {
                ToastUtil.showShort("仅8.0以后版本支持画中画哦~")
            }
        }
        // 事件 EasyFloat-> 应用内悬浮窗
        var appFloatShow = renewAppPip()
        this.mBinding.btnEfAppPip.setOnClickListener {
            if (appFloatShow) {
                FloatingX.control().hide()
            } else {
                FloatingX.control().show()
            }
            appFloatShow = renewAppPip()
        }
    }

    private fun renewAppPip(): Boolean {
        val appFloatShow = FloatingX.control().isShow()
        if (appFloatShow) {
            this.mBinding.btnEfAppPip.text = "EasyFloat-> 应用内悬浮窗（关闭）"
        } else {
            this.mBinding.btnEfAppPip.text = "EasyFloat-> 应用内悬浮窗（开启）"
        }
        return appFloatShow
    }
}
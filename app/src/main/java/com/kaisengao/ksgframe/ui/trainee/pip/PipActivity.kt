package com.kaisengao.ksgframe.ui.trainee.pip

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.View
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
//        var appFloatShow = renewAppPip()
        this.mBinding.btnEfAppPip.setOnClickListener {
//            if (appFloatShow) {
//                FloatingX.control().hide()
//            } else {
//                FloatingX.control().show()
//            }
//            appFloatShow = renewAppPip()
        }
    }
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
//        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)

        // 验证 画中画模式
        if (isInPictureInPictureMode) {
            // CoverManager
            // Container
//            this.mBinding.playerContainerFull.visibility = View.VISIBLE
//            mRoomPlayerView.getPlayer().bindContainer(mBinding.playerContainerFull)
            this.mBinding.btnPip.visibility = View.GONE
            this.mBinding.btnEfAppPip.visibility = View.GONE
        } else {
            this.mBinding.btnPip.visibility = View.VISIBLE
            this.mBinding.btnEfAppPip.visibility = View.VISIBLE

//            this.mBinding.playerContainerFull.visibility = View.GONE
//            mRoomPlayerView.getPlayer().bindContainer(mBinding.playerContainer)
        }
        Log.d(
            "zzz",
            "onPictureInPictureModeChanged() called with: isInPictureInPictureMode = $isInPictureInPictureMode, newConfig = $newConfig"
        )

//        val container = findViewById<FrameLayout>(R.id.local_video_view_container)
//        val surfaceView = container.getChildAt(0) as SurfaceView
//        surfaceView.setZOrderMediaOverlay(!isInPictureInPictureMode)
//        surfaceView.visibility = if (isInPictureInPictureMode) View.GONE else View.VISIBLE
//        container.visibility = if (isInPictureInPictureMode) View.GONE else View.VISIBLE
    }
//    private fun renewAppPip(): Boolean {
//        val appFloatShow = FloatingX.control().isShow()
//        if (appFloatShow) {
//            this.mBinding.btnEfAppPip.text = "EasyFloat-> 应用内悬浮窗（关闭）"
//        } else {
//            this.mBinding.btnEfAppPip.text = "EasyFloat-> 应用内悬浮窗（开启）"
//        }
//        return appFloatShow
//    }
}
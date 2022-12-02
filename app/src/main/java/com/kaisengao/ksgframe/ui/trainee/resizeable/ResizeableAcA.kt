package com.kaisengao.ksgframe.ui.trainee.resizeable

import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.databinding.ActivityResizeableABinding
import com.kaisengao.mvvm.base.activity.BaseActivity

/**
 * @ClassName: ResizeableAcA
 * @Author: GaoXin
 * @CreateDate: 2022/10/24 16:49
 * @Description: 折叠屏适配 （分屏模式）
 */
class ResizeableAcA : BaseActivity<ActivityResizeableABinding>() {

    override fun getContentLayoutId(): Int = R.layout.activity_resizeable_a

    override fun initWidget() {
        super.initWidget()
        // TO B
        this.mBinding.resizeableA.setOnClickListener {
            this.startActivity(Intent(this, ResizeableAcB::class.java))
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(
            "LOG_resizeable", "ResizeableAcA newConfig.screenHeightDp:" + newConfig.screenHeightDp
                .toString() + ", newConfig.screenWidthDp" + newConfig.screenWidthDp
        )
    }
}
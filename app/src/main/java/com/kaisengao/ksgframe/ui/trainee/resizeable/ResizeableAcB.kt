package com.kaisengao.ksgframe.ui.trainee.resizeable

import android.content.res.Configuration
import android.util.Log
import com.kaisengao.ksgframe.R
import com.kaisengao.ksgframe.databinding.ActivityResizeableABinding
import com.kaisengao.ksgframe.databinding.ActivityResizeableBBinding
import com.kaisengao.mvvm.base.activity.BaseActivity

/**
 * @ClassName: ResizeableAcB
 * @Author: GaoXin
 * @CreateDate: 2022/10/24 16:49
 * @Description: 折叠屏适配 （分屏模式）
 */
class ResizeableAcB : BaseActivity<ActivityResizeableBBinding>() {

    override fun getContentLayoutId(): Int = R.layout.activity_resizeable_b

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(
            "LOG_resizeable", "ResizeableAcB newConfig.screenHeightDp:" + newConfig.screenHeightDp
                .toString() + ", newConfig.screenWidthDp" + newConfig.screenWidthDp
        )
    }
}
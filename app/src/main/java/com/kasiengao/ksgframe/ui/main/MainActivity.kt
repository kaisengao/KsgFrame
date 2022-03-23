package com.kasiengao.ksgframe.ui.main

import androidx.core.view.GravityCompat
import com.kaisengao.base.util.StatusBarUtil
import com.kaisengao.mvvm.base.activity.BaseVmActivity
import com.kasiengao.ksgframe.BR
import com.kasiengao.ksgframe.R
import com.kasiengao.ksgframe.databinding.ActivityMainBinding
import com.kasiengao.ksgframe.ui.main.viewmodel.MainViewModel
import com.kasiengao.ksgframe.ui.trainee.TraineeActivity


/**
 * @ClassName: MainActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 15:36
 * @Description: 启动页面
 */
class MainActivity : BaseVmActivity<ActivityMainBinding, MainViewModel>() {

    override fun getContentLayoutId(): Int = R.layout.activity_main

    override fun initVariableId(): Int = BR.viewModel

    override fun initBefore() {
        super.initBefore()
        // 添加状态栏高度 Padding
        StatusBarUtil.setStatusBarPadding(this, mBinding.mainAppbar)
    }

    override fun initWidget() {
        super.initWidget()

        mBinding.mainBottomBar.setNavigationOnClickListener {
            mBinding.drawerLayout.openDrawer(GravityCompat.START)
        }

        mBinding.mainTrainee.setOnClickListener {
            TraineeActivity.startActivity(this)
        }
    }
}

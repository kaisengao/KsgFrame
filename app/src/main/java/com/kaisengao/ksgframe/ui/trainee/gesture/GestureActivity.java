package com.kaisengao.ksgframe.ui.trainee.gesture;

import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kaisengao.ksgframe.BR;
import com.kaisengao.ksgframe.R;
import com.kaisengao.ksgframe.databinding.ActivityGestureBinding;

/**
 * @ClassName: GestureActivity
 * @Author: KaiSenGao
 * @CreateDate: 2021/7/7 14:06
 * @Description:
 */
public class GestureActivity extends BaseVmActivity<ActivityGestureBinding, GestureViewModel> {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_gesture;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
package com.kasiengao.ksgframe.ui.trainee.gesture;

import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kasiengao.ksgframe.BR;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.databinding.ActivityGestureBinding;

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
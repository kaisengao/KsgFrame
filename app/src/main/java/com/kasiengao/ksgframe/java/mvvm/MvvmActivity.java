package com.kasiengao.ksgframe.java.mvvm;

import androidx.databinding.library.baseAdapters.BR;

import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.databinding.MvvmBinding;

/**
 * @ClassName: MvvmActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/11 13:02
 * @Description: MVVM
 */
public class MvvmActivity extends BaseVmActivity<MvvmBinding, MvvmViewModel> {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mvvm;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}

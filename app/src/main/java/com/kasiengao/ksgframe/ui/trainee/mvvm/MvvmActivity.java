package com.kasiengao.ksgframe.ui.trainee.mvvm;

import androidx.databinding.library.baseAdapters.BR;

import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kaisengao.base.annotations.ReloadAnnotations;
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

    /**
     * LoadSir 点击事件回调
     *
     * @param target 绑定的View
     */
    @ReloadAnnotations
    public void onLoadSirReload(Object target) {
        if (target == mBinding.loadRoot1) {
            this.mBinding.loadBtn1.performClick();
        } else if (target == mBinding.loadRoot2) {
            this.mBinding.loadBtn2.performClick();
        }
    }
}

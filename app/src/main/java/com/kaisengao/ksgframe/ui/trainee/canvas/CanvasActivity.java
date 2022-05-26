package com.kaisengao.ksgframe.ui.trainee.canvas;

import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kaisengao.ksgframe.BR;
import com.kaisengao.ksgframe.R;
import com.kaisengao.ksgframe.databinding.ActivityCanvasKsgBinding;

/**
 * @ClassName: CanvasActivity
 * @Author: KaiSenGao
 * @CreateDate: 2021/10/25 10:29
 * @Description: 绘制
 */
public class CanvasActivity extends BaseVmActivity<ActivityCanvasKsgBinding, CanvasViewModel> {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_canvas_ksg;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


}
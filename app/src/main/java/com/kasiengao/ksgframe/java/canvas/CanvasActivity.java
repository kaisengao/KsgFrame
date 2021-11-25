package com.kasiengao.ksgframe.java.canvas;

import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kasiengao.ksgframe.BR;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.databinding.ActivityCanvasKsgBinding;

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
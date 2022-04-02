package com.kasiengao.ksgframe.ui.trainee.retrofit;

import com.kaisengao.mvvm.base.activity.BaseVmActivity;
import com.kasiengao.ksgframe.BR;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.databinding.ActivityRxRetrofitBinding;

/**
 * @ClassName: RxRetrofitActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 18:46
 * @Description: Rx+Retrofit
 */
public class RxRetrofitActivity extends BaseVmActivity<ActivityRxRetrofitBinding, RxRetrofitViewModel> {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_rx_retrofit;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

}

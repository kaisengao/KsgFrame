package com.kaisengao.mvvm.base.fragment;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.kaisengao.mvvm.base.viewmodel.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @ClassName: BaseVmFragment
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/22 17:16
 * @Description: MVVM BaseViewModelFragment
 */
public abstract class BaseVmFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment<DB> {

    protected VM mViewModel;

    @Override
    protected void initWidget() {
        super.initWidget();
        // 绑定 ViewModel
        this.mViewModel = bindViewModel(mViewModel);
    }

    /**
     * 初始化 ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 绑定 ViewModel
     *
     * @param viewModel VM
     * @return VM
     */
    private VM bindViewModel(VM viewModel) {
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                modelClass = BaseViewModel.class;
            }
            // noinspection unchecked
            viewModel = (VM) createViewModel(initVariableId(), modelClass);
        }
        // 绑定Lifecycle
        this.mBinding.setLifecycleOwner(this);
        // return
        return viewModel;
    }

    /**
     * 创建 ViewModel
     *
     * @param modelClass 泛型类型
     * @param <OVM>      BaseViewModel
     * @return ViewModel
     */
    protected <OVM extends BaseViewModel> OVM createViewModel(int variableId, Class<OVM> modelClass) {
        // 创建ViewModel
        OVM viewModel = new ViewModelProvider(this).get(modelClass);
        // 绑定ViewModel
        this.mBinding.setVariable(variableId, viewModel);
        // return
        return viewModel;
    }

}

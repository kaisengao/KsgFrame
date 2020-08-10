package com.kaisengao.mvvm.base.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.kaisengao.mvvm.R;
import com.kaisengao.mvvm.base.viewmodel.BaseViewModel;
import com.kaisengao.mvvm.viewmodel.ToolbarViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @ClassName: BaseVmActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/6/17 10:32
 * @Description: MVVM BaseViewModelActivity
 */
public abstract class BaseVmActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity<DB> {

    protected VM mViewModel;

    @Override
    protected void initBefore() {
        super.initBefore();
        // 绑定 ViewModel
        this.mViewModel = bindViewModel(mViewModel);
        // Init Toolbar
        this.initToolbar();
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

    /**
     * 子类Activity重写该方法可以设置是否添加Toolbar
     *
     * @return 默认返回true表示添加Toolbar，如不需要Toolbar，则重写该方法返回false
     */
    protected boolean isDisplayToolbar() {
        return true;
    }

    /**
     * 得到当前界面Toolbar的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected int getToolbarLayoutId() {
        return R.layout.layout_toolbar;
    }

    /**
     * 将Toolbar布局添加到容器中
     */
    private void initContentView(View root) {
        // 获取Ac父容器content
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        // 创建一个垂直线性布局
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        // 将线性布局添加入父容器中，作为Ac页面布局的父容器
        viewGroup.addView(parentLayout);
        // 将Toolbar添加到父容器布局中
        getLayoutInflater().inflate(getToolbarLayoutId(), parentLayout);
        // 将ContentLayout添加到父容器布局中
        parentLayout.addView(root);
    }

    /**
     * Init Toolbar
     */
    private void initToolbar() {
        if (isDisplayToolbar()) {
            this.initContentView(mBinding.getRoot());
        }
        // Init Toolbar VM
        this.initToolbarVm();
    }

    /**
     * Init Toolbar VM
     */
    private void initToolbarVm() {
        // 验证 是否继承了 ToolbarViewModel
        if (mViewModel instanceof ToolbarViewModel) {
            ToolbarViewModel toolbarViewModel = (ToolbarViewModel) mViewModel;
            // Back事件
            toolbarViewModel.getBackPressed().observe(this, aVoid -> this.onBackPressed());
        }
    }
}

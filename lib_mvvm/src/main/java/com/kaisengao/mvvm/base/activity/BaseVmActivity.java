package com.kaisengao.mvvm.base.activity;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.kaisengao.base.util.StatusBarUtil;
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

    private MaterialToolbar mToolbar;

    @Override
    protected void initBefore() {
        super.initBefore();
        // 绑定 ViewModel
        this.bindViewModel(mViewModel);
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
     */
    private void bindViewModel(VM viewModel) {
        if (viewModel == null) {
            Class<BaseViewModel> modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                // noinspection unchecked
                modelClass = (Class<BaseViewModel>) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                modelClass = BaseViewModel.class;
            }
            // noinspection unchecked
            viewModel = (VM) createViewModel(initVariableId(), modelClass);
        }
        this.mViewModel = viewModel;
        // 绑定Lifecycle
        this.mBinding.setLifecycleOwner(this);
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
     * 得到当前界面Toolbar的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    private int getToolbarLayoutId() {
        return R.layout.layout_toolbar;
    }

    /**
     * Init Toolbar
     */
    private void initToolbar() {
        if (mViewModel instanceof ToolbarViewModel) {
            // Toolbar
            this.mToolbar = findViewById(R.id.toolbar);
            // 非空验证
            if (mToolbar == null) {
                // 将Toolbar布局添加到容器中
                this.initContentView();
            }
            // Init Toolbar VM
            this.initToolbarVm();
        }
    }

    /**
     * 将Toolbar布局添加到容器中
     */
    private void initContentView() {
        // 获取Ac父容器content
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        // 创建一个垂直线性布局
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        // 将线性布局添加入父容器中，作为Ac页面布局的父容器
        viewGroup.addView(parentLayout);
        // 将Toolbar添加到父容器布局中
        this.getLayoutInflater().inflate(getToolbarLayoutId(), parentLayout);
        // 将ContentLayout添加到父容器布局中
        parentLayout.addView(mBinding.getRoot());
        // 添加状态栏高度
        StatusBarUtil.setStatusBarPadding(this, parentLayout.getChildAt(0));
        // Toolbar
        this.mToolbar = findViewById(R.id.toolbar);
    }

    /**
     * Init Toolbar VM
     */
    private void initToolbarVm() {
        if (mToolbar != null) {
            this.setSupportActionBar(mToolbar);
        }
        // ViewModel
        ToolbarViewModel viewModel = (ToolbarViewModel) mViewModel;
        // Title
        viewModel.getTitle().observe(this, title -> {
            if (!TextUtils.isEmpty(title) && mToolbar != null) {
                this.setTitle(title);
            }
        });
        // 设置导航资源
        viewModel.getNavigationIcon().observe(this, resId -> {
            if (resId != null && mToolbar != null) {
                if (resId <= 0) {
                    this.mToolbar.setNavigationIcon(null);
                } else {
                    this.mToolbar.setNavigationIcon(resId);
                }
            }
        });
        // 设置菜单资源
        viewModel.getMenuRes().observe(this, resId -> {
            if ((resId != null && resId > 0) && mToolbar != null) {
                this.mToolbar.inflateMenu(resId);
            }
        });
        // Back事件
        viewModel.getBackPressed().observe(this, aVoid -> this.onClickBack());
    }

    /**
     * 菜单的响应事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onClickBack();
            return true;
        }
        return onOptionsItemSelected(item);
    }

    /**
     * 返回键
     */
    protected void onClickBack() {
        this.onBackPressed();
    }
}

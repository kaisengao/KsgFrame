package com.kasiengao.mvp.java;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;

import com.google.android.material.appbar.MaterialToolbar;
import com.kaisengao.base.util.StatusBarUtil;
import com.kasiengao.mvp.R;

/**
 * @ClassName: BaseToolbarActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 14:33
 * @Description: 带有 ToolBar 的 Activity基类
 */
public abstract class BaseToolbarActivity extends BaseActivity {

    private MaterialToolbar mToolbar;

    private View mContentLayout;

    private LinearLayout mParentLinearLayout;

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        if (layoutResId == 0) {
            return;
        }
        if (isDisplayToolbar()) {
            this.initContentView(layoutResId);
        } else {
            super.setContentView(layoutResId);
        }
    }

    /**
     * 将Toolbar布局添加到容器中
     *
     * @param layoutResId 内容布局Id
     */
    private void initContentView(@LayoutRes int layoutResId) {
        // 获取Ac父容器content
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        // 创建一个垂直线性布局
        this.mParentLinearLayout = new LinearLayout(this);
        this.mParentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        // 将线性布局添加入父容器中，作为Ac页面布局的父容器
        viewGroup.addView(mParentLinearLayout);
        // 将Toolbar添加到父容器布局中
        this.getLayoutInflater().inflate(getToolbarLayoutId(), mParentLinearLayout);
        // 将ContentLayout添加到父容器布局中
        this.getLayoutInflater().inflate(layoutResId, mParentLinearLayout);
        // 获取ContentLayout的View 以作为LoadSir的注册布局
        this.mContentLayout = mParentLinearLayout.getChildAt(1);
        // Padding一下状态栏高度
        StatusBarUtil.setStatusBarPadding(this, mParentLinearLayout.getChildAt(0));
    }

    /**
     * 子类Activity重写该方法可以设置是否显示Toolbar
     *
     * @return 默认返回true表示显示Toolbar，如不需要Toolbar，则重写该方法返回false
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
     * 返回LoadSir需要覆盖的布局
     *
     * @return 默认返回true表示显示覆盖 toolbar以下的布局
     */
    @Override
    protected Object getInflate() {
        if (mParentLinearLayout != null && mParentLinearLayout.getChildCount() > 0) {
            return isDisplayToolbar() ? mContentLayout : super.getInflate();
        } else {
            return super.getInflate();
        }
    }

    /**
     * 得到当前页面 View
     *
     * @return ViewGroup
     */
    protected ViewGroup getContentView() {
        return mParentLinearLayout;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        // 初始化Toolbar
        this.initToolbar();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        this.mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            this.setSupportActionBar(mToolbar);
        }
    }

    /**
     * 修改左侧返回箭头图标
     *
     * @param resId 资源
     */
    protected void setNavigationIcon(@DrawableRes int resId) {
        if (mToolbar != null) {
            this.mToolbar.setNavigationIcon(resId);
        }
    }

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

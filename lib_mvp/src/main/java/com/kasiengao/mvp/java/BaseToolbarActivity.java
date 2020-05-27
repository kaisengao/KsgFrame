package com.kasiengao.mvp.java;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.kasiengao.base.util.StatusBarUtil;
import com.kasiengao.mvp.R;

/**
 * @ClassName: BaseToolbarActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 14:33
 * @Description: 带有 ToolBar 的 Activity基类
 */
public abstract class BaseToolbarActivity extends BaseActivity {

    private Toolbar mToolbar;

    private ActionBar mActionBar;

    private View mContentLayout;

    private LinearLayout mParentLinearLayout;

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        if (layoutResId == 0) {
            return;
        }
        if (this.isDisplayToolbar()) {
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
        ViewGroup viewGroup = this.findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        // 创建一个垂直线性布局
        this.mParentLinearLayout = new LinearLayout(this);
        this.mParentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        // 将线性布局添加入父容器中，作为Ac页面布局的父容器
        viewGroup.addView(this.mParentLinearLayout);
        // 将Toolbar添加到父容器布局中
        this.getLayoutInflater().inflate(getToolbarLayoutId(), mParentLinearLayout);
        if (this.mParentLinearLayout.getChildCount() > 0) {
            // 沉浸式状态栏 添加padding高度
            StatusBarUtil.setPaddingSmart(this, this.mParentLinearLayout.getChildAt(0));
            StatusBarUtil.setStatusBarColor(this, R.color.white);
            StatusBarUtil.darkMode(this);
        } else {
            // 如果没有添加Toolbar就移除沉浸式效果
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 将ContentLayout添加到父容器布局中
        this.getLayoutInflater().inflate(layoutResId, this.mParentLinearLayout);
        // 获取ContentLayout的View 以作为LoadSir的注册布局
        this.mContentLayout = this.mParentLinearLayout.getChildAt(1);
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
        return R.layout.toolbar;
    }

    /**
     * 返回LoadSir需要覆盖的布局
     *
     * @return 默认返回true表示显示覆盖 toolbar以下的布局
     */
    @Override
    protected Object getInflate() {
        if (this.mParentLinearLayout != null && this.mParentLinearLayout.getChildCount() > 0) {
            return this.isDisplayToolbar() ? this.mContentLayout : super.getInflate();
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
        return this.mParentLinearLayout;
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
        this.mToolbar = this.findViewById(R.id.toolbar);
        if (this.mToolbar != null) {
            this.setSupportActionBar(this.mToolbar);
            this.mActionBar = this.getSupportActionBar();
            this.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 修改左侧返回箭头图标
     *
     * @param resId 资源
     */
    protected void setNavigationIcon(@DrawableRes int resId) {
        if (this.mToolbar != null) {
            this.mToolbar.setNavigationIcon(resId);
        }
    }

    /**
     * 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
     *
     * @param showHomeAsUp true/false
     */
    protected void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        if (this.mActionBar != null) {
            this.mActionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onClickBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 返回键
     */
    protected void onClickBack() {
        this.finish();
    }
}

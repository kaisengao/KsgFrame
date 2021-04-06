package com.kasiengao.mvp.kt

import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.kaisengao.base.util.StatusBarUtil
import com.kasiengao.mvp.R

/**
 * @ClassName: BaseToolbarActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 16:55
 * @Description: 带有 ToolBar 的 Activity基类
 */
abstract class BaseToolbarActivity : BaseActivity() {

    private var mToolbar: Toolbar? = null

    private var mActionBar: ActionBar? = null

    private var mContentLayout: View? = null

    private var mParentLinearLayout: LinearLayout? = null

    override fun setContentView(@LayoutRes layoutResId: Int) {
        if (layoutResId == 0) {
            return
        }
        if (this.isDisplayToolbar()) {
            this.initContentView(layoutResId)
        } else {
            super.setContentView(layoutResId)
        }
    }

    /**
     * 将Toolbar布局添加到容器中
     *
     * @param layoutResId 内容布局Id
     */
    private fun initContentView(@LayoutRes layoutResId: Int) {
        // 获取Ac父容器content
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        viewGroup.removeAllViews()
        // 创建一个垂直线性布局
        this.mParentLinearLayout = LinearLayout(this)
        this.mParentLinearLayout!!.orientation = LinearLayout.VERTICAL
        // 将线性布局添加入父容器中，作为Ac页面布局的父容器
        viewGroup.addView(this.mParentLinearLayout)
        // Padding一下状态栏高度
        StatusBarUtil.setPaddingSmart(this, mParentLinearLayout)
        // 将Toolbar添加到父容器布局中
        this.layoutInflater.inflate(getToolbarLayoutId(), this.mParentLinearLayout)
        // 将ContentLayout添加到父容器布局中
        this.layoutInflater.inflate(layoutResId, this.mParentLinearLayout)
        // 获取ContentLayout的View 以作为LoadSir的注册布局
        this.mContentLayout = this.mParentLinearLayout!!.getChildAt(1)
    }

    /**
     * 子类Activity重写该方法可以设置是否显示Toolbar
     *
     * @return 默认返回true表示显示Toolbar，如不需要Toolbar，则重写该方法返回false
     */
    protected open fun isDisplayToolbar(): Boolean = true

    /**
     * 得到当前界面Toolbar的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected open fun getToolbarLayoutId(): Int = R.layout.toolbar

    /**
     * 返回LoadSir需要覆盖的布局
     *
     * @return 默认返回true表示显示覆盖 toolbar以下的布局
     */
    override fun getInflate(): Any? =
        if (this.mParentLinearLayout != null && this.mParentLinearLayout!!.childCount > 0) {
            if (this.isDisplayToolbar()) this.mContentLayout else super.getInflate()
        } else {
            super.getInflate()
        }

    /**
     * 得到当前页面 View
     *
     * @return ViewGroup
     */
    protected open fun getContentView(): ViewGroup? = this.mParentLinearLayout

    /**
     * 初始化控件
     */
    override fun initWidget() {
        super.initWidget()
        // 初始化Toolbar
        this.initToolbar()
    }

    /**
     * 初始化Toolbar
     */
    private fun initToolbar() {
        this.mToolbar = findViewById(R.id.toolbar)
        if (this.mToolbar != null) {
            this.setSupportActionBar(this.mToolbar)
            this.mActionBar = this.supportActionBar
            this.setDisplayHomeAsUpEnabled(true)
        }
    }

    /**
     * 修改左侧返回箭头图标
     *
     * @param resId 资源
     */
    protected open fun setNavigationIcon(@DrawableRes resId: Int) {
        if (this.mToolbar != null) {
            this.mToolbar!!.setNavigationIcon(resId)
        }
    }

    /**
     * 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
     *
     * @param showHomeAsUp true/false
     */
    protected open fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean) {
        if (this.mActionBar != null) {
            this.mActionBar!!.setDisplayHomeAsUpEnabled(showHomeAsUp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
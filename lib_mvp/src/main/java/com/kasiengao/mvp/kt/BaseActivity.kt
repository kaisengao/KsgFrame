package com.kasiengao.mvp.kt

import android.os.Bundle
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * @ClassName: BaseActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 16:14
 * @Description:
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initWindow()
        this.initArgs(intent.extras)
        this.setContentView(getContentLayoutId())
        this.initBefore()
        this.initWidget()
        this.initData()
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract fun getContentLayoutId(): Int

    /**
     * 初始化窗口
     */
    protected open fun initWindow() {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     */
    protected open fun initArgs(bundle: Bundle?) {}

    /**
     * 初始化控件调用之前
     */
    protected open fun initBefore() {
    }

    /**
     * 初始化控件
     */
    protected open fun initWidget() {
    }

    /**
     * 初始化数据
     */
    protected open fun initData() {}

    /**
     * 返回LoadSir需要覆盖的布局
     *
     * @return 默认返回true表示显示覆盖 toolbar以下的布局
     */
    protected open fun getInflate(): Any? = this
}
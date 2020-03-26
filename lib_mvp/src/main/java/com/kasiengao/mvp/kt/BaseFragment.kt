package com.kasiengao.mvp.kt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * @ClassName: BaseFragment
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 18:15
 * @Description: Fragment 的基类 Base层
 */
abstract class BaseFragment : Fragment() {

    private var mContentView: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 初始化相关参数
        this.initArgs(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mContentView = inflater.inflate(getContentLayoutId(), container, false)
        // 初始化控件
        this.initWidget()
        return this.mContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 初始化数据
        this.initData()
    }

    protected open fun getContentView(): View? {
        return this.mContentView
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract fun getContentLayoutId(): Int

    /**
     * 初始化相关参数
     */
    protected open fun initArgs(bundle: Bundle?) {}

    /**
     * 初始化控件
     */
    protected open fun initWidget() {}

    /**
     * 初始化数据
     */
    protected open fun initData() {}

}
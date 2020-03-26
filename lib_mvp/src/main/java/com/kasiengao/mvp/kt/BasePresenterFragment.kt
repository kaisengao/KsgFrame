package com.kasiengao.mvp.kt

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.kasiengao.base.util.ToastUtil

/**
 * @ClassName: BasePresenterFragment
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 18:17
 * @Description: 带有 Presenter 的 Fragment基类
 */
abstract class BasePresenterFragment<Presenter : BaseContract.BasePresenter<*>> : BaseFragment(),
    BaseContract.BaseView {

    /**
     * 是否是第一次加载数据
     */
    private var isFirstLoad = true

    protected var mPresenter: Presenter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.mPresenter = initPresenter()
        @Suppress("UNREACHABLE_CODE", "CAST_NEVER_SUCCEEDS")
        this.mPresenter?.attachView(this as Nothing)
    }

    override fun onResume() {
        super.onResume()
        if (this.isFirstLoad) {
            this.onFirstInit()
            this.isFirstLoad = false
        }
    }

    /**
     * 当首次初始化数据的时候会调用的方法
     */
    protected abstract fun onFirstInit()

    /**
     * 初始化Presenter
     *
     * @return Presenter
     */
    abstract fun initPresenter(): Presenter

    /**
     * 显示一个Toast
     *
     * @param message Toast信息
     */
    override fun showToast(message: String?) {
        ToastUtil.showLongSafe(message)
    }

    override val context: FragmentActivity?
        get() = super.getActivity()

    override val lifecycleOwner: LifecycleOwner?
        get() = super.getViewLifecycleOwner()

    override fun onDestroyView() {
        super.onDestroyView()
        // 销毁视图后 初始化懒加载
        this.isFirstLoad = true
    }

    override fun onDestroy() {
        super.onDestroy()

        this.mPresenter?.detachView()
    }
}
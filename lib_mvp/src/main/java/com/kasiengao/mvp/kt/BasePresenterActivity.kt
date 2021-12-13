package com.kasiengao.mvp.kt

import android.content.Context
import androidx.lifecycle.LifecycleOwner


/**
 * @ClassName: BasePresenterActivity
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 17:04
 * @Description: 带有Presenter 的 Activity基类
 */
abstract class BasePresenterActivity<Presenter : BaseContract.BasePresenter<*>> :
    BaseToolbarActivity(), BaseContract.BaseView {

    protected var mPresenter: Presenter? = null

    override fun initBefore() {
        super.initBefore()

        this.mPresenter = initPresenter()
        @Suppress("UNREACHABLE_CODE", "CAST_NEVER_SUCCEEDS")
        this.mPresenter?.attachView(this as Nothing)
    }

    /**
     * 初始化Presenter
     *
     * @return Presenter
     */
    abstract fun initPresenter(): Presenter

    override val context: Context?
        get() = this

    override val lifecycleOwner: LifecycleOwner?
        get() = this

    override fun onDestroy() {
        super.onDestroy()

        this.mPresenter?.detachView()
    }
}
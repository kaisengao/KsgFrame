package com.kasiengao.mvp.kt

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

/**
 * @ClassName: BasePresenter
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 17:00
 * @Description:
 */
public open class BasePresenter<V : BaseContract.BaseView> : BaseContract.BasePresenter<V> {

    private var mView: V? = null

    private var mWeakReference: WeakReference<V>? = null

    /**
     * 给子类使用的获取View的操作
     * 不允许复写
     *
     * @return View
     */
    public fun getView(): V = this.mView!!

    override fun attachView(rootView: V) {
        this.mView = rootView
        this.mWeakReference = WeakReference(rootView)
    }

    override fun detachView() {
        this.mView = null
        this.mWeakReference!!.clear()
    }

    protected fun getContext(): Context? = this.mView!!.context

    protected fun getLifecycleOwner(): LifecycleOwner? = this.mView!!.lifecycleOwner
}
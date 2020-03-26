package com.kasiengao.mvp.kt

import android.content.Context
import androidx.lifecycle.LifecycleOwner

/**
 * @ClassName: BaseContract
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 17:00
 * @Description:
 */
interface BaseContract {

    interface BaseView {
        /**
         * 上下文
         *
         * @return Context
         */
        val context: Context?

        /**
         * 显示一个Toast
         *
         * @param message Toast信息
         */
        fun showToast(message: String?)

        /**
         * LifecycleOwner
         *
         * @return LifecycleOwner
         */
        val lifecycleOwner: LifecycleOwner?
    }

    interface BasePresenter<V : BaseView> {
        /**
         * 绑定View
         *
         * @param rootView View
         */
        fun attachView(rootView: V)

        /**
         * 解绑View
         */
        fun detachView()
    }
}
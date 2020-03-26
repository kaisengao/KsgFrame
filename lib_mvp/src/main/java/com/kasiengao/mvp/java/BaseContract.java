package com.kasiengao.mvp.java;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

/**
 * @ClassName: BaseContract
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/26 16:00
 * @Description:
 */
public interface BaseContract {

    interface BaseView {

        /**
         * 上下文
         *
         * @return Context
         */
        Context getContext();

        /**
         * 显示一个Toast
         *
         * @param message Toast信息
         */
        void showToast(String message);

        /**
         * LifecycleOwner
         *
         * @return LifecycleOwner
         */
        LifecycleOwner getLifecycleOwner();
    }

    interface BasePresenter<V extends BaseView> {

        /**
         * 绑定View
         *
         * @param rootView View
         */
        void attachView(V rootView);

        /**
         * 解绑View
         */
        void detachView();
    }

}

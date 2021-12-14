package com.kasiengao.ksgframe.ui.trainee.mvp;

import com.kasiengao.mvp.java.BaseContract;

/**
 * @ClassName: MvpContract
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/27 23:55
 * @Description: 契约逻辑处理类
 */
public interface MvpContract {

    interface IView extends BaseContract.BaseView {

        /**
         * 返回 预告视频列表
         *
         * @param trailerBean 预告视频列表
         */
        void resultTrailerList(TrailerBean trailerBean);
    }

    interface IPresenter extends BaseContract.BasePresenter<IView> {

        /**
         * 请求 预告视频列表
         */
        void requestTrailerList();
    }
}

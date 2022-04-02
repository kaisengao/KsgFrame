package com.kasiengao.ksgframe.ui.trainee.mvp;

import com.kasiengao.ksgframe.ui.trainee.bean.VideoBean;
import com.kasiengao.mvp.java.BaseContract;

import java.util.List;

/**
 * @ClassName: MvpContract
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/27 23:55
 * @Description: MVP
 */
public interface MvpContract {

    interface IView extends BaseContract.BaseView {

        /**
         * 返回 预告视频列表
         *
         * @param videos 预告视频列表
         */
        void resultVideos(List<VideoBean> videos);
    }

    interface IPresenter extends BaseContract.BasePresenter<IView> {

        /**
         * 请求 预告视频列表
         */
        void requestVideos();
    }
}

package com.kaisengao.ksgframe.ui.trainee.mvp;

import com.kasiengao.mvp.java.BasePresenter;

/**
 * @ClassName: MvpPresenter
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/27 23:59
 * @Description: MVP
 */
public class MvpPresenter extends BasePresenter<MvpContract.IView> implements MvpContract.IPresenter {

    private final MvpModel mModel;

    public MvpPresenter() {
        this.mModel = new MvpModel();
    }

    /**
     * 请求 预告视频列表
     */
    @Override
    public void requestVideos() {
        // Model层生成数据
        this.getView().resultVideos(mModel.requestVideos());
    }
}

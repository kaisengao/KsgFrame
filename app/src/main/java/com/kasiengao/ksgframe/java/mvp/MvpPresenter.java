package com.kasiengao.ksgframe.java.mvp;

import com.kasiengao.base.configure.ThreadPool;
import com.kasiengao.mvp.java.BasePresenter;

/**
 * @ClassName: MvpPresenter
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/27 23:59
 * @Description: 契约逻辑处理实现类
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
    public void requestTrailerList() {
        // Model层生成数据
        TrailerBean trailerBean = this.mModel.requestTrailerList();
        // 在此处理逻辑数据等.......

        // 回调View层 模拟假数据 延迟3秒
        ThreadPool.MainThreadHandler.getInstance().post(() -> {
            getView().resultTrailerList(trailerBean);
        }, 3000);
    }
}

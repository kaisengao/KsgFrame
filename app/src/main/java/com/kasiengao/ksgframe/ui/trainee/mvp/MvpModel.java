package com.kasiengao.ksgframe.ui.trainee.mvp;

import com.google.gson.Gson;
import com.kaisengao.base.factory.AppFactory;
import com.kaisengao.base.util.CommonUtil;

/**
 * @ClassName: MvpModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/28 0:00
 * @Description: 数据请求（数据生成）类
 */
public class MvpModel {

    private final Gson mJson;

    public MvpModel() {
        this.mJson = new Gson();
    }

    /**
     * 请求 预告视频列表
     *
     * @return 预告视频列表集合
     */
    public TrailerBean requestTrailerList() {
        // 解析Json
        return this.mJson.fromJson(CommonUtil.getAssetsJson(AppFactory.application(), "VideosData.json"), TrailerBean.class);
    }
}

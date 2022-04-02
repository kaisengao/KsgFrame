package com.kasiengao.ksgframe.ui.trainee.mvp;

import com.google.gson.reflect.TypeToken;
import com.kaisengao.base.factory.AppFactory;
import com.kaisengao.base.util.CommonUtil;
import com.kaisengao.retrofit.factory.GsonBuilderFactory;
import com.kasiengao.ksgframe.ui.trainee.bean.VideoBean;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @ClassName: MvpModel
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/28 0:00
 * @Description: MVP
 */
public class MvpModel {

    public MvpModel() {
    }

    /**
     * 请求 预告视频列表
     *
     * @return 预告视频列表集合
     */
    public List<VideoBean> requestVideos() {
        // 数据
        String json = CommonUtil.getAssetsJson(AppFactory.application(), "VideosData.json");
        // 解析
        Type type = new TypeToken<List<VideoBean>>() {
        }.getType();
        return GsonBuilderFactory.getInstance().fromJson(json, type);
    }
}

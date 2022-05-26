package com.kaisengao.ksgframe.ui.trainee.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: VideoBean
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/28 0:04
 * @Description: 预告视频实体类
 */
public class VideoBean {

    @SerializedName("movieName")
    private String mMovieName;
    @SerializedName("coverImg")
    private String mCoverImg;
    @SerializedName("heightUrl")
    private String mHeighUrl;
    @SerializedName("type")
    private List<String> mType;

    public String getMovieName() {
        return mMovieName;
    }

    public String getCoverImg() {
        return mCoverImg;
    }

    public String getHeighUrl() {
        return mHeighUrl;
    }

    public List<String> getType() {
        return mType;
    }

}

package com.kasiengao.ksgframe.java.mvp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: TrailerBean
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/28 0:04
 * @Description: 预告视频实体类
 */
public class TrailerBean {

    @SerializedName("trailers")
    private List<TrailersBean> mTrailers;

    public List<TrailersBean> getTrailers() {
        return mTrailers;
    }

    public static class TrailersBean {
        @SerializedName("movieName")
        private String mMovieName;
        @SerializedName("coverImg")
        private String mCoverImg;
        @SerializedName("hightUrl")
        private String mHighUrl;
        @SerializedName("type")
        private List<String> mType;

        public String getMovieName() {
            return mMovieName;
        }

        public String getCoverImg() {
            return mCoverImg;
        }

        public String getHighUrl() {
            return mHighUrl;
        }

        public List<String> getType() {
            return mType;
        }
    }

}

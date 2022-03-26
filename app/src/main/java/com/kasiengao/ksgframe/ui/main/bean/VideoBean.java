package com.kasiengao.ksgframe.ui.main.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: VideoBean
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/26 17:22
 * @Description:
 */
public class VideoBean {

    @SerializedName("trailers")
    private List<TrailersBean> mTrailers;

    public List<TrailersBean> getTrailers() {
        return mTrailers;
    }

    public static class TrailersBean {

        @SerializedName("movieName")
        private String mMovieName;
        @SerializedName("heightUrl")
        private String mVideoUrl;
        @SerializedName("videoTitle")
        private String mVideoTitle;
        @SerializedName("coverImg")
        private String mCoverImg;
        @SerializedName("type")
        private List<String> mType;

        public String getMovieName() {
            return mMovieName;
        }

        public String getVideoUrl() {
            return mVideoUrl;
        }

        public String getVideoTitle() {
            return mVideoTitle;
        }

        public String getCoverImg() {
            return mCoverImg;
        }

        public List<String> getType() {
            return mType;
        }
    }
}

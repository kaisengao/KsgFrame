package com.kasiengao.ksgframe.java.retrofit;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @ClassName: NewsTopBean
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 19:23
 * @Description: 聚合数据 新闻
 */
public class NewsTopBean {

    @SerializedName("resultcode")
    private String mResultCode;
    @SerializedName("reason")
    private String mReason;
    @SerializedName("result")
    private ResultBean mResult;

    public String getResultCode() {
        return mResultCode;
    }

    public String getReason() {
        return mReason;
    }

    public ResultBean getResult() {
        return mResult;
    }

    public static class ResultBean {

        @SerializedName("data")
        private List<DataBean> mData;

        public List<DataBean> getData() {
            return mData;
        }

        public static class DataBean {

            @SerializedName("author_name")
            private String mAuthorName;
            @SerializedName("category")
            private String mCategory;
            @SerializedName("date")
            private String mDate;
            @SerializedName("thumbnail_pic_s")
            private String mThumbnailPicS;
            @SerializedName("title")
            private String mTitle;
            @SerializedName("url")
            private String mUrl;

            public String getAuthorName() {
                return mAuthorName;
            }

            public String getCategory() {
                return mCategory;
            }

            public String getDate() {
                return mDate;
            }

            public String getThumbnailPicS() {
                return mThumbnailPicS;
            }

            public String getTitle() {
                return mTitle;
            }

            public String getUrl() {
                return mUrl;
            }

            @NotNull
            @Override
            public String toString() {
                return "DataBean{" +
                        "mAuthorName='" + mAuthorName + '\'' +
                        ", mCategory='" + mCategory + '\'' +
                        ", mDate='" + mDate + '\'' +
                        ", mThumbnailPicS='" + mThumbnailPicS + '\'' +
                        ", mTitle='" + mTitle + '\'' +
                        ", mUrl='" + mUrl + '\'' +
                        '}';
            }
        }

        @NotNull
        @Override
        public String toString() {
            return "ResultBean{" +
                    "mData=" + mData +
                    '}';
        }

    }

    @NotNull
    @Override
    public String toString() {
        return "NewsTopBean{" +
                "mResultCode='" + mResultCode + '\'' +
                ", mReason='" + mReason + '\'' +
                ", mResult=" + mResult +
                '}';
    }
}

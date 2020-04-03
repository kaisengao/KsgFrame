package com.kasiengao.ksgframe.java.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: NewsTopBean
 * @Author: KaiSenGao
 * @CreateDate: 2020/3/31 19:23
 * @Description: 聚合数据 新闻
 */
public class NewsTopBean {

    @SerializedName("error_code")
    private int mErrorCode;
    @SerializedName("reason")
    private String mReason;
    @SerializedName("result")
    private ResultBean mResult;

    public int getErrorCode() {
        return mErrorCode;
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

        @Override
        public String toString() {
            return "ResultBean{" +
                    "mData=" + mData +
                    '}';
        }

    }

    @Override
    public String toString() {
        return "NewsTopBean{" +
                "mErrorCode=" + mErrorCode +
                ", mReason='" + mReason + '\'' +
                ", mResult=" + mResult +
                '}';
    }
}

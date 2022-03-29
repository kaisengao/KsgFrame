package com.kasiengao.ksgframe.ui.main.bean;

import com.google.gson.annotations.SerializedName;
import com.kaisengao.base.util.NumberUtil;
import com.kasiengao.ksgframe.R;
import com.kasiengao.ksgframe.common.util.TextUtil;

/**
 * @ClassName: VideoBean
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/26 17:22
 * @Description:
 */
public class VideoBean {

    @SerializedName("movieName")
    private String mMovieName;
    @SerializedName("heightUrl")
    private String mVideoUrl;
    @SerializedName("videoTitle")
    private String mVideoTitle;
    @SerializedName("coverImg")
    private String mCoverImg;

    private String mAvatar;

    private String mNickname;

    private String mProfile;

    private int mFans;

    private int mPraise;

    private int mComment;

    private int mShare;

    private boolean isPraised;

    private boolean isStepped;

    public VideoBean(String avatar, String nickname, String profile) {
        this.mAvatar = avatar;
        this.mNickname = nickname;
        this.mProfile = profile;
    }

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

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        this.mAvatar = avatar;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        this.mNickname = nickname;
    }

    public void setProfile(String profile) {
        this.mProfile = profile;
    }

    public String getProfile() {
        return mProfile;
    }

    public void setFans(int fans) {
        this.mFans = fans;
    }

    public String getFans() {
        if (mFans <= 0) {
            return "";
        }
        return NumberUtil.formatBigNum(mFans) + TextUtil.getString(R.string.fans);
    }

    public String getPraise() {
        if (mPraise <= 0) {
            return TextUtil.getString(R.string.main_video_item_interact_praise);
        }
        return NumberUtil.formatBigNum(mPraise);
    }

    public void setPraise(int praise) {
        this.mPraise = praise;
    }

    public void addPraise() {
        this.mPraise = mPraise + 1;
    }

    public void subPraise() {
        this.mPraise = mPraise - 1;
    }

    public String getComment() {
        if (mComment <= 0) {
            return TextUtil.getString(R.string.main_video_item_interact_comment);
        }
        return NumberUtil.formatBigNum(mComment);
    }

    public void setComment(int comment) {
        this.mComment = comment;
    }

    public String getShare() {
        if (mShare <= 0) {
            return TextUtil.getString(R.string.main_video_item_interact_share);
        }
        return NumberUtil.formatBigNum(mShare);
    }

    public void setShare(int share) {
        this.mShare = share;
    }

    public boolean isPraised() {
        return isPraised;
    }

    public void setPraised(boolean praise) {
        this.isPraised = praise;
    }

    public boolean isStepped() {
        return isStepped;
    }

    public void setStepped(boolean stepped) {
        this.isStepped = stepped;
    }
}

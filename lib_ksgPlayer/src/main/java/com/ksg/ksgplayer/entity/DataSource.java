package com.ksg.ksgplayer.entity;

import java.io.Serializable;

/**
 * @author kaisengao
 * @create: 2019/1/4 15:35
 * @describe: 视频资料的一些基本信息
 */
public class DataSource implements Serializable {

    /**
     * 视频 url
     */
    private String mUrl;

    /**
     * 视频 名字
     */
    private String mTitle;

    /**
     * 视频 名字 头像
     */
    private String mImageUrl;

    /**
     * 在指定的位置开始播放
     */
    private int mStartPos;

    /**
     * 是否是直播
     */
    private boolean mLive;

    /**
     * 直播流 类型
     */
    private int mLiveType = -1;

    public DataSource() {
    }

    public DataSource(String data) {
        this.mUrl = data;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public int getStartPos() {
        return mStartPos;
    }

    public void setStartPos(int startPos) {
        this.mStartPos = startPos;
    }

    public boolean isLive() {
        return mLive;
    }

    public void setLive(boolean live) {
        mLive = live;
    }

    public int getLiveType() {
        return mLiveType;
    }

    public void setLiveType(int liveType) {
        mLiveType = liveType;
    }
}

package com.ksg.ksgplayer.data;

import java.io.Serializable;

/**
 * @ClassName: DataSource
 * @Author: KaiSenGao
 * @CreateDate: 2020/7/6 11:15
 * @Description:
 */
public class DataSource implements Serializable {

    private String mUrl;

    /**
     * 是否是直播类型
     */
    private boolean mLive;

    /**
     * 直播流 类型
     */
    private int mLiveType = -1;

    public DataSource(String url) {
        this.mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
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

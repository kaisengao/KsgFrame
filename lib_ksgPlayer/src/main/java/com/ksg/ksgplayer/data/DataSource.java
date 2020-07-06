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

    public DataSource(String url) {
        this.mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}

package com.ksg.ksgplayer.data;

import com.ksg.ksgplayer.renderer.IRenderer;

import java.io.Serializable;

/**
 * @ClassName: DataSource
 * @Author: KaiSenGao
 * @CreateDate: 2020/7/6 11:15
 * @Description:
 */
public class DataSource implements Serializable {

    private String mUrl;

    //------------------- 渲染器 --------------------------//

    private int mRendererType = IRenderer.RENDER_TYPE_SURFACE_VIEW;

    //------------------- 缓存 --------------------------//

    private int mMaxVideoCacheCount;

    private boolean isVideoCache;

    private String mVideoCacheFileName;

    public DataSource() {
    }

    public DataSource(String url) {
        this.mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public int getRendererType() {
        return mRendererType;
    }

    public void setRendererType(int rendererType) {
        mRendererType = rendererType;
    }

    public int getMaxVideoCacheCount() {
        return mMaxVideoCacheCount;
    }

    public void setMaxVideoCacheCount(int maxVideoCacheCount) {
        mMaxVideoCacheCount = maxVideoCacheCount;
    }

    public boolean isVideoCache() {
        return isVideoCache;
    }

    public void setVideoCache(boolean videoCache) {
        isVideoCache = videoCache;
    }

    public String getVideoCacheFileName() {
        return mVideoCacheFileName;
    }

    public void setVideoCacheFileName(String videoCacheFileName) {
        mVideoCacheFileName = videoCacheFileName;
    }
}

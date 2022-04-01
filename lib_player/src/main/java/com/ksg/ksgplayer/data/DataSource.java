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

    //------------------- 视频缓存 --------------------------//

    private int mMaxVideoCacheCount;

    private boolean isVideoCache;

    private String mVideoCacheFileName;

    //------------------- 播放进度缓存 --------------------------//

    private boolean isProgressCache = true;

    private boolean isReadProgressCache = true;

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

    //------------------- 渲染器 --------------------------//

    public int getRendererType() {
        return mRendererType;
    }

    public void setRendererType(int rendererType) {
        mRendererType = rendererType;
    }

    //------------------- 视频缓存 --------------------------//

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

    //------------------- 播放进度缓存 --------------------------//

    public boolean isProgressCache() {
        return isProgressCache;
    }

    public void setProgressCache(boolean progressCache) {
        isProgressCache = progressCache;
    }

    public boolean isReadProgressCache() {
        return isReadProgressCache;
    }

    public void setReadProgressCache(boolean readProgressCache) {
        isReadProgressCache = readProgressCache;
    }
}

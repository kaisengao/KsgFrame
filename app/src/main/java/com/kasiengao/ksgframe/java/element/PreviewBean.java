package com.kasiengao.ksgframe.java.element;

import java.io.Serializable;

/**
 * @ClassName: PreviewBean
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 16:31
 * @Description: 预览资源 Bean
 */
public class PreviewBean implements IPreviewPagerParams, Serializable {

    public int mWidth;

    public int mHeight;

    public String mMediaType;

    public String mMediaUrl;

    @Override
    public int getWidth() {
        return this.mWidth;
    }

    @Override
    public int getHeight() {
        return this.mHeight;
    }

    @Override
    public String getMediaType() {
        return this.mMediaType;
    }

    @Override
    public String getMediaUrl() {
        return this.mMediaUrl;
    }

}

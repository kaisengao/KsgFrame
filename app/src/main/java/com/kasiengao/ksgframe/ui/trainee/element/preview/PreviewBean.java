package com.kasiengao.ksgframe.ui.trainee.element.preview;

import java.io.Serializable;

/**
 * @ClassName: PreviewBean
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 16:31
 * @Description: 预览资源 Bean
 */
public class PreviewBean implements IPreviewParams, Serializable {

    public int mWidth;

    public int mHeight;

    public String mMediaType;

    public String mPictureUrl;

    public String mVideoUrl;

    @Override
    public int getWidth() {
        return mWidth;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    @Override
    public String getMediaType() {
        return mMediaType;
    }

    @Override
    public String getPictureUrl() {
        return mPictureUrl;
    }

    @Override
    public String getVideoUrl() {
        return mVideoUrl;
    }
}

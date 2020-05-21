package com.kasiengao.ksgframe.java.element;

/**
 * @ClassName: IPreviewParams
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/20 10:57
 * @Description: 预览 Banner 基础参数
 */
public interface IPreviewParams {

    /**
     * 媒体 宽
     *
     * @return 宽
     */
    int getWidth();

    /**
     * 媒体 高
     *
     * @return 高
     */
    int getHeight();

    /**
     * 媒体类型
     *
     * @return { image/video }
     */
    String getMediaType();

    /**
     * 图片Url
     *
     * @return url
     */
    String getPictureUrl();

    /**
     * 视频Url
     *
     * @return url
     */
    String getVideoUrl();
}

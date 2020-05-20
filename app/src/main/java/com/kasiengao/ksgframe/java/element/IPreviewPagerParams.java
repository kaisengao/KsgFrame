package com.kasiengao.ksgframe.java.element;

/**
 * @ClassName: IPreviewPagerParams
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/20 10:57
 * @Description: 自适应 Banner 基础参数
 */
public interface IPreviewPagerParams {

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
     * 媒体Url
     *
     * @return url
     */
    String getMediaUrl();

}

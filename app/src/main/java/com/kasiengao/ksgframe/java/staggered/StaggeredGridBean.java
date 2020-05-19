package com.kasiengao.ksgframe.java.staggered;

import com.kasiengao.ksgframe.java.element.PreviewBean;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: StaggeredGridBean
 * @Author: KaiSenGao
 * @CreateDate: 2020/5/19 10:23
 * @Description: 瀑布流 Bean
 */
public class StaggeredGridBean implements Serializable {

    public String mName;

    public String mContent;

    public String mDetailContent;

    public List<PreviewBean> mPreviewBeans;

}

package com.kaisengao.ksgframe.ui.trainee.staggered;

import com.kaisengao.ksgframe.ui.trainee.element.preview.PreviewBean;

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

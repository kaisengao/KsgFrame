package com.kasiengao.ksgframe.ui.main.bean;

import androidx.annotation.LayoutRes;

/**
 * @ClassName: MainPagerBean
 * @Author: KaiSenGao
 * @CreateDate: 2022/3/24 10:27
 * @Description:
 */
public class MainPagerBean {

    private final String mTitle;

    private final int mLayoutRes;

    public MainPagerBean(String title, @LayoutRes int layoutRes) {
        this.mTitle = title;
        this.mLayoutRes = layoutRes;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getLayoutRes() {
        return mLayoutRes;
    }
}
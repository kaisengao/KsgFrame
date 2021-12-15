package com.kaisengao.base.loadpage.load;

import android.view.View;

import com.kaisengao.base.loadpage.listener.OnLoadViewClickListener;

/**
 * @ClassName: OriginalViewLoad
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/15 12:00
 * @Description: 存储原视图
 */
public class OriginalViewLoad extends BaseLoad {

    public OriginalViewLoad(View rootView, OnLoadViewClickListener loadViewClickListener) {
        super(rootView, loadViewClickListener);
    }

    @Override
    protected int getContentLayoutId() {
        return 0;
    }
}
package com.kaisengao.base.loadpage.load;

import android.view.View;

/**
 * @ClassName: OriginalViewLoad
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/15 12:00
 * @Description: 原视图
 */
public class OriginalViewLoad extends BaseLoad {

    public OriginalViewLoad(View rootView, Object target) {
        super(rootView, target);
    }

    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected boolean onClickEvent() {
        return true;
    }
}
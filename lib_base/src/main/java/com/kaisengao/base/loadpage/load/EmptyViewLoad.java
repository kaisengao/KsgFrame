package com.kaisengao.base.loadpage.load;

import com.kaisengao.base.loadpage.load.base.BaseLoad;
import com.kasiengao.base.R;

/**
 * @ClassName: EmptyViewLoad
 * @Author: KaiSenGao
 * @CreateDate: 2021/12/15 13:29
 * @Description: Empty视图
 */
public class EmptyViewLoad extends BaseLoad {

    @Override
    protected int getContentLayoutId() {
        return R.layout.layout_empty;
    }

    @Override
    protected boolean onClickEvent() {
        return false;
    }

}